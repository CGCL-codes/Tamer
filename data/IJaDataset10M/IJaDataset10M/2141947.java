package org.redpin.core.measure;

import java.util.Vector;
import org.openbandy.io.Serializable;
import org.openbandy.io.SerializationBuffer;
import org.openbandy.io.SerializationIdentifiers;
import org.openbandy.io.Serializer;
import org.openbandy.service.LogService;
import org.redpin.core.Types;
import org.redpin.util.FixedPointLong;
import org.redpin.util.FixedPointLongException;

/**
 * Describes a GSM reading
 * 
 * @author Philipp Bolliger (philipp@bolliger.name)
 * @author Simon Tobler (simon.p.tobler@gmx.ch)
 * @version 0.1
 */
public class GSMReading implements Serializable {

    private int gsmReadingId = SerializationIdentifiers.NO_ID;

    private String cellId = "";

    private String areaId = "";

    private String signalStrength = "";

    private String MCC = "";

    private String MNC = "";

    private String networkName = "";

    /**
	 * @return the cellId
	 */
    public String getCellId() {
        return cellId;
    }

    /**
	 * @param cellId
	 *            the cellId to set
	 */
    public void setCellId(String cellId) {
        this.cellId = cellId;
    }

    /**
	 * @return the areaId
	 */
    public String getAreaId() {
        return areaId;
    }

    /**
	 * @param areaId
	 *            the areaId to set
	 */
    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    /**
	 * @return the signalStrength
	 */
    public String getSignalStrength() {
        return signalStrength;
    }

    /**
	 * @param signalStrength
	 *            the signalStrength to set
	 */
    public void setSignalStrength(String signalStrength) {
        this.signalStrength = signalStrength;
    }

    /**
	 * @return the mCC
	 */
    public String getMCC() {
        return MCC;
    }

    /**
	 * @param mcc
	 *            the mCC to set
	 */
    public void setMCC(String mcc) {
        MCC = mcc;
    }

    /**
	 * @return the mNC
	 */
    public String getMNC() {
        return MNC;
    }

    /**
	 * @param mnc
	 *            the mNC to set
	 */
    public void setMNC(String mnc) {
        MNC = mnc;
    }

    /**
	 * @return the networkName
	 */
    public String getNetworkName() {
        return networkName;
    }

    /**
	 * @param networkName
	 *            the networkName to set
	 */
    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    public String getType() {
        return Types.GSM;
    }

    public String getId() {
        return MCC + ":" + MNC + ":" + cellId + ":" + areaId;
    }

    public boolean isValid() {
        if (this.cellId.equals("")) {
            LogService.info(this, "isValid: cellId == ''");
            return false;
        }
        if (this.signalStrength.equals("")) {
            LogService.info(this, "isValid: signalStrength == ''");
            return false;
        }
        return true;
    }

    public int getNormalizedSignalStrength() {
        try {
            int nativeSignal = Integer.parseInt(signalStrength);
            if (nativeSignal > 0) nativeSignal *= -1;
            long a, b;
            try {
                a = FixedPointLong.stringToFlong("1.6164");
                b = FixedPointLong.stringToFlong("182.3836");
            } catch (FixedPointLongException fple) {
                throw new RuntimeException("Cannot get FPLs for statics!");
            }
            long signal = nativeSignal * a + b;
            int value = FixedPointLong.intValue(signal);
            if (value > 100) value = 100;
            return value;
        } catch (NumberFormatException nfe) {
            LogService.error(this, nfe.getMessage(), nfe);
            return 0;
        }
    }

    public String getHumanReadableName() {
        return "" + networkName + ":" + areaId + ":" + cellId;
    }

    public String toString() {
        return "GSM Reading: " + Types.HUMANREADABLENAME + "=" + networkName + "|" + Types.CELLID + "=" + cellId + "|" + Types.AREAID + "=" + areaId + "|" + Types.MCC + "=" + MCC + "|" + Types.MNC + "=" + MNC + "|" + Types.SIGNAL + "=" + signalStrength + "|" + Types.PERCENTAGE + "=" + getNormalizedSignalStrength();
    }

    public void serialize(Serializer serializer, SerializationBuffer serializationBuffer) {
        serializationBuffer.container().addString("cellId", cellId);
        serializationBuffer.container().addString("areaId", areaId);
        serializationBuffer.container().addString("signalStrength", signalStrength);
        serializationBuffer.container().addString("MCC", MCC);
        serializationBuffer.container().addString("MNC", MNC);
        serializationBuffer.container().addString("networkName", networkName);
    }

    public void deserialize(Serializer serializer, SerializationBuffer serializationBuffer) {
        cellId = serializationBuffer.container().getString("cellId");
        areaId = serializationBuffer.container().getString("areaId");
        signalStrength = serializationBuffer.container().getString("signalStrength");
        MCC = serializationBuffer.container().getString("MCC");
        MNC = serializationBuffer.container().getString("MNC");
        networkName = serializationBuffer.container().getString("networkName");
    }

    public String getIdName() {
        return "gsmReadingId";
    }

    public int getIdValue() {
        return gsmReadingId;
    }

    public void setIdValue(int idValue) {
        gsmReadingId = idValue;
    }

    public void getAttributeNames(Vector attributes) {
        attributes.addElement("cellId");
        attributes.addElement("areaId");
        attributes.addElement("signalStrength");
        attributes.addElement("MCC");
        attributes.addElement("MNC");
        attributes.addElement("networkName");
    }

    public void getReferences(Vector references) {
    }

    public void getReferencedIdNames(Vector referencedIdNames) {
    }

    public String getLegibleString() {
        return this.toString();
    }

    public boolean isEqualShallow(Serializable serializableObject) throws ClassCastException {
        GSMReading comparingReading = (GSMReading) serializableObject;
        return ((this.areaId.equals(comparingReading.areaId)) && (this.cellId.equals(comparingReading.cellId)) && (this.MCC.equals(comparingReading.MCC)) && (this.MNC.equals(comparingReading.MNC)) && (this.networkName.equals(comparingReading.networkName)) && (this.signalStrength.equals(comparingReading.signalStrength)));
    }

    public boolean isEqual(Serializable serializableObject) throws ClassCastException {
        return isEqualShallow(serializableObject);
    }

    public Serializable getCopy() {
        GSMReading clone = new GSMReading();
        clone.areaId = this.areaId;
        clone.cellId = this.cellId;
        clone.MCC = this.MCC;
        clone.MNC = this.MNC;
        clone.networkName = this.networkName;
        clone.signalStrength = this.signalStrength;
        return clone;
    }

    public Serializable getNewInstance() {
        return new GSMReading();
    }
}
