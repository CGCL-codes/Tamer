package net.sf.nxtassembler.types;

import java.util.Hashtable;

/**
 * This class manages the difference that occurs in RAM and in the flattered 
 * stream.
 * 
 * @author Matteo Valdina, zanfire@users.sourceforge.net
 * @version 1.0
 */
public class Segment {

    private Hashtable<String, BitsCollection> subSegment = null;

    private boolean keepAling = false;

    public Segment() {
        subSegment = new Hashtable<String, BitsCollection>();
    }

    public void setKeepAlign(boolean t) {
        keepAling = t;
    }

    public void addSubSegment(String name) {
        subSegment.put(name, new BitsCollection());
    }

    public BitsCollection generateSubSegmentStream(String name) {
        BitsCollection bin = subSegment.get(name);
        BitsCollection newBin = new BitsCollection();
        for (int i = 0; i < bin.data.size(); i++) {
            Bits bits = bin.data.get(i);
            if (!bits.isPlaceholder) {
                newBin.append(bits);
            }
        }
        return newBin;
    }

    public BitsCollection retriveSubSegmentRAM(String name) {
        return subSegment.get(name);
    }

    /**
	 * 
	 * @param name
	 * @param bits
	 * @return return the offset (in bit) of Bits inserted.
	 */
    public int addToSubSegmentStream(String name, Bits bits) {
        BitsCollection bin = subSegment.get(name);
        if (keepAling) {
            int currentByteLength = bin.length() / 8;
            int addedByteLength = bits.length() / 8;
            keepAlign(bin, currentByteLength, addedByteLength);
        }
        return bin.append(bits);
    }

    public int addToSubSegmentRAM(String name, Bits bits) {
        BitsCollection bin = subSegment.get(name);
        bits.isPlaceholder = true;
        if (keepAling) {
            int currentByteLength = bin.length() / 8;
            int addedByteLength = bits.length() / 8;
            keepAlign(bin, currentByteLength, addedByteLength);
        }
        return bin.append(bits);
    }

    private void keepAlign(BitsCollection bin, int currentByteLength, int addedByteLength) {
        int totalLength = currentByteLength + addedByteLength;
        if (currentByteLength % 4 != 0 && totalLength % 4 != 0) {
            int dif = (4 - (currentByteLength % 4)) * 8;
            Bits pl = new Bits(0, dif);
            pl.isPlaceholder = true;
            bin.append(pl);
        }
    }
}
