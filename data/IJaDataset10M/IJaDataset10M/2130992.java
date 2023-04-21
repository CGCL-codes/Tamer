package layer.disseminator.applicationInterfaceAdapter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import layer.APDU;
import layer.ISerializable;
import apps.disseminator.DDU;
import apps.disseminator.ProtocolConstants;

/**
 * 
 * @author Yark Schroeder, Manuel Scholz
 *
 */
public class BI_PDU extends APDU implements ISerializable, DDU {

    private int miHopsHeaderField = -1;

    private byte[] mabyPayload;

    public byte[] serialize() {
        ByteArrayOutputStream kByteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream kDataOutputStream;
        kDataOutputStream = new DataOutputStream(kByteArrayOutputStream);
        try {
            kDataOutputStream.writeByte(getPrtTypeHeaderField());
            kDataOutputStream.write(getPayload());
            kDataOutputStream.close();
            return kByteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean unserialize(byte[] abyData) {
        ByteArrayInputStream kByteArrayInputStream = new ByteArrayInputStream(abyData);
        DataInputStream kDataInputStream;
        try {
            kDataInputStream = new DataInputStream(kByteArrayInputStream);
            kDataInputStream.skip(1);
            int iPayloadLength = kDataInputStream.available();
            byte[] abyDat = new byte[iPayloadLength];
            kDataInputStream.read(abyDat);
            setPayload(abyDat);
            kDataInputStream.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public int getPrtTypeHeaderField() {
        return ProtocolConstants.DBIP;
    }

    public void setPayload(byte[] abyPayload) {
        mabyPayload = abyPayload;
    }

    public byte[] getPayload() {
        return mabyPayload;
    }

    /**
	 * @return value of the hopHeaderField
	 */
    public int getHopsHeaderField() {
        return this.miHopsHeaderField;
    }

    /**
	 * Sets the headerfield for hops.
	 * @param iHopsHeaderField	- headerfield of hops 
	 */
    public void setHopsHeaderField(int iHopsHeaderField) {
        this.miHopsHeaderField = iHopsHeaderField;
    }
}
