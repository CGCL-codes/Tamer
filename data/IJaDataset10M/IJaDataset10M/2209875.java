package net.sipvipbase.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

public class VideoScrollBox extends Composite {

    public VideoScrollBox(String urlstr) {
        content = new ScrollPanel();
        content.setStyleName("scrollbox");
        JsonRequests.getJsonScrollPanel(content, urlstr);
        initWidget(content);
    }

    private ScrollPanel content;
}
