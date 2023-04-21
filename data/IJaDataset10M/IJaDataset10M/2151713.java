package gui.swing;

import gui.Systray;
import gui.factory.components.AbstractDialogWithTime;
import gui.factory.components.AbstractMessageDisplayer;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import resources.Messages;

public class MessageDisplayer implements AbstractMessageDisplayer {

    private static MessageDisplayer messageDisplayer = new MessageDisplayer();

    private MessageDisplayer() {
    }

    public static final synchronized MessageDisplayer getInstance() {
        if (messageDisplayer == null) messageDisplayer = new MessageDisplayer();
        return messageDisplayer;
    }

    private static final JLabel msg = new JLabel();

    static {
        msg.setFont(Constantes.unicodeFontBold);
    }

    private final void displayMessage(String message, int type) {
        msg.setText(message);
        JOptionPane.showMessageDialog(MainGUI.mainGUI, msg, "OpenP2M", type);
    }

    private final void displayMessageWithTime(String message, int type) {
        String ok = Messages.message.getString("geral.ok");
        JDialogWithTime jd = new JDialogWithTime(message, type, 10, new String[] { ok }, ok, ok, ok);
        jd.getAnswer();
    }

    public final void displayErrorMessage(String message) {
        if (Systray.getInstance().isTrayIconON()) Systray.getInstance().displaySystrayMessage(message, Systray.ERROR_MESSAGE); else displayMessage(message, JOptionPane.ERROR_MESSAGE);
    }

    public final void displayErrorMessageWithTime(String message) {
        if (Systray.getInstance().isTrayIconON()) Systray.getInstance().displaySystrayMessage(message, Systray.ERROR_MESSAGE); else displayMessageWithTime(message, JOptionPane.ERROR_MESSAGE);
    }

    public final void displayInfoMessageWithTime(String message) {
        if (Systray.getInstance().isTrayIconON()) Systray.getInstance().displaySystrayMessage(message, Systray.INFORMATION_MESSAGE); else displayMessageWithTime(message, JOptionPane.INFORMATION_MESSAGE);
    }

    public final void displayInfoMessage(String message) {
        if (Systray.getInstance().isTrayIconON()) Systray.getInstance().displaySystrayMessage(message, Systray.INFORMATION_MESSAGE); else displayMessage(message, JOptionPane.INFORMATION_MESSAGE);
    }

    public final void displayWarningMessage(String message) {
        if (Systray.getInstance().isTrayIconON()) Systray.getInstance().displaySystrayMessage(message, Systray.WARNING_MESSAGE); else displayMessage(message, JOptionPane.WARNING_MESSAGE);
    }

    public final void showException(Exception exception) {
        new MessagesGUI(MainGUI.mainGUI, exception).setVisible(true);
        if (Systray.getInstance().isTrayIconON()) Systray.getInstance().displayErrorMessage(exception.getMessage());
        MainGUI.mainGUI.setVisible(true);
    }

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
    public final AbstractDialogWithTime createDialogWithTime(String question, int time, String[] textOptions, String defaultOptionWithoutChoice, String defaultOptionWhenClosed, String optionFocused) {
        return new JDialogWithTime(question, time, textOptions, defaultOptionWithoutChoice, defaultOptionWhenClosed, optionFocused);
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
    public final AbstractDialogWithTime createDialogWithTime(String question, int type, int time, String[] textOptions, String defaultOptionWithoutChoice, String defaultOptionWhenClosed, String optionFocused) {
        return new JDialogWithTime(question, time, textOptions, defaultOptionWithoutChoice, defaultOptionWhenClosed, optionFocused);
    }
}
