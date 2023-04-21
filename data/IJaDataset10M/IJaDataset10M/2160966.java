package tuwien.auto.calimero.knxnetip.servicetype;

import java.io.ByteArrayOutputStream;
import tuwien.auto.calimero.exception.KNXFormatException;
import tuwien.auto.calimero.knxnetip.util.DIB;
import tuwien.auto.calimero.knxnetip.util.DeviceDIB;
import tuwien.auto.calimero.knxnetip.util.ManufacturerDIB;
import tuwien.auto.calimero.knxnetip.util.SuppFamiliesDIB;

/**
 * Represents a description response.
 * <p>
 * Such response is sent by a server in reply to a description request from a client.<br>
 * A response contains a device description information block (DIB) and a supported
 * service families DIB. Optionally, it might contain an additional DIB with other
 * information. Such optional DIB is only paid attention in case of type manufacturer data
 * DIB, otherwise it is ignored.
 * <p>
 * Objects of this type are immutable.
 * 
 * @author B. Malinowsky
 * @author Bernhard Erb
 * @see tuwien.auto.calimero.knxnetip.servicetype.DescriptionRequest
 */
public class DescriptionResponse extends ServiceType {

    private final DeviceDIB device;

    private final SuppFamiliesDIB suppfam;

    private ManufacturerDIB mfr;

    /**
	 * Creates a new description response out of a byte array.
	 * <p>
	 * 
	 * @param data byte array containing a description response structure
	 * @param offset start offset of response in <code>data</code>
	 * @throws KNXFormatException if no description response was found or invalid
	 *         structure of DIBs carried in the response
	 */
    public DescriptionResponse(byte[] data, int offset) throws KNXFormatException {
        super(KNXnetIPHeader.DESCRIPTION_RES);
        device = new DeviceDIB(data, offset);
        suppfam = new SuppFamiliesDIB(data, offset + device.getStructLength());
        final int len = device.getStructLength() + suppfam.getStructLength();
        if (len + 1 < data.length - offset && (data[offset + len + 1] & 0xFF) == DIB.MFR_DATA) mfr = new ManufacturerDIB(data, offset);
    }

    /**
	 * Creates a new description response containing a device DIB and a supported service
	 * families DIB.
	 * <p>
	 * 
	 * @param device device description
	 * @param suppSvcFam supported service families
	 */
    public DescriptionResponse(DeviceDIB device, SuppFamiliesDIB suppSvcFam) {
        super(KNXnetIPHeader.DESCRIPTION_RES);
        this.device = device;
        suppfam = suppSvcFam;
    }

    /**
	 * Creates a new description response containing a device DIB, a supported service
	 * families DIB and a manufacturer DIB.
	 * <p>
	 * 
	 * @param device device description
	 * @param suppSvcFam supported service families
	 * @param mfr manufacturer specific data
	 */
    public DescriptionResponse(DeviceDIB device, SuppFamiliesDIB suppSvcFam, ManufacturerDIB mfr) {
        this(device, suppSvcFam);
        this.mfr = mfr;
    }

    /**
	 * Returns the device description information block contained in the response.
	 * <p>
	 * 
	 * @return a device DIB
	 */
    public final DeviceDIB getDevice() {
        return device;
    }

    /**
	 * Returns the supported service families description information block.
	 * <p>
	 * 
	 * @return a DIB with the supported service families
	 */
    public final SuppFamiliesDIB getServiceFamilies() {
        return suppfam;
    }

    /**
	 * Returns the manufacturer data description information block optionally contained in
	 * the response.
	 * <p>
	 * The manufacturer data is not a mandatory part of a description response. It is only
	 * available, if the optional DIB information of a response matches this DIB type.<br>
	 * 
	 * @return a manufacturer DIB, or <code>null</code> if no such DIB
	 */
    public final ManufacturerDIB getManufacturerData() {
        return mfr;
    }

    short getStructLength() {
        int len = device.getStructLength() + suppfam.getStructLength();
        if (mfr != null) len += mfr.getStructLength();
        return (short) len;
    }

    byte[] toByteArray(ByteArrayOutputStream os) {
        byte[] buf = device.toByteArray();
        os.write(buf, 0, buf.length);
        buf = suppfam.toByteArray();
        os.write(buf, 0, buf.length);
        if (mfr != null) {
            buf = mfr.toByteArray();
            os.write(buf, 0, buf.length);
        }
        return os.toByteArray();
    }
}
