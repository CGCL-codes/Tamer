package api.gui;

import api.dbc.DBConnexion;
import api.i18n.Lang;
import api.time.TimerQuestion;
import api.utils.ImgTxtMerger;
import api.xml.Utils;
import com.sun.management.OperatingSystemMXBean;
import exceptions.BadXMLFileException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import model.Action;
import model.ActionScreenshot;
import model.QCMChkBox;
import model.QCMChoice;
import model.QCMRadio;
import model.QReponseLibre;
import model.Question;
import model.Regle;
import model.Reponse;
import model.SessionManager;
import sun.management.ManagementFactory;

/**
 * Class AskFrame.java
 * @description Frame to ask questions and get user's answers
 * @author Sébastien Faure  <sebastien.faure3@gmail.com>
 * @version 2011-07-18
 */
public class AskFrame extends GenericFrame {

    private static interiorPanel thePanel = null;

    Timer hideCD = null;

    public static String labelButtonValider = Lang.getLang().getValueFromRef("QuestionFrame.labelButtonValider");

    public static String helpCtrlMaj = Lang.getLang().getValueFromRef("QuestionFrame.helpCtrlMaj");

    public static String appTitle = Lang.getLang().getValueFromRef("QuestionFrame.appTitle");

    public static String labelButtonSkip = Lang.getLang().getValueFromRef("QuestionFrame.labelButtonSkip");

    public static String toolTipButtonSkip = Lang.getLang().getValueFromRef("QuestionFrame.toolTipButtonSkip");

    public static String toolTipLogo = Lang.getLang().getValueFromRef("QuestionFrame.toolTipLogo");

    /** Full screenshot file name **/
    private String absoluteScreenshotFilePath = "";

    private Question currentQuestion = null;

    private static int EVENT_CPY_PROBA = 20;

    private Regle currentRegle = null;

    /**
     * To set vertical scrollbar at the top if visible
     */
    private void fixScrollBar() {
        try {
            Thread.currentThread().sleep(300);
        } catch (InterruptedException ex) {
            Logger.getLogger(AskFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        JViewport jv = thePanel.jspMid.getViewport();
        jv.setViewPosition(new Point(0, 1));
        thePanel.jpMiddle.revalidate();
        this.setVisible(true);
        jv.setViewPosition(new Point(0, 0));
        if (thePanel.jpMiddle.getComponents().length > 0) {
            ((JPanel) thePanel.jpMiddle.getComponent(0)).getComponent(1).requestFocus();
        }
    }

    /**
     * Intern panel of the frame
     */
    private static class interiorPanel extends JPanel {

        AskFrame listeners = null;

        public JTextField jta1 = null;

        public JTextArea jta2 = null;

        public static JButton b1 = null;

        public static JLabel lbCtrlMaj = null;

        public ButtonGroup bg2 = null;

        public JPanel jpMiddle = null;

        public JScrollPane jspMid = null;

        public AnswerTextField jtaOther = null;

        public static JButton buttonSkip = null;

        public JLabel logo = null;

        public JPanel jpUp = null;

        public interiorPanel(AskFrame param) {
            super();
            this.listeners = param;
            this.setLayout(new BorderLayout(0, 10));
            this.setBackground(new Color(178, 34, 34));
            logo = new JLabel(new ImageIcon("media/describe-title.jpg"));
            logo.setToolTipText(toolTipLogo);
            jpUp = new JPanel(new GridLayout());
            jpUp.add(logo);
            jpUp.setBackground(Color.white);
            MouseMotionListener mml = new MouseMotionListener() {

                public void mouseDragged(MouseEvent e) {
                    AskFrame.getTheFrame().setLocation(AskFrame.getTheFrame().getLocation().x + e.getX() - (jpUp.getWidth() / 2), AskFrame.getTheFrame().getLocation().y + e.getY() - (jpUp.getHeight() / 2));
                }

                public void mouseMoved(MouseEvent e) {
                }
            };
            MouseListener ml = new MouseListener() {

                public void mouseClicked(MouseEvent e) {
                }

                public void mousePressed(MouseEvent e) {
                }

                public void mouseReleased(MouseEvent e) {
                }

                public void mouseEntered(MouseEvent e) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                    ;
                }

                public void mouseExited(MouseEvent e) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            };
            jpUp.addMouseMotionListener(mml);
            jpUp.addMouseListener(ml);
            logo.addMouseMotionListener(mml);
            logo.addMouseListener(ml);
            jpUp.setToolTipText(toolTipLogo);
            this.add(jpUp, BorderLayout.NORTH);
            this.add(this.ButtonsRow(), BorderLayout.SOUTH);
        }

        private JPanel oneRow() {
            JPanel res = new JPanel();
            res.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            return res;
        }

        /** Initialize question's textfield
         * @deprecated
         * @return
         */
        private JPanel firstTextField() {
            JPanel res = this.oneRow();
            jta1 = new JTextField();
            jta1.setEditable(false);
            jta1.setBackground(Color.lightGray);
            jta1.setFont(new Font("Verdana", Font.BOLD, 14));
            jta1.setForeground(Color.BLUE);
            jta1.setHorizontalAlignment(JTextField.CENTER);
            JScrollPane jsp = new JScrollPane(jta1);
            jsp.setPreferredSize(new Dimension(500, 150));
            res.add(jsp);
            res.setBackground(new Color(178, 34, 34));
            return res;
        }

        /**
         * Initialize answer text area
         * @deprecated
         * @return
         */
        private JPanel secondTextField() {
            JPanel res = this.oneRow();
            jta2 = new JTextArea();
            jta2.setLineWrap(true);
            jta2.addKeyListener(listeners);
            JScrollPane jsp = new JScrollPane(jta2);
            jsp.setPreferredSize(new Dimension(500, 150));
            res.add(jsp);
            res.setBackground(new Color(178, 34, 34));
            return res;
        }

        /**
         * Initialize choices button group
         * @deprecated 
         * @param choices
         * @param style
         * @return
         */
        private JPanel choicesGroup(ArrayList<QCMChoice> choices, String style) {
            Boolean other = false;
            JPanel res = new JPanel(new GridLayout(0, 1));
            bg2 = new ButtonGroup();
            for (Iterator<QCMChoice> it = choices.iterator(); it.hasNext(); ) {
                QCMChoice qcmC = it.next();
                if (style.equals("radio")) {
                    bg2.add(new JRadioButton(qcmC.getText()));
                    Enumeration<AbstractButton> en = thePanel.bg2.getElements();
                    while (en.hasMoreElements()) {
                        AbstractButton ab = en.nextElement();
                        ab.addKeyListener(listeners);
                        res.add(ab);
                    }
                } else if (style.equals("checkbox")) {
                    JCheckBox jcb = new JCheckBox(qcmC.getText());
                    jcb.addKeyListener(listeners);
                    res.add(jcb);
                }
                if (qcmC.getIsOtherChoice()) {
                    other = true;
                }
            }
            if (other) {
                jtaOther = new AnswerTextField();
                jtaOther.addKeyListener(listeners);
                res.add(jtaOther);
            }
            res.setPreferredSize(new Dimension(500, 150));
            res.setBackground(new Color(178, 34, 34));
            jpMiddle = res;
            return res;
        }

        /**
         * Initialize the row of buttons (submit,...) of the frame
         * @return
         */
        private JPanel ButtonsRow() {
            JPanel res = this.oneRow();
            res.setLayout(new GridLayout(2, 1));
            JPanel jpBt = new JPanel(new FlowLayout());
            lbCtrlMaj = new JLabel(helpCtrlMaj);
            lbCtrlMaj.setFont(new Font("Verdana", Font.PLAIN, 10));
            lbCtrlMaj.setForeground(Color.white);
            b1 = new JButton(labelButtonValider);
            b1.addActionListener(listeners);
            buttonSkip = new JButton(labelButtonSkip);
            buttonSkip.addActionListener(listeners);
            buttonSkip.setToolTipText(toolTipButtonSkip);
            res.add(lbCtrlMaj);
            jpBt.add(b1);
            jpBt.add(new JLabel("                       "));
            jpBt.add(buttonSkip);
            res.add(jpBt);
            jpBt.setBackground(new Color(178, 34, 34));
            res.setBackground(new Color(178, 34, 34));
            return res;
        }

        /**
         * Initialize the intern panel of the frame
         */
        public void initThePanel() {
        }

        /**
         * Add a new question to the frame
         * @param q
         */
        public void addQuestion(Question q) {
            JPanel panQuestion = new JPanel(new BorderLayout());
            panQuestion.setBackground(Color.lightGray);
            QuestionTextArea jlQuest = new QuestionTextArea("\n" + q.intitule);
            jlQuest.setBackground(new Color(197, 211, 209));
            jlQuest.setLineWrap(true);
            jlQuest.setWrapStyleWord(true);
            jlQuest.setEditable(false);
            jlQuest.setSize(476, 50);
            panQuestion.add(jlQuest, BorderLayout.NORTH);
            jlQuest.setFont(new Font("Verdana", Font.BOLD, 13));
            jlQuest.setForeground(Color.BLUE);
            if (q instanceof QReponseLibre) {
                AnswerTextArea ata = new AnswerTextArea();
                ata.addKeyListener(listeners);
                ata.setLineWrap(true);
                ata.setWrapStyleWord(true);
                ata.setSize(476, 100);
                panQuestion.add(ata, BorderLayout.CENTER);
            } else if ((q instanceof QCMChkBox) || (q instanceof QCMRadio)) {
                ArrayList<QCMChoice> choices = new ArrayList<QCMChoice>();
                if (q instanceof QCMChkBox) choices = ((QCMChkBox) q).getChoices();
                if (q instanceof QCMRadio) choices = ((QCMRadio) q).getChoices();
                Boolean other = false;
                FlowLayout fL = new FlowLayout();
                fL.setAlignment(FlowLayout.LEFT);
                fL.setAlignOnBaseline(true);
                JPanel res = new JPanel(fL);
                bg2 = new ButtonGroup();
                int widthOfChoices = 0;
                for (Iterator<QCMChoice> it = choices.iterator(); it.hasNext(); ) {
                    QCMChoice qcmC = it.next();
                    if (q instanceof QCMRadio) {
                        JRadioButton jRB = new JRadioButton(qcmC.getText());
                        widthOfChoices += jRB.getText().length() * 9.1;
                        bg2.add(jRB);
                        Enumeration<AbstractButton> en = thePanel.bg2.getElements();
                        while (en.hasMoreElements()) {
                            AbstractButton ab = en.nextElement();
                            ab.addKeyListener(listeners);
                            ab.setBackground(Color.lightGray);
                            res.add(ab);
                        }
                    } else if (q instanceof QCMChkBox) {
                        JCheckBox jcb = new JCheckBox(qcmC.getText());
                        jcb.addKeyListener(listeners);
                        jcb.setBackground(Color.lightGray);
                        res.add(jcb);
                        widthOfChoices += jcb.getText().length() * 9;
                    }
                    if (qcmC.getIsOtherChoice()) {
                        other = true;
                    }
                }
                if (other) {
                    jtaOther = new AnswerTextField();
                    jtaOther.setPreferredSize(new Dimension(160, 25));
                    jtaOther.addKeyListener(listeners);
                    res.add(jtaOther);
                    widthOfChoices += 160;
                }
                res.setPreferredSize(new Dimension(476, 35));
                if (widthOfChoices > 0) {
                    if ((int) ((widthOfChoices / 476) * 35) > 0) {
                        res.setPreferredSize(new Dimension(476, ((int) (widthOfChoices / 476) + 1) * 35));
                    }
                }
                res.setBackground(Color.lightGray);
                panQuestion.add(res, BorderLayout.CENTER);
            }
            panQuestion.setBorder(BorderFactory.createLineBorder(Color.darkGray, 2));
            jpMiddle.add(panQuestion);
        }
    }

    /**
     * Constructor
     */
    private AskFrame() {
        super("DEScribe");
        thePanel = new interiorPanel(this);
        getContentPane().add(thePanel);
        thePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pack();
    }

    /**
     * Unique instance of the frame
     */
    private static AskFrame instance;

    /**
     * Create or get (if already exists) the unique frame instance
     * @return
     */
    public static AskFrame getTheFrame() {
        if (instance == null) {
            instance = new AskFrame();
        }
        return instance;
    }

    /**
     * Fill question textfield
     * @deprecated
     * @param param
     */
    public static void setText1(String param) {
        thePanel.jta1.setText(param);
    }

    /**
     * Get contents of question textfield
     * @deprecated
     * @param param
     */
    public static String getText1() {
        return thePanel.jta1.getText();
    }

    /**
     * Fill answer text area
     * @deprecated
     * @param param
     */
    public static void setText2(String param) {
        thePanel.jta2.setText(param);
    }

    /**
     * Get contents of answer text area
     * @deprecated
     * @return
     */
    public static String getText2() {
        return thePanel.jta2.getText();
    }

    /**
     * Show the frame with intialized questions
     * @param quest
     */
    @Override
    public void showTheFrame(String quest) {
        if (quest == null) {
            currentRegle = null;
        }
        absoluteScreenshotFilePath = "";
        try {
            if (thePanel.getComponent(2) != null) {
                thePanel.remove(thePanel.getComponent(2));
            }
        } catch (Exception ex) {
        }
        if (quest == null) currentQuestion = selectQuestionFromForm(); else if (!quest.equals("rule")) {
            currentQuestion = selectQuestionFromForm();
        }
        this.refresh();
        if (currentQuestion != null) {
            hideCD = new Timer();
            TimerTask taskCD = new TimerTask() {

                @Override
                public void run() {
                    AskFrame.getTheFrame().hideTheFrame();
                }
            };
            hideCD.schedule(taskCD, 100000);
            ArrayList<Question> lesQuestions = new ArrayList<Question>();
            try {
                lesQuestions = Utils.importFormXML();
            } catch (BadXMLFileException ex) {
                Logger.getLogger(AskFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.setLocation((x - this.getSize().width) / 2, (y - this.getSize().height) / 2);
            if (OptionFrame.isSoundEnabled()) {
                Toolkit.getDefaultToolkit().beep();
            }
            this.pack();
            Thread t = new Thread(new Runnable() {

                public void run() {
                    fixScrollBar();
                }
            });
            t.start();
        }
    }

    /**
     * Refresh contents
     */
    private void refresh() {
        try {
            int nbQuests = 0;
            if (currentRegle != null) {
                nbQuests = getAskableQuestionsFromFormWithRule(currentRegle).size();
                thePanel.jpMiddle = new JPanel(new GridLayout(nbQuests, 1));
            } else {
                nbQuests = getAskableQuestionsFromForm().size();
                thePanel.jpMiddle = new JPanel(new GridLayout(nbQuests, 1));
            }
            thePanel.jspMid = new JScrollPane();
            if (thePanel.jtaOther != null) {
                thePanel.remove(thePanel.jtaOther);
            }
            if (nbQuests == 1) {
                thePanel.jspMid.setPreferredSize(new Dimension(520, (int) (Toolkit.getDefaultToolkit().getScreenSize().height / 4)));
            } else {
                thePanel.jspMid.setPreferredSize(new Dimension(520, (int) (Toolkit.getDefaultToolkit().getScreenSize().height / 2)));
            }
            thePanel.jspMid.setViewportView(thePanel.jpMiddle);
            pack();
            if (currentRegle != null) {
                thePanel.jpMiddle = new JPanel(new GridLayout2(getAskableQuestionsFromFormWithRule(currentRegle).size(), 1));
            } else {
                thePanel.jpMiddle = new JPanel(new GridLayout2(getAskableQuestionsFromForm().size(), 1));
            }
            thePanel.jspMid = new JScrollPane();
            if (thePanel.jtaOther != null) {
                thePanel.remove(thePanel.jtaOther);
            }
            loadForm();
            if (nbQuests == 1) {
                thePanel.jspMid.setPreferredSize(new Dimension(520, (int) (Toolkit.getDefaultToolkit().getScreenSize().height / 4)));
            } else {
                thePanel.jspMid.setPreferredSize(new Dimension(520, (int) (Toolkit.getDefaultToolkit().getScreenSize().height / 2)));
            }
            thePanel.jspMid.setViewportView(thePanel.jpMiddle);
            thePanel.add(thePanel.jspMid, BorderLayout.CENTER);
            pack();
            labelButtonValider = Lang.getLang().getValueFromRef("QuestionFrame.labelButtonValider");
            interiorPanel.b1.setText(labelButtonValider);
            labelButtonSkip = Lang.getLang().getValueFromRef("QuestionFrame.labelButtonSkip");
            interiorPanel.buttonSkip.setText(labelButtonSkip);
            toolTipButtonSkip = Lang.getLang().getValueFromRef("QuestionFrame.toolTipButtonSkip");
            interiorPanel.buttonSkip.setToolTipText(toolTipButtonSkip);
            helpCtrlMaj = Lang.getLang().getValueFromRef("QuestionFrame.helpCtrlMaj");
            interiorPanel.lbCtrlMaj.setText(helpCtrlMaj);
            appTitle = Lang.getLang().getValueFromRef("QuestionFrame.appTitle");
            toolTipLogo = Lang.getLang().getValueFromRef("QuestionFrame.toolTipLogo");
            thePanel.logo.setToolTipText(toolTipLogo);
            thePanel.jpUp.setToolTipText(toolTipLogo);
            this.setTitle(appTitle);
            ArrayList<Action> lesActions = new ArrayList<Action>();
            lesActions = Utils.importFormActionsXML();
            try {
                DBConnexion conn = DBConnexion.getConnexion();
                SessionManager sm = SessionManager.getSessionManager();
                for (Iterator<Action> it = lesActions.iterator(); it.hasNext(); ) {
                    Action s = it.next();
                    if (s instanceof ActionScreenshot) {
                        Long t = conn.getMaxIdReponseBySession(sm.getSessionCourante()) + 1;
                        ((ActionScreenshot) s).takeCapture("session" + sm.getSessionCourante().getId() + "_reponse" + t + "_screenshot");
                        absoluteScreenshotFilePath = ((ActionScreenshot) s).getAbsFileName();
                    }
                }
            } catch (Exception ex) {
                javax.swing.JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        } catch (BadXMLFileException ex) {
            javax.swing.JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    /**
     * Clear the frame
     */
    private void clearFrame() {
        thePanel.jpMiddle.removeAll();
        thePanel.jspMid.removeAll();
        thePanel.jspMid.revalidate();
        thePanel.jpMiddle.revalidate();
        setVisible(true);
        hideCD.cancel();
    }

    /**
     * Get the first askable question of form
     * @return Question
     */
    public Question selectQuestionFromForm() {
        ArrayList<Question> lesQuestions = new ArrayList<Question>();
        ArrayList<Question> askableQuestions = new ArrayList<Question>();
        Question selectedQuestion = null;
        try {
            lesQuestions = Utils.importFormXML();
        } catch (BadXMLFileException ex) {
            Logger.getLogger(AskFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        Boolean foundQuestion = false;
        int j = 0;
        while ((j < lesQuestions.size())) {
            ArrayList<Regle> lesRegles = lesQuestions.get(j).getRegles();
            Boolean foundRuleFalse = false;
            int k = 0;
            while ((k < lesRegles.size()) && (!foundRuleFalse)) {
                Regle r = lesRegles.get(k);
                if (r.getEvent().equals("fullscreen")) {
                    if (r.getType().equals("if")) {
                    } else if (r.getType().equals("notif")) {
                    }
                } else if (r.getEvent().equals("copyImage")) {
                    foundRuleFalse = true;
                } else if (r.getEvent().equals("copyText")) {
                    foundRuleFalse = true;
                } else if (r.getEvent().equals("copyFile")) {
                    foundRuleFalse = true;
                } else if (r.getEvent().equals("copyFile")) {
                    foundRuleFalse = true;
                } else if (r.getEvent().equals("copy")) {
                    foundRuleFalse = true;
                }
                k++;
            }
            if (!foundRuleFalse) {
                foundQuestion = true;
                askableQuestions.add(lesQuestions.get(j));
            }
            j++;
        }
        if (askableQuestions.size() > 0) {
            int rang = (new Random().nextInt(askableQuestions.size()));
            selectedQuestion = askableQuestions.get(rang);
        } else {
            selectedQuestion = null;
        }
        return selectedQuestion;
    }

    /**
     * Get first askable question of form with rule reg
     * @param reg
     * @return Question
     */
    public Question selectQuestionFromFormWithRule(Regle reg) {
        ArrayList<Question> lesQuestions = new ArrayList<Question>();
        ArrayList<Question> askableQuestions = new ArrayList<Question>();
        Question selectedQuestion = null;
        try {
            lesQuestions = Utils.importFormXML();
        } catch (BadXMLFileException ex) {
            Logger.getLogger(AskFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        Boolean foundQuestion = false;
        int j = 0;
        while (j < lesQuestions.size()) {
            ArrayList<Regle> lesRegles = lesQuestions.get(j).getRegles();
            Boolean foundRuleFalse = false;
            Boolean foundThisRule = false;
            int k = 0;
            while ((k < lesRegles.size()) && ((foundRuleFalse) || (!foundThisRule))) {
                Regle r = lesRegles.get(k);
                if ((r.getEvent().equals(reg.getEvent())) && (r.getType().equals(reg.getType()))) {
                    foundThisRule = true;
                }
                if (r.getEvent().equals("fullscreen")) {
                    if (r.getType().equals("if")) {
                    } else if (r.getType().equals("notif")) {
                    }
                } else if (r.getEvent().equals("copyImage")) {
                    if (r.getType().equals("if")) {
                    } else if (r.getType().equals("notif")) {
                    }
                } else if (r.getEvent().equals("copyText")) {
                    if (r.getType().equals("if")) {
                    } else if (r.getType().equals("notif")) {
                    }
                } else if (r.getEvent().equals("copyFile")) {
                    if (r.getType().equals("if")) {
                    } else if (r.getType().equals("notif")) {
                    }
                } else if (r.getEvent().equals("copy")) {
                    if (r.getType().equals("if")) {
                    } else if (r.getType().equals("notif")) {
                    }
                }
                k++;
            }
            if ((!foundRuleFalse) && (foundThisRule)) {
                foundQuestion = true;
                askableQuestions.add(lesQuestions.get(j));
            }
            j++;
        }
        if (askableQuestions.size() > 0) {
            int rang = (new Random().nextInt(askableQuestions.size()));
            selectedQuestion = askableQuestions.get(rang);
        } else {
            selectedQuestion = null;
        }
        return selectedQuestion;
    }

    /**
     * Ask question with rule event
     * @param event
     */
    public void askQuestionWithRule(String event) {
        Question q;
        if (TimerQuestion.getTimerQuestion().canIAskQuestionNow()) {
            q = selectQuestionFromFormWithRule(new Regle(event, "if"));
            if (q != null) {
                currentQuestion = q;
                Random rand = new Random();
                int nb = rand.nextInt(100);
                if (nb < EVENT_CPY_PROBA) {
                    currentRegle = new Regle(event, "if");
                    showTheFrame("rule");
                    TimerQuestion.getTimerQuestion().resetTimerAfterQuestion();
                }
            }
        }
    }

    /**
     * Get all askable question from form
     * @return
     */
    public ArrayList<Question> getAskableQuestionsFromForm() {
        ArrayList<Question> lesQuestions = new ArrayList<Question>();
        ArrayList<Question> askableQuestions = new ArrayList<Question>();
        Question selectedQuestion = null;
        try {
            lesQuestions = Utils.importFormXML();
        } catch (BadXMLFileException ex) {
            Logger.getLogger(AskFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        Boolean foundQuestion = false;
        int j = 0;
        while ((j < lesQuestions.size())) {
            ArrayList<Regle> lesRegles = lesQuestions.get(j).getRegles();
            Boolean foundRuleFalse = false;
            int k = 0;
            while ((k < lesRegles.size()) && (!foundRuleFalse)) {
                Regle r = lesRegles.get(k);
                if (r.getEvent().equals("fullscreen")) {
                    if (r.getType().equals("if")) {
                    } else if (r.getType().equals("notif")) {
                    }
                } else if (r.getEvent().equals("copyImage")) {
                    foundRuleFalse = true;
                } else if (r.getEvent().equals("copyText")) {
                    foundRuleFalse = true;
                } else if (r.getEvent().equals("copyFile")) {
                    foundRuleFalse = true;
                } else if (r.getEvent().equals("copyFile")) {
                    foundRuleFalse = true;
                } else if (r.getEvent().equals("copy")) {
                    foundRuleFalse = true;
                }
                k++;
            }
            if (!foundRuleFalse) {
                foundQuestion = true;
                askableQuestions.add(lesQuestions.get(j));
            }
            j++;
        }
        if (askableQuestions.size() > 0) {
            int rang = (new Random().nextInt(askableQuestions.size()));
            selectedQuestion = askableQuestions.get(rang);
        } else {
            selectedQuestion = null;
        }
        return askableQuestions;
    }

    /**
     * Get all askable questions from form with rule reg
     * @param reg
     * @return
     */
    public ArrayList<Question> getAskableQuestionsFromFormWithRule(Regle reg) {
        ArrayList<Question> lesQuestions = new ArrayList<Question>();
        ArrayList<Question> askableQuestions = new ArrayList<Question>();
        Question selectedQuestion = null;
        try {
            lesQuestions = Utils.importFormXML();
        } catch (BadXMLFileException ex) {
            Logger.getLogger(AskFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        Boolean foundQuestion = false;
        int j = 0;
        while (j < lesQuestions.size()) {
            ArrayList<Regle> lesRegles = lesQuestions.get(j).getRegles();
            Boolean foundRuleFalse = false;
            Boolean foundThisRule = false;
            int k = 0;
            while ((k < lesRegles.size()) && ((foundRuleFalse) || (!foundThisRule))) {
                Regle r = lesRegles.get(k);
                if ((r.getEvent().equals(reg.getEvent())) && (r.getType().equals(reg.getType()))) {
                    foundThisRule = true;
                }
                if (r.getEvent().equals("fullscreen")) {
                    if (r.getType().equals("if")) {
                    } else if (r.getType().equals("notif")) {
                    }
                } else if (r.getEvent().equals("copyImage")) {
                    if (r.getType().equals("if")) {
                    } else if (r.getType().equals("notif")) {
                    }
                } else if (r.getEvent().equals("copyText")) {
                    if (r.getType().equals("if")) {
                    } else if (r.getType().equals("notif")) {
                    }
                } else if (r.getEvent().equals("copyFile")) {
                    if (r.getType().equals("if")) {
                    } else if (r.getType().equals("notif")) {
                    }
                } else if (r.getEvent().equals("copy")) {
                    if (r.getType().equals("if")) {
                    } else if (r.getType().equals("notif")) {
                    }
                }
                k++;
            }
            if ((!foundRuleFalse) && (foundThisRule)) {
                foundQuestion = true;
                askableQuestions.add(lesQuestions.get(j));
            }
            j++;
        }
        if (askableQuestions.size() > 0) {
            int rang = (new Random().nextInt(askableQuestions.size()));
            selectedQuestion = askableQuestions.get(rang);
        } else {
            selectedQuestion = null;
        }
        return askableQuestions;
    }

    /**
     * Fill the frame form with questions
     */
    public void loadForm() {
        ArrayList<Question> questions = new ArrayList<Question>();
        if (currentRegle != null) {
            questions = getAskableQuestionsFromFormWithRule(currentRegle);
        } else {
            questions = getAskableQuestionsFromForm();
        }
        currentQuestion = null;
        for (int i = 0; i < questions.size(); i++) {
            currentQuestion = questions.get(i);
            thePanel.addQuestion(questions.get(i));
        }
    }

    /**
         * Buttons actions
         * @param e
         */
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        if (s.equals(labelButtonValider)) {
            try {
                DBConnexion conn = DBConnexion.getConnexion();
                SessionManager sm = SessionManager.getSessionManager();
                Date maDate = new Date();
                Reponse rep = null;
                String questions = "";
                String answers = "";
                int idQuestion = 1;
                Component panQuests[] = thePanel.jpMiddle.getComponents();
                for (int i = 0; i < panQuests.length; i++) {
                    Component tsub[] = ((JPanel) panQuests[i]).getComponents();
                    for (int j = 0; j < tsub.length; j++) {
                        if (tsub[j] instanceof QuestionTextArea) {
                            QuestionTextArea ql = (QuestionTextArea) tsub[j];
                            questions += idQuestion + ". " + ql.getText().replace("\n", "") + " \n";
                            if (idQuestion == 1) {
                                answers += " " + idQuestion + ". " + ql.getText().replace("\n", "") + " : ";
                            } else {
                                answers += idQuestion + ". " + ql.getText().replace("\n", "") + " : ";
                            }
                            idQuestion++;
                        } else {
                            if (tsub[j] instanceof JPanel) {
                                Component tsub2[] = ((JPanel) tsub[j]).getComponents();
                                for (int k = 0; k < tsub2.length; k++) {
                                    if (tsub2[k] instanceof AnswerTextField) {
                                        AnswerTextField atf = (AnswerTextField) tsub2[k];
                                        if (!atf.equals("")) {
                                            if (k == tsub2.length - 1) {
                                                answers += atf.getText() + " \n ";
                                            } else {
                                                answers += atf.getText() + " --- ";
                                            }
                                        }
                                    } else if (tsub2[k] instanceof AbstractButton) {
                                        AbstractButton ab = (AbstractButton) tsub2[k];
                                        if (ab.isSelected()) {
                                            if (k == tsub2.length - 1) {
                                                answers += ab.getText() + " \n ";
                                            } else {
                                                answers += ab.getText() + " --- ";
                                            }
                                        }
                                    } else if (tsub2[k] instanceof AnswerTextArea) {
                                        AnswerTextArea ata = (AnswerTextArea) tsub2[k];
                                        if (!ata.equals("")) {
                                            if (k == tsub2.length - 1) {
                                                answers += ata.getText() + " \n ";
                                            } else {
                                                answers += ata.getText() + " --- ";
                                            }
                                        }
                                    }
                                }
                            } else {
                                if (tsub[j] instanceof AnswerTextArea) {
                                    AnswerTextArea ata = (AnswerTextArea) tsub[j];
                                    if (!ata.equals("")) {
                                        if (j == tsub.length - 1) {
                                            answers += ata.getText() + " \n ";
                                        } else {
                                            answers += ata.getText() + " --- ";
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                rep = new Reponse(conn.getMaxIdReponseBySession(sm.getSessionCourante()) + 1, questions, answers, maDate, sm.getSessionCourante(), absoluteScreenshotFilePath);
                rep.setReglesQuestion(currentQuestion.getRegles());
                ArrayList<Regle> lesR = rep.getReglesQuestion();
                String strRegles = "";
                for (int i = 0; i < lesR.size(); i++) {
                    if (i == 0) {
                        strRegles += lesR.get(i).getType() + ":" + lesR.get(i).getEvent();
                    } else if (i <= (lesR.size() - 1)) {
                        strRegles += ", " + lesR.get(i).getType() + ":" + lesR.get(i).getEvent();
                    }
                }
                conn.newAddEntry(rep);
                if (!absoluteScreenshotFilePath.equals("")) {
                    ImgTxtMerger imTxtM = new ImgTxtMerger();
                    imTxtM.fusion(rep);
                }
                clearFrame();
                pack();
                this.validate();
                this.repaint(0);
                this.hideTheFrame();
                JViewport jv = thePanel.jspMid.getViewport();
                jv.setViewPosition(new Point(0, 0));
                Runtime.getRuntime().gc();
                OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
                Long usedMemory = operatingSystemMXBean.getTotalPhysicalMemorySize() - operatingSystemMXBean.getFreePhysicalMemorySize();
                double cons = ((double) usedMemory / (double) operatingSystemMXBean.getTotalPhysicalMemorySize()) * 100.0;
                if (cons > 80) {
                    api.utils.appManagement.restartApplication(this, false);
                }
            } catch (Exception ex) {
                javax.swing.JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        } else if (s.equals(labelButtonSkip)) {
            if (absoluteScreenshotFilePath != null) {
                if (!absoluteScreenshotFilePath.equals("")) {
                    File f = new File(absoluteScreenshotFilePath);
                    if (f.exists()) {
                        f.delete();
                    }
                }
            }
            clearFrame();
            pack();
            this.validate();
            this.repaint(0);
            this.hideTheFrame();
            JViewport jv = thePanel.jspMid.getViewport();
            jv.setViewPosition(new Point(0, 0));
        }
    }

    public void windowOpened(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    /**
         * Stop timer when key typed
         * @param e
         */
    public void keyTyped(KeyEvent e) {
        hideCD.cancel();
    }

    public void keyPressed(KeyEvent e) {
    }

    /**
         * Validating form by typing ctrl+enter
         * @param e
         */
    public void keyReleased(KeyEvent e) {
        if (e.getModifiers() == KeyEvent.CTRL_MASK && e.getKeyCode() == KeyEvent.VK_ENTER) {
            thePanel.b1.doClick();
        }
    }
}
