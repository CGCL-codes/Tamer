package net.sf.doolin.app.mt.gui.bean;

import net.sf.doolin.app.mt.MTCommentType;
import org.joda.time.DateTime;

public class MTComment {

    private final DateTime date;

    private final MTCommentType type;

    private final String content;

    public MTComment(DateTime date, MTCommentType type, String content) {
        this.date = date;
        this.type = type;
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    public DateTime getDate() {
        return this.date;
    }

    public MTCommentType getType() {
        return this.type;
    }
}
