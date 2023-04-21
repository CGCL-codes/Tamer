package org.jdonkey.proto.messages;

import org.jdonkey.proto.ByteBuffer;
import org.jdonkey.proto.Constants;
import org.jdonkey.proto.messages.*;

public class UsersCountBean extends Message {

    long usersCount;

    long filesCount;

    public byte getTag() {
        return Constants.usersCountTag;
    }

    public void setUsersCount(long value) {
        usersCount = value;
    }

    public long getUsersCount() {
        return usersCount;
    }

    public void setFilesCount(long value) {
        filesCount = value;
    }

    public long getFilesCount() {
        return filesCount;
    }

    /**	 * @see Message#getBytes()	 */
    public byte[] getBytes() {
        return null;
    }

    /**	 * @see Message#setBytes(byte[])	 */
    public void setBytes(byte[] data) {
        ByteBuffer buffer = new ByteBuffer();
        buffer.setBytes(data);
        if (buffer.readByte() == getTag()) {
            usersCount = buffer.readUInt32();
            filesCount = buffer.readUInt32();
        } else {
            System.err.println("UsersCountBean: invalid tag");
        }
    }

    public String toString() {
        return usersCount + " users, " + filesCount + " files";
    }
}
