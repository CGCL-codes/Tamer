package com.google.code.sagetvaddons.sjq.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;

final class MediaTracePanel extends VerticalPanel {

    private static MediaTracePanel instance = null;

    public static MediaTracePanel getInstance() {
        if (instance == null) instance = new MediaTracePanel();
        return instance;
    }

    private VerticalPanel container;

    private Label title;

    private SuggestBox suggestBox;

    private Button debug;

    private MediaTracePanel() {
        setSize("100%", "100%");
        setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        container = new VerticalPanel();
        container.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        container.addStyleName("sjqLoginBox");
        title = new Label("Media Debugger");
        title.addStyleName("sjqLoginBox-Label");
        debug = new Button("Debug");
        debug.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (suggestBox.getText().length() == 0) return;
                final StatusPanel status = StatusPanel.getInstance();
                RequestBuilder req = new RequestBuilder(RequestBuilder.POST, AppState.getInstance().getCommandURL("debugMediaFile"));
                req.setHeader("Content-Type", "application/x-www-form-urlencoded; UTF-8");
                String data = "f=" + URL.encodeComponent(suggestBox.getText());
                try {
                    req.sendRequest(data, new RequestCallback() {

                        public void onError(Request request, Throwable exception) {
                            status.setMessage(exception.getLocalizedMessage(), StatusPanel.MessageType.ERROR);
                        }

                        public void onResponseReceived(Request request, Response response) {
                            if (response.getText().equals("Success")) status.setMessage("Debugger finished; check server logs for debug output."); else status.setMessage("Invalid file name: '" + suggestBox.getText() + "' is not a registered SageTV media file object!", StatusPanel.MessageType.ERROR);
                        }
                    });
                } catch (RequestException e) {
                    status.setMessage(e.getLocalizedMessage(), StatusPanel.MessageType.ERROR);
                }
            }
        });
        suggestBox = new SuggestBox(new JsonSuggestOracle());
        container.add(title);
        container.add(suggestBox);
        container.add(debug);
        add(container);
    }
}
