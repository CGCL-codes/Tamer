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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import uk.ac.kingston.aqurate.author_controllers.DefaultController;
import uk.ac.kingston.aqurate.author_controllers.OrderInteractionController;
import uk.ac.kingston.aqurate.util.HTMLTextEnum;
import uk.ac.kingston.aqurate.util.QTITableModel;

public class OrderInteractionViewPanel extends AbstractViewPanel implements FocusListener, TableModelListener, ActionListener, ListSelectionListener {

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

    private static final int maxRowAllowed = 10;

    private static final long serialVersionUID = 1L;

    public AttachImagePanel attachImagePanel = null;

    private Hashtable<String, String> correctOrderElements = new Hashtable<String, String>();

    private DefaultListModel defaultlistmodel = null;

    private String defaultPrompt = "Can you rearrange the following items into the correct finishing order?";

    public Properties elementsPositions = null;

    public HTMLPanel htmlPanelPrompt = null;

    private JButton jButtonAddElement = null;

    private JButton jButtonDelElement = null;

    private JButton jButtonDown = null;

    private JButton jButtonUp = null;

    private JLabel jLabelAnswer = null;

    private JLabel jLabelInteraction = null;

    /** ASSESMENT TYPE UI COMPONENTS */
    private JLabel jLabelPrompt = null;

    private JList jListCorrectAnswer = null;

    public JPanel jPanelInteraction = null;

    public JPanel jPanelWording = null;

    private JScrollPane jScrollPaneCorrectOrder = null;

    private JScrollPane jScrollPaneElements = null;

    private JTabbedPane jTabbedPaneAttributes = null;

    private JTable jTableElements = null;

    public OrderInteractionController oic;

    /** ORDER INTERACTION COMPONENTS */
    public QTITableModel qtiTableModel = null;

    public QuestionAttributes questionAttributesPanel = null;

    public ResponseAttributes responseAttributesPanel = null;

    public MetadataAttributes metadataAttributesPanel = null;

    /** CONTRUCTOR */
    public OrderInteractionViewPanel(AqurateFramework owner, DefaultController controller) {
        super(owner, "OrderInteractionViewPanel", controller);
        oic = (OrderInteractionController) controller;
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
                oic.changePreamble("");
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
                oic.changePostscript("");
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
                oic.changeCorrectChoices(iselected, "UP");
            } else {
                JOptionPane.showMessageDialog(null, "Please select an element to move up.", "No element selected!", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (e.getSource().equals(jButtonDown)) {
            int iselected = this.jListCorrectAnswer.getSelectedIndex();
            if (iselected != -1) {
                oic.changeCorrectChoices(iselected, "DOWN");
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

    public void focusGained(FocusEvent arg0) {
    }

    public void focusLost(FocusEvent arg0) {
    }

    private JPanel getAttachImagePanel() {
        if (attachImagePanel == null) {
            attachImagePanel = new AttachImagePanel(owner, oic, getHTMLPanelPrompt());
        }
        return attachImagePanel;
    }

    /** ************** UI METHODS ******************* */
    @Override
    public HTMLPanel getHTMLPanelPrompt() {
        if (htmlPanelPrompt == null) {
            htmlPanelPrompt = new HTMLPanel(owner, oic, HTMLTextEnum.PROMPT);
        }
        return htmlPanelPrompt;
    }

    private JButton getJButtonAddElement() {
        if (jButtonAddElement == null) {
            jButtonAddElement = new JButton("Add Element");
            jButtonAddElement.setPreferredSize(new Dimension(130, 30));
        }
        jButtonAddElement.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                try {
                    if (qtiTableModel.getRowCount() == 0) oic.changeAddChoice("��false"); else if (qtiTableModel.getRowCount() < maxRowAllowed) oic.changeAddChoice("��false"); else JOptionPane.showMessageDialog(null, "Max 10 elements is allowed", "Too many elements", JOptionPane.ERROR_MESSAGE);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        });
        return jButtonAddElement;
    }

    private JButton getJButtonDelElement() {
        if (jButtonDelElement == null) {
            jButtonDelElement = new JButton("Delete Element");
            jButtonDelElement.setPreferredSize(new Dimension(130, 30));
        }
        jButtonDelElement.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                try {
                    int[] rowsSelected = jTableElements.getSelectedRows();
                    int numberSelected = rowsSelected.length;
                    if (numberSelected != 0 && numberSelected < jTableElements.getRowCount()) {
                        for (int rs = 0; rs < numberSelected; rs++) {
                            if ((Boolean) qtiTableModel.getValueAt(rowsSelected[rs], 2) && qtiTableModel.getRowCount() > 1) {
                                boolean allOtherUnchecked = true;
                                for (int i = 0; i < qtiTableModel.getRowCount(); i++) {
                                    if (i != rowsSelected[rs] && (Boolean) qtiTableModel.getValueAt(i, 2)) allOtherUnchecked = false;
                                }
                                if (allOtherUnchecked) {
                                    if (rowsSelected[rs] >= 1) qtiTableModel.setValueAt(true, rowsSelected[rs] - 1, 2); else qtiTableModel.setValueAt(true, rowsSelected[rs] + 1, 2);
                                }
                            }
                            oic.changeDelChoice(Integer.toString(rowsSelected[rs]));
                            updateSelectedRows(rowsSelected);
                        }
                    } else if (numberSelected == 0) {
                        JOptionPane.showMessageDialog(null, "Please select the element to delete", "No element selected!", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "You must have at least 1 element to order", "Can not delete!", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        });
        return jButtonDelElement;
    }

    private JButton getJButtonDown() {
        if (jButtonDown == null) {
            jButtonDown = new JButton("Down");
            jButtonDown.setPreferredSize(new Dimension(70, 25));
        }
        jButtonDown.addActionListener(this);
        return jButtonDown;
    }

    private JButton getJButtonUp() {
        if (jButtonUp == null) {
            jButtonUp = new JButton("Up");
            jButtonUp.setPreferredSize(new Dimension(70, 25));
        }
        jButtonUp.addActionListener(this);
        return jButtonUp;
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
            jLabelInteraction = new JLabel();
            jLabelInteraction.setText("Define all the answers in the following table: ");
            jPanelInteraction = new JPanel();
            jPanelInteraction.setPreferredSize(new Dimension(680, 360));
            jPanelInteraction.setLayout(new GridBagLayout());
            jPanelInteraction.setBorder(new TitledBorder("Response Declaration"));
            jPanelInteraction.setVisible(true);
            jPanelInteraction.add(jLabelInteraction, this.buildConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST, 10, 10, 10, 0));
            jPanelInteraction.add(getJScrollPaneElements(), this.buildConstraints(0, 1, 3, 2, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.WEST, 0, 10, 30, 20));
            jPanelInteraction.add(getJButtonAddElement(), this.buildConstraints(3, 1, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST, 20, 10, 0, 0));
            jPanelInteraction.add(getJButtonDelElement(), this.buildConstraints(3, 2, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.EAST, 20, 0, 40, 0));
            jPanelInteraction.add(getJScrollPaneCorrectOrder(), this.buildConstraints(0, 4, 2, 2, 0, 0, GridBagConstraints.BOTH, GridBagConstraints.WEST, 0, 10, 0, 0));
            jPanelInteraction.add(getJButtonUp(), this.buildConstraints(2, 4, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST, 40, 15, 0, 0));
            jPanelInteraction.add(getJButtonDown(), this.buildConstraints(2, 5, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST, 10, 15, 40, 0));
        }
        return jPanelInteraction;
    }

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
            jScrollPaneCorrectOrder = new JScrollPane();
            jScrollPaneCorrectOrder.setPreferredSize(new Dimension(150, 100));
            jScrollPaneCorrectOrder.setBorder(new TitledBorder("CORRECT ORDERING"));
            jScrollPaneCorrectOrder.setViewportView(getJListCorrectAnswer());
        }
        return jScrollPaneCorrectOrder;
    }

    private JScrollPane getJScrollPaneElements() {
        if (jScrollPaneElements == null) {
            jScrollPaneElements = new JScrollPane();
            jScrollPaneElements.setPreferredSize(new Dimension(400, 70));
            jScrollPaneElements.setViewportView(getJTableElements());
        }
        return jScrollPaneElements;
    }

    private JTabbedPane getJTabbedPaneAttributes() {
        if (jTabbedPaneAttributes == null) {
            jTabbedPaneAttributes = new JTabbedPane();
            jTabbedPaneAttributes.setPreferredSize(new Dimension(310, 380));
        }
        questionAttributesPanel = new QuestionAttributes(owner, oic);
        responseAttributesPanel = new ResponseAttributes(owner, oic, qtiTableModel, jTableElements, null, null);
        metadataAttributesPanel = new MetadataAttributes(owner, oic);
        responseAttributesPanel.disableShowScoreColumn();
        jTabbedPaneAttributes.addTab("Title and Presentation", questionAttributesPanel);
        jTabbedPaneAttributes.addTab("Scoring and Feedback", responseAttributesPanel);
        jTabbedPaneAttributes.addTab("Metadata", metadataAttributesPanel);
        return jTabbedPaneAttributes;
    }

    private JTable getJTableElements() {
        if (jTableElements == null) {
            jTableElements = new JTable();
        }
        Vector<String> columnNames = new Vector<String>();
        columnNames.addElement("id.");
        columnNames.addElement("ELEMENTS");
        columnNames.addElement("FIX POSITION");
        Vector<Object[]> v = new Vector<Object[]>(columnNames.size());
        qtiTableModel = new QTITableModel(v, columnNames);
        jTableElements.setModel(qtiTableModel);
        jTableElements.getModel().addTableModelListener(this);
        jTableElements.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        qtiTableModel.showHideLastColumn(true);
        TableColumn column = null;
        for (int i = 0; i < jTableElements.getColumnModel().getColumnCount(); i++) {
            column = jTableElements.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(35);
            } else if (i == 1) {
                column.setPreferredWidth(350);
            } else if (i == 2) {
                column.setPreferredWidth(100);
            }
        }
        jTableElements.setRowHeight(25);
        jTableElements.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent arg0) {
            }

            public void focusLost(FocusEvent arg0) {
            }
        });
        jTableElements.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    jTableElements.changeSelection(jTableElements.getSelectedRow(), jTableElements.getSelectedColumn(), false, false);
                }
            }

            public void mouseEntered(MouseEvent me) {
            }

            public void mouseExited(MouseEvent me) {
            }

            public void mousePressed(MouseEvent me) {
            }

            public void mouseReleased(MouseEvent me) {
            }
        });
        return jTableElements;
    }

    /** INIT THE INTERFACE */
    public void initialize() {
        this.getJPanelPreamble();
        this.getJPanelPostcript();
        elementsPositions = new Properties();
        jLabelPrompt = new JLabel();
        jLabelPrompt.setText("Write your question (e.g. Can you rearrange them into the correct finishing order?)");
        this.setLayout(new GridBagLayout());
        add(getJButtonPreamble(), this.buildConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.NORTHWEST, 10, 10, 0, 0));
        add(getJPanelWording(), this.buildConstraints(0, 1, 2, 2, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST, 20, 10, 0, 0));
        add(getJPanelInteraction(), this.buildConstraints(0, 3, 2, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST, 20, 10, 0, 0));
        add(getJButtonPostcript(), this.buildConstraints(0, 4, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.NORTHWEST, 10, 10, 20, 0));
        add(getJTabbedPaneAttributes(), this.buildConstraints(2, 1, 1, 3, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER, 0, 50, 250, 50));
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
            jEditorPanePostscript.setText(popupPostscript.returnHtmlEditorText());
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
        } else if (evt.getPropertyName().equals(DefaultController.DOC_TICKET_PROPERTY)) {
            String newStringValue = evt.getNewValue().toString();
            this.questionAttributesPanel.jlTicket.setText("Ticket number:" + newStringValue);
        } else if (evt.getPropertyName().equals(OrderInteractionController.DOCUMENT_CHOICE_ADD)) {
            String newStringValue = evt.getNewValue().toString();
            String[] newChoice = newStringValue.split("�");
            Object[] data = { qtiTableModel.getRowCount() + 1, newChoice[1], new Boolean(newChoice[2]) };
            qtiTableModel.addRow(data);
            correctOrderElements.put(newChoice[0], newChoice[1]);
        } else if (evt.getPropertyName().equals(OrderInteractionController.DOCUMENT_CHOICE_DEL)) {
            int iIndex = Integer.parseInt((String) evt.getNewValue());
            for (int i = iIndex + 1; i < qtiTableModel.getRowCount(); i++) {
                qtiTableModel.setValueAt(i, i, 0);
            }
            qtiTableModel.delRow(iIndex);
        } else if (evt.getPropertyName().equals(OrderInteractionController.DOCUMENT_CHOICE_CHANGE)) {
            String[] newValue = evt.getNewValue().toString().split("�");
            String sID = newValue[0];
            String sText = newValue[1];
            for (int i = 0; i < defaultlistmodel.getSize(); i++) {
                if (defaultlistmodel.elementAt(i).equals(correctOrderElements.get(sID))) {
                    defaultlistmodel.setElementAt(sText, i);
                    break;
                }
            }
            correctOrderElements.put(sID, sText);
        }
        if (evt.getPropertyName().equals(OrderInteractionController.DOCUMENT_CORRECT_ADD)) {
            String sID = evt.getNewValue().toString();
            defaultlistmodel.addElement(correctOrderElements.get(sID));
        } else if (evt.getPropertyName().equals(OrderInteractionController.DOCUMENT_CORRECT_DEL)) {
            String sID = evt.getNewValue().toString();
            int iIndex = -1;
            for (int i = 0; i < defaultlistmodel.getSize(); i++) {
                if (defaultlistmodel.elementAt(i).equals(correctOrderElements.get(sID))) {
                    iIndex = i;
                    break;
                }
            }
            if (iIndex >= 0) defaultlistmodel.removeElementAt(iIndex);
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
            String feedbacksValue = evt.getNewValue().toString();
            String[] splitNewValue = feedbacksValue.split("�");
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

    /** **** END UI ***** */
    public void removeElementPosition(int k) {
        this.elementsPositions.remove(k);
    }

    public void tableChanged(TableModelEvent e) {
        if (e.getColumn() == 1 || e.getColumn() == 2) {
            String element = (String) qtiTableModel.getValueAt(e.getLastRow(), 1);
            String fixcolumn = qtiTableModel.getValueAt(e.getLastRow(), 2).toString();
            String s = "";
            s = s.concat(element);
            s = s.concat("�");
            s = s.concat(fixcolumn);
            oic.changeChoice(e.getLastRow(), s);
        }
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

    public void updateElementsPositions() {
        this.elementsPositions.clear();
        int rows = this.qtiTableModel.getRowCount();
        for (int r = 0; r < rows; r++) {
            int key = (Integer) this.qtiTableModel.getValueAt(r, 0);
            String elem = (String) this.qtiTableModel.getValueAt(r, 1);
            ElementPosition ep = new ElementPosition(elem, 0);
            this.elementsPositions.put(key, ep);
        }
    }

    private void updateSelectedRows(int[] selectedRows) {
        for (int rs = 0; rs < selectedRows.length; rs++) {
            selectedRows[rs] = selectedRows[rs] - 1;
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
}
