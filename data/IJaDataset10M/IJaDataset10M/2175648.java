package PolicyAlgebra.Type;

/** 
 * This class represents the MLS portion that may be used with
 * any object and subject.
 */
public class MlsLevel {

    private int low;

    private int high;

    private int curr;

    private String catset;

    /** 
	 * Default constructor, sets defaults to what is
	 * equivalent to no MLS at all.
	 */
    public MlsLevel() {
        low = 0;
        high = 0;
        curr = 0;
        catset = "{}";
    }

    /** 
	 * Custom string transformation method. 
	 * 
	 * @return The string representation of this MLS capable named atom.
	 */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" [");
        sb.append(low);
        sb.append("-");
        sb.append(high);
        sb.append(":");
        sb.append(curr);
        sb.append("]");
        sb.append(catset);
        return sb.toString();
    }
}
