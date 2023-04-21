package uk.ac.ebi.intact.util.protein.utils;

import java.util.ArrayList;
import java.util.Collection;

/**
 * TODO comment this
 *
 * @author Catherine Leroy (cleroy@ebi.ac.uk)
 * @version $Id$
 * @since TODO
 */
public class ProteinToDeleteManager {

    private static ThreadLocal<Collection<String>> threadLocal = new ThreadLocal<Collection<String>>() {

        @Override
        protected Collection<String> initialValue() {
            return new ArrayList<String>();
        }
    };

    public static void addProteinAc(String ac) {
        Collection<String> acsToDelete = threadLocal.get();
        acsToDelete.add(ac);
        threadLocal.set(acsToDelete);
    }

    public static Collection<String> getAcToDelete() {
        return threadLocal.get();
    }
}
