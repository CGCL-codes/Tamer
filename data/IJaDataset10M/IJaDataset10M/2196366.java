package org.labrad.browser.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LogWindow extends VerticalPanel {

    /**
   * singleton instance
   */
    private static LogWindow instance = null;

    public static LogWindow getInstance() {
        if (instance == null) {
            instance = new LogWindow();
        }
        return instance;
    }

    private VerticalPanel logger;

    private int LOG_LENGTH = 50;

    /**
   * Simple panel for logging messages
   */
    public LogWindow() {
        Button btn = new Button("clear log");
        btn.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent e) {
                clear();
            }
        });
        logger = new VerticalPanel();
        add(btn);
        add(logger);
    }

    public void clear() {
        logger.clear();
    }

    public void logStuff(String message, String details) {
        logger.insert(new LogMessage(message, details), 0);
        if (logger.getWidgetCount() > LOG_LENGTH) {
            logger.remove(logger.getWidgetCount() - 1);
        }
    }

    public static void log(String message, String details) {
        getInstance().logStuff(message, details);
    }

    public static void log(String message, Throwable caught) {
        getInstance().logStuff(message, caught.toString());
    }
}
