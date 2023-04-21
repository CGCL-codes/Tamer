package it.crs4.seal.recab;

/**
 * Bean to store the minimal definition of a SNP: contig and position.
 */
public class VariantRegion {

    private String contigName;

    private int startPos;

    private int length;

    public VariantRegion() {
    }

    public VariantRegion(String contig, int startPos) {
        this(contig, startPos, 1);
    }

    public VariantRegion(String contig, int startPos, int length) {
        if (startPos <= 0) throw new IllegalArgumentException("Variant position must be > 0");
        if (length <= 0) throw new IllegalArgumentException("Variant length  must be > 0");
        contigName = contig;
        this.startPos = startPos;
        this.length = length;
    }

    public String getContigName() {
        return contigName;
    }

    public int getPosition() {
        return startPos;
    }

    public int getLength() {
        return length;
    }

    public void setContigName(String name) {
        contigName = name;
    }

    public void setPosition(int pos) {
        if (pos <= 0) throw new IllegalArgumentException("Variant position must be > 0");
        startPos = pos;
    }

    public void setLength(int len) {
        if (len <= 0) throw new IllegalArgumentException("Variant length  must be > 0");
        length = len;
    }

    public void set(VariantRegion other) {
        contigName = other.contigName;
        startPos = other.startPos;
        length = other.length;
    }
}
