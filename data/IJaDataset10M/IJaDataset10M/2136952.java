package de.innosystec.unrar.rarfile;

import de.innosystec.unrar.io.Raw;

/**
 * DOCUMENT ME
 *
 * @author $LastChangedBy$
 * @version $LastChangedRevision$
 */
public class SignHeader extends BaseBlock {

    public static final short signHeaderSize = 8;

    private int creationTime = 0;

    private short arcNameSize = 0;

    private short userNameSize = 0;

    public SignHeader(BaseBlock bb, byte[] signHeader) {
        super(bb);
        int pos = 0;
        creationTime = Raw.readIntLittleEndian(signHeader, pos);
        pos += 4;
        arcNameSize = Raw.readShortLittleEndian(signHeader, pos);
        pos += 2;
        userNameSize = Raw.readShortLittleEndian(signHeader, pos);
    }

    public short getArcNameSize() {
        return arcNameSize;
    }

    public void setArcNameSize(short arcNameSize) {
        this.arcNameSize = arcNameSize;
    }

    public int getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(int creationTime) {
        this.creationTime = creationTime;
    }

    public short getUserNameSize() {
        return userNameSize;
    }

    public void setUserNameSize(short userNameSize) {
        this.userNameSize = userNameSize;
    }
}
