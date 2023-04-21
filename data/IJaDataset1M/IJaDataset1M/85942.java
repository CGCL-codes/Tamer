package com.hp.hpl.jena.ontology.daml.impl;

import com.hp.hpl.jena.vocabulary.*;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.rdf.model.impl.*;

/**
 * Contains knowledge of different versions of the DAML vocabulary, to help
 * with managing the different versions of the namespace.
 *
 * @author Ian Dickinson, HP Labs (<a href="mailto:Ian.Dickinson@hp.com">email</a>)
 * @version CVS info: $Id: VocabularyManager.java,v 1.8 2006/03/22 13:52:23 andy_seaborne Exp $
 */
public class VocabularyManager {

    /**
     * Answer the vocabulary that corresponds to the namespace of the
     * given resource. By default, answer the most recent vocabulary.
     *
     * @param resource The RDF resource denoting a namespace
     * @return a DAML vocabulary object for the namespace
     */
    public static DAMLVocabulary getVocabulary(Resource resource) {
        return getDefaultVocabulary();
    }

    /**
     * Answer the vocabulary that corresponds to the namespace of the
     * given URI. By default, answer the most recent vocabulary.
     *
     * @param uri A URI denoting a namespace
     * @return a DAML vocabulary object for the namespace
     */
    public static DAMLVocabulary getVocabulary(String uri) {
        if (uri != null) {
            int splitPoint = Util.splitNamespace(uri);
            String namespace = (splitPoint < 0) ? uri : uri.substring(0, splitPoint);
            if (namespace != null) {
            }
        }
        return getDefaultVocabulary();
    }

    /**
     * Answer the default (latest) vocabulary.
     *
     * @return a DAML+OIL vocabulary
     */
    public static DAMLVocabulary getDefaultVocabulary() {
        return DAML_OIL.getInstance();
    }
}
