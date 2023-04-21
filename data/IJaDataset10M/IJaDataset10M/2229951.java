package com.romanenco.gwtphp2.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * GWT PHP example
 * Application asks server for current time on button press
 * 
 * @author Andrew Romanenco
 * andrew@romanenco.com
 * http://romanenco.com/gwtphp
 * 
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Main implements EntryPoint {

    private static final String url = "/gwtphp/echo.php";

    /**
   * This is the entry point method.
   */
    public void onModuleLoad() {
        final Button button = new Button("PHPEcho call");
        final TextBox echo = new TextBox();
        echo.setReadOnly(true);
        button.addClickListener(new ClickListener() {

            public void onClick(Widget sender) {
                echo.setText("Sending request to: " + url);
                RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url));
                try {
                    builder.sendRequest(null, new RequestCallback() {

                        public void onError(Request request, Throwable exception) {
                            echo.setText("Error with HTTP code: " + exception.getMessage());
                        }

                        public void onResponseReceived(Request request, Response response) {
                            echo.setText(response.getText());
                        }
                    });
                } catch (RequestException e) {
                    echo.setText("Exception: " + e.getMessage());
                }
            }
        });
        RootPanel.get("slot1").add(button);
        RootPanel.get("slot2").add(echo);
    }
}
