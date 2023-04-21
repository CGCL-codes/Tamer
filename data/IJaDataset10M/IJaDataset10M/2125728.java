package org.openXpertya.model;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */
public class MPackage extends X_M_Package {

    /**
     * Descripción de Método
     *
     *
     * @param shipment
     * @param shipper
     * @param shipDate
     *
     * @return
     */
    public static MPackage create(MInOut shipment, MShipper shipper, Timestamp shipDate) {
        MPackage retValue = new MPackage(shipment, shipper);
        if (shipDate != null) {
            retValue.setShipDate(shipDate);
        }
        retValue.save();
        MInOutLine[] lines = shipment.getLines(false);
        for (int i = 0; i < lines.length; i++) {
            MInOutLine sLine = lines[i];
            MPackageLine pLine = new MPackageLine(retValue);
            pLine.setInOutLine(sLine);
            pLine.save();
        }
        return retValue;
    }

    /**
     * Constructor de la clase ...
     *
     *
     * @param ctx
     * @param M_Package_ID
     * @param trxName
     */
    public MPackage(Properties ctx, int M_Package_ID, String trxName) {
        super(ctx, M_Package_ID, trxName);
        if (M_Package_ID == 0) {
            setShipDate(new Timestamp(System.currentTimeMillis()));
        }
    }

    /**
     * Constructor de la clase ...
     *
     *
     * @param ctx
     * @param rs
     * @param trxName
     */
    public MPackage(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /**
     * Constructor de la clase ...
     *
     *
     * @param shipment
     * @param shipper
     */
    public MPackage(MInOut shipment, MShipper shipper) {
        this(shipment.getCtx(), 0, shipment.get_TrxName());
        setClientOrg(shipment);
        setM_InOut_ID(shipment.getM_InOut_ID());
        setM_Shipper_ID(shipper.getM_Shipper_ID());
    }

    public MPackage(Properties ctx, int M_Package_ID) {
        super(ctx, M_Package_ID, null);
        if (M_Package_ID == 0) {
            setShipDate(new Timestamp(System.currentTimeMillis()));
        }
    }

    public MPackage(MEnvio envio, MInOut shipment) {
        this(shipment.getCtx(), 0);
        setClientOrg(shipment);
        setM_InOut_ID(shipment.getM_InOut_ID());
        setM_Envio_ID(envio.getM_Envio_ID());
    }
}
