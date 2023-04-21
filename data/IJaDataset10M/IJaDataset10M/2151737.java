package jhomenet.responsive.gui;

import java.util.*;
import java.lang.reflect.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import org.apache.log4j.Logger;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import foxtrot.*;
import jhomenet.gui.AbstractEditor;
import jhomenet.gui.WindowStatusBar;
import jhomenet.responsive.*;
import jhomenet.responsive.response.*;

/**
 * ID: $Id: PlanEditor.java 830 2005-10-22 19:21:02Z dhirwinjr $
 * Description: This editor is used to edit sensor response plans.
 */
public class PlanEditor extends AbstractEditor implements PlanListener {

    /***
     * Serial version ID information - used for the serialization process.
     */
    private static final long serialVersionUID = 00001;

    /**
     * Define a logging mechanism.
     */
    private static Logger logger = Logger.getLogger(PlanEditor.class.getName());

    /**
     * The default editor title.
     */
    private static final String defaultTitle = "Plan editor";

    /**
     * The plan associated with the plan editor.
     */
    private Plan plan;

    /**
     * Test fields
     */
    private JTextField planDesc_tf;

    /**
     * Lists
     */
    private JList onTrueResponses_l, onFalseResponses_l;

    /**
     * Check boxes
     */
    private JCheckBox alwaysTrue_cb;

    /**
     * List models
     */
    private DefaultListModel expression_lm, onTrueResponses_lm, onFalseResponses_lm;

    private final String EMPTY = "<EMPTY>";

    /**
     * Popup menus
     */
    private final ResponsePopupMenu responseMenu = new ResponsePopupMenu();

    private final JPopupMenu expressionMenu = new JPopupMenu();

    private final JMenuItem newExpression_mi = new JMenuItem("New");

    /**
     * Default constructor.
     */
    public PlanEditor() {
        super();
        plan = new Plan();
        plan.addListener(this);
        setTitle("Plan editor");
    }

    /** 
     * @see jhomenet.gui.AbstractWindow#initializeIdentifier()
     */
    @Override
    public String initializeIdentifier() {
        return this.getClass().getName();
    }

    /**
     * Build and add the status bar to the Plan editor window.
     * 
     * @see jhomenet.gui.AbstractWindow#buildStatusBar()
     */
    @Override
    protected WindowStatusBar buildStatusBar() {
        WindowStatusBar statusBar = new WindowStatusBar(this);
        return statusBar;
    }

    /**
     * @see jhomenet.gui.AbstractEditor#buildMainPanel()
     */
    @Override
    protected JPanel buildMainPanel() {
        setTitle(defaultTitle);
        FormLayout panelLayout = new FormLayout("4dlu, right:pref, 4dlu, 125dlu, 4dlu", "pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, top:75dlu, 5dlu, top:75dlu, 5dlu");
        CellConstraints cc = new CellConstraints();
        PanelBuilder builder = new PanelBuilder(panelLayout);
        builder.addLabel("Plan description:", cc.xy(2, 1));
        planDesc_tf = new JTextField();
        planDesc_tf.setText(plan.getDescription());
        planDesc_tf.setToolTipText("Enter a plan description");
        builder.add(planDesc_tf, cc.xy(4, 1));
        builder.addSeparator("Expression information", cc.xyw(2, 3, 3));
        builder.addLabel("Expression:", cc.xy(2, 5));
        expression_lm = new DefaultListModel();
        final JList expression_l = new JList(expression_lm);
        expression_l.setToolTipText("Right-click for options");
        expression_l.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent evt) {
                if (SwingUtilities.isRightMouseButton(evt)) {
                    expressionMenu.show(evt.getComponent(), evt.getX(), evt.getY());
                }
            }
        });
        expression_l.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        builder.add(expression_l, cc.xy(4, 5));
        alwaysTrue_cb = new JCheckBox("Evaluate to true");
        alwaysTrue_cb.setToolTipText("Set the expression to always evaluate to true");
        builder.add(alwaysTrue_cb, cc.xy(4, 7));
        alwaysTrue_cb.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (alwaysTrue_cb.isSelected()) {
                    expression_l.setEnabled(false);
                    newExpression_mi.setEnabled(false);
                } else {
                    expression_l.setEnabled(true);
                    newExpression_mi.setEnabled(true);
                }
            }
        });
        builder.addSeparator("Response information", cc.xyw(2, 9, 3));
        builder.addLabel("On-True responses:", cc.xy(2, 11));
        onTrueResponses_lm = new DefaultListModel();
        onTrueResponses_l = new JList(onTrueResponses_lm);
        onTrueResponses_l.setToolTipText("Right-click for options");
        onTrueResponses_l.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent evt) {
                if (SwingUtilities.isRightMouseButton(evt)) {
                    responseMenu.setResponseType(Plan.ResponseType.ONTRUE);
                    responseMenu.show(evt.getComponent(), evt.getX(), evt.getY());
                }
            }
        });
        JScrollPane onTrueScrollPane = new JScrollPane(onTrueResponses_l);
        builder.add(onTrueScrollPane, cc.xy(4, 11));
        builder.addLabel("On-False responses:", cc.xy(2, 13));
        onFalseResponses_lm = new DefaultListModel();
        onFalseResponses_l = new JList(onFalseResponses_lm);
        onFalseResponses_l.setToolTipText("Right-click for options");
        onFalseResponses_l.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent evt) {
                if (SwingUtilities.isRightMouseButton(evt)) {
                    responseMenu.setResponseType(Plan.ResponseType.ONFALSE);
                    responseMenu.show(evt.getComponent(), evt.getX(), evt.getY());
                }
            }
        });
        JScrollPane onFalseScrollPane = new JScrollPane(onFalseResponses_l);
        builder.add(onFalseScrollPane, cc.xy(4, 13));
        expressionUpdated();
        onTrueResponseListUpdated();
        createExpressionPopupMenu();
        try {
            Worker.post(new Task() {

                public Object run() throws Exception {
                    createResponsePopupMenu();
                    return null;
                }
            });
        } catch (Exception x) {
            logger.error("Error while building elements in Foxtrot thread: " + x.getMessage());
        }
        JPanel panel = builder.getPanel();
        panel.setBorder(new javax.swing.border.TitledBorder("Plan Setup"));
        return panel;
    }

    /**
     * Create a expression popup menu.
     */
    private void createExpressionPopupMenu() {
        newExpression_mi.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                addToDesktop(new ExpressionEditor());
            }
        });
        expressionMenu.add(newExpression_mi);
        JMenuItem edit_mi = new JMenuItem("Edit");
        edit_mi.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                ExpressionEditor editor = new ExpressionEditor();
                addToDesktop(editor);
                editor.loadObject(plan.getExpression());
            }
        });
        expressionMenu.add(edit_mi);
        JMenuItem delete_mi = new JMenuItem("Delete");
        delete_mi.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                plan.removeExpression();
            }
        });
        expressionMenu.add(delete_mi);
    }

    /**
     * Create a response popup menu
     */
    private void createResponsePopupMenu() {
        final JMenu newResponseMenu = new JMenu("New");
        ArrayList<ArrayList<Class>> classes = GUIManager.getClasses();
        for (int i = 0; i < classes.size(); i++) {
            ArrayList<Class> clazz = classes.get(i);
            final Class editorResponseClass = clazz.get(GUIManager.EDITOR);
            final Class responseClass = clazz.get(GUIManager.RESPONSE);
            AbstractResponse response = null;
            try {
                response = (AbstractResponse) responseClass.newInstance();
            } catch (IllegalAccessException iae) {
                logger.error("Illegal access exception while building response class: " + iae.getMessage());
            } catch (InstantiationException ie) {
                logger.error("Instantiation exception while building response class: " + ie.getMessage());
            }
            JMenuItem tmp_mi = new JMenuItem(response.getResponseDescription());
            tmp_mi.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        final AbstractResponseEditor editor = (AbstractResponseEditor) editorResponseClass.newInstance();
                        editor.setResponseType(responseMenu.getResponseType());
                        addToDesktop(editor);
                    } catch (IllegalAccessException iae) {
                        logger.error("Illegal access exception while building response editor: " + iae.getMessage());
                    } catch (InstantiationException ie) {
                        logger.error("Instantiation exception while building response editor: " + ie.getMessage());
                    }
                }
            });
            newResponseMenu.add(tmp_mi);
        }
        responseMenu.add(newResponseMenu);
        JMenuItem edit_mi = new JMenuItem("Edit");
        edit_mi.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                AbstractResponse response = null;
                if (responseMenu.getResponseType() == Plan.ResponseType.ONTRUE) {
                    response = (AbstractResponse) onTrueResponses_l.getSelectedValue();
                } else if (responseMenu.getResponseType() == Plan.ResponseType.ONFALSE) {
                    response = (AbstractResponse) onFalseResponses_l.getSelectedValue();
                }
                if (response != null) {
                    Class editorClass = GUIManager.getMatchingClass(response.getClass());
                    try {
                        AbstractResponseEditor editor = (AbstractResponseEditor) editorClass.newInstance();
                        editor.setResponseType(responseMenu.getResponseType());
                        addToDesktop(editor);
                        editor.loadObject(response);
                    } catch (IllegalAccessException iae) {
                        logger.error("Illegal access exception while building response editor: " + iae.getMessage());
                    } catch (IllegalArgumentException iae) {
                        logger.error("Illegal argument exception while building response editor: " + iae.getMessage());
                    } catch (InstantiationException ie) {
                        logger.error("Instantiation exception while building response editor: " + ie.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a response for editting.", "Response editting", JOptionPane.OK_OPTION);
                }
            }
        });
        responseMenu.add(edit_mi);
        JMenuItem delete_mi = new JMenuItem("Delete");
        delete_mi.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                AbstractResponse response = (AbstractResponse) onTrueResponses_l.getSelectedValue();
                if (response != null) {
                    plan.removeResponse(response);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a response for deletion.", "Response deletion", JOptionPane.OK_OPTION);
                }
            }
        });
        responseMenu.add(delete_mi);
    }

    private static void showConstructors(Class c) {
        Constructor[] theConstructors = c.getConstructors();
        for (int i = 0; i < theConstructors.length; i++) {
            System.out.print("( ");
            Class[] parameterTypes = theConstructors[i].getParameterTypes();
            for (int k = 0; k < parameterTypes.length; k++) {
                String parameterString = parameterTypes[k].getName();
                System.out.print(parameterString + " ");
            }
            System.out.println(")");
        }
    }

    /** 
     * @see jhomenet.gui.AbstractEditor#resetGUI()
     */
    @Override
    protected void resetGUI() {
    }

    /**
     * Update the internal plan with information from the editor
     * fields.
     */
    private void updatePlan() {
        String tmp = planDesc_tf.getText();
        if ((tmp.trim()).length() != 0) {
            plan.setDescription(tmp);
        }
    }

    /**
     * Add the editor to the desktop. This method should be called to add
     * all editors as it makes sure that the plan is also updated before
     * adding the editor.
     *
     * @param editor
     */
    private void addToDesktop(AbstractResponseEditor editor) {
        updatePlan();
        editor.setPlan(plan);
        addChildWindow(editor);
    }

    /**
     * @see jhomenet.gui.AbstractEditor#commitButtonClicked()
     */
    @Override
    protected void commitButtonClicked() {
        logger.debug("Plan editor save button clicked");
        plan.setDescription(planDesc_tf.getText());
        if (alwaysTrue_cb.isSelected()) {
            Expression tmp = new Expression();
            tmp.addCondition(new BooleanCondition(true));
            plan.setExpression(tmp);
        }
        if (plan.getExpression() == null) {
            JOptionPane.showMessageDialog(getPanel(), "Empty expression! Must create an expression!", "Plan editor error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (plan.getOnTrueResponses().size() == 0) {
            int userResponse = JOptionPane.showConfirmDialog(getPanel(), "No responses defined. Is this ok?", "No responses defined!", JOptionPane.YES_NO_OPTION);
            if (userResponse == JOptionPane.NO_OPTION) {
                return;
            } else if (userResponse == JOptionPane.YES_OPTION) {
            }
        }
        startWork();
        try {
            Worker.post(new Task() {

                public Object run() throws Exception {
                    ResponsiveScheduler.getInstance().addOrUpdatePlan(plan);
                    return null;
                }
            });
        } catch (Exception e) {
            logger.error("Error while adding plan to scheduler: " + e.getMessage());
            JOptionPane.showMessageDialog(getPanel(), "Error while adding plan to scheduler!\r\n" + e.getMessage(), "Plan editor error", JOptionPane.ERROR_MESSAGE);
            return;
        } finally {
            stopWork();
        }
        closeWindow();
    }

    /** 
     * @see jhomenet.gui.AbstractEditor#loadObject(Object)
     */
    @Override
    public void loadObject(Object obj) {
        logger.debug("Loading object");
        if (obj instanceof Plan) {
            logger.debug("Object is a responsive Plan");
            plan = (Plan) obj;
        }
    }

    /**
     * A plan's expression has been updated. Replace the existing
     * expression with the newly updated expression.
     * 
     * @see jhomenet.responsive.PlanListener#expressionUpdated()
     */
    public void expressionUpdated() {
        logger.debug("Received expression update");
        if (plan != null) {
            Expression expression = plan.getExpression();
            expression_lm.removeAllElements();
            if (expression != null) {
                expression_lm.addElement(expression.getDescription());
                newExpression_mi.setEnabled(false);
            } else {
                expression_lm.addElement(EMPTY);
                newExpression_mi.setEnabled(true);
            }
        }
    }

    /**
     * A plan's list of responses has been updated.
     * 
     * TODO: This method is implemented rather poorly. When an
     * update is requested, all the list elements are removed and
     * then are added back again from the plan. Can we cache this
     * better to be more efficient?
     * 
     * @see jhomenet.responsive.PlanListener#onTrueResponseListUpdated()
     */
    public void onTrueResponseListUpdated() {
        logger.debug("Received on-true response list update");
        logger.debug("  Old response list size: " + onTrueResponses_lm.size());
        if (plan != null) {
            onTrueResponses_lm.removeAllElements();
            Collection<AbstractResponse> responses = plan.getOnTrueResponses();
            logger.debug("  New on-true response lists size: " + responses.size());
            for (AbstractResponse response : responses) {
                onTrueResponses_lm.addElement(response);
            }
        }
    }

    /**
     * A plan's list of on-false responses has been updated.
     * 
     * TODO: This method is implemented rather poorly. When an
     * update is requested, all the list elements are removed and
     * then are added back again from the plan. Can we cache this
     * better to be more efficient?
     * 
     * @see jhomenet.responsive.PlanListener#onFalseResponseListUpdated()
     */
    public void onFalseResponseListUpdated() {
        logger.debug("Received on-false response list update");
        logger.debug("  Old response list size: " + onFalseResponses_lm.size());
        if (plan != null) {
            onFalseResponses_lm.removeAllElements();
            Collection<AbstractResponse> responses = plan.getOnFalseResponses();
            logger.debug("  New on-false response lists size: " + responses.size());
            for (AbstractResponse response : responses) {
                onFalseResponses_lm.addElement(response);
            }
        }
    }

    /**
     * @see jhomenet.responsive.PlanListener#responseUpdated()
     */
    public void responseUpdated() {
    }
}
