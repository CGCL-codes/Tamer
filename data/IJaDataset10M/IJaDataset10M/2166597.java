package com.google.gwt.sample.logexample.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.logging.client.HasWidgetsLogHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * A page to help users understand logging in GWT.
 */
public class LogExample implements EntryPoint {

    interface MyUiBinder extends UiBinder<HTMLPanel, LogExample> {
    }

    private static Logger childLogger = Logger.getLogger("ParentLogger.Child");

    private static Logger parentLogger = Logger.getLogger("ParentLogger");

    private static Logger rootLogger = Logger.getLogger("");

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField
    SimplePanel customLogArea;

    @UiField
    SimplePanel handlerControls;

    @UiField
    SimplePanel loggerControls;

    @UiField
    SimplePanel logOnServerButton;

    public void onModuleLoad() {
        HTMLPanel p = uiBinder.createAndBindUi(this);
        RootPanel.get().add(p);
        loggerControls.setWidget(new LoggerController(rootLogger, parentLogger, childLogger).getPanel());
        handlerControls.setWidget(new HandlerController(rootLogger).getPanel());
        logOnServerButton.setWidget(new ServerLoggingArea().getPanel());
        customLogArea.setWidget(new CustomLogArea(childLogger).getPanel());
        Handler[] handlers = Logger.getLogger("").getHandlers();
        if (handlers != null) {
            for (Handler h : handlers) {
                if (h instanceof HasWidgetsLogHandler) {
                    String msg = "This popup can be resized, moved and minimized";
                    h.publish(new LogRecord(Level.SEVERE, msg));
                }
            }
        }
    }
}
