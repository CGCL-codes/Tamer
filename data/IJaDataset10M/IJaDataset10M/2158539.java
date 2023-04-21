package gui.display;

import javax.swing.JLabel;

/**
 *
 */
public class ChannelUser extends JLabel implements Comparable<ChannelUser> {

    private boolean isOperator = false;

    private boolean isVoice = false;

    private String nick_;

    public ChannelUser(String user) {
        super();
        if (user.startsWith("@")) {
            isOperator = true;
            user = user.substring(1);
        } else if (user.startsWith("+")) {
            isVoice = true;
            user = user.substring(1);
        }
        nick_ = user;
        updateText();
    }

    public final String getNick() {
        return nick_;
    }

    protected final void changeNick(final String nick) {
        nick_ = nick;
    }

    private void updateText() {
        String mode = "";
        if (isOperator) mode += "@"; else if (isVoice) mode += "+";
        setText("  " + mode + nick_ + "  ");
    }

    public int compareTo(ChannelUser o) {
        return nick_.compareToIgnoreCase(o.getNick());
    }
}
