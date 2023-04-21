package ru.goldenforests.forum.beans;

import java.sql.Timestamp;
import ru.goldenforests.forum.ForumPostRevision;
import ru.goldenforests.forum.util.OutputFormatter;

/**
 * @author svv
 *
 */
public class ConstForumPostRevision {

    private final String subject;

    private final String sig;

    private final String text;

    private final String sourceIP;

    private final Timestamp date;

    public ConstForumPostRevision(ForumPostRevision revision) {
        this.subject = revision.getSubject();
        this.sig = revision.getSig();
        this.text = revision.getText();
        this.sourceIP = revision.getSourceIP();
        this.date = revision.getDate();
    }

    public String getSubject() {
        return OutputFormatter.screenSimple(subject);
    }

    public String getSig() {
        return OutputFormatter.screenSimple(sig);
    }

    public String getText() {
        return OutputFormatter.fullFormat(text);
    }

    public String getSourceIP() {
        return OutputFormatter.screenSimple(sourceIP);
    }

    public String getDate() {
        return OutputFormatter.formatDate(date);
    }
}
