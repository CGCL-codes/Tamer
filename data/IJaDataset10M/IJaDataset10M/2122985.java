package org.mitre.interactive.refimpl.gui.view;

import java.util.List;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.EventListenerList;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;
import org.jdesktop.application.Action;
import org.mitre.interactive.x02.BooleanQuestionDocument.BooleanQuestion;
import org.mitre.interactive.x02.BooleanQuestionTestActionDocument.BooleanQuestionTestAction;
import org.mitre.interactive.x02.ChoiceQuestionDocument.ChoiceQuestion;
import org.mitre.interactive.x02.ChoiceQuestionTestActionDocument.ChoiceQuestionTestAction;
import org.mitre.interactive.x02.InstructionsDocument.Instructions;
import org.mitre.interactive.x02.ItemBaseType;
import org.mitre.interactive.x02.NumericQuestionDocument.NumericQuestion;
import org.mitre.interactive.x02.NumericQuestionTestActionDocument.NumericQuestionTestAction;
import org.mitre.interactive.x02.QuestionResultType;
import org.mitre.interactive.x02.QuestionTestActionType;
import org.mitre.interactive.x02.QuestionType;
import org.mitre.interactive.x02.QuestionnaireDocument.Questionnaire;
import org.mitre.interactive.x02.ResultChoiceType;
import org.mitre.interactive.x02.ResultType;
import org.mitre.interactive.x02.ResultsDocument.Results.QuestionnaireResult;
import org.mitre.interactive.x02.StringQuestionDocument.StringQuestion;
import org.mitre.interactive.x02.StringQuestionTestActionDocument.StringQuestionTestAction;
import org.mitre.interactive.refimpl.gui.common.InteractiveDocumentManager;
import org.mitre.interactive.refimpl.gui.common.IInteractiveGUIEventListener;
import org.mitre.interactive.refimpl.gui.common.InteractiveGUIEvent;
import org.mitre.interactive.refimpl.gui.common.InteractiveGUIUtilities;
import org.mitre.interactive.refimpl.gui.components.BooleanQuestionTestActionPanel;
import org.mitre.interactive.refimpl.gui.components.ChoiceQuestionTestActionPanel;
import org.mitre.interactive.refimpl.gui.components.IQuestionTestActionPanel;
import org.mitre.interactive.refimpl.gui.components.InstructionsTreePanel;
import org.mitre.interactive.refimpl.gui.components.InteractiveTreeNode;
import org.mitre.interactive.refimpl.gui.components.NumericQuestionTestActionPanel;
import org.mitre.interactive.refimpl.gui.components.StringQuestionTestActionPanel;
import org.mitre.interactive.x02.ResultsDocument.Results.TestActionResult;

/**
 * QuestionTestActionView is a JPanel displaying a question test action,
 * it's path to other test actions, it's result, and instructions.
 * 
 * @author  mcasipe
 */
public class QuestionTestActionView extends javax.swing.JPanel implements IView {

    private static Logger logger = Logger.getLogger(QuestionTestActionView.class.getPackage().getName());

    private EventListenerList listeners = new EventListenerList();

    private QuestionTestActionType questionTestAction = null;

    private IQuestionTestActionPanel questionTestActionPanel = null;

    private Object source = null;

    private InstructionsTreePanel instructionsTreePanel = null;

    /**
     * QuestionTestActionView Constructor
     */
    public QuestionTestActionView() {
        initComponents();
        progressTable.setColumnSelectionAllowed(false);
        progressTable.setCellSelectionEnabled(false);
        progressTable.setRowSelectionAllowed(true);
        ListSelectionModel selectionModel = progressTable.getSelectionModel();
        selectionModel.setSelectionMode(selectionModel.SINGLE_SELECTION);
        progressTable.setAutoCreateRowSorter(true);
        splitPanel.setResizeWeight(0.50);
    }

    public void rebuild() {
        buildView(source, questionTestAction);
    }

    /**
     * This method fills in the information that is displayed in the panel.
     *
     * @param s         - calling object
     * @param selected  - question test action to display
     */
    public void buildView(Object s, QuestionTestActionType qta) {
        questionTestAction = qta;
        source = s;
        rebuildProgressTable();
        questionTestActionPanel = getQuestionPanel(qta);
        splitPanel.setBottomComponent((JPanel) questionTestActionPanel);
        progressTable.getSelectionModel().setSelectionInterval(0, 0);
        QuestionType question = InteractiveDocumentManager.getQuestionById(qta.getQuestionRef());
        setInstructionsTab(question);
    }

    public void setInstructionsTab(QuestionType question) {
        if (question == null) {
            if (tabbedPane.getComponentCount() > 1) tabbedPane.remove(1);
        } else if (question.isSetInstructions()) {
            if (tabbedPane.getComponentCount() > 1) tabbedPane.remove(1);
            Instructions instructions = question.getInstructions();
            instructionsTreePanel = new InstructionsTreePanel(instructions);
            tabbedPane.add("Instructions", instructionsTreePanel);
        } else if (tabbedPane.getComponentCount() > 1) tabbedPane.remove(1);
        tabbedPane.revalidate();
    }

    /**
     * This method retrieves the calling object of this panel.
     * 
     * @return
     */
    public Object getSource() {
        return source;
    }

    /**
     * This method adds an interactive event listener to the QuestionTestAction View.
     *
     * @param l
     */
    public void addInteractiveEventListener(IInteractiveGUIEventListener l) {
        listeners.add(IInteractiveGUIEventListener.class, l);
    }

    /**
     * A helper method to rebuild (starts from scratch) the question test 
     * action path's table.
     * 
     */
    private void rebuildProgressTable() {
        logger.info("QuestionTestActionView, rebuildProgressTable(): Cleaning table.");
        DefaultTableModel model = (DefaultTableModel) progressTable.getModel();
        model.setRowCount(0);
        logger.info("QuestionTestActionView, rebuildProgressTable(): Retrieving path.");
        List<ItemBaseType> items = InteractiveDocumentManager.getTestActionPath(questionTestAction);
        QuestionTestActionType selected = (questionTestActionPanel == null) ? null : questionTestActionPanel.getQuestionTestAction();
        for (ItemBaseType item : items) {
            if (item instanceof Questionnaire) addQuestionnaireToProgressTable((Questionnaire) item); else if (item instanceof QuestionTestActionType) {
                QuestionTestActionType qta = (QuestionTestActionType) item;
                boolean select = false;
                if (selected != null && qta.getId().equalsIgnoreCase(selected.getId())) select = true;
                addQuestionTestActionToProgressTable(qta, select);
            } else {
                logger.debug("QuestionTestActionView, rebuildProgressTable(): Unsupported item - " + item.getClass().getName());
            }
        }
        logger.info("QuestionTestActionView, rebuildProgressTable(): All items had been added.");
        int lastRow = progressTable.getRowCount() - 1;
        String rowResult = (String) progressTable.getValueAt(lastRow, 2);
        finalResultLabel.setText(rowResult);
    }

    /**
     * A helper method to add a row of question test action to the table.
     * 
     * @param qta       - question test action object for this row
     * @param select    - to select or not to select this row
     */
    private void addQuestionTestActionToProgressTable(QuestionTestActionType qta, boolean select) {
        String col0 = Integer.toString(progressTable.getRowCount() + 1);
        InteractiveTreeNode col1 = new InteractiveTreeNode(qta);
        String col2 = "";
        ResultChoiceType next = InteractiveDocumentManager.getNext(qta, InteractiveDocumentManager.getQuestionResultById(qta.getQuestionRef()));
        if (next == null) col2 = ResultType.NOT_TESTED.toString(); else if (next.isSetTestActionRef()) col2 = next.getTestActionRef().getStringValue(); else if (next.isSetResult()) col2 = next.getResult().toString();
        QuestionType question = InteractiveDocumentManager.getQuestionById(qta.getQuestionRef());
        String col3 = question.getQuestionTextArray(0);
        String col4 = "";
        QuestionResultType qresult = InteractiveDocumentManager.getQuestionResultById(qta.getQuestionRef());
        if (qresult == null) col4 = ResultType.NOT_TESTED.toString(); else col4 = InteractiveDocumentManager.getQuestionAnswerInString(question, qresult);
        DefaultTableModel model = (DefaultTableModel) progressTable.getModel();
        Object[] row = new Object[] { col0, col1, col2, col3, col4 };
        model.addRow(row);
        int size = progressTable.getRowCount();
        if (select || size == 1) {
            progressTable.setRowSelectionInterval(size - 1, size - 1);
            setInstructionsTab(question);
        }
    }

    /**
     * A helper method to add a row of questionnaire to the table.
     * 
     * @param qn    - questionnaire object for this row
     */
    private void addQuestionnaireToProgressTable(Questionnaire qn) {
        String col0 = Integer.toString(progressTable.getRowCount() + 1);
        InteractiveTreeNode col1 = new InteractiveTreeNode(qn);
        QuestionnaireResult qnResult = InteractiveDocumentManager.getQuestionnaireResultById(qn.getId());
        String col2 = (qnResult == null) ? ResultType.NOT_TESTED.toString() : qnResult.getResult().toString();
        String col3 = "N/A";
        String col4 = "N/A";
        DefaultTableModel model = (DefaultTableModel) progressTable.getModel();
        Object[] row = new Object[] { col0, col1, col2, col3, col4 };
        model.addRow(row);
        setInstructionsTab(null);
    }

    /**
     * A helper method for building a question panel for a question test
     * action.  It returns null if it cannot be build.  Otherwise, it returns
     * the question test action panel.
     * 
     * @param qta   - question test action object for the panel.
     * @return  
     */
    private IQuestionTestActionPanel getQuestionPanel(QuestionTestActionType qta) {
        IQuestionTestActionPanel panel = null;
        QuestionType question = InteractiveDocumentManager.getQuestionById(qta.getQuestionRef());
        if (question instanceof BooleanQuestion) panel = new BooleanQuestionTestActionPanel((BooleanQuestionTestAction) qta, (BooleanQuestion) question); else if (question instanceof ChoiceQuestion) panel = new ChoiceQuestionTestActionPanel((ChoiceQuestionTestAction) qta, (ChoiceQuestion) question); else if (question instanceof NumericQuestion) panel = new NumericQuestionTestActionPanel((NumericQuestionTestAction) qta, (NumericQuestion) question); else if (question instanceof StringQuestion) panel = new StringQuestionTestActionPanel((StringQuestionTestAction) qta, (StringQuestion) question); else {
            logger.debug("QuestionTestActionView, getQuestionPanel(): Unsupported QuestionType - " + question.getClass().getName());
            return null;
        }
        return panel;
    }

    /**
     * This method goes to the appropriate previous page.
     * 
     */
    @Action
    public void previousPage() {
        logger.info("QuestionTestActionView, previousPage(): Going to the previous page.");
        int row = progressTable.getSelectedRow();
        switch(row) {
            case -1:
                logger.debug("QuestionTestActionView, previousPage(): No Row is Selected.");
                break;
            case 0:
                if (source != null) {
                    logger.info("TestActionView, previousPage(): Sending out a Go to Previous Page command.");
                    InteractiveGUIEvent event = new InteractiveGUIEvent(this, InteractiveGUIEvent.COMMAND.GO_TO_PREVIOUS_PAGE, questionTestAction.getId());
                    InteractiveGUIUtilities.sendInteractiveEventToListeners(this, listeners, event);
                }
                break;
            default:
                InteractiveTreeNode node = (InteractiveTreeNode) progressTable.getValueAt(row - 1, 1);
                progressTable.setRowSelectionInterval(row - 1, row - 1);
                if (node.isQuestionnaire()) {
                    logger.debug("TestActionView, previousPage(): Previous node is a Questionnaire.");
                    setInstructionsTab(null);
                } else if (node.isQuestionTestActionType()) {
                    logger.info("TestActionView, previousPage(): Building the previous question panel.");
                    QuestionTestActionType qta = (QuestionTestActionType) node.getUserObject();
                    questionTestActionPanel = getQuestionPanel(qta);
                    splitPanel.setBottomComponent((JPanel) questionTestActionPanel);
                    setInstructionsTab(InteractiveDocumentManager.getQuestionById(qta.getQuestionRef()));
                } else {
                    logger.debug("TestActionView, previousPage(): The previous node is not supported.");
                    setInstructionsTab(null);
                }
        }
    }

    /**
     * This method goes to the appropriate next page.
     * 
     */
    @Action
    public void nextPage() {
        logger.info("TestActionView, nextPage(): Going to the next page.");
        boolean submitted = questionTestActionPanel.submitAnswer();
        if (!submitted) return;
        logger.info("TestActionView, nextPage(): Answer has been submitted.");
        QuestionTestActionType qta = questionTestActionPanel.getQuestionTestAction();
        ResultChoiceType next = InteractiveDocumentManager.getNext(qta, InteractiveDocumentManager.getQuestionResultById(qta.getQuestionRef()));
        if (next == null) {
            logger.debug("TestActionView, nextPage(): next is null!");
        } else if (next.isSetTestActionRef()) {
            String id = next.getTestActionRef().getStringValue();
            ItemBaseType item = InteractiveDocumentManager.getTestActionById(id);
            if (item == null) item = InteractiveDocumentManager.getQuestionnaireById(id);
            if (item instanceof QuestionTestActionType) {
                logger.info("TestActionView, nextPage(): Building the next question panel.");
                questionTestActionPanel = getQuestionPanel((QuestionTestActionType) item);
                splitPanel.setBottomComponent((JPanel) questionTestActionPanel);
            } else if (item instanceof Questionnaire) {
                logger.info("TestActionView, nextPage(): Sending out a Start Questionnaire command.");
                InteractiveGUIEvent event = new InteractiveGUIEvent(this, InteractiveGUIEvent.COMMAND.START_QUESTIONNAIRE, ((Questionnaire) item).getId());
                InteractiveGUIUtilities.sendInteractiveEventToListeners(this, listeners, event);
            } else {
                logger.debug("TestActionView, nextPage(): The next test action is not supported - " + id);
            }
        } else if (next.isSetResult()) {
            List<ItemBaseType> items = InteractiveDocumentManager.getTestActionPath(questionTestAction);
            for (ItemBaseType item : items) {
                if (item instanceof QuestionTestActionType) {
                    String id = ((QuestionTestActionType) item).getId();
                    TestActionResult taResult = InteractiveDocumentManager.getTestActionResultById(id);
                    if (taResult == null) {
                        taResult = InteractiveDocumentManager.getResults().addNewTestActionResult();
                        taResult.setTestActionRef(id);
                    }
                    taResult.setResult(next.getResult());
                    logger.info("Submitting result: " + taResult);
                    InteractiveDocumentManager.submitResult(taResult);
                } else if (item instanceof Questionnaire) {
                    String id = ((Questionnaire) item).getId();
                    QuestionnaireResult qnResult = InteractiveDocumentManager.getQuestionnaireResultById(id);
                    if (qnResult == null) {
                        qnResult = InteractiveDocumentManager.getResults().addNewQuestionnaireResult();
                        qnResult.setQuestionnaireRef(id);
                    }
                    qnResult.setResult(next.getResult());
                    logger.info("Submitting result: " + qnResult);
                    InteractiveDocumentManager.submitResult(qnResult);
                } else {
                    logger.debug("Test Action Result could not be submitted.");
                }
            }
            InteractiveDocumentManager.updateQuestionnaireResults(items);
            logger.info("TestActionView, nextPage(): Sending out a Go to Next Page command.");
            InteractiveGUIEvent event = new InteractiveGUIEvent(this, InteractiveGUIEvent.COMMAND.GO_TO_NEXT_PAGE, qta.getId());
            InteractiveGUIUtilities.sendInteractiveEventToListeners(this, listeners, event);
        } else {
            logger.debug("TestActionView, nextPage(): At the final else!");
        }
        logger.info("TestActionView, nextPage(): Rebuilding the progress table.");
        rebuildProgressTable();
    }

    private void initComponents() {
        nextButton = new javax.swing.JButton();
        previousButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        splitPanel = new javax.swing.JSplitPane();
        tabbedPane = new javax.swing.JTabbedPane();
        testActionPathPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        progressTable = new javax.swing.JTable();
        instructionsPanel = new javax.swing.JPanel();
        noInstructionsPanel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        finalResultLabel = new javax.swing.JLabel();
        setMinimumSize(new java.awt.Dimension(500, 500));
        setName("Form");
        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(org.mitre.interactive.refimpl.gui.InteractiveRefImplApp.class).getContext().getActionMap(QuestionTestActionView.class, this);
        nextButton.setAction(actionMap.get("nextPage"));
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.mitre.interactive.refimpl.gui.InteractiveRefImplApp.class).getContext().getResourceMap(QuestionTestActionView.class);
        nextButton.setText(resourceMap.getString("nextButton.text"));
        nextButton.setName("nextButton");
        previousButton.setAction(actionMap.get("previousPage"));
        previousButton.setText(resourceMap.getString("previousButton.text"));
        previousButton.setName("previousButton");
        jLabel1.setFont(resourceMap.getFont("jLabel1.font"));
        jLabel1.setText(resourceMap.getString("jLabel1.text"));
        jLabel1.setName("jLabel1");
        splitPanel.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        splitPanel.setName("splitPanel");
        tabbedPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8));
        tabbedPane.setFont(resourceMap.getFont("tabbedPane.font"));
        tabbedPane.setName("tabbedPane");
        testActionPathPanel.setName("testActionPathPanel");
        jScrollPane1.setName("jScrollPane1");
        progressTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] { "#", "Test Action", "Result", "Question", "Answer" }) {

            Class[] types = new Class[] { java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class };

            boolean[] canEdit = new boolean[] { false, false, false, false, false };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        progressTable.setName("progressTable");
        progressTable.getTableHeader().setReorderingAllowed(false);
        progressTable.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                progressTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(progressTable);
        javax.swing.GroupLayout testActionPathPanelLayout = new javax.swing.GroupLayout(testActionPathPanel);
        testActionPathPanel.setLayout(testActionPathPanelLayout);
        testActionPathPanelLayout.setHorizontalGroup(testActionPathPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE));
        testActionPathPanelLayout.setVerticalGroup(testActionPathPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE));
        tabbedPane.addTab(resourceMap.getString("testActionPathPanel.TabConstraints.tabTitle"), testActionPathPanel);
        instructionsPanel.setName("instructionsPanel");
        noInstructionsPanel.setName("noInstructionsPanel");
        jLabel4.setText(resourceMap.getString("jLabel4.text"));
        jLabel4.setName("jLabel4");
        javax.swing.GroupLayout noInstructionsPanelLayout = new javax.swing.GroupLayout(noInstructionsPanel);
        noInstructionsPanel.setLayout(noInstructionsPanelLayout);
        noInstructionsPanelLayout.setHorizontalGroup(noInstructionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(noInstructionsPanelLayout.createSequentialGroup().addContainerGap().addComponent(jLabel4).addContainerGap()));
        noInstructionsPanelLayout.setVerticalGroup(noInstructionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(noInstructionsPanelLayout.createSequentialGroup().addContainerGap().addComponent(jLabel4).addContainerGap()));
        javax.swing.GroupLayout instructionsPanelLayout = new javax.swing.GroupLayout(instructionsPanel);
        instructionsPanel.setLayout(instructionsPanelLayout);
        instructionsPanelLayout.setHorizontalGroup(instructionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(instructionsPanelLayout.createSequentialGroup().addContainerGap().addComponent(noInstructionsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(400, Short.MAX_VALUE)));
        instructionsPanelLayout.setVerticalGroup(instructionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(instructionsPanelLayout.createSequentialGroup().addContainerGap().addComponent(noInstructionsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(50, Short.MAX_VALUE)));
        tabbedPane.addTab(resourceMap.getString("instructionsPanel.TabConstraints.tabTitle"), instructionsPanel);
        splitPanel.setTopComponent(tabbedPane);
        tabbedPane.getAccessibleContext().setAccessibleName(resourceMap.getString("jTabbedPane1.AccessibleContext.accessibleName"));
        finalResultLabel.setFont(resourceMap.getFont("finalResultLabel.font"));
        finalResultLabel.setText(resourceMap.getString("finalResultLabel.text"));
        finalResultLabel.setName("finalResultLabel");
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(previousButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 454, Short.MAX_VALUE).addComponent(nextButton)).addGroup(layout.createSequentialGroup().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 262, Short.MAX_VALUE).addComponent(finalResultLabel)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(splitPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 616, Short.MAX_VALUE))).addContainerGap()));
        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { nextButton, previousButton });
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(finalResultLabel)).addGap(18, 18, 18).addComponent(splitPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(nextButton).addComponent(previousButton)).addContainerGap()));
    }

    /**
     * This method responds to a mouse click in the progress table.
     * 
     * @param evt
     */
    private void progressTableMouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 1) {
            int row = progressTable.getSelectedRow();
            if (row < 0) return;
            InteractiveTreeNode node = (InteractiveTreeNode) progressTable.getValueAt(row, 1);
            if (node.isQuestionnaire()) {
                logger.info("TestActionView, progressTableMouseClicked(): Sending out a Start Questionnaire command.");
                InteractiveGUIEvent event = new InteractiveGUIEvent(this, InteractiveGUIEvent.COMMAND.START_QUESTIONNAIRE, node.getId());
                InteractiveGUIUtilities.sendInteractiveEventToListeners(this, listeners, event);
            } else if (node.isQuestionTestActionType()) {
                logger.info("TestActionView, progressTableMouseClicked(): Building the question panel.");
                questionTestActionPanel = getQuestionPanel((QuestionTestActionType) node.getUserObject());
                splitPanel.setBottomComponent((JPanel) questionTestActionPanel);
            } else {
                logger.debug("TestActionView, progressTableMouseClicked(): Node is not supported.");
            }
        }
    }

    private javax.swing.JLabel finalResultLabel;

    private javax.swing.JPanel instructionsPanel;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JButton nextButton;

    private javax.swing.JPanel noInstructionsPanel;

    private javax.swing.JButton previousButton;

    private javax.swing.JTable progressTable;

    private javax.swing.JSplitPane splitPanel;

    private javax.swing.JTabbedPane tabbedPane;

    private javax.swing.JPanel testActionPathPanel;
}
