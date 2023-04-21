package com.hazelcast.nio;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Address implements DataSerializable {

    private static final long serialVersionUID = -7626390274220424603L;

    private transient String peerID;

    private int hash = -1;

    private boolean thisAddress = false;

    public Address() {
        peerID = null;
    }

    public Address(String peerID) {
        this.peerID = peerID;
    }

    public Address(Address address) {
        this.peerID = address.peerID;
    }

    public boolean isThisAddress() {
        return thisAddress;
    }

    public void setThisAddress(boolean thisAddress) {
        this.thisAddress = thisAddress;
    }

    public void writeData(DataOutput out) throws IOException {
        out.writeInt(peerID.getBytes().length);
        out.write(peerID.getBytes());
    }

    public void readData(DataInput in) throws IOException {
        int length = in.readInt();
        byte[] buf = new byte[length];
        in.readFully(buf, 0, length);
        peerID = new String(buf);
    }

    public void readObject(ByteBuffer buffer) {
        int length = buffer.getInt();
        byte[] buf = new byte[length];
        buffer.get(buf, 0, length);
        peerID = new String(buf);
    }

    public void writeObject(ByteBuffer buffer) {
        buffer.putInt(peerID.getBytes().length);
        buffer.put(peerID.getBytes());
    }

    public String getHost() {
        return peerID;
    }

    public String toString() {
        return "Address[" + peerID + "]";
    }

    public String addressToString() {
        return peerID;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Address)) return false;
        final Address address = (Address) o;
        return peerID.equals(address.peerID);
    }

    public int hashCode() {
        if (hash == -1) setHashCode();
        return hash;
    }

    private void setHashCode() {
        this.hash = hash(peerID.getBytes());
    }

    private int hash(byte[] id) {
        int hash = 0;
        for (byte anId : id) {
            hash = (hash * 29) + anId;
        }
        return hash;
    }
}
