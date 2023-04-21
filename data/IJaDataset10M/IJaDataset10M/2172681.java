package org.dicom4j.network.protocoldataunit.userinformationsubitems.items;

import java.io.IOException;
import java.nio.ByteOrder;
import org.dicom4j.dicom.DicomException;
import org.dicom4j.io.BinaryInputStream;
import org.dicom4j.io.BinaryOutputStream;
import org.dicom4j.network.protocoldataunit.userinformationsubitems.UserInformationSubItemType;
import org.dicom4j.network.protocoldataunit.userinformationsubitems.support.AbstractUserInformationSubItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SCP/SCU Role Selection (SUB-ITEM).
 * 
 * <p>
 * <table border="1">
 * <tr>
 * <td>Item Bytes</td>
 * <td>Field Name</td>
 * <td>Description of Field</td>
 * </tr>
 * <tr>
 * <td>1</td>
 * <td>Item-type</td>
 * <td>54H</td>
 * </tr>
 * </table>
 * 
 * @since 0.0.1
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte
 * 
 */
public class ScpScuRoleSelection extends AbstractUserInformationSubItem {

    private static Logger logger = LoggerFactory.getLogger(ScpScuRoleSelection.class);

    private boolean isSCP;

    private boolean isSCU;

    private String sopClassUID;

    public ScpScuRoleSelection() {
        super();
    }

    public ScpScuRoleSelection(String aSOPClassUID) {
        this();
    }

    public ScpScuRoleSelection(String aSOPClassUID, boolean aIsSCU, boolean aIsSCP) {
        this();
        this.setSOPClassUID(aSOPClassUID);
        this.setIsSCU(aIsSCU);
        this.setIsSCP(aIsSCP);
    }

    public boolean getIsSCP() {
        return this.isSCP;
    }

    public boolean getIsSCU() {
        return this.isSCU;
    }

    @Override
    public int getLength() {
        return this.sopClassUID.length() + 8;
    }

    @Override
    public String getName() {
        return "SCP SCU Role Selection";
    }

    public String getSOPClassUID() {
        return this.sopClassUID;
    }

    @Override
    public UserInformationSubItemType getType() {
        return UserInformationSubItemType.SCP_SCU_ROLE_SELECTION;
    }

    @Override
    public void read(BinaryInputStream stream, int length) throws DicomException, IOException {
        logger.debug("Start reading, length: " + length);
        int lUIDlength = stream.readUnsigned16();
        this.setSOPClassUID(stream.readASCII(lUIDlength));
        this.isSCU = stream.readBoolean();
        this.isSCP = stream.readBoolean();
        logger.debug("Strop reading");
    }

    public void setIsSCP(boolean aIsSCP) {
        this.isSCP = aIsSCP;
    }

    public void setIsSCU(boolean aIsSCU) {
        this.isSCU = aIsSCU;
    }

    public void setSOPClassUID(String sOPClassUID) {
        logger.debug("setSOPClassUID: " + sOPClassUID);
        this.sopClassUID = sOPClassUID;
    }

    @Override
    public String toString() {
        return "Role Selection: " + this.getSopClassRegistry().getSopClassAsString(this.getSOPClassUID()) + " (SCU role: " + Boolean.toString(this.getIsSCU()) + ", SCP role : " + Boolean.toString(this.getIsSCP()) + ")";
    }

    @Override
    public void write(BinaryOutputStream stream) throws DicomException, IOException {
        logger.debug("Start writing");
        if (stream.getByteOrder() != ByteOrder.BIG_ENDIAN) {
            throw new DicomException("the stream must be in BIG_ENDIAN");
        }
        stream.write(this.getType().value());
        stream.write(0x00);
        stream.writeUnsigned16(this.getLength() - 4);
        stream.writeUnsigned16(this.sopClassUID.length());
        stream.writeASCII(this.getSOPClassUID());
        stream.writeBoolean(this.getIsSCU());
        stream.writeBoolean(this.getIsSCP());
        logger.debug("Stop writing");
    }
}
