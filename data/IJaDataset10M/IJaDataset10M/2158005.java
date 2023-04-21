package it.cnr.stlab.xd.utils;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.semanticweb.owl.model.OWLAnnotation;
import org.semanticweb.owl.model.OWLConstant;
import org.semanticweb.owl.model.OWLEntity;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLTypedConstant;
import org.semanticweb.owl.vocab.OWLRDFVocabulary;

/**
 * @author Enrico Daga
 * 
 */
public class XDAnnotationsProvider {

    OWLEntity entity = null;

    Set<OWLOntology> ontologySet = new HashSet<OWLOntology>();

    public static final String DEFAULT_LANG = "en";

    public XDAnnotationsProvider(OWLEntity entity, Set<OWLOntology> ontologySet) {
        this.entity = entity;
        this.ontologySet.addAll(ontologySet);
    }

    public String getPreferredLabel(boolean shorter) {
        String value = "";
        Set<String> prefLabels = getLabelsForLang(DEFAULT_LANG);
        if (prefLabels.size() == 0) {
            prefLabels.addAll(getValuesFor(getAllLabelsAnnotations()).keySet());
        }
        for (String label : prefLabels) {
            if (value.equals("")) {
                value = label;
                continue;
            } else if (label.length() > value.length()) {
                if (!shorter) value = label;
            }
        }
        if (value.equals("")) value = entity.toString();
        return value;
    }

    @SuppressWarnings("unchecked")
    public Set<OWLAnnotation> getAllLabelsAnnotations() {
        return getAllAnnotations(OWLRDFVocabulary.RDFS_LABEL.getURI());
    }

    @SuppressWarnings("unchecked")
    private Set<OWLAnnotation> getAllAnnotations(URI u) {
        Set<OWLAnnotation> anSet = new HashSet<OWLAnnotation>();
        for (OWLOntology o : this.ontologySet) {
            Set<OWLAnnotation> anns = entity.getAnnotations(o, u);
            anSet.addAll(anns);
        }
        return anSet;
    }

    public Set<String> getLabelsForLang(String lang) {
        return getValuesForLang(getAllLabelsAnnotations(), lang);
    }

    @SuppressWarnings("unchecked")
    public Set<OWLAnnotation> getAllCommentsAnnotations() {
        return getAllAnnotations(OWLRDFVocabulary.RDFS_COMMENT.getURI());
    }

    @SuppressWarnings("unchecked")
    public Set<String> getCommentsForLang(String lang) {
        return getValuesForLang(getAllCommentsAnnotations(), lang);
    }

    @SuppressWarnings("unchecked")
    private Set<String> getValuesForLang(Set<OWLAnnotation> annotations, String lang) {
        Set<String> values = new HashSet<String>();
        for (OWLAnnotation a : annotations) {
            OWLConstant c = a.getAnnotationValueAsConstant();
            if (!c.isTyped()) {
                if (c.asOWLUntypedConstant().getLang() == lang) {
                    values.add(c.getLiteral());
                }
            }
        }
        return values;
    }

    /**
     * returns a Map where the key is the Literal and the Value is the Language
     * (if any)
     * 
     * @param annotations
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> getValuesFor(Set<OWLAnnotation> annotationSet) {
        Map<String, String> vMap = new HashMap<String, String>();
        for (OWLAnnotation a : annotationSet) {
            if (!a.getAnnotationValueAsConstant().isTyped()) {
                vMap.put(a.getAnnotationValueAsConstant().asOWLUntypedConstant().getLiteral(), a.getAnnotationValueAsConstant().asOWLUntypedConstant().getLang());
            } else {
                OWLTypedConstant tc = a.getAnnotationValueAsConstant().asOWLTypedConstant();
                vMap.put(tc.getLiteral(), "");
            }
        }
        return vMap;
    }

    public Map<String, String> getAllLabels() {
        return getValuesFor(getAllLabelsAnnotations());
    }

    public Map<String, String> getAllComments() {
        return getValuesFor(getAllCommentsAnnotations());
    }
}
