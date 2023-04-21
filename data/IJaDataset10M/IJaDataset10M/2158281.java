package de.marc1schroeder.ratingtest;

import java.awt.Color;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.PrintWriter;
import de.marc1schroeder.ratingtest.ResponsePanel.AnswerListener;

public class CandidatePanel extends javax.swing.JPanel {

    private javax.swing.JLabel label;

    private int intendedWidth;

    private int intendedHeight;

    private DragLogger dragLogger;

    protected String id;

    protected ResponsePanel.AnswerListener answerListener;

    /** Creates new form CandidatePanel */
    public CandidatePanel(String labelString, int intendedWidth, int intendedHeight) {
        this(labelString, intendedWidth, intendedHeight, labelString);
    }

    public CandidatePanel(String labelString, int intendedWidth, int intendedHeight, String id) {
        initComponents();
        setInactiveBackground();
        label.setText(labelString);
        this.intendedWidth = intendedWidth;
        this.intendedHeight = intendedHeight;
        this.id = id;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() {
        label = new javax.swing.JLabel();
        setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        setMaximumSize(new java.awt.Dimension(25, 25));
        setMinimumSize(new java.awt.Dimension(25, 25));
        setPreferredSize(new java.awt.Dimension(25, 25));
        addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (answerListener != null) answerListener.setAnswerGiven(true);
            }
        });
        add(label);
    }

    protected void setInactiveBackground() {
        setBackground(Color.lightGray);
    }

    protected void setActiveBackground() {
        setBackground(Color.yellow);
    }

    public int getIntendedWidth() {
        return intendedWidth;
    }

    public int getIntendedHeight() {
        return intendedHeight;
    }

    /**
     * Must be called befpre adding the candidate panel to a gui element.
     * Subclasses implement specific resource-allocating code here. They should also call this method.
     */
    public void activate(PrintWriter logWriter, long startTime) {
        javax.swing.event.MouseInputAdapter mia = new CandidateMouseHandler();
        addMouseListener(mia);
        addMouseMotionListener(mia);
        dragLogger = new DragLogger(logWriter, id, startTime);
        addMouseListener(dragLogger);
    }

    /**
     * Must be called after removing the candidate panel from a gui element.
     * Subclasses release
     * allocated resources here.  They should also call this method.
     */
    public void deactivate() {
        MouseListener[] mls = (MouseListener[]) getListeners(MouseListener.class);
        for (int i = 0; i < mls.length; i++) removeMouseListener(mls[i]);
        MouseMotionListener[] mmls = (MouseMotionListener[]) getListeners(MouseMotionListener.class);
        for (int i = 0; i < mmls.length; i++) removeMouseMotionListener(mmls[i]);
    }

    public void setLabelString(String labelString) {
        label.setText(labelString);
    }

    /**
     * Return the normalised location on the y axis.
     * In the normalised space, top is 100 and bottom is 0.
     */
    public int getNormalisedYPosition() {
        return 100 - (int) (getLocation().getY() / (getParent().getHeight() - getHeight()) * 100);
    }

    /**
     * Return the normalised location on the x axis.
     * In the normalised space, left is 0 and right is 100.
     */
    public int getNormalisedXPosition() {
        return (int) (getLocation().getX() / (getParent().getWidth() - getWidth()) * 100);
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getID() {
        return id;
    }

    public String toString() {
        return getID();
    }

    /**
     * Set a reference to an external answer listener. The button will be disabled
     * here and in reset(), and will be need to be enabled by subclasses 
     * when a sufficient response has been given.
     * @param doneButton
     */
    public void setAnswerListener(AnswerListener answerListener) {
        this.answerListener = answerListener;
        if (answerListener != null) answerListener.setAnswerGiven(false);
    }
}
