package uk.ac.ebi.rhea.mapper.util;

import java.util.Collection;
import java.util.Collections;

/**
 * Dummy helper which returns the same compound name (white space trimmed).
 * @author rafalcan
 *
 */
public class DummyCompoundNameHelper implements ICompoundNameHelper {

    public String getCleanName(String compoundText) {
        return compoundText.trim();
    }

    public Collection<String> getNameVariants(String compoundText) {
        return Collections.singleton(compoundText.trim());
    }
}
