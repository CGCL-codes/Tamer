package com.css.notifier.style;

/**
 * Created by IntelliJ IDEA.
 * User: liuzhy
 * Date: 2010-2-24
 * Time: 15:23:16
 */
public class NotifyStyleBold extends NotifyStyleDecorator {

    public NotifyStyleBold(NotifyStyle notifyStyle) {
        super(notifyStyle);
    }

    @Override
    public String showStyle(String notifyMessage) {
        this.notifyMessage = notifyMessage;
        return "<b>" + super.showStyle(notifyMessage) + "</b>";
    }
}
