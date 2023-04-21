package tools;

/**
 *
 * @author root
 */
public class RankComperator implements java.util.Comparator {

    /** Creates a new instance of RankComperator */
    public RankComperator() {
    }

    public int compare(Object o1, Object o2) {
        try {
            java.util.Vector vector1 = (java.util.Vector) o1;
            java.util.Vector vector2 = (java.util.Vector) o2;
            Float value1 = (Float) vector1.get(8);
            Float value2 = (Float) vector2.get(8);
            return value2.compareTo(value1);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
