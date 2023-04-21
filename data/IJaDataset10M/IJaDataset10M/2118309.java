package org.sourceforge.espro.gui;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.sourceforge.espro.model.Item;
import org.sourceforge.espro.model.ItemData;
import org.sourceforge.espro.model.RunLogger;
import org.sourceforge.espro.model.RunnableQuestionnaire;
import org.sourceforge.espro.model.Settings;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

/**
 * The QuestinnaireAssistant runs the questionnaire.
 *
 * @author (c) 2007 Martin Kaffanke
 * @version 2.0
 */
public class QuestionnaireAssistant extends PageManager {

    /**  */
    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(QuestionnaireAssistant.class.getName());

    private static JFrame frame;

    private JTextField titleText = null;

    private RunLogger rLogger = new RunLogger();

    private RunnableQuestionnaire questionnaire = null;

    private boolean doneButtonAdded = false;

    /**
     * The default constructor. This runs the actual edited
     * questionnaire, or exatcly, this runs the saved version of the actual
     * edited questionnaire.
     */
    public QuestionnaireAssistant() {
        super();
        initialize();
    }

    /**
     * An action to run the questionnaire.
     *
     * @return The action.
     */
    public static AbstractAction getRunQuestionnaireAction() {
        class RunQuestionnaireAction extends AbstractAction {

            /**  */
            private static final long serialVersionUID = 5903228327293657362L;

            /**
             * The action to create a new questionnaire. Used in the
             * menu and toolbar.
             */
            public RunQuestionnaireAction() {
                super("Run");
                putValue(SHORT_DESCRIPTION, "Runs the actual questionnaire");
                putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_R));
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
                putValue(SMALL_ICON, IconProvider.getIcon(IconProvider.RUN));
            }

            /**
             * 
             * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
             */
            public void actionPerformed(final ActionEvent e) {
                QuestionnaireAssistant.run();
            }
        }
        return new RunQuestionnaireAction();
    }

    /**
     * Adds a page to the editor which edits the given Item.
     *
     * @param item The Item for that we want a new page.
     */
    public void add(final Item item) {
        super.add(generatePanel(item), item.getId());
    }

    /**
     * Returns the questionnaire.
     *
     * @return The questionnaire.
     */
    public RunnableQuestionnaire getQuestionnaire() {
        return questionnaire;
    }

    /**
     * Returns the titleText.
     *
     * @return The titleText.
     */
    public JTextField getTitleText() {
        return titleText;
    }

    /**
     * Runs the Questionnaire.
     */
    public static void run() {
        final QuestionnaireAssistant qAss = new QuestionnaireAssistant();
        frame = new JFrame();
        frame.setUndecorated(true);
        frame.setSize(new Dimension(1000, 700));
        frame.setResizable(true);
        frame.setLayout(new BorderLayout());
        GraphicsDevice device;
        device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (device.isFullScreenSupported()) {
            device.setFullScreenWindow(frame);
            frame.setAlwaysOnTop(true);
        } else {
            logger.log(Level.WARNING, "Full screen not supported");
        }
        final BorderLayout layout = new BorderLayout();
        final JPanel wrapperPanel = new JPanel();
        wrapperPanel.setLayout(layout);
        frame.add(BorderLayout.CENTER, wrapperPanel);
        wrapperPanel.add(BorderLayout.CENTER, qAss);
        if (qAss.getQuestionnaire() == null) {
            frame.dispose();
            return;
        }
        RunnableQuestionnaire questionnaire = qAss.getQuestionnaire();
        if (questionnaire.getSettings().getBoolean("autoPersonSid")) {
            questionnaire.setSid(questionnaire.getSettings().getString("autoPersonSidPrefix") + questionnaire.getParent().getData().size());
        } else {
            String inputValue = JOptionPane.showInputDialog(qAss, "Please input your subject id.", questionnaire.getSettings().getString("autoPersonSidPrefix"));
            if (inputValue == null) {
                frame.dispose();
                return;
            }
            questionnaire.setSid(inputValue);
            System.out.println(inputValue);
        }
        frame.setTitle("ESPRO - " + qAss.getQuestionnaire().getSettings().getString("title"));
        if (questionnaire.getSettings().getBoolean("pageNumbers")) {
            qAss.setNotify(NotificationArea.getInstance("assistantWindow"));
            final NotificationArea notificationArea = NotificationArea.getInstance("assistantWindow");
            wrapperPanel.add(BorderLayout.SOUTH, notificationArea);
        }
        frame.setVisible(true);
    }

    /**
     * Sets the titleText
     *
     * @param titleText The titleText to set.
     */
    public void setTitleText(final JTextField titleText) {
        this.titleText = titleText;
    }

    /**
     * Loads the defined items.
     */
    private void addPages() {
        if (questionnaire == null) {
            return;
        }
        for (final Item i : questionnaire.getItems()) {
            add(i);
        }
    }

    /**
     * generates an ItemEditor Page for the item.
     *
     * @param item The Item.
     *
     * @return The generated JPanel.
     */
    private JPanel generatePanel(final Item item) {
        JPanel panel = null;
        panel = new ItemAssistant(item);
        return panel;
    }

    private void initialize() {
        final QuestionnaireManager qm = QuestionnaireManager.getInstance();
        if (qm.getActualQuestionnaire() == null) {
            qm.openQuestionnaire();
        }
        if (qm.getActualQuestionnaire() == null) {
            return;
        }
        questionnaire = qm.getActualQuestionnaire().getRunnable();
        String logFilePath = questionnaire.getSettings().getString("logFilePath");
        if ((logFilePath != null) && (logFilePath != "")) {
            rLogger = new RunLogger(logFilePath);
        }
        final JPanel first = new JPanel();
        titleText = new JTextField(30);
        final FormLayout layout = new FormLayout("center:pref:grow", "center:pref:grow, 7dlu, pref");
        first.setLayout(layout);
        final PanelBuilder builder = new PanelBuilder(layout, first);
        builder.setDefaultDialogBorder();
        final CellConstraints cc = new CellConstraints();
        builder.addLabel("Title", cc.xy(1, 1));
        builder.add(titleText, cc.xy(1, 3));
        this.addPropertyChangeListener("show", new PropertyChangeListener() {

            public void propertyChange(final PropertyChangeEvent arg0) {
                final RunnableQuestionnaire q = QuestionnaireAssistant.this.questionnaire;
                Item i = q.getItem(pages.get(getCurrent()));
                q.setCurrent(i);
                i.setDone(true);
                if ((!doneButtonAdded) && questionnaire.isDone()) {
                    addButton(new DoneAction());
                    doneButtonAdded = true;
                }
            }
        });
        setBackwards(questionnaire.getSettings().getBoolean("canBackwards"));
        addPages();
        questionnaire.start();
        questionnaire.getCurrent().start();
        rLogger.log("START: New questionnaire run started.");
        String s = "";
        s += "\n====================================================\n";
        s += "Settings:\n";
        Settings settings = questionnaire.getSettings();
        for (String key : settings.getSettings().keySet()) {
            s += (key + " == " + settings.getString(key) + "\n");
        }
        s += "\n====================================================\n";
        rLogger.log(s);
        questionnaire.addPropertyChangeListener("current", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                Item o = (Item) evt.getOldValue();
                o.stop();
                o.getMethod().free();
                Item n = (Item) evt.getNewValue();
                n.start();
                logItem(o);
            }
        });
        viewPage((Component) null);
        addPageAction(new PageAction());
    }

    private void logItem(Item i) {
        if (!rLogger.isLogging()) {
            return;
        }
        String s = "";
        s += "\n====================================================\n";
        s += ("Item: " + i.getId() + " has the following values:\n");
        ItemData data = new ItemData(i);
        for (String key : data.getData().keySet()) {
            s += (key + " == " + data.get(key) + "\n");
        }
        s += "====================================================\n";
        rLogger.log(s);
    }

    private class DoneAction extends AbstractAction {

        public DoneAction() {
            super("done");
            putValue(SMALL_ICON, IconProvider.getIcon(IconProvider.DONE));
            putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_D));
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
        }

        /**
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(final ActionEvent e) {
            rLogger.log("BUTTON: Done button clicked.");
            questionnaire.getCurrent().stop();
            logItem(questionnaire.getCurrent());
            rLogger.log("FINISHED: Questionnaire successfully finished.");
            rLogger.close();
            QuestionnaireAssistant.this.frame.dispose();
            questionnaire.storeData();
            if (questionnaire.getSettings().getBoolean("autoSaveAfterSubject")) {
                AbstractAction saveAction = QuestionnaireManager.getInstance().getSaveQuestionnaireAction();
                saveAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
            }
        }
    }

    private class PageAction extends AbstractPageAction {

        public void actionPerformed(ActionEvent e) {
            if (e.getID() == PageManager.NEXT_ACTION) {
                rLogger.log("BUTTON: Next button clicked.");
            }
            if (e.getID() == PageManager.PREVIOUS_ACTION) {
                rLogger.log("BUTTON: Previous button clicked.");
            }
        }
    }
}
