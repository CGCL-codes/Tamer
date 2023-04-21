package za.co.OO7J.utils;

import java.util.ArrayList;
import java.util.List;
import za.co.OO7J.AtomicPart;
import com.db4o.query.Predicate;

public class Db4oNativeQueries {

    /**
	 * 
	 * Find atomic parts with build date in range:
	 * 
	 * lowerDate <= build date <= MaxAtomicDate
	 * 
	 * Used in Query2+3
	 * 
	 * @param min
	 * @param max
	 * @return 06-May-2006
	 */
    public static List nativeSelectAtomicPartsInRange(final long min, final long max) {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * pvz
	 * 
	 * from Document doc, AtomicPart part where doc.docId = part.docId
	 * 
	 * @return 24 Feb 2008
	 */
    public static List selectAllAtomicPartsAndAssociatedDocs() {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
