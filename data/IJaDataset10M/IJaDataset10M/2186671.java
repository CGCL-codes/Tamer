package org.xith3d.utility.geometry.nvtristrip;

/**
 * @author YVG
 */
public class PrimitiveGroup {

    public static final int PT_LIST = 0;

    public static final int PT_STRIP = 1;

    public static final int PT_FAN = 2;

    public int type;

    public int[] indices;

    public int numIndices;

    public PrimitiveGroup() {
        type = PT_STRIP;
    }

    public String getTypeString() {
        switch(type) {
            case PT_LIST:
                return "list";
            case PT_STRIP:
                return "strip";
            case PT_FAN:
                return "fan";
            default:
                return "????";
        }
    }

    @Override
    public String toString() {
        return getTypeString() + " : " + numIndices;
    }

    public String getFullInfo() {
        if (type != PT_STRIP) return toString();
        int[] stripLengths = new int[numIndices];
        int prev = -1;
        int length = -1;
        for (int i = 0; i < numIndices; i++) {
            if (indices[i] == prev) {
                stripLengths[length]++;
                length = -1;
                prev = -1;
            } else {
                prev = indices[i];
                length++;
            }
        }
        stripLengths[length]++;
        StringBuffer sb = new StringBuffer();
        sb.append("Strip:").append(numIndices).append("\n");
        for (int i = 0; i < stripLengths.length; i++) {
            if (stripLengths[i] > 0) {
                sb.append(i).append("->").append(stripLengths[i]).append("\n");
            }
        }
        return sb.toString();
    }

    public int[] getTrimmedIndices() {
        if (indices.length == numIndices) return indices;
        int[] nind = new int[numIndices];
        System.arraycopy(indices, 0, nind, 0, numIndices);
        return nind;
    }
}
