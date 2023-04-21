package apollo.gui.menus;

import apollo.editor.AnnotationEditor;
import apollo.config.Config;
import apollo.config.FeatureProperty;
import apollo.config.PropertyScheme;
import apollo.config.DisplayPrefsI;
import apollo.gui.*;
import apollo.gui.genomemap.ApolloPanelI;
import apollo.gui.genomemap.AnnotationView;
import apollo.gui.genomemap.FeatureView;
import apollo.gui.genomemap.FeatureTierManager;
import apollo.gui.genomemap.ResultView;
import apollo.gui.genomemap.StrandedZoomableApolloPanel;
import apollo.gui.synteny.CurationManager;
import apollo.gui.drawable.DrawableSeqFeature;
import apollo.gui.detailviewers.seqexport.SeqExport;
import apollo.gui.detailviewers.blixem.*;
import apollo.gui.detailviewers.PropertyDisplay;
import apollo.util.*;
import apollo.datamodel.*;
import apollo.dataadapter.*;
import apollo.seq.io.FastaFile;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.log4j.*;

public class TierPopupMenu extends JPopupMenu implements ActionListener {

    protected static final Logger logger = LogManager.getLogger(TierPopupMenu.class);

    private static String no_tag = "None";

    ResultView resultView;

    ApolloPanelI ap;

    JMenuItem collapseTier;

    JMenuItem expandTier;

    JMenuItem showLabel;

    JMenuItem hideLabel;

    JMenuItem hideTier;

    JMenuItem changeColor;

    JMenuItem typeSettings;

    AnnotationEditor editor = null;

    Selection selection;

    JMenuItem dnaBlixem;

    JMenuItem protBlixem;

    JMenuItem getData;

    JMenuItem set5Prime;

    JMenuItem set3Prime;

    JMenuItem setEnds;

    JMenuItem createGeneTrans;

    TypesMenu typesMenu;

    JMenuItem addTranscript;

    JMenuItem flipResult;

    JMenuItem sequence;

    JMenuItem props;

    private JMenuItem loadSyntenyLink;

    JMenuItem cancel;

    Hashtable resultTags = new Hashtable();

    private AlignMenuItems jalviewMenus;

    Point pos;

    JMenuItem preferences;

    ButtonGroup tag_group;

    private class CopyAction implements ActionListener {

        JTextArea area;

        public CopyAction(JTextArea area) {
            this.area = area;
        }

        public void actionPerformed(ActionEvent e) {
            ClipboardUtil.copyTextToClipboard(area.getSelectedText());
        }
    }

    public TierPopupMenu(ApolloPanelI ap, ResultView resultView, Selection selection, Point pos) {
        super("Tier operations");
        this.ap = ap;
        this.resultView = resultView;
        this.pos = pos;
        this.selection = selection;
        AnnotationView av = resultView.getAnnotationView();
        if (av != null) {
            editor = av.getAnnotationEditor();
            FeatureSetI avTopModel = av.getGeneHolder();
            Selection cursorSel = selection.getSelectionDescendedFromModel(avTopModel, true);
            editor.setSelections(av, selection, cursorSel.getSelectedVector(), av.getTransform().toUser(pos).x, av.getStrand());
        } else {
            logger.warn("getAnnotationView returned null in TierPopupMenu");
        }
        menuInit();
    }

    public TierPopupMenu(ApolloPanelI ap, ResultView resultView, Point pos) {
        this(ap, resultView, null, pos);
    }

    private boolean sequenceAllowed() {
        return true;
    }

    private ApolloFrame getApolloFrame() {
        return ApolloFrame.getApolloFrame();
    }

    public void menuInit() {
        dnaBlixem = new JMenuItem("Blixem on DNA hits");
        protBlixem = new JMenuItem("Blixem on protein hits");
        changeColor = new JMenuItem("Change color of this feature type");
        typeSettings = new JMenuItem("Settings for this feature type");
        collapseTier = new JMenuItem("Collapse tier");
        expandTier = new JMenuItem("Expand tier");
        showLabel = new JMenuItem("Show Label");
        hideLabel = new JMenuItem("Hide Label");
        hideTier = new JMenuItem("Hide tier");
        getData = new JMenuItem("Get info about this feature via Web");
        loadSyntenyLink = new JMenuItem("Bring up link as other species in synteny");
        set5Prime = new JMenuItem("Set as 5' end");
        set3Prime = new JMenuItem("Set as 3' end");
        setEnds = new JMenuItem("Set both ends");
        typesMenu = new TypesMenu(editor);
        createGeneTrans = new JMenuItem("Add as gene transcript");
        addTranscript = new JMenuItem("Add as new transcript to selected gene");
        flipResult = new JMenuItem("Move to other strand");
        sequence = new JMenuItem("Sequence...");
        props = new JMenuItem("Print this feature's properties");
        cancel = new JMenuItem("Close menu");
        preferences = new JMenuItem("Preferences");
        add(sequence);
        add(getData);
        add(props);
        props.setEnabled(true);
        addSeparator();
        if (Config.getStyle().addSyntenyResultMenuItem()) {
            add(loadSyntenyLink);
            loadSyntenyLink.setEnabled(enableSyntenyLink());
        }
        if (Config.getBlixemLocation() != null && apollo.util.IOUtil.isUnix()) {
            add(dnaBlixem);
            add(protBlixem);
        }
        FeatureSetI annotTop = resultView.getAnnotationView().getTopModel();
        FeatureSetI resultTop = resultView.getTopModel();
        jalviewMenus = new AlignMenuItems(annotTop, resultTop, selection, ap.getController());
        add(jalviewMenus.getAlignSelectionMenuItem());
        add(jalviewMenus.getAlignRegionMenuItem());
        addSeparator();
        if (apollo.config.Config.isEditingEnabled()) {
            add(set5Prime);
            add(set3Prime);
            add(setEnds);
            addSeparator();
            add(typesMenu);
            add(createGeneTrans);
            add(addTranscript);
            add(flipResult);
            if (selection.size() == 1) addTagItems(selection.getSelectedData(0));
            addSeparator();
        }
        add(preferences);
        add(changeColor);
        add(typeSettings);
        add(collapseTier);
        add(expandTier);
        add(showLabel);
        add(hideLabel);
        add(hideTier);
        if (editor == null) {
            set5Prime.setEnabled(false);
            set3Prime.setEnabled(false);
            setEnds.setEnabled(false);
            typesMenu.setEnabled(false);
            createGeneTrans.setEnabled(false);
            addTranscript.setEnabled(false);
            flipResult.setEnabled(false);
            preferences.setEnabled(false);
            changeColor.setEnabled(false);
            typeSettings.setEnabled(false);
            collapseTier.setEnabled(false);
            expandTier.setEnabled(false);
            showLabel.setEnabled(false);
            hideLabel.setEnabled(false);
            hideTier.setEnabled(false);
        } else {
            set5Prime.setEnabled(editor.setExonTerminusAllowed());
            set3Prime.setEnabled(editor.setExonTerminusAllowed());
            setEnds.setEnabled(editor.setExonTerminusAllowed());
            typesMenu.setEnabled(editor.addGeneOrTranscriptAllowed());
            createGeneTrans.setEnabled(editor.addGeneOrTranscriptAllowed());
            addTranscript.setEnabled(editor.addTranscriptAllowed());
            flipResult.setEnabled(editor.resultIsSelected());
            preferences.setEnabled(editor.resultIsSelected());
            changeColor.setEnabled(editor.resultIsSelected());
            typeSettings.setEnabled(editor.resultIsSelected());
            collapseTier.setEnabled(editor.resultIsSelected());
            expandTier.setEnabled(editor.resultIsSelected());
            showLabel.setEnabled(editor.resultIsSelected());
            hideLabel.setEnabled(editor.resultIsSelected());
            hideTier.setEnabled(editor.resultIsSelected());
        }
        getData.setEnabled(selection.size() >= 1);
        boolean have_seq = false;
        if (selection.size() > 0) {
            SeqFeatureI firstFeat = selection.getFeature(0);
            SequenceI seq = firstFeat.getRefSequence();
            have_seq = (seq != null && (seq.isLazy() || seq.getResidues() != null));
        }
        sequence.setEnabled(have_seq);
        add(new ShowMenu(resultView, pos));
        addSeparator();
        add(cancel);
        dnaBlixem.addActionListener(this);
        protBlixem.addActionListener(this);
        set5Prime.addActionListener(this);
        set3Prime.addActionListener(this);
        setEnds.addActionListener(this);
        createGeneTrans.addActionListener(this);
        addTranscript.addActionListener(this);
        flipResult.addActionListener(this);
        getData.addActionListener(this);
        props.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new PropertyDisplay(selection);
            }
        });
        sequence.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new SeqExport(selection, ap.getController());
            }
        });
        loadSyntenyLink.addActionListener(this);
        cancel.addActionListener(this);
        collapseTier.addActionListener(this);
        expandTier.addActionListener(this);
        showLabel.addActionListener(this);
        hideLabel.addActionListener(this);
        hideTier.addActionListener(this);
        preferences.addActionListener(this);
        changeColor.addActionListener(this);
        typeSettings.addActionListener(this);
        collapseTier.setMnemonic('O');
        expandTier.setMnemonic('P');
        hideTier.setMnemonic('H');
    }

    private Vector getFeatureSets(Vector exons) {
        Hashtable out = new Hashtable();
        Vector v = new Vector();
        if (exons != null) {
            for (int i = 0; i < exons.size(); i++) {
                SeqFeatureI feature = (SeqFeatureI) exons.elementAt(i);
                feature = getFeatureSet(feature);
                if (feature != null && feature.getId() != null) out.put(feature.getId(), feature);
            }
            Enumeration e = out.elements();
            while (e.hasMoreElements()) v.addElement(e.nextElement());
        }
        return v;
    }

    private FeatureSetI getFeatureSet(SeqFeatureI feature) {
        if (feature == null) return null; else if (feature instanceof DrawableSeqFeature) return getFeatureSet(((DrawableSeqFeature) feature).getFeature()); else if (feature.canHaveChildren()) return (FeatureSetI) feature; else return getFeatureSet(feature.getRefFeature());
    }

    public void actionPerformed(ActionEvent e) {
        FeatureTierManager tm = (FeatureTierManager) resultView.getTierManager();
        if (e.getSource() == dnaBlixem || e.getSource() == protBlixem) {
            StrandedZoomableApolloPanel szap = ap.getStrandedZoomableApolloPanel();
            Vector types = getUniqueListOfAllTypesUsedInSelection();
            int centre = getCentreOfSelection();
            int resType = ((e.getSource() == dnaBlixem) ? BlixemRunner.DNA : BlixemRunner.PROTEIN);
            logger.info("running blixem");
            BlixemRunner br = new BlixemRunner(szap.getCurationSet(), types, centre, resType);
            br.run();
        } else if (e.getSource() == set5Prime) {
            editor.setAs5Prime();
        } else if (e.getSource() == getData) {
            String url = makeURLForFeature();
            if (url != null) {
                logger.debug("loading web page " + url);
                HTMLUtil.loadIntoBrowser(url);
            }
        } else if (e.getSource() == set3Prime) {
            editor.setAs3Prime();
        } else if (e.getSource() == setEnds) {
            editor.setAsBothEnds();
        } else if (e.getSource() == createGeneTrans) {
            editor.addGeneOrTranscript();
        } else if (e.getSource() == addTranscript) {
            editor.addTranscript();
        } else if (e.getSource() == flipResult) {
            editor.flipResult();
        } else if (resultTags.get(e.getSource()) != null) {
            setTag((JRadioButtonMenuItem) e.getSource());
        } else if (e.getSource() == loadSyntenyLink) {
            getApolloFrame().loadSyntenyLink(selection.getSelectedData(0));
        } else if (e.getSource() == collapseTier) {
            tm.collapseTier(resultView, ap);
        } else if (e.getSource() == expandTier) {
            tm.expandTier(resultView, ap);
        } else if (e.getSource() == showLabel) {
            tm.showLabelTier(resultView, ap);
        } else if (e.getSource() == hideLabel) {
            tm.hideLabelTier(resultView, ap);
        } else if (e.getSource() == hideTier) {
            tm.hideTier(resultView, ap);
            ap.clearSelection();
        } else if (e.getSource() == changeColor) {
            tm.changeTypeColor(resultView, selection);
        } else if (e.getSource() == typeSettings) {
            tm.editTypeSettings(resultView, selection);
        } else if (e.getSource() == preferences) {
            PreferenceWindow.getInstance(selection).setVisible(true);
        }
        StrandedZoomableApolloPanel szap = CurationManager.getActiveCurationState().getSZAP();
        szap.setViewColours();
        szap.setAnnotations(szap.getCurationSet());
        szap.setFeatureSet(szap.getCurationSet());
        szap.setAnnotationViewsVisible(Config.getStyle().getShowAnnotations());
        szap.setResultViewsVisible(Config.getStyle().getShowResults());
    }

    /** Returns a vector with a unique list of all types being used in 
      selection in resultView. */
    private Vector getUniqueListOfAllTypesUsedInSelection() {
        Vector v = new Vector();
        v.addAll(ap.getSelection().getSelectedVisualTypes());
        return v;
    }

    public int getCentreOfSelection() {
        int min = -1;
        int max = -1;
        if (resultView instanceof FeatureView) {
            FeatureView fv = (FeatureView) resultView;
            Selection s = fv.getViewSelection(ap.getSelection());
            for (int i = 0; i < s.size(); i++) {
                SeqFeatureI sf = s.getFeature(i);
                if (sf.getLow() < min || min == -1) {
                    min = sf.getLow();
                }
                if (sf.getHigh() > max || max == -1) {
                    max = sf.getHigh();
                }
            }
        }
        return ((max - min) + min);
    }

    private void addTagItems(SeqFeatureI sf) {
        if (sf instanceof FeaturePair) {
            sf = (SeqFeatureI) sf.getRefFeature();
        }
        resultTags.clear();
        Hashtable tags = (Config.getStyle()).getResultTags();
        Enumeration e = tags.keys();
        while (e.hasMoreElements()) {
            String result_type = (String) e.nextElement();
            FeatureProperty fp = Config.getPropertyScheme().getFeatureProperty(sf.getFeatureType());
            if (fp.getDisplayType().equals(result_type)) {
                Vector result_tags = (Vector) tags.get(result_type);
                if (!result_tags.contains(no_tag)) result_tags.addElement(no_tag);
                String current_tag = sf.getProperty("tag");
                if (current_tag == null || current_tag.equals("")) {
                    current_tag = no_tag;
                }
                JMenu tag_menu = new JMenu("Change tag from \"" + current_tag + "\"");
                tag_group = new ButtonGroup();
                for (int i = 0; i < result_tags.size(); i++) {
                    String text = (String) result_tags.elementAt(i);
                    JRadioButtonMenuItem tag = new JRadioButtonMenuItem(text);
                    tag.setEnabled(true);
                    tag.addActionListener(this);
                    tag_menu.add(tag);
                    tag_group.add(tag);
                    tag.setSelected(text.equals(current_tag));
                    resultTags.put(tag, sf);
                }
                add(tag_menu);
            }
        }
    }

    private void setTag(JRadioButtonMenuItem item) {
        SeqFeatureI sf = selection.getSelectedData(0);
        SeqFeatureI orig_sf = sf;
        if (orig_sf instanceof FeaturePair) {
            sf = (SeqFeatureI) orig_sf.getRefFeature();
        }
        String text = item.getText();
        if (!text.equals(no_tag)) {
            sf.replaceProperty("tag", text);
            Vector spans = sf.getFeatures();
            for (int i = 0; i < spans.size(); i++) {
                SeqFeatureI span = (SeqFeatureI) spans.elementAt(i);
                if (span.getProperty("tag") != null && !span.getProperty("tag").equals("")) {
                    span.replaceProperty("tag", text);
                }
                SeqFeatureI query = ((FeaturePair) span).getQueryFeature();
                SeqFeatureI subject = ((FeaturePair) span).getHitFeature();
                if (query.getProperty("tag") != null && !query.getProperty("tag").equals("")) query.replaceProperty("tag", text);
                subject.replaceProperty("tag", text);
            }
            if (resultView instanceof ResultView) {
                ResultView rv = (ResultView) resultView;
                rv.setInvalidity(true);
                rv.getComponent().repaint(rv.getBounds().x, rv.getBounds().y, rv.getBounds().width, rv.getBounds().height * 2);
            }
        } else {
            sf.removeProperty("tag");
        }
    }

    private String makeURLForFeature() {
        DisplayPrefsI displayPrefs = Config.getDisplayPrefs();
        return displayPrefs.generateURL(selection.last());
    }

    private boolean isSyntenyLinked(SeqFeatureI sf) {
        return Config.getPropertyScheme().getFeatureProperty(sf.getTopLevelType()).isSyntenyLinked();
    }

    /** return true if selection is single featSet with kids that are synteny linked, 
      or selection is syn linked kids of common parent   */
    private boolean enableSyntenyLink() {
        SeqFeatureI sf = selection.getFeature(0);
        if (selection.size() == 1) {
            return isSyntenyLinked(sf);
        } else if (selection.size() >= 1) {
            SeqFeatureI parent = null;
            for (int i = 0; i < selection.size(); i++) {
                sf = selection.getSelectedData(i);
                if (!isSyntenyLinked(sf)) return false;
                if (parent == null) parent = sf.getParent();
                if (parent != sf.getParent()) return false;
            }
            return true;
        }
        return false;
    }

    /** Clean up dangling references (mem leaks) */
    public void clear() {
        editor = null;
        if (jalviewMenus != null) jalviewMenus.clear();
    }
}
