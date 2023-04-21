package uk.ac.imperial.ma.metric.gui;

import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import java.awt.Font;

/**
 * This panel contains the mark for the current question e.g. right, wrong, 
 * press check answer.
 *
 * @author Daniel J. R. May
 * @version 0.2.0 23 March 2004
 */
public class MarkPanel extends JPanel {

    /** The label which contains the message to the user. */
    private JLabel jlblMark;

    private int textSize = 16;

    /**
     * Construtor.
     */
    public MarkPanel() {
        jlblMark = new JLabel("Press 'Mark It'", JLabel.CENTER);
        jlblMark.setFont(new Font("SansSerif", Font.BOLD, textSize - 2));
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Mark", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("SansSerif", Font.BOLD, textSize - 2)));
        setBackground(Color.WHITE);
        setLayout(new GridLayout(1, 1));
        add(jlblMark);
    }

    /**
     * Changes the text to read "Correct".
     */
    public void setCorrect() {
        jlblMark.setText("Correct");
    }

    /**
     * Changes the text to read "Incorrect".
     */
    public void setIncorrect() {
        jlblMark.setText("Incorrect");
    }

    /**
     * Changes the text to read "Press 'Mark It'".
     */
    public void setPressCheckAnswer() {
        jlblMark.setText("Press 'Mark It'");
    }

    public void clear() {
        jlblMark.setText("");
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        jlblMark.setFont(new Font("SansSerif", Font.BOLD, textSize - 2));
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Mark", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("SansSerif", Font.BOLD, textSize - 2)));
    }
}
