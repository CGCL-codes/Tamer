package uk.ac.ebi.intact.commons.dataset;

import java.io.InputStream;
import java.util.Collection;

/**
 * Implemented for those classes that are datasets (for testing)
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id$
 */
@Deprecated
public interface TestDataset {

    String getId();

    InputStream getSource();

    boolean containsAllCVs();

    Collection<String> getAvailableIds();
}
