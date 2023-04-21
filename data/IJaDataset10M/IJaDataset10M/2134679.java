package org.coode.annotate;

import org.apache.log4j.Logger;
import org.coode.annotate.prefs.AnnotationTemplateDescriptor;
import org.coode.annotate.prefs.AnnotationTemplatePrefs;
import org.protege.editor.owl.model.OWLModelManager;
import org.semanticweb.owlapi.model.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.IOException;
import java.util.*;

/**
 * Author: Nick Drummond<br>
 * http://www.cs.man.ac.uk/~drummond/<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Aug 16, 2007<br><br>
 */
public class TemplateModel {

    private static final Logger logger = Logger.getLogger(TemplateModel.class.getName());

    private final java.util.List<TemplateRow> cList = new ArrayList<TemplateRow>();

    private final AnnotationComponentComparator comparator = new AnnotationComponentComparator();

    private final Set<TemplateModelListener> listeners = new HashSet<TemplateModelListener>();

    private final OWLModelManager mngr;

    private OWLAnnotationSubject subject;

    private OWLOntologyChangeListener ontChangeListener = new OWLOntologyChangeListener() {

        public void ontologiesChanged(List<? extends OWLOntologyChange> list) throws OWLException {
            handleOntologyChanges(list);
        }
    };

    private ChangeListener layoutChangeListener = new ChangeListener() {

        public void stateChanged(ChangeEvent event) {
            refresh();
        }
    };

    public TemplateModel(OWLModelManager mngr) throws IOException {
        this.mngr = mngr;
        mngr.addOntologyChangeListener(ontChangeListener);
        AnnotationTemplatePrefs.getInstance().addChangeListener(layoutChangeListener);
    }

    public EditorType getComponentType(OWLAnnotationProperty property) {
        return getDefaultDescriptor().getEditor(property);
    }

    public Set<OWLAnnotationAssertionAxiom> getAnnotations(OWLAnnotationSubject annotationSubject) {
        Set<OWLAnnotationAssertionAxiom> annotations = new HashSet<OWLAnnotationAssertionAxiom>();
        for (OWLOntology ont : mngr.getActiveOntologies()) {
            annotations.addAll(ont.getAnnotationAssertionAxioms(annotationSubject));
        }
        return annotations;
    }

    public Set<OWLOntology> getOntologiesContainingAnnotation(TemplateRow templateRow) {
        Set<OWLOntology> onts = new HashSet<OWLOntology>();
        OWLAxiom ax = templateRow.getAxiom();
        if (ax != null) {
            for (OWLOntology ont : mngr.getActiveOntologies()) {
                if (ont.containsAxiom(ax)) {
                    onts.add(ont);
                }
            }
        }
        return onts;
    }

    public OWLModelManager getOWLModelManager() {
        return mngr;
    }

    public OWLAnnotationSubject getSubject() {
        return subject;
    }

    private void handleOntologyChanges(List<? extends OWLOntologyChange> changes) {
        for (OWLOntologyChange change : changes) {
            if (change.isAxiomChange() && change.getAxiom().isOfType(AxiomType.ANNOTATION_ASSERTION) && ((OWLAnnotationAssertionAxiom) change.getAxiom()).getSubject().equals(subject)) {
                refresh();
                return;
            }
        }
    }

    private void refresh() {
        setSubject(subject);
    }

    public void setSubject(OWLAnnotationSubject annotationSubject) {
        subject = annotationSubject;
        cList.clear();
        if (annotationSubject != null) {
            final List<OWLAnnotationProperty> properties = getDefaultDescriptor().getProperties();
            Set<OWLAnnotationAssertionAxiom> annots = getAnnotations(annotationSubject);
            Set<OWLAnnotationProperty> usedProperties = new HashSet<OWLAnnotationProperty>();
            for (OWLAnnotationAssertionAxiom annot : annots) {
                final OWLAnnotationProperty property = annot.getAnnotation().getProperty();
                if (properties.contains(property)) {
                    usedProperties.add(property);
                    cList.add(new TemplateRow(annot, this));
                }
            }
            for (OWLAnnotationProperty property : properties) {
                if (!usedProperties.contains(property)) {
                    cList.add(new TemplateRow(annotationSubject, property, this));
                }
            }
            Collections.sort(cList, comparator);
        }
        notifyStructureChanged();
    }

    public List<TemplateRow> getRows() {
        return Collections.unmodifiableList(cList);
    }

    public void addRow(OWLAnnotationProperty property) {
        cList.add(new TemplateRow(subject, property, this));
        Collections.sort(cList, comparator);
        notifyStructureChanged();
    }

    public void removeRow(TemplateRow c) {
        c.setValue(null);
        cList.remove(c);
        notifyStructureChanged();
    }

    private void notifyStructureChanged() {
        for (TemplateModelListener l : listeners) {
            l.modelStructureChanged();
        }
    }

    public void addModelListener(TemplateModelListener l) {
        listeners.add(l);
    }

    public void removeModelListener(TemplateModelListener l) {
        listeners.remove(l);
    }

    /**
     * Called by the rows when they make changes
     * (so that the table can manage itself without having to refresh completely)
     * This implementation disables the ontology change listeners temporarily
     *
     * @param changes the set of changes to apply
     */
    public void requestApplyChanges(List<OWLOntologyChange> changes) {
        if (!changes.isEmpty()) {
            mngr.removeOntologyChangeListener(ontChangeListener);
            mngr.applyChanges(changes);
            mngr.addOntologyChangeListener(ontChangeListener);
        }
    }

    public void dispose() {
        mngr.removeOntologyChangeListener(ontChangeListener);
        AnnotationTemplatePrefs.getInstance().removeChangeListener(layoutChangeListener);
        listeners.clear();
    }

    private AnnotationTemplateDescriptor getDefaultDescriptor() {
        return AnnotationTemplatePrefs.getInstance().getDefaultDescriptor(mngr.getOWLDataFactory());
    }

    class AnnotationComponentComparator implements Comparator<TemplateRow> {

        public int compare(TemplateRow c1, TemplateRow c2) {
            OWLAnnotationProperty uri1 = c1.getProperty();
            OWLAnnotationProperty uri2 = c2.getProperty();
            for (OWLAnnotationProperty uri : getDefaultDescriptor().getProperties()) {
                if (uri.equals(uri1)) {
                    return -1;
                }
                if (uri.equals(uri2)) {
                    return 1;
                }
            }
            return 0;
        }
    }
}
