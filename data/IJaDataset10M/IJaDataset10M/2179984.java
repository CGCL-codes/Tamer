package net.simpleframework.applets.notification;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MailMessageNotification extends AbstractMessageNotification {

    private static final long serialVersionUID = -2756839612191089076L;

    private MailSender sender;

    private String cc;

    private File[] attactments;

    private boolean htmlContent;

    private Collection<Object> to;

    public MailSender getSender() {
        return sender;
    }

    public void setSender(final MailSender sender) {
        this.sender = sender;
    }

    public Collection<Object> getTo() {
        if (to == null) {
            to = new ArrayList<Object>();
        }
        return to;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(final String cc) {
        this.cc = cc;
    }

    public File[] getAttactments() {
        return attactments;
    }

    public void setAttactments(final File[] attactments) {
        this.attactments = attactments;
    }

    public boolean isHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(final boolean htmlContent) {
        this.htmlContent = htmlContent;
    }
}
