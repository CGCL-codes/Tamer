package hu.gbalage.owlforms.internal;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import org.semanticweb.owl.model.OWLAnnotation;
import org.semanticweb.owl.model.OWLEntity;
import org.semanticweb.owl.model.OWLObject;
import org.semanticweb.owl.model.OWLOntology;
import hu.gbalage.owlforms.OWLFormsManager;

/**
 * @author Grill Balazs (balage.g@gmail.com)
 *
 */
public class AnnotationCollector {

    OWLFormsManager manager;

    public AnnotationCollector(OWLFormsManager manager) {
        this.manager = manager;
    }

    @SuppressWarnings("unchecked")
    public Set<OWLAnnotation<OWLObject>> collect(OWLEntity entity, URI annotationuri) {
        Set<OWLAnnotation<OWLObject>> annotations = new HashSet<OWLAnnotation<OWLObject>>();
        for (OWLOntology ontology : manager.getReasoner().getLoadedOntologies()) {
            Set<OWLAnnotation> as = entity.getAnnotations(ontology, annotationuri);
            for (OWLAnnotation a : as) annotations.add(a);
        }
        return annotations;
    }

    @SuppressWarnings("unchecked")
    public void logAnnotations(OWLEntity entity) {
        manager.getLog().debug("Annotations on " + entity.getURI() + ":");
        for (OWLOntology ontology : manager.getReasoner().getLoadedOntologies()) {
            Set<OWLAnnotation> as = entity.getAnnotations(ontology);
            for (OWLAnnotation a : as) manager.getLog().debug("    " + a.getAnnotationURI());
        }
    }
}
