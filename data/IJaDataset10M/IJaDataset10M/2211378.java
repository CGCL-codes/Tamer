package gui.swing;

import gui.factory.components.AbstractDialogWithTime;
import java.text.MessageFormat;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * This class shows a question, and waits X seconds for the user to answer.
 * The difference between this and a common JDialog or JOptionPane is the TIME feature,
 * that let you to choose the maximum time for a answer, the option that will be assumed
 * as the answer if no option was chosen by the time configured, the option assumed if
 * the user closes the dialog without a choice and the default focused option
 *
 * @author Glauber Magalh�es Pires
 *
 */
public class JDialogWithTime extends Thread implements AbstractDialogWithTime {

    private JLabel jLabelMessage = new JLabel();

    private String defaultOptionWithoutChoice;

    private String defaultOptionWhenClosed;

    private int time;

    private String question;

    private JOptionPane jop;

    private boolean answered = false;

    /**
     * Creates a JOptionPane dialog with time, to see more information see class comment
     * (will use JOptionPane.QUESTION_MESSAGE as icon)
     * @param question the question to display
     * @param time the time to wait for a answer (in seconds)
     * @param textOptions an array of Strings that gives the possible selections
     * @param defaultOptionWithoutChoice the option that will be assumed as the
     * answer if no option was chosen by the time configured
     * @param defaultOptionWhenClosed the option assumed if the user closes
     * the dialog without a choice
     * @param optionFocused the default focused option
     */
    public JDialogWithTime(String question, int time, String[] textOptions, String defaultOptionWithoutChoice, String defaultOptionWhenClosed, String optionFocused) {
        this(question, JOptionPane.QUESTION_MESSAGE, time, textOptions, defaultOptionWithoutChoice, defaultOptionWhenClosed, optionFocused);
    }

    /**
     * Creates a JOptionPane dialog with time, see more information see class comment
     *
     * @param question the question display
     * @param type
     * @param time the time to wait for a answer (in seconds)
     * @param textOptions an array of Strings that gives the possible selections
     * @param defaultOptionWithoutChoice the option that will be assumed as the
     * answer if no option was chosen by the time configured
     * @param defaultOptionWhenClosed the option assumed if the user closes
     * the dialog without a choice
     * @param optionFocused the default focused option
     */
    public JDialogWithTime(String question, int type, int time, String[] textOptions, String defaultOptionWithoutChoice, String defaultOptionWhenClosed, String optionFocused) {
        this.defaultOptionWithoutChoice = defaultOptionWithoutChoice;
        this.defaultOptionWhenClosed = defaultOptionWhenClosed;
        this.time = time;
        this.question = question;
        this.jLabelMessage.setText(MessageFormat.format(question, new Object[] { String.valueOf(time) }));
        this.jop = new JOptionPane(this.jLabelMessage, type, JOptionPane.DEFAULT_OPTION, null, textOptions);
        this.jop.setInitialValue(optionFocused);
    }

    /**
     * Gets the answer for the question
     * @return The selected answer, if the Dialog was close returns "defaultOptionWhenClosed" else wait the "time" defined on the
     * constructor, and returns "defaultOptionWithoutChoice"
     */
    public String getAnswer() {
        JDialog jopDialog = jop.createDialog(MainGUI.mainGUI, "OpenP2M");
        jop.setFont(Constantes.unicodeFontBold);
        jopDialog.setVisible(true);
        Object userSelection = jop.getValue();
        answered = true;
        if (userSelection == null) return defaultOptionWhenClosed;
        return (String) userSelection;
    }

    public void run() {
        while (time > 0 && !answered) {
            try {
                this.jLabelMessage.setText(MessageFormat.format(question, new Object[] { String.valueOf(time) }));
                this.jop.setMessage(this.jLabelMessage);
                sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            time--;
        }
        if (!answered) this.jop.setValue(defaultOptionWithoutChoice);
    }
}
