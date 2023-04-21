package org.jcvi.vics.web.gwt.common.client.popup;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;
import org.jcvi.vics.web.gwt.common.client.ui.ButtonSet;
import org.jcvi.vics.web.gwt.common.client.ui.RoundedButton;
import org.jcvi.vics.web.gwt.common.client.util.HtmlUtils;

/**
 * Modal alert popup.
 */
public class ErrorPopupPanel extends ModalPopupPanel {

    private String _message = "";

    public ErrorPopupPanel(String alertMessage) {
        this(alertMessage, true);
    }

    public ErrorPopupPanel(String alertMessage, boolean doFade) {
        super("Error", false, doFade);
        _message = alertMessage;
    }

    protected void populateContent() {
        add(HtmlUtils.getHtml(_message, "error"));
        add(HtmlUtils.getHtml("&nbsp;", "text"));
    }

    protected ButtonSet createButtons() {
        RoundedButton[] tmpButtons = new RoundedButton[1];
        tmpButtons[0] = new RoundedButton("OK", new ClickListener() {

            public void onClick(Widget widget) {
                hide();
            }
        });
        return new ButtonSet(tmpButtons);
    }
}
