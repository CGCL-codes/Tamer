package cz.cvut.phone.mailer.DTO;

import java.util.List;
import java.io.File;

/**
 *
 * @author Jarda
 */
public class MessageDTO {

    private String[] sender;

    private String reciever;

    private String subject;

    private List<File> attachments;

    private String text;

    public MessageDTO(String[] sender, String reciever, String subject, List<File> attachments, String text) {
        this.sender = sender;
        this.reciever = reciever;
        this.subject = subject;
        this.attachments = attachments;
        this.text = text;
    }

    public List<File> getAttachments() {
        return attachments;
    }

    public String getReciever() {
        return reciever;
    }

    public String[] getSender() {
        return sender;
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }
}
