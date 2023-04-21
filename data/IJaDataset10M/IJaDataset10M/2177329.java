package src.projects.findPeaks.objects;

/**
 * @version $Revision: 2532 $
 * @author 
 */
public class Peakdesc {

    private final int length;

    private final int max_loc;

    private final int offset;

    private final float height;

    private final int hashkey;

    private int seq_key = -1;

    /**
	 * 	// ESCA-JAVA0138:
	 * @param offset	genomic coordinates
	 * @param length 	relative to the offset (start position.)
	 * @param max_loc   relative to the offset (start position.)
	 * @param height
	 * @param hashkey
	 */
    public Peakdesc(int offset, int length, int max_loc, float height, int hashkey) {
        this.length = length;
        this.max_loc = max_loc;
        this.offset = offset;
        this.height = height;
        this.hashkey = hashkey;
    }

    /**
	 * Setters
	 * @param a The sequence under the peak
	 */
    public final void set_seq_key(int a) {
        this.seq_key = a;
    }

    /**
	 * Getters - end
	 * 
	 * @return end position of peak relative to the start of the int[]
	 */
    public final int get_length() {
        return this.length;
    }

    /**
	 * Getters - max_loc
	 * 
	 * @return the location of the maxima in this peak (or set of peaks if
	 *         -subpeaks is not being used)
	 */
    public final int get_max_loc() {
        return this.max_loc;
    }

    /**
	 * Getters - offset
	 * 
	 * @return the location of the offset of the start of the int array
	 */
    public final int get_offset() {
        return this.offset;
    }

    /**
	 * Getters - height
	 * 
	 * @return height of the peak at the maximum 
	 */
    public final float get_height() {
        return this.height;
    }

    /** 
	 * Getters - score
	 * @return A key to identify the int[] associated with the peak.
	 */
    public final int get_hashkey() {
        return this.hashkey;
    }

    /**
	 * Getter - Stored key to sequence under the peak
	 * @return
	 */
    public final int get_seq_key() {
        return this.seq_key;
    }

    public final String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("offset/start: " + this.offset);
        sb.append("\tlength: " + this.length);
        sb.append("\tmax loc: " + this.max_loc);
        sb.append("\theight: " + this.height);
        sb.append("\thashkey: " + this.hashkey);
        sb.append("\tseq_key: " + this.seq_key);
        sb.append("\n");
        return sb.toString();
    }
}
