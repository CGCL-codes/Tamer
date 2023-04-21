package uk.ac.kingston.aqurate.author_UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import uk.ac.kingston.aqurate.author_controllers.DefaultController;
import uk.ac.kingston.aqurate.author_controllers.GraphicOrderInteractionController;
import uk.ac.kingston.aqurate.author_controllers.HotspotInteractionController;
import uk.ac.kingston.aqurate.author_controllers.OrderInteractionController;
import uk.ac.kingston.aqurate.util.HTMLTextEnum;
import uk.ac.kingston.aqurate.util.ImageFileFilter;
import uk.ac.kingston.aqurate.util.ScrollablePicture;

public class GraphicOrderInteractionViewPanel extends AbstractViewPanel implements ActionListener, ListSelectionListener {

    public class ElementPosition {

        String element;

        int position;

        public ElementPosition(String pElement, int pPosition) {
            position = pPosition;
            element = pElement;
        }

        public void changeElement(String elem) {
            element = elem;
        }

        public void setNewPosition(int pos) {
            position = pos;
        }
    }

    private static final long serialVersionUID = 1L;

    public AttachImagePanel attachImagePanel = null;

    private int count = 0;

    /** CHOICE INTERACTION COMPONENTS */
    private DefaultListModel defaultlistmodel = null;

    private String defaultPrompt = "Mark the airports shown on the map according to Lorna's preferences.";

    private Properties elementsPositions = null;

    private File file = null;

    public GraphicOrderInteractionController goc;

    private JScrollPane hotspotImgScrollPane = null;

    public HTMLPanel htmlPanelPrompt = null;

    private BufferedImage img = null;

    private JButton jButtonAddChoice = null;

    private JButton jButtonDown = null;

    private JButton jButtonRemChoice = null;

    private JButton jButtonUp = null;

    private JButton jButtonUpload = null;

    private JFileChooser jfc = new JFileChooser();

    private JLabel jLabelHotspotChoices = null;

    private JLabel jLabelIntroduction = null;

    /** ASSESMENT TYPE UI COMPONENTS */
    private JLabel jLabelPrompt = null;

    private JList jListCorrectAnswer = null;

    public JPanel jPanelInteraction = null;

    public JPanel jPanelWording = null;

    private JScrollPane jScrollPaneCorrectOrder = null;

    private JTabbedPane jTabbedPaneAttributes = null;

    private ScrollablePicture picture = null;

    public QuestionAttributes questionAttributesPanel = null;

    public ResponseAttributes responseAttributesPanel = null;

    public MetadataAttributes metadataAttributesPanel = null;

    private double scalingFactor = 1.0;

    public GraphicOrderInteractionViewPanel(AqurateFramework owner, DefaultController controller) {
        super(owner, "GraphicOrderInteractionViewPanel", controller);
        goc = (GraphicOrderInteractionController) controller;
        this.initialize();
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(jButtonPreamble)) {
            popupPreamble.setCurrentPreamblePostscript("");
            popupPreamble.setVisible(true);
        }
        if (e.getSource().equals(jButtonPostscript)) {
            popupPostscript.setCurrentPreamblePostscript("");
            popupPostscript.setVisible(true);
        }
        if (e.getSource().equals(jButtonDeletePreamble)) {
            Object[] options = { "Yes, sure", "No, cancel" };
            int n = JOptionPane.showOptionDialog(null, "This action will delete\n" + " the preamble defined.\n" + "Do you want delete it?\n", "Preamble Action", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
            if (n == JOptionPane.YES_OPTION) {
                popupPreamble.clearContent();
                jEditorPanePreamble.setText("");
                goc.changePreamble("");
                this.remove(jPanelPreamble);
                this.add(jButtonPreamble, this.buildConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.NORTHWEST, 10, 10, 0, 0));
                this.updateUI();
            }
            if (n == JOptionPane.NO_OPTION) {
            }
        }
        if (e.getSource().equals(jButtonDeletePostscript)) {
            Object[] options = { "Yes, sure", "No, cancel" };
            int n = JOptionPane.showOptionDialog(null, "This action will delete\n" + " the postscript defined.\n" + "Do you want delete it?\n", "Postscript Action", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
            if (n == JOptionPane.YES_OPTION) {
                popupPostscript.clearContent();
                jEditorPanePostscript.setText(popupPostscript.returnHtmlEditorText());
                goc.changePostscript("");
                this.remove(jPanelPostscript);
                this.add(jButtonPostscript, this.buildConstraints(0, 5, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.NORTHWEST, 10, 10, 20, 0));
                this.updateUI();
            }
            if (n == JOptionPane.NO_OPTION) {
            }
        }
        if (e.getSource().equals(jButtonUp)) {
            int iselected = this.jListCorrectAnswer.getSelectedIndex();
            if (iselected != -1) {
                goc.changeCorrectChoices(iselected, "UP");
            } else {
                JOptionPane.showMessageDialog(null, "Please select an element to move up.", "No element selected!", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (e.getSource().equals(jButtonDown)) {
            int iselected = this.jListCorrectAnswer.getSelectedIndex();
            if (iselected != -1) {
                goc.changeCorrectChoices(iselected, "DOWN");
            } else {
                JOptionPane.showMessageDialog(null, "Please select the element to move down", "No element selected!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void addElementPosition(int k, ElementPosition ep) {
        this.elementsPositions.put(k, ep);
    }

    public GridBagConstraints buildConstraints(int x, int y, int w, int h, int wx, int wy, int fill, int anchor, int top, int left, int bottom, int right) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = w;
        gbc.gridheight = h;
        gbc.weightx = wx;
        gbc.weighty = wy;
        gbc.fill = fill;
        gbc.anchor = anchor;
        gbc.insets = new Insets(top, left, bottom, right);
        return gbc;
    }

    private JPanel getAttachImagePanel() {
        if (attachImagePanel == null) {
            attachImagePanel = new AttachImagePanel(owner, goc, getHTMLPanelPrompt());
        }
        return attachImagePanel;
    }

    private JScrollPane getHotspotImgScrollPane() {
        if (hotspotImgScrollPane == null) {
            hotspotImgScrollPane = new JScrollPane();
            hotspotImgScrollPane.setPreferredSize(new Dimension(500, 200));
        }
        return hotspotImgScrollPane;
    }

    @Override
    public HTMLPanel getHTMLPanelPrompt() {
        if (htmlPanelPrompt == null) {
            htmlPanelPrompt = new HTMLPanel(owner, goc, HTMLTextEnum.PROMPT);
        }
        htmlPanelPrompt.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
            }

            public void focusLost(FocusEvent e) {
            }
        });
        return htmlPanelPrompt;
    }

    private JButton getJButtonAddChoice() {
        if (jButtonAddChoice == null) {
            jButtonAddChoice = new JButton("Add Hotspot");
            jButtonAddChoice.setPreferredSize(new Dimension(130, 30));
        }
        jButtonAddChoice.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                if (picture.isNewHotspot()) {
                    String sID = "GO" + count++;
                    boolean correctChoice = true;
                    goc.changeAddChoice(sID + "�" + (int) (picture.getCurrentX() / scalingFactor) + "," + (int) (picture.getCurrentY() / scalingFactor) + ",8" + "�" + correctChoice);
                }
            }
        });
        return jButtonAddChoice;
    }

    private JButton getJButtonDown() {
        if (jButtonDown == null) {
            jButtonDown = new JButton("Down");
            jButtonDown.setPreferredSize(new Dimension(70, 25));
        }
        jButtonDown.addActionListener(this);
        return jButtonDown;
    }

    private JButton getJButtonRemChoice() {
        if (jButtonRemChoice == null) {
            jButtonRemChoice = new JButton("Remove Hotspot");
            jButtonRemChoice.setPreferredSize(new Dimension(130, 30));
        }
        jButtonRemChoice.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                int iselected = jListCorrectAnswer.getSelectedIndex();
                if (iselected != -1) {
                    goc.changeDelChoice(Integer.toString(iselected));
                    picture.deleteHotspot(iselected);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select an element to remove.", "No element selected!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return jButtonRemChoice;
    }

    private JButton getJButtonUp() {
        if (jButtonUp == null) {
            jButtonUp = new JButton("Up");
            jButtonUp.setPreferredSize(new Dimension(70, 25));
        }
        jButtonUp.addActionListener(this);
        return jButtonUp;
    }

    private JButton getJButtonUpload() {
        if (jButtonUpload == null) {
            jButtonUpload = new JButton("Upload Image");
            jButtonUpload.setPreferredSize(new Dimension(130, 30));
        }
        jButtonUpload.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jfc.setFileFilter(new ImageFileFilter());
                int returnVal = jfc.showOpenDialog(new javax.swing.JFrame());
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    file = jfc.getSelectedFile();
                    goc.changeHotspotImage(file.getPath());
                }
            }
        });
        return jButtonUpload;
    }

    private JList getJListCorrectAnswer() {
        if (jListCorrectAnswer == null) {
            defaultlistmodel = new DefaultListModel();
            jListCorrectAnswer = new JList(defaultlistmodel);
            jListCorrectAnswer.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jListCorrectAnswer.setBackground(Color.lightGray);
        }
        jListCorrectAnswer.addListSelectionListener(this);
        return jListCorrectAnswer;
    }

    public JPanel getJPanelInteraction() {
        if (jPanelInteraction == null) {
            jLabelIntroduction = new JLabel();
            jLabelIntroduction.setText("Move the white Crosshair to the required place on the image, then click �add Hotspot�. Repeat as necessary");
            jLabelHotspotChoices = new JLabel();
            jLabelHotspotChoices.setText("Edit information for the hotspots defined, remove hotspot choice using \"Remove Hotspot\" button.");
            jPanelInteraction = new JPanel();
            jPanelInteraction.setPreferredSize(new Dimension(680, 470));
            jPanelInteraction.setLayout(new GridBagLayout());
            jPanelInteraction.setBorder(new TitledBorder("Response Declaration"));
            jPanelInteraction.setVisible(true);
            jPanelInteraction.add(jLabelIntroduction, this.buildConstraints(0, 0, 3, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST, 0, 0, 10, 0));
            jPanelInteraction.add(getHotspotImgScrollPane(), this.buildConstraints(0, 1, 3, 2, 0, 0, GridBagConstraints.BOTH, GridBagConstraints.WEST, 0, 0, 10, 0));
            jPanelInteraction.add(getJButtonUpload(), this.buildConstraints(3, 1, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST, 30, 0, 0, 10));
            jPanelInteraction.add(getJButtonAddChoice(), this.buildConstraints(3, 2, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST, 0, 0, 0, 10));
            jPanelInteraction.add(jLabelHotspotChoices, this.buildConstraints(0, 3, 3, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST, 0, 0, 0, 0));
            jPanelInteraction.add(getJScrollPaneCorrectOrder(), this.buildConstraints(0, 4, 1, 3, 0, 0, GridBagConstraints.BOTH, GridBagConstraints.WEST, 10, 0, 0, 20));
            jPanelInteraction.add(getJButtonRemChoice(), this.buildConstraints(3, 4, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST, 20, 0, 0, 10));
            jPanelInteraction.add(getJButtonUp(), this.buildConstraints(1, 5, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST, 30, 0, 0, 150));
            jPanelInteraction.add(getJButtonDown(), this.buildConstraints(1, 6, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST, 0, 0, 20, 150));
        }
        return jPanelInteraction;
    }

    /** UI METHODS */
    public JPanel getJPanelWording() {
        if (jPanelWording == null) {
            jPanelWording = new JPanel();
        }
        jPanelWording.setPreferredSize(new Dimension(680, 220));
        jPanelWording.setLayout(new BorderLayout());
        jPanelWording.setBorder(new TitledBorder("Wording Declaration"));
        jPanelWording.setVisible(true);
        jPanelWording.add(getHTMLPanelPrompt(), BorderLayout.CENTER);
        jPanelWording.add(getAttachImagePanel(), BorderLayout.SOUTH);
        return jPanelWording;
    }

    private JScrollPane getJScrollPaneCorrectOrder() {
        if (jScrollPaneCorrectOrder == null) {
            jScrollPaneCorrectOrder = new JScrollPane(getJListCorrectAnswer());
            jScrollPaneCorrectOrder.setPreferredSize(new Dimension(150, 170));
            jScrollPaneCorrectOrder.setBorder(new TitledBorder("CORRECT ORDERING"));
        }
        return jScrollPaneCorrectOrder;
    }

    private JTabbedPane getJTabbedPaneAttributes() {
        if (jTabbedPaneAttributes == null) {
            jTabbedPaneAttributes = new JTabbedPane();
            jTabbedPaneAttributes.setPreferredSize(new Dimension(310, 380));
        }
        questionAttributesPanel = new QuestionAttributes(owner, goc);
        responseAttributesPanel = new ResponseAttributes(owner, goc);
        metadataAttributesPanel = new MetadataAttributes(owner, goc);
        questionAttributesPanel.disableShuffle();
        responseAttributesPanel.disableShowScoreColumn();
        jTabbedPaneAttributes.addTab("Title and Presentation", questionAttributesPanel);
        jTabbedPaneAttributes.addTab("Scoring and Feedback", responseAttributesPanel);
        jTabbedPaneAttributes.addTab("Metadata", metadataAttributesPanel);
        return jTabbedPaneAttributes;
    }

    /** INIT THE INTERFACE */
    public void initialize() {
        this.getJPanelPreamble();
        this.getJPanelPostcript();
        this.setLayout(new GridBagLayout());
        add(getJButtonPreamble(), this.buildConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.NORTHWEST, 10, 10, 0, 0));
        add(getJPanelWording(), this.buildConstraints(0, 1, 2, 2, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST, 20, 10, 0, 0));
        add(getJPanelInteraction(), this.buildConstraints(0, 3, 2, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST, 0, 10, 0, 0));
        add(getJButtonPostcript(), this.buildConstraints(0, 4, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.NORTHWEST, 0, 10, 20, 0));
        add(getJTabbedPaneAttributes(), this.buildConstraints(2, 1, 1, 3, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER, 0, 50, 400, 50));
        jButtonPreamble.addActionListener(this);
        jButtonDeletePreamble.addActionListener(this);
        jButtonPostscript.addActionListener(this);
        jButtonDeletePostscript.addActionListener(this);
        Object[] data = { "" };
        AqurateFramework.tableModel.addRow(data);
        AqurateFramework.currentViewIndex = AqurateFramework.tableModel.getRowCount() - 1;
        AqurateFramework.jTableQuestionsPool.getSelectionModel().setSelectionInterval(AqurateFramework.currentViewIndex, AqurateFramework.currentViewIndex);
    }

    /** OVERRIDEN METHODS */
    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(DefaultController.DOC_TITLE_PROPERTY)) {
            String newStringValue = evt.getNewValue().toString();
            if (!questionAttributesPanel.getJTextFieldTitle().getText().equals(newStringValue)) {
                questionAttributesPanel.jTextFieldTitle.setText(newStringValue);
            }
            AqurateFramework.updateQuestionTitle(newStringValue);
        } else if (evt.getPropertyName().equals(DefaultController.DOC_ADAPTIVE_PROPERTY)) {
            try {
                Boolean newValue = (Boolean) evt.getNewValue();
                this.questionAttributesPanel.setAdaptiveValue(newValue);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        } else if (evt.getPropertyName().equals(DefaultController.DOC_IDENTIFIER_PROPERTY)) {
        } else if (evt.getPropertyName().equals(DefaultController.DOC_TICKET_PROPERTY)) {
            String newStringValue = evt.getNewValue().toString();
            this.questionAttributesPanel.jlTicket.setText("Ticket number:" + newStringValue);
        } else if (evt.getPropertyName().equals(DefaultController.DOC_PROMPT_PROPERTY)) {
            String newStringValue = evt.getNewValue().toString();
            try {
                if (!htmlPanelPrompt.getText().equals(newStringValue)) htmlPanelPrompt.setText(newStringValue);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        } else if (evt.getPropertyName().equals(DefaultController.DOC_PREAMBLE_PROPERTY)) {
            String newStringValue = evt.getNewValue().toString();
            jEditorPanePreamble.setText(newStringValue);
            this.remove(this.jButtonPreamble);
            this.add(jPanelPreamble, this.buildConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.NORTHWEST, 10, 10, 0, 0));
            this.updateUI();
            try {
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        } else if (evt.getPropertyName().equals(DefaultController.DOC_POSTSCRIPT_PROPERTY)) {
            String newStringValue = evt.getNewValue().toString();
            jEditorPanePostscript.setText(newStringValue);
            this.remove(this.jButtonPostscript);
            this.add(jPanelPostscript, this.buildConstraints(0, 5, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.NORTHWEST, 10, 10, 20, 0));
            this.updateUI();
            try {
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        } else if (evt.getPropertyName().equals(DefaultController.DOC_TIMEDEPENDENT_PROPERTY)) {
            try {
                Boolean newValue = (Boolean) evt.getNewValue();
                this.questionAttributesPanel.setTimeDepValue(newValue);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        } else if (evt.getPropertyName().equals(DefaultController.DOC_SHUFFLE_PROPERTY)) {
            try {
                Boolean newValue = (Boolean) evt.getNewValue();
                this.questionAttributesPanel.setShuffleValue(newValue);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        } else if (evt.getPropertyName().equals(HotspotInteractionController.DOCUMENT_CHOICE_ADD)) {
            String newStringValue = evt.getNewValue().toString();
            String[] newChoice = newStringValue.split("�");
            String[] coords = newChoice[1].split(",");
            picture.addHotspot(newChoice[0], (int) (Integer.parseInt(coords[0]) * scalingFactor), (int) (Integer.parseInt(coords[1]) * scalingFactor));
        } else if (evt.getPropertyName().equals(HotspotInteractionController.DOCUMENT_CHOICE_DEL)) {
        } else if (evt.getPropertyName().equals(OrderInteractionController.DOCUMENT_CORRECT_ADD)) {
            String sID = evt.getNewValue().toString();
            defaultlistmodel.addElement(sID);
        } else if (evt.getPropertyName().equals(OrderInteractionController.DOCUMENT_CORRECT_DEL)) {
            String sID = evt.getNewValue().toString();
            int iIndex = -1;
            for (int i = 0; i < defaultlistmodel.getSize(); i++) {
                if (defaultlistmodel.elementAt(i).equals(sID)) {
                    iIndex = i;
                    break;
                }
            }
            if (iIndex >= 0) {
                defaultlistmodel.removeElementAt(iIndex);
                if (iIndex < defaultlistmodel.getSize() && defaultlistmodel.getSize() != 0) jListCorrectAnswer.setSelectedIndex(iIndex); else jListCorrectAnswer.setSelectedIndex(iIndex - 1);
            }
        } else if (evt.getPropertyName().equals(HotspotInteractionController.DOCUMENT_HOTSPOTING_CHANGE)) {
            String sVal = evt.getNewValue().toString();
            file = new File(sVal);
            picture = setBackgroundImage(file);
            hotspotImgScrollPane.setViewportView(picture);
            count = 0;
            updateUI();
        } else if (evt.getPropertyName().equals(OrderInteractionController.DOCUMENT_CORRECT_CHANGE)) {
            String[] newValue = evt.getNewValue().toString().split("�");
            int iIndex = Integer.parseInt(newValue[0]);
            String sValue = newValue[1];
            if (sValue.equals("UP")) {
                Object objSelected = this.defaultlistmodel.remove(iIndex);
                Object objUp = this.defaultlistmodel.remove(iIndex - 1);
                this.defaultlistmodel.add(iIndex - 1, objSelected);
                this.defaultlistmodel.add(iIndex, objUp);
                jListCorrectAnswer.setSelectedIndex(iIndex - 1);
            } else if (sValue.equals("DOWN")) {
                Object objSelected = this.defaultlistmodel.get(iIndex);
                Object objDown = this.defaultlistmodel.remove(iIndex + 1);
                this.defaultlistmodel.add(iIndex + 1, objSelected);
                this.defaultlistmodel.removeElementAt(iIndex);
                this.defaultlistmodel.add(iIndex, objDown);
                jListCorrectAnswer.setSelectedIndex(iIndex + 1);
            }
        } else if (evt.getPropertyName().equals(DefaultController.DOC_MODAL_FEEDBACK_PROPERTY)) {
            String newValue = evt.getNewValue().toString();
            String[] splitNewValue = newValue.split("�");
            String sCorrectFeedback = splitNewValue[0];
            String sNoCorrectFeedback = splitNewValue[1];
            String fieldToChange = splitNewValue[2];
            if (fieldToChange.equals("true")) {
                if (!this.responseAttributesPanel.getCorrectFeedback().equals(sCorrectFeedback)) this.responseAttributesPanel.setCorrectFeedback(sCorrectFeedback);
            } else if (fieldToChange.equals("false")) {
                if (!this.responseAttributesPanel.getNoCorrectFeedback().equals(sNoCorrectFeedback)) this.responseAttributesPanel.setNoCorrectFeedback(sNoCorrectFeedback);
            } else if (fieldToChange.equals(" ")) {
                if ((!this.responseAttributesPanel.getCorrectFeedback().equals(sCorrectFeedback)) & (!this.responseAttributesPanel.getNoCorrectFeedback().equals(sNoCorrectFeedback))) this.responseAttributesPanel.setCorrectFeedback(sCorrectFeedback);
                this.responseAttributesPanel.setNoCorrectFeedback(sNoCorrectFeedback);
            }
        }
    }

    public void removeElementPosition(int k) {
        this.elementsPositions.remove(k);
    }

    private ScrollablePicture setBackgroundImage(File file) {
        ImageIcon icon = null;
        try {
            img = ImageIO.read(file);
            scalingFactor = getImageScale((int) hotspotImgScrollPane.getPreferredSize().getWidth(), (int) hotspotImgScrollPane.getPreferredSize().getHeight(), img.getWidth(), img.getHeight());
            BufferedImage bi = new BufferedImage((int) (img.getWidth() * scalingFactor), (int) (img.getHeight() * scalingFactor), BufferedImage.TYPE_INT_ARGB);
            bi.getGraphics().drawImage(img, 0, 0, (int) (img.getWidth() * scalingFactor), (int) (img.getHeight() * scalingFactor), null);
            icon = new ImageIcon(bi);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ScrollablePicture(icon, 10, scalingFactor);
    }

    /** ******* UTIL ********** */
    public void updateElementsInList() {
        this.defaultlistmodel.removeAllElements();
        Enumeration<Object> e = this.elementsPositions.elements();
        while (e.hasMoreElements()) {
            ElementPosition ep = (ElementPosition) e.nextElement();
            this.defaultlistmodel.add(this.defaultlistmodel.getSize(), ep.element);
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        if (this.jListCorrectAnswer.getSelectedIndex() == 0) {
            this.jButtonUp.setEnabled(false);
            this.jButtonDown.setEnabled(true);
        } else if (this.jListCorrectAnswer.getSelectedIndex() == (this.defaultlistmodel.getSize() - 1)) {
            this.jButtonUp.setEnabled(true);
            this.jButtonDown.setEnabled(false);
        } else {
            this.jButtonUp.setEnabled(true);
            this.jButtonDown.setEnabled(true);
        }
    }

    private double getImageScale(int x, int y, int imgW, int imgH) {
        double scale = 1.0;
        if (imgH > y) {
            scale = (double) y / (double) imgH;
            if (imgW * scale > x) {
                scale = (double) x / (double) imgW;
            }
        } else if (imgW > x) {
            scale = (double) x / (double) imgW;
        }
        return scale;
    }
}
