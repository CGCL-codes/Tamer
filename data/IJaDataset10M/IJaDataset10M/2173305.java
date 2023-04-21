package com.acv.connector.ocean.model;

import java.util.LinkedList;
import java.util.List;
import com.acv.common.model.constants.Engine;
import com.acv.common.model.entity.BookableServiceType;
import com.acv.common.model.entity.FlightSegment;
import com.acv.common.model.entity.ImmutablePassengerGroup;
import com.acv.common.model.entity.OutboundFlightShellService;
import com.acv.common.model.entity.ReturnFlightShellService;
import com.vacv.ocean.data.CRSTransportDetails;

public class OceanReturnFlightShellServiceImpl extends OceanFlightShellServiceImpl implements ReturnFlightShellService, PriceUpdateable {

    private static final long serialVersionUID = -3485052897012536076L;

    protected List<OutboundFlightShellService> compatibleFlightShells = new LinkedList<OutboundFlightShellService>();

    public OceanReturnFlightShellServiceImpl() {
    }

    public OceanReturnFlightShellServiceImpl(Engine sourceEngine, int combinationId, List<FlightSegment> segments, ImmutablePassengerGroup passengerGroup, String brochureCode, String bookingClass, CRSTransportDetails crsTransportDetails) {
        super(sourceEngine, combinationId, segments, passengerGroup, brochureCode, bookingClass, crsTransportDetails);
    }

    public List<OutboundFlightShellService> getCompatibleFlightShells() {
        return compatibleFlightShells;
    }

    public void addCompatibleFlightShell(OutboundFlightShellService compatibleShell) {
        if (!compatibleFlightShells.contains(compatibleShell)) this.compatibleFlightShells.add(compatibleShell);
    }

    public void setCompatibleFlightShells(List<OutboundFlightShellService> compatibleFlightShells) {
        this.compatibleFlightShells = compatibleFlightShells;
    }

    public BookableServiceType getType() {
        return BookableServiceType.RETURN_FLIGHT;
    }
}
