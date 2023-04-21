package apollo.gui.featuretree;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import apollo.datamodel.FeatureSetI;
import apollo.datamodel.SeqFeatureI;
import apollo.editor.AnnotationChangeEvent;
import apollo.editor.AnnotationChangeListener;
import apollo.editor.FeatureChangeEvent;
import apollo.dataadapter.DataLoadEvent;
import apollo.dataadapter.DataLoadListener;
import apollo.gui.ControlledObjectI;
import apollo.gui.Controller;
import apollo.gui.FeatureNavigationI;
import apollo.gui.event.BaseFocusEvent;
import apollo.gui.event.BaseFocusListener;
import apollo.gui.event.FeatureSelectionEvent;
import apollo.gui.event.FeatureSelectionListener;
import apollo.gui.synteny.GuiCurationState;
import apollo.util.FeatureList;

/**
 * A scrollpane containing a SWING tree of the annotations (not related to the
 * FeatureTree or FeatureTreeNode).
 */
public class FeatureTreeFrame extends JFrame implements BaseFocusListener, ControlledObjectI, FeatureSelectionListener, AnnotationChangeListener, DataLoadListener, FeatureNavigationI {

    protected FeatureTreePanel panel = null;

    private GuiCurationState curationState;

    public FeatureTreeFrame(FeatureSetI feature_set, GuiCurationState curationState) {
        setController(curationState.getController());
        setTitle("Annotation Tree");
        this.getContentPane().setLayout(new BorderLayout());
        this.curationState = curationState;
        panel = new FeatureTreePanel(this, curationState);
        panel.setFeatureSet(feature_set);
        this.getContentPane().add(panel, BorderLayout.CENTER);
        this.setSize(new Dimension(400, 300));
    }

    public void setFeatureSet(FeatureSetI fs) {
        panel.setFeatureSet(fs);
    }

    public boolean handleAnnotationChangeEvent(AnnotationChangeEvent evt) {
        if (evt.isEndOfEditSession()) {
            panel.updateFeatureSet();
            panel.findObject(evt.getAnnotTop());
        }
        return true;
    }

    public boolean handleFeatureSelectionEvent(FeatureSelectionEvent evt) {
        if (isVisible() && evt.getSource() != this) {
            selectFeatures(evt.getFeatures());
        }
        return false;
    }

    private void selectFeatures(FeatureList features) {
        if (features.size() != 0) {
            for (int i = 0; i < features.size(); i++) {
                SeqFeatureI sf = features.getFeature(i);
                panel.findObject(sf);
            }
        } else {
            panel.findObject(null);
        }
    }

    public void featureSelected(SeqFeatureI sf) {
        fireFeatureSelectionEvent(sf);
    }

    /** the one method that a featureNavigator must support
      Takes whatever action is needed when a feature is
      selected in the tree */
    public void fireBaseFocusEvent(int position, SeqFeatureI sf) {
        BaseFocusEvent evt = new BaseFocusEvent(this, position, sf);
        getController().handleBaseFocusEvent(evt);
    }

    /** The SeqFeature passed to this might be a single exon,
      a transcript (FeatureSet of exons), or an annotation/gene
      (FeatureSet of FeatureSet of exons). The problem is that
      if sf is an annotation/gene, the Type ends up being NO_TYPE.
      I'm not sure why this is--you can rubberband several
      transcripts and they appear properly in the detail panel.
      I tried iterating through the transcripts that are features
      of the annotation and calling handleFeatureSelectionEvent
      for each one, but then if there are multiple transcripts,
      we end up just selecting the LAST one.  This is wrong,
      but it's not the end of the world-- it doesn't die, and
      you do at least select one of the transcripts of that gene.
      But if possible, it should be debugged.  --NH, 02/04/2002

      The problem was in EvidencePanel and SetDetailPanel as they
      were not handling genes properly. This is now fixed so I
      reverted the temp fix noted in the above comment. - MG 2/28/02
  */
    public void fireFeatureSelectionEvent(SeqFeatureI sf) {
        if (!panel.remoteUpdate()) {
            if (panel.endToggling() == true) {
                fireBaseFocusEvent(sf.getLow(), sf);
            } else {
                fireBaseFocusEvent(sf.getHigh(), sf);
            }
        }
        curationState.getSelectionManager().select(sf, this);
    }

    public void setController(Controller controller) {
        controller.addListener(this);
    }

    public Controller getController() {
        return curationState.getController();
    }

    public Object getControllerWindow() {
        return this;
    }

    public boolean needsAutoRemoval() {
        return false;
    }

    public boolean handleBaseFocusEvent(BaseFocusEvent evt) {
        repaint();
        return false;
    }

    public void addNotify() {
        super.addNotify();
        setController(getController());
    }

    public boolean handleDataLoadEvent(DataLoadEvent e) {
        if (e.dataRetrievalBeginning()) {
            setFeatureSet(null);
        } else if (e.dataRetrievalDone()) {
            setFeatureSet(e.getCurationSet().getAnnots());
        }
        return true;
    }
}
