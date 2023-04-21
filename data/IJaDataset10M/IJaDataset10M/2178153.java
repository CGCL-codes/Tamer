package uk.ac.ebi.intact.application.dataConversion.psiUpload.checker;

import uk.ac.ebi.intact.application.dataConversion.psiUpload.model.EntrySetTag;
import uk.ac.ebi.intact.application.dataConversion.psiUpload.model.EntryTag;
import uk.ac.ebi.intact.business.IntactHelper;
import uk.ac.ebi.intact.util.BioSourceFactory;
import uk.ac.ebi.intact.util.UpdateProteinsI;
import java.util.Collection;
import java.util.Iterator;

/**
 * That class .
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @version $Id: EntrySetChecker.java 5204 2006-07-05 01:00:41Z baranda $
 */
public final class EntrySetChecker {

    public static void check(final EntrySetTag entrySet, final UpdateProteinsI proteinFactory, final BioSourceFactory bioSourceFactory) {
        final Collection entries = entrySet.getEntries();
        for (Iterator iterator = entries.iterator(); iterator.hasNext(); ) {
            final EntryTag entry = (EntryTag) iterator.next();
            EntryChecker.check(entry, proteinFactory, bioSourceFactory);
        }
    }
}
