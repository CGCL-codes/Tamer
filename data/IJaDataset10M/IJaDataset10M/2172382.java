package edu.ucdavis.genomics.metabolomics.binbase.connector.references.sdf;

import java.util.Map;

/**
 * simple notifier to find inchi codes
 * @author wohlgemuth
 *
 */
public interface InchFinder {

    public void foundInchi(String inchi, Map<Object, Object> map) throws Exception;
}
