package org.qsari.effectopedia.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import org.qsari.effectopedia.core.objects.Link_SubstanceToReactiveSubstance;
import org.qsari.effectopedia.defaults.DefaultGOSettings;

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
public class Link_SubstanceToReactiveSubstanceUI extends javax.swing.JScrollPane implements AdjustableUI, LoadableEditorUI, SizeOptimizableUI {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private LinkHeaderSimplifiedUI lhuiHeader;

    private DescriptionUI duiDescription;

    private PathwaysListUI pluiAssociatedPathways;

    private ReferencesUI ruiReferences;

    private BoxLayout thisLayout;

    private JPanel jpEditPanel;

    private OntologyInstancesUI oiuiEnzymeSystem;

    /**
		 * Auto-generated main method to display this JPanel inside a new JFrame.
		 */
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.getContentPane().add(new Link_SubstanceToReactiveSubstanceUI());
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public Link_SubstanceToReactiveSubstanceUI() {
        super();
        initGUI();
        adjustUI(EDIT);
    }

    private void initGUI() {
        try {
            jpEditPanel = new JPanel();
            jpEditPanel.setPreferredSize(optimalSize);
            this.setViewportView(jpEditPanel);
            thisLayout = new BoxLayout(jpEditPanel, javax.swing.BoxLayout.Y_AXIS);
            jpEditPanel.setLayout(thisLayout);
            jpEditPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(DefaultGOSettings.linkColor, 2, false), "Link", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12), DefaultGOSettings.linkColor));
            jpEditPanel.setBackground(Color.WHITE);
            {
                lhuiHeader = new LinkHeaderSimplifiedUI();
                jpEditPanel.add(lhuiHeader);
                lhuiHeader.setPreferredSize(new java.awt.Dimension(388, 41));
            }
            {
                duiDescription = new DescriptionUI();
                jpEditPanel.add(duiDescription);
            }
            {
                oiuiEnzymeSystem = new OntologyInstancesUI("Enzyme system");
                jpEditPanel.add(oiuiEnzymeSystem);
            }
            {
                pluiAssociatedPathways = new PathwaysListUI();
                jpEditPanel.add(pluiAssociatedPathways);
            }
            {
                ruiReferences = new ReferencesUI();
                jpEditPanel.add(ruiReferences);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
		 * Adjust <code>visible</code> properties to the current and the contained
		 * components
		 * 
		 * @see AdjustableUI
		 * 
		 * @param visualOptions
		 *         an long that specifies which of the contained components are
		 *         visible
		 */
    public void adjustUI(long visualOptions) {
        if ((visualOptions & HEADER) != 0) {
            setVisible(true);
            this.setBackground(Color.WHITE);
            AdjustbleUserInterfaceTools.adjustChildren(this, visualOptions);
        } else {
            setVisible(false);
        }
    }

    public void load(Object o) {
        if (!(o instanceof Link_SubstanceToReactiveSubstance)) return;
        link = (Link_SubstanceToReactiveSubstance) o;
        lhuiHeader.load(link);
        ruiReferences.load(link.getReferenceIDs());
        duiDescription.load(link.getDescriptionIDs());
        pluiAssociatedPathways.load(link.getPathwayIDs());
        oiuiEnzymeSystem.load(link.getEnzymeSystem());
        updateOptimalSize();
    }

    public void updateOptimalSize() {
        int difference;
        Insets insets = getBorder().getBorderInsets(this);
        optimalSize.width = getWidth();
        optimalSize.height = lhuiHeader.getPreferredSize().height + ruiReferences.getPreferredSize().height + pluiAssociatedPathways.getPreferredSize().height + duiDescription.getPreferredSize().height;
        optimalSize.height += insets.top + insets.bottom;
        difference = optimalSize.height - jpEditPanel.getHeight();
        jpEditPanel.setSize(optimalSize);
        JScrollBar scrollbar = this.getVerticalScrollBar();
        if (scrollbar != null) scrollbar.setValue(scrollbar.getValue() + difference);
        Container parent = getParent();
        if ((parent != null) && (parent instanceof SizeOptimizableUI)) ((SizeOptimizableUI) parent).updateOptimalSize();
    }

    private Dimension optimalSize = new Dimension(400, 1000);

    private Link_SubstanceToReactiveSubstance link;
}
