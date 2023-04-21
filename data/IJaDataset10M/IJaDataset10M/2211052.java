package ru.smartcrx.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import ru.smartcrx.client.window.LoginWindow;

/**
 * Main entry point.
 *
 * @author Dude
 */
public class MainEntryPoint implements EntryPoint {

    /** 
     * Creates a new instance of MainEntryPoint
     */
    public MainEntryPoint() {
    }

    /** 
     * The entry point method, called automatically by loading a module
     * that declares an implementing class as an entry-point
     */
    public void onModuleLoad() {
        final Button smartButton = new Button("LoginWindow");
        smartButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                new LoginWindow().show();
            }
        });
        RootPanel.get().add(smartButton);
    }
}
