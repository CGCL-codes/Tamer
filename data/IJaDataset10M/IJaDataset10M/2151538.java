package uk.ac.ebi.intact.util.protein.utils.comparator;

import uk.ac.ebi.intact.uniprot.model.UniprotXref;
import java.util.Comparator;
import java.util.Map;

/**
 * Comparator for uniprot xref
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>18/11/11</pre>
 */
public class UniprotXrefComparator implements Comparator<UniprotXref> {

    private Map<String, String> databaseName2mi;

    public UniprotXrefComparator(Map<String, String> databaseName2mi) {
        this.databaseName2mi = databaseName2mi;
    }

    @Override
    public int compare(UniprotXref o1, UniprotXref o2) {
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;
        String db1 = databaseName2mi.get(o1.getDatabase().toLowerCase());
        String db2 = databaseName2mi.get(o2.getDatabase().toLowerCase());
        if (db1 != null && db2 != null) {
            if (db1.equalsIgnoreCase(db2)) {
                return o1.getAccession().toLowerCase().compareTo(o2.getAccession().toLowerCase());
            } else {
                return db1.compareTo(db2);
            }
        } else if (db1 == null && db2 != null) {
            return AFTER;
        } else if (db1 != null && db2 == null) {
            return BEFORE;
        } else {
            return EQUAL;
        }
    }
}
