package your.app.gwt;

import java.util.HashMap;
import wogwt.translatable.WOGWTClientUtil;
import wogwt.translatable.http.LogOnErrorRequestCallback;
import wogwt.translatable.richtext.RichTextToolbar;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.gen2.logging.shared.Log;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class RichTextExampleScript implements EntryPoint {

    public void onModuleLoad() {
        if (!WOGWTClientUtil.hostPageNameEquals("RichTextExample")) {
            return;
        }
        Log.finest(getClass().getName() + ": onModuleLoad");
        final RichTextArea textArea = new RichTextArea();
        RichTextToolbar toolbar = new RichTextToolbar(textArea);
        VerticalPanel panel = new VerticalPanel();
        panel.add(toolbar);
        panel.add(textArea);
        textArea.setHeight("14em");
        textArea.setWidth("100%");
        toolbar.setWidth("100%");
        panel.setWidth("100%");
        DOM.setStyleAttribute(panel.getElement(), "marginRight", "4px");
        panel.setWidth("32em");
        Button button = new Button();
        button.setText("Send to Server");
        button.addClickListener(new ClickListener() {

            public void onClick(Widget sender) {
                sendTextToServer(textArea);
            }
        });
        panel.add(button);
        RootPanel.get(CONTAINER_ID).add(panel);
    }

    private void sendTextToServer(final RichTextArea textArea) {
        String url = WOGWTClientUtil.publishedUrlForComponentActionNamed("getRichTextAction");
        url = WOGWTClientUtil.componentUrlToAjaxUrl(url, STATUS_CONTAINER_ID);
        HashMap formValues = new HashMap();
        formValues.put(RICH_TEXT_KEY, textArea.getHTML());
        RequestCallback callback = new LogOnErrorRequestCallback() {

            @Override
            public void onSuccess(Request request, Response response) {
                Element statusContainer = DOM.getElementById(STATUS_CONTAINER_ID);
                statusContainer.setInnerHTML(response.getText());
            }
        };
        WOGWTClientUtil.fetchUrl(url, true, formValues, 10000, callback);
    }

    public static final String CONTAINER_ID = "container";

    public static final String STATUS_CONTAINER_ID = "statusContainer";

    public static final String RICH_TEXT_KEY = "richtext";
}
