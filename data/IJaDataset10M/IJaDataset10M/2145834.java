package com.goodow.web.logging.client;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

@Singleton
public final class LogHandler extends Handler {

    private PopupPanel popup;

    private HTML html = new HTML();

    private Level currentLevel = Level.ALL;

    @Inject
    public LogHandler(final LogFormatter formatter) {
        setFormatter(formatter);
        setLevel(Level.INFO);
        popup = new PopupPanel(true);
        popup.setPreviewingAllNativeEvents(true);
        popup.setWidget(html);
        popup.getElement().getStyle().setZIndex(990);
    }

    @Override
    public void close() {
    }

    @Override
    public void flush() {
    }

    @Override
    public void publish(final LogRecord record) {
        boolean requestHide = record.getMessage() == null && Level.INFO.equals(record.getLevel());
        if (!isLoggable(record) && !requestHide) {
            return;
        }
        if (popup.isShowing() && currentLevel.equals(Level.SEVERE)) {
            return;
        }
        if (requestHide) {
            popup.hide();
            return;
        }
        if (record.getLevel().intValue() >= Level.SEVERE.intValue()) {
        }
        currentLevel = record.getLevel();
        Formatter formatter = getFormatter();
        String msg = formatter.format(record);
        html.setHTML(msg);
        show();
    }

    private void show() {
        popup.hide();
        popup.center();
        popup.setPopupPosition(popup.getPopupLeft(), 0);
        popup.show();
        popup.getElement().getStyle().setPosition(Position.FIXED);
    }
}
