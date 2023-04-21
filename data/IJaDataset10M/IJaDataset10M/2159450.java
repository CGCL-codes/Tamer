package gallery.model.command;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class SendEmail {

    private String text;

    private String email_from;

    private String email_to;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getEmail_from() {
        return email_from;
    }

    public void setEmail_from(String email_from) {
        this.email_from = email_from;
    }

    public String getEmail_to() {
        return email_to;
    }

    public void setEmail_to(String email_to) {
        this.email_to = email_to;
    }
}
