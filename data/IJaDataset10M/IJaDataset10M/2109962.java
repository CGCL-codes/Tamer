package quizgame.protocol.typer;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author gsohtell
 */
public class Message implements TyperPacket {

    private String title;

    private String message;

    public enum Type {

        INFORMATION, WARNING, ERROR
    }

    ;

    private Message.Type type;

    /** Creates a new instance of Message */
    public Message(String message, String title, Message.Type type) {
        this.title = title;
        this.message = message;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public Message.Type getType() {
        return type;
    }

    /**
     *  Shows a JOptionPane containing the message and its properties
     *  @param parent The parent component
     */
    public void showJOptionPane(Component parent) {
        if (type == Type.ERROR) {
            JOptionPane.showMessageDialog(parent, getMessage(), getTitle(), JOptionPane.ERROR_MESSAGE);
        } else if (type == Type.INFORMATION) {
            JOptionPane.showMessageDialog(parent, getMessage(), getTitle(), JOptionPane.INFORMATION_MESSAGE);
        } else if (type == Type.WARNING) {
            JOptionPane.showMessageDialog(parent, getMessage(), getTitle(), JOptionPane.WARNING_MESSAGE);
        }
    }
}
