package org.mbari.vars.query.ui;

import foxtrot.Job;
import foxtrot.Worker;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mbari.swing.SearchableComboBoxModel;
import org.mbari.vars.dao.DAOException;
import org.mbari.vars.knowledgebase.model.Concept;
import org.mbari.vars.knowledgebase.model.ConceptName;
import org.mbari.vars.knowledgebase.model.LinkTemplate;
import org.mbari.vars.knowledgebase.model.dao.KnowledgeBaseCache;
import org.mbari.vars.ui.HierachicalConceptNameComboBox;

/**
 * @author Brian Schlining
 * @version $Id: LinkTemplateSelectionPanel.java 332 2006-08-01 18:38:46Z hohonuuli $
 */
public class LinkTemplateSelectionPanel extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = 3736494989429665881L;

    private static final Concept selfConcept = new Concept(new ConceptName("self", ConceptName.NAMETYPE_PRIMARY), null);

    private static final LinkTemplate nilLinkTemplate = new LinkTemplate(ConceptConstraints.WILD_CARD_STRING, ConceptConstraints.WILD_CARD_STRING, ConceptConstraints.WILD_CARD_STRING);

    private static final Concept nilConcept = new Concept(new ConceptName(ConceptConstraints.WILD_CARD_STRING, ConceptName.NAMETYPE_PRIMARY), null);

    private static final Logger log = LoggerFactory.getLogger(LinkTemplateSelectionPanel.class);

    /**
	 * @uml.property  name="topPanel"
	 * @uml.associationEnd  
	 */
    private JPanel topPanel = null;

    /**
	 * @uml.property  name="tfSearch"
	 * @uml.associationEnd  
	 */
    private JTextField tfSearch = null;

    /**
	 * @uml.property  name="tfLinkValue"
	 * @uml.associationEnd  
	 */
    private JTextField tfLinkValue = null;

    /**
	 * @uml.property  name="tfLinkName"
	 * @uml.associationEnd  
	 */
    private JTextField tfLinkName = null;

    /**
	 * @uml.property  name="middlePanel"
	 * @uml.associationEnd  
	 */
    private JPanel middlePanel = null;

    /**
	 * @uml.property  name="lblToConcept"
	 * @uml.associationEnd  
	 */
    private JLabel lblToConcept = null;

    /**
	 * @uml.property  name="lblSearch"
	 * @uml.associationEnd  
	 */
    private JLabel lblSearch = null;

    /**
	 * @uml.property  name="lblLinkValue"
	 * @uml.associationEnd  
	 */
    private JLabel lblLinkValue = null;

    /**
	 * @uml.property  name="lblLinkName"
	 * @uml.associationEnd  
	 */
    private JLabel lblLinkName = null;

    /**
	 * @uml.property  name="cbToConcept"
	 * @uml.associationEnd  
	 */
    private HierachicalConceptNameComboBox cbToConcept = null;

    /**
	 * @uml.property  name="cbLinkTemplates"
	 * @uml.associationEnd  
	 */
    private JComboBox cbLinkTemplates = null;

    /**
	 * @uml.property  name="bottomPanel"
	 * @uml.associationEnd  
	 */
    private JPanel bottomPanel = null;

    /**
     *
     */
    public LinkTemplateSelectionPanel() {
        super();
        initialize();
    }

    /**
     * @param isDoubleBuffered
     */
    public LinkTemplateSelectionPanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        initialize();
    }

    /**
     * @param layout
     */
    public LinkTemplateSelectionPanel(LayoutManager layout) {
        super(layout);
        initialize();
    }

    /**
     * @param layout
     * @param isDoubleBuffered
     */
    public LinkTemplateSelectionPanel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
        initialize();
    }

    /**
	 * This method initializes jPanel
	 * @return  javax.swing.JPanel
	 * @uml.property  name="bottomPanel"
	 */
    private JPanel getBottomPanel() {
        if (bottomPanel == null) {
            bottomPanel = new JPanel();
            lblLinkValue = new JLabel();
            bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
            lblToConcept = new JLabel();
            lblLinkName = new JLabel();
            lblLinkName.setText("link");
            lblToConcept.setText("to");
            lblLinkValue.setText("value");
            bottomPanel.add(lblLinkName, null);
            bottomPanel.add(getTfLinkName(), null);
            bottomPanel.add(lblToConcept, null);
            bottomPanel.add(getCbToConcept(), null);
            bottomPanel.add(lblLinkValue, null);
            bottomPanel.add(getTfLinkValue(), null);
        }
        return bottomPanel;
    }

    /**
	 * This method initializes jComboBox
	 * @return  javax.swing.JComboBox
	 * @uml.property  name="cbLinkTemplates"
	 */
    private JComboBox getCbLinkTemplates() {
        if (cbLinkTemplates == null) {
            cbLinkTemplates = new JComboBox();
            cbLinkTemplates.setModel(new SearchableComboBoxModel());
            cbLinkTemplates.setToolTipText("Links in Knowledgebase");
            cbLinkTemplates.addItemListener(new java.awt.event.ItemListener() {

                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        setLinkTemplate((LinkTemplate) e.getItem());
                    }
                }
            });
        }
        return cbLinkTemplates;
    }

    /**
	 * This method initializes jComboBox1
	 * @return  javax.swing.JComboBox
	 * @uml.property  name="cbToConcept"
	 */
    private HierachicalConceptNameComboBox getCbToConcept() {
        if (cbToConcept == null) {
            cbToConcept = new HierachicalConceptNameComboBox();
        }
        return cbToConcept;
    }

    /**
	 * This method initializes jPanel
	 * @return  javax.swing.JPanel
	 * @uml.property  name="middlePanel"
	 */
    private JPanel getMiddlePanel() {
        if (middlePanel == null) {
            middlePanel = new JPanel();
            middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.X_AXIS));
            middlePanel.add(getCbLinkTemplates(), null);
        }
        return middlePanel;
    }

    /**
	 * This method initializes jTextField
	 * @return  javax.swing.JTextField
	 * @uml.property  name="tfLinkName"
	 */
    private JTextField getTfLinkName() {
        if (tfLinkName == null) {
            tfLinkName = new JTextField();
            tfLinkName.setEditable(false);
        }
        return tfLinkName;
    }

    /**
	 * This method initializes jTextField1
	 * @return  javax.swing.JTextField
	 * @uml.property  name="tfLinkValue"
	 */
    private JTextField getTfLinkValue() {
        if (tfLinkValue == null) {
            tfLinkValue = new JTextField();
        }
        return tfLinkValue;
    }

    /**
	 * This method initializes jTextField
	 * @return  javax.swing.JTextField
	 * @uml.property  name="tfSearch"
	 */
    private JTextField getTfSearch() {
        if (tfSearch == null) {
            tfSearch = new JTextField();
            tfSearch.addFocusListener(new FocusAdapter() {

                public void focusGained(FocusEvent fe) {
                    tfSearch.setSelectionStart(0);
                    tfSearch.setSelectionEnd(tfSearch.getText().length());
                }
            });
            tfSearch.addActionListener(new ActionListener() {

                public void actionPerformed(final ActionEvent e) {
                    final JComboBox cb = getCbLinkTemplates();
                    int startIndex = cb.getSelectedIndex() + 1;
                    SearchableComboBoxModel linksModel = (SearchableComboBoxModel) cb.getModel();
                    int index = linksModel.searchForItemContaining(tfSearch.getText(), startIndex);
                    if (index > -1) {
                        cb.setSelectedIndex(index);
                        cb.hidePopup();
                    } else {
                        if (startIndex > 0) {
                            index = linksModel.searchForItemContaining(tfSearch.getText());
                            if (index > -1) {
                                cb.setSelectedIndex(index);
                                cb.hidePopup();
                            }
                        }
                    }
                }
            });
        }
        return tfSearch;
    }

    /**
	 * This method initializes jPanel
	 * @return  javax.swing.JPanel
	 * @uml.property  name="topPanel"
	 */
    private JPanel getTopPanel() {
        if (topPanel == null) {
            lblSearch = new JLabel();
            topPanel = new JPanel();
            topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
            lblSearch.setText("Search:");
            topPanel.add(lblSearch, null);
            topPanel.add(getTfSearch(), null);
        }
        return topPanel;
    }

    /**
     * This method initializes this
     *
     */
    private void initialize() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setSize(384, 110);
        this.add(getTopPanel(), null);
        this.add(getMiddlePanel(), null);
        this.add(getBottomPanel(), null);
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param concept
     */
    public void setConcept(Concept concept) {
        log.info("Retrieveing LinkTemplates from " + concept);
        if (concept == null) {
            try {
                concept = KnowledgeBaseCache.getInstance().findRootConcept();
            } catch (DAOException e) {
                log.error("Failed to lookup root concept", e);
            }
        }
        final Concept fConcept = concept;
        Collection linkTemplates = (Collection) Worker.post(new Job() {

            public Object run() {
                return Arrays.asList(fConcept.getHierarchicalLinkTemplates());
            }
        });
        linkTemplates = new ArrayList(linkTemplates);
        linkTemplates.add(nilLinkTemplate);
        SearchableComboBoxModel model = (SearchableComboBoxModel) getCbLinkTemplates().getModel();
        model.clear();
        model.addAll(linkTemplates);
        model.setSelectedItem(nilLinkTemplate);
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param linkTemplate
     */
    public void setLinkTemplate(LinkTemplate linkTemplate) {
        if (linkTemplate == null) {
            linkTemplate = nilLinkTemplate;
        }
        getTfLinkName().setText(linkTemplate.getLinkName());
        Concept toConcept = null;
        if (linkTemplate.getToConcept().toLowerCase() == "self") {
            toConcept = selfConcept;
        } else if (linkTemplate.getToConcept().equalsIgnoreCase(ConceptConstraints.WILD_CARD_STRING)) {
            toConcept = nilConcept;
        } else {
            try {
                toConcept = KnowledgeBaseCache.getInstance().findConceptByName(linkTemplate.getToConcept());
                if (toConcept == null) {
                    toConcept = KnowledgeBaseCache.getInstance().findRootConcept();
                }
            } catch (DAOException e) {
                log.error("Failed to lookup " + linkTemplate.getToConcept(), e);
                toConcept = new Concept();
                ConceptName conceptName = new ConceptName();
                conceptName.setName(ConceptConstraints.WILD_CARD_STRING);
                conceptName.setNameType(ConceptName.NAMETYPE_PRIMARY);
                toConcept.setPrimaryConceptName(conceptName);
            }
        }
        final Concept fToConcept = toConcept;
        Worker.post(new Job() {

            public Object run() {
                getCbToConcept().setConcept(fToConcept);
                return null;
            }
        });
        getCbToConcept().setSelectedItem(toConcept.getPrimaryConceptNameAsString());
        getCbToConcept().addItem(nilConcept.getPrimaryConceptName());
        getCbToConcept().addItem(selfConcept.getPrimaryConceptName());
        getTfLinkValue().setText(linkTemplate.getLinkValue());
    }
}
