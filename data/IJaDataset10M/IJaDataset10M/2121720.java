package de.tobiasbudde.regedit.swing;

import java.io.File;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import org.jdesktop.application.Application;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import de.tobiasbudde.regedit.reg.config.CutMatches;
import de.tobiasbudde.regedit.reg.config.ReplaceStrategy;
import de.tobiasbudde.regedit.reg.config.Workset;
import de.tobiasbudde.regedit.swing.components.JPopupTextArea;
import de.tobiasbudde.regedit.swing.components.UndoableTextArea;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class RegExpReplacePanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 1L;

    private JScrollPane replacePane;

    private JRadioButton alsoApplyFunction;

    private JRadioButton alsoReplace;

    private JRadioButton onlySearch;

    private ButtonGroup replaceStrategy;

    protected File searchfile;

    {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getLookAndFeel());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private JRadioButton cutLines;

    private JRadioButton cutAllMatcherSepLines;

    private JRadioButton cutAllMatches;

    private JRadioButton dontCutAnything;

    private JPopupTextArea replacePattern;

    private ButtonGroup cuttingGroup;

    public RegExpReplacePanel(RegExpCallBack callback) {
        this(null, callback);
    }

    public RegExpReplacePanel(Workset workset, RegExpCallBack callback) {
        super();
        if (workset == null) {
            workset = new Workset();
        }
        initGUI(workset, callback);
    }

    private void initGUI(Workset workset, RegExpCallBack callback) {
        try {
            AnchorLayout thisLayout = new AnchorLayout();
            this.setLayout(thisLayout);
            replacePane = new JScrollPane();
            this.add(replacePane, new AnchorConstraint(0, 0, 69, 0, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
            replacePane.setName("replacePane");
            replacePane.setAutoscrolls(true);
            replacePane.getVerticalScrollBar().setAutoscrolls(true);
            replacePane.getHorizontalScrollBar().setAutoscrolls(true);
            replacePane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            replacePane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            replacePane.getVerticalScrollBar().setPreferredSize(new java.awt.Dimension(15, 15));
            replacePane.getHorizontalScrollBar().setSize(15, 15);
            replacePane.getVerticalScrollBar().setSize(15, 15);
            replacePattern = new UndoableTextArea();
            replacePane.setViewportView(replacePattern);
            replacePattern.addKeyListener(callback.asKeyListener());
            replacePattern.setChangeCallback(callback);
            replacePattern.setName("replacePattern");
            replacePattern.setMinimumSize(new java.awt.Dimension(10, 10));
            replacePattern.setMargin(new java.awt.Insets(0, 0, 0, 0));
            replacePattern.setText(workset.getReplaceconfiguration().getReplacePattern());
            onlySearch = new JRadioButton();
            onlySearch.setPreferredSize(new java.awt.Dimension(185, 18));
            onlySearch.setText("only search");
            onlySearch.setSelected(ReplaceStrategy.SEARCH.equals(workset.getReplaceconfiguration().getReplaceStrategy()));
            onlySearch.setActionCommand("SEARCH");
            onlySearch.addItemListener(callback.asItemListener());
            onlySearch.setName("onlySearch");
            getReplaceStrategy().add(onlySearch);
            alsoReplace = new JRadioButton();
            alsoReplace.setPreferredSize(new java.awt.Dimension(185, 18));
            alsoReplace.setText("replace");
            alsoReplace.setSelected(ReplaceStrategy.REPLACE.equals(workset.getReplaceconfiguration().getReplaceStrategy()));
            alsoReplace.setActionCommand("REPLACE");
            alsoReplace.addItemListener(callback.asItemListener());
            alsoReplace.setName("alsoReplace");
            getReplaceStrategy().add(alsoReplace);
            alsoApplyFunction = new JRadioButton();
            alsoApplyFunction.setPreferredSize(new java.awt.Dimension(185, 18));
            alsoApplyFunction.setText("replace and apply function");
            alsoApplyFunction.setSelected(ReplaceStrategy.APPLYFUNCTION.equals(workset.getReplaceconfiguration().getReplaceStrategy()));
            alsoApplyFunction.setActionCommand("APPLYFUNCTION");
            alsoApplyFunction.addItemListener(callback.asItemListener());
            alsoApplyFunction.setName("alsoApplyFunction");
            getReplaceStrategy().add(alsoApplyFunction);
            this.add(alsoReplace, new AnchorConstraint(489, 109, 35, 12, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
            this.add(onlySearch, new AnchorConstraint(435, 142, 51, 12, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
            this.add(alsoApplyFunction, new AnchorConstraint(544, 245, 19, 12, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
            dontCutAnything = new JRadioButton();
            this.add(dontCutAnything, new AnchorConstraint(435, 847, 51, 232, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
            dontCutAnything.setText("keep everything");
            dontCutAnything.setToolTipText("keep the complete original text");
            dontCutAnything.setSelected(CutMatches.CUT_NOTHING.equals(workset.getReplaceconfiguration().getCuttingMode()));
            dontCutAnything.setPreferredSize(new java.awt.Dimension(237, 18));
            dontCutAnything.setActionCommand("CUT_NOTHING");
            dontCutAnything.addItemListener(callback.asItemListener());
            dontCutAnything.setName("dontCutAnything");
            cuttingGroup = new ButtonGroup();
            cuttingGroup.add(dontCutAnything);
            cutAllMatches = new JRadioButton();
            this.add(cutAllMatches, new AnchorConstraint(489, 847, 35, 232, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
            cutAllMatches.setText("cut all matches");
            cutAllMatches.setToolTipText("keep the found matches only");
            cutAllMatches.setSelected(CutMatches.CUT_MATCHES.equals(workset.getReplaceconfiguration().getCuttingMode()));
            cutAllMatches.setPreferredSize(new java.awt.Dimension(237, 18));
            cutAllMatches.setActionCommand("CUT_MATCHES");
            cutAllMatches.addItemListener(callback.asItemListener());
            cutAllMatches.setName("cutAllMatches");
            cuttingGroup.add(cutAllMatches);
            cutAllMatcherSepLines = new JRadioButton();
            this.add(cutAllMatcherSepLines, new AnchorConstraint(544, 847, 19, 232, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
            cutAllMatcherSepLines.setText("cut all matches in seperate lines");
            cutAllMatcherSepLines.setToolTipText("keep the found matches seperated by new lines");
            cutAllMatcherSepLines.setSelected(CutMatches.CUT_MATCHES_SEPERATE_LINES.equals(workset.getReplaceconfiguration().getCuttingMode()));
            cutAllMatcherSepLines.setPreferredSize(new java.awt.Dimension(237, 18));
            cutAllMatcherSepLines.setActionCommand("CUT_MATCHES_SEPERATE_LINES");
            cutAllMatcherSepLines.addItemListener(callback.asItemListener());
            cutAllMatcherSepLines.setName("cutAllMatcherSepLines");
            cuttingGroup.add(cutAllMatcherSepLines);
            cutLines = new JRadioButton();
            this.add(cutLines, new AnchorConstraint(598, 847, 4, 232, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
            cutLines.setText("cut all matching lines");
            cutLines.setToolTipText("keep all lines from the original text that contain matches");
            cutLines.setSelected(CutMatches.CUT_LINES.equals(workset.getReplaceconfiguration().getCuttingMode()));
            cutLines.setPreferredSize(new java.awt.Dimension(237, 17));
            cutLines.setActionCommand("CUT_LINES");
            cutLines.addItemListener(callback.asItemListener());
            cutLines.setName("cutLines");
            cuttingGroup.add(cutLines);
            Application.getInstance().getContext().getResourceMap(getClass()).injectComponents(this);
        } catch (Exception e) {
            System.out.println("Failed to initialize application: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    ButtonGroup getReplaceStrategy() {
        if (replaceStrategy == null) {
            replaceStrategy = new ButtonGroup();
        }
        return replaceStrategy;
    }

    public ButtonGroup getCuttingGroup() {
        return cuttingGroup;
    }

    public JTextArea getReplacePattern() {
        return replacePattern;
    }
}
