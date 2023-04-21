package org.wfp.rita.web.common;

import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
import org.wfp.rita.base.RitaException;
import org.wfp.rita.dao.ArrivalType;
import org.wfp.rita.dao.Bundle;
import org.wfp.rita.dao.JourneyType;
import org.wfp.rita.dao.Location;
import org.wfp.rita.dao.LocationType;
import org.wfp.rita.dao.Movement;
import org.wfp.rita.dao.QuantityTriplet;
import org.wfp.rita.dao.RequestLine;
import org.wfp.rita.dao.Shipment;
import org.wfp.rita.datafacade.DataFacade;

/**
 * Common methods to create movements for specific stock movements
 * 
 * @author rkiraly@wfp.org
 * 
 */
public class MovementUtils {

    private static Logger logger = Logger.getLogger(MovementUtils.class);

    /**
	 * Create movement records for a dispatch plan This will create a -S and +P
	 * record on the origin location
	 * 
	 * @param line
	 * @param ltuQty
	 * @param journey
	 * @throws RitaException
	 */
    public static void createDispatchPlan(RequestLine line, QuantityTriplet qt, Shipment shipment, Location origin) throws RitaException {
        logger.info("Creating dispatch plan");
        DataFacade f = MBeanUtils.getDataFacade();
        if (shipment.getJourney().getIsDispatched() || shipment.getJourney().getIsDeleted()) {
            logger.info("Journey is not modificable, aborting changes");
            return;
        }
        QuantityTriplet reverse = qt.reverse();
        Movement p1 = getDefaultMovement(line, origin);
        p1.setShipment(shipment);
        p1.setMovementLedger(Movement.Ledger.S);
        p1.setQuantities(reverse);
        logCreation(p1);
        f.save(p1);
        Movement p2 = getDefaultMovement(line, origin);
        p2.setShipment(shipment);
        p2.setMovementLedger(Movement.Ledger.P);
        p2.setQuantities(qt);
        logCreation(p2);
        f.save(p2);
    }

    /**
	 * Create movement records for a dispatch plan This will create a -U and +X
	 * record on the origin location
	 * 
	 * @param line
	 * @param ltuQty
	 * @param journey
	 * @throws RitaException
	 */
    public static void createDispatchPlanFromCustomer(RequestLine line, QuantityTriplet qt, Shipment shipment, Location origin) throws RitaException {
        logger.info("Creating dispatch plan");
        DataFacade f = MBeanUtils.getDataFacade();
        if (shipment.getJourney().getIsDispatched() || shipment.getJourney().getIsDeleted()) {
            logger.info("Journey is not modificable, aborting changes");
            return;
        }
        QuantityTriplet reverse = qt.reverse();
        Movement p1 = getDefaultMovement(line, origin);
        p1.setShipment(shipment);
        p1.setMovementLedger(Movement.Ledger.U);
        p1.setQuantities(reverse);
        logCreation(p1);
        f.save(p1);
        Movement p2 = getDefaultMovement(line, origin);
        p2.setShipment(shipment);
        p2.setMovementLedger(Movement.Ledger.X);
        p2.setQuantities(qt);
        logCreation(p2);
        f.save(p2);
    }

    /**
	 * Create movement records for a requestline. This will create a -C record
	 * on the requestline's destination site location and a +E record on the
	 * destination location
	 * 
	 * @param line
	 * @param ltuQty
	 * @param journey
	 * @throws RitaException
	 */
    public static void updateRequestLineMovements(RequestLine line, Bundle bundle) throws RitaException {
        logger.info("Updating request line movements");
        DataFacade f = MBeanUtils.getDataFacade();
        for (Movement m : line.getMovements()) {
            logger.info("Deleting old line movement for ledger:" + m.getMovementLedger());
            m.setIsDeleted(true);
        }
        QuantityTriplet qt = new QuantityTriplet(line);
        QuantityTriplet reverse = qt.reverse();
        Shipment sh = line.getRequest().getInitialShipment();
        Location arrivalLocation = line.getRequest().getArrivalLocation();
        Location arrivalSiteLocation = f.getSiteDao().getSiteLocation(arrivalLocation.getParentSite());
        Movement p1 = getDefaultMovement(line, arrivalSiteLocation);
        p1.setShipment(sh);
        p1.setBundle(bundle);
        p1.setMovementLedger(Movement.Ledger.C);
        p1.setQuantities(reverse);
        logCreation(p1);
        f.save(p1);
        Movement p2 = getDefaultMovement(line, arrivalLocation);
        p2.setShipment(sh);
        if (line.getRequest().getArrivalType().getCode() == ArrivalType.Code.C) {
            p2.setMovementLedger(Movement.Ledger.U);
        } else {
            p2.setMovementLedger(Movement.Ledger.I);
        }
        p2.setQuantities(qt);
        logCreation(p2);
        f.save(p2);
        Set<Movement> bundleMovements = new HashSet<Movement>();
        bundleMovements.add(p1);
        bundle.setMovements(bundleMovements);
    }

    /**
	 * Balance all P and X records of a shipment and create the corresponding E
	 * records on the shipment's destination location
	 * 
	 * 
	 * @param shipment
	 *            is he shipment to dispatch
	 * @throws RitaException
	 */
    public static void dispatch(Shipment shipment) throws RitaException {
        logger.info("Creating dispatch");
        DataFacade f = MBeanUtils.getDataFacade();
        for (Movement m : shipment.getMovements()) {
            if (m.getMovementLedger().equals(Movement.Ledger.P) || m.getMovementLedger().equals(Movement.Ledger.X)) {
                QuantityTriplet qt = new QuantityTriplet(m);
                QuantityTriplet reverse = qt.reverse();
                Movement p1 = getDefaultMovement(m.getRequestLine(), m.getAffectedLocation());
                p1.setMovementLedger(m.getMovementLedger());
                p1.setLtuQty(reverse.ltuQty);
                p1.setQuantities(reverse);
                p1.setShipment(shipment);
                logCreation(p1);
                f.save(p1);
                Movement p2;
                if (shipment.getJourney().getType().getCode() == JourneyType.Code.R || shipment.getDestination().getLocationType().getCode() == LocationType.Code.C) {
                    p2 = getDefaultMovement(m.getRequestLine(), f.getSiteDao().getSiteLocation(shipment.getDestination().getParentSite()));
                    p2.setMovementLedger(Movement.Ledger.C);
                } else {
                    p2 = getDefaultMovement(m.getRequestLine(), shipment.getDestination());
                    p2.setMovementLedger(Movement.Ledger.E);
                }
                p2.setShipment(shipment);
                p2.setQuantities(qt);
                logCreation(p2);
                f.save(p2);
            }
        }
    }

    /**
	 * Create movement records for a dispatch plan This will create a -S record
	 * on the origin location
	 * 
	 * @param line
	 * @param ltuQty
	 * @param journey
	 * @throws RitaException
	 */
    public static void receive(RequestLine line, QuantityTriplet qt, Shipment shipment, Location origin, Location target, boolean isCustomerDelivery) throws RitaException {
        logger.info("Receiving...");
        DataFacade f = MBeanUtils.getDataFacade();
        QuantityTriplet reverse = qt.reverse();
        Movement p1 = getDefaultMovement(line, origin);
        p1.setShipment(shipment);
        if (isCustomerDelivery) {
            p1.setMovementLedger(Movement.Ledger.I);
        } else {
            p1.setMovementLedger(Movement.Ledger.E);
        }
        p1.setQuantities(reverse);
        logCreation(p1);
        f.save(p1);
        Movement p2 = getDefaultMovement(line, target);
        p2.setShipment(shipment);
        p2.setMovementLedger(Movement.Ledger.S);
        p2.setQuantities(qt);
        logCreation(p2);
        f.save(p2);
    }

    /**
	 * Get a basic movement record
	 * 
	 * @throws RitaException
	 */
    private static Movement getDefaultMovement(RequestLine line, Location loc) throws RitaException {
        DataFacade f = MBeanUtils.getDataFacade();
        Movement m = new Movement();
        m.setId(new Movement.Id(f.getActiveSite().getId()));
        m.setAffectedLocation(loc);
        m.setLtuQty(0);
        m.setLtuVolume(0d);
        m.setLtuWeight(0d);
        m.setRequestLine(line);
        return m;
    }

    private static void logCreation(Movement m) {
        logger.info("Movement created with ledger code:" + m.getMovementLedger() + " Quantity triplett: (" + m.getLtuQty() + ";" + m.getLtuWeight() + ";" + m.getLtuVolume() + ")");
    }

    private static void logDeletion(Movement m) {
        logger.info("Movement deleted with ledger code:" + m.getMovementLedger() + " Quantity triplett: (" + m.getLtuQty() + ";" + m.getLtuWeight() + ";" + m.getLtuVolume() + ")");
    }
}
