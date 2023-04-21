package org.gruposp2p.dnie.client.ui.dialogs;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 *
 * @author jj
 */
public class ErrorDialog extends DialogBox implements ClickHandler {

    private HTML body = new HTML("");

    public ErrorDialog() {
        setStylePrimaryName("DynaTable-ErrorDialog");
        Button closeButton = new Button("Close", this);
        VerticalPanel panel = new VerticalPanel();
        panel.setSpacing(4);
        panel.add(body);
        panel.add(closeButton);
        panel.setCellHorizontalAlignment(closeButton, VerticalPanel.ALIGN_RIGHT);
        setWidget(panel);
    }

    public String getBody() {
        return body.getHTML();
    }

    public void onClick(ClickEvent event) {
        hide();
    }

    public void setBody(String html) {
        body.setHTML(html);
    }
}
