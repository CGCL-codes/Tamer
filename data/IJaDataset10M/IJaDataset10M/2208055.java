package apollo.gui.drawable;

import java.util.*;
import java.awt.*;
import apollo.datamodel.*;
import apollo.gui.genomemap.FeatureTierManager;
import apollo.gui.Transformer;
import apollo.gui.schemes.*;
import apollo.gui.event.AnnotationChangeEvent;

/**
 * A Drawable for drawing annotation feature sets.
 */
public class DrawableAnnotatedFeatureSet extends DrawableFeatureSet implements DrawableAnnotationConstants, GenericAnnotationSetI {

    public DrawableAnnotatedFeatureSet() {
        super();
    }

    public DrawableAnnotatedFeatureSet(FeatureSetI fset) {
        super(fset);
    }

    public void addDecorations(Graphics g, Rectangle boxBounds, Transformer transformer, FeatureTierManager manager) {
        super.addDecorations(g, boxBounds, transformer, manager);
    }

    public String getDisplayLabel() {
        String name = null;
        if (getFeature() instanceof GenericAnnotationI) {
            GenericAnnotationI gi = (GenericAnnotationI) getFeature();
            name = gi.getName();
            SeqFeatureI parent = gi.getRefFeature();
            Font label_font;
            if (parent != null) {
                if (!parent.getBioType().equals("gene")) {
                    name = parent.getName();
                } else {
                    if (parent.getName().equals(parent.getId())) {
                        if (!name.startsWith(parent.getName())) name = parent.getName() + ":" + gi.getName(); else name = gi.getName();
                    } else {
                        if (name.startsWith(parent.getName())) name = "(" + parent.getId() + "):" + gi.getName(); else if (name.startsWith(parent.getId())) name = parent.getName() + ":" + gi.getName(); else name = (parent.getName() + "(" + parent.getId() + ")" + ":" + gi.getName());
                    }
                }
            }
        }
        return name;
    }

    public Vector getTopAnnotations() {
        return getTopAnnotations(new Vector());
    }

    public Vector getTopAnnotations(Vector v) {
        for (int i = 0; i < size(); i++) {
            DrawableSeqFeature dsf = (DrawableSeqFeature) getFeatureAt(i);
            if (dsf.getFeature() instanceof GenericAnnotationSetI) {
                v.addElement(dsf.getFeature());
            } else if (dsf instanceof DrawableAnnotatedFeatureSet) {
                ((DrawableAnnotatedFeatureSet) dsf).getTopAnnotations(v);
            }
        }
        return v;
    }

    public Identifier getIdentifier() {
        return ((GenericAnnotationSetI) getFeature()).getIdentifier();
    }

    public void setIdentifier(Identifier id) {
        ((GenericAnnotationSetI) getFeature()).setIdentifier(id);
    }

    public Vector getComments() {
        return ((GenericAnnotationSetI) getFeature()).getComments();
    }

    public void addComment(Comment comm) {
        ((GenericAnnotationSetI) getFeature()).addComment(comm);
    }

    public void deleteComment(Comment comm) {
        ((GenericAnnotationSetI) getFeature()).deleteComment(comm);
    }

    public void clearComments() {
        ((GenericAnnotationSetI) getFeature()).clearComments();
    }

    public Vector getSynonyms() {
        return ((GenericAnnotationSetI) getFeature()).getSynonyms();
    }

    public void addSynonym(String syn) {
        ((GenericAnnotationSetI) getFeature()).addSynonym(syn);
    }

    public void deleteSynonym(String syn) {
        ((GenericAnnotationSetI) getFeature()).deleteSynonym(syn);
    }

    public void clearSynonyms() {
        ((GenericAnnotationSetI) getFeature()).clearSynonyms();
    }

    public boolean isProblematic() {
        return ((GenericAnnotationSetI) getFeature()).isProblematic();
    }

    public void setIsProblematic(boolean icky) {
        ((GenericAnnotationSetI) getFeature()).setIsProblematic(icky);
    }

    public Vector getdbXrefs() {
        return ((GenericAnnotationSetI) getFeature()).getdbXrefs();
    }

    public Vector getEvidence() {
        return ((GenericAnnotationI) getFeature()).getEvidence();
    }

    public void addEvidence(String evidenceId) {
        ((GenericAnnotationI) getFeature()).addEvidence(evidenceId);
    }

    public void addEvidence(String evidenceId, int type) {
        ((GenericAnnotationI) getFeature()).addEvidence(evidenceId, type);
    }

    public int deleteEvidence(String evidenceId) {
        return ((GenericAnnotationI) getFeature()).deleteEvidence(evidenceId);
    }

    public void setEvidenceFinder(EvidenceFinder ef) {
        ((GenericAnnotationI) getFeature()).setEvidenceFinder(ef);
        for (int i = 0; i < size(); i++) {
            DrawableSeqFeature dsf = (DrawableSeqFeature) getFeatureAt(i);
            if (dsf instanceof DrawableAnnotatedSeqFeature) {
                ((DrawableAnnotatedSeqFeature) dsf).setEvidenceFinder(ef);
            } else if (dsf instanceof DrawableAnnotatedFeatureSet) {
                ((DrawableAnnotatedFeatureSet) dsf).setEvidenceFinder(ef);
            }
        }
    }

    public EvidenceFinder getEvidenceFinder() {
        return ((GenericAnnotationI) getFeature()).getEvidenceFinder();
    }

    public DrawableFeatureSet repairFeatureSet(AnnotationChangeEvent ace) {
        DrawableAnnotatedFeatureSet repair = (DrawableAnnotatedFeatureSet) super.repairFeatureSet(ace);
        if (repair != null && repair.getRefFeature() != null && repair.getRefFeature() instanceof GenericAnnotationI) {
            GenericAnnotationI annots = (GenericAnnotationI) repair.getRefFeature();
            repair.setEvidenceFinder(annots.getEvidenceFinder());
        } else {
            if (repair != null && repair.getRefFeature() != null) System.out.println("Gene " + repair.getFeature().getName() + " topped not by GenericAnnotationI, but by " + repair.getRefFeature().getClass().getName());
        }
        return repair;
    }
}
