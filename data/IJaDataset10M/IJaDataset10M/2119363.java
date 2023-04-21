package org.tei.comparator.web.client;

import org.tei.comparator.web.client.info.StatusBar;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Provides helper methods to indicate that TEI Comparator is working at the moment.
 * 
 * @author Arno Mittelbach
 *
 */
public class Work {

    private static boolean inWork = false;

    private static Timer workTimer;

    public static void show() {
        show(StatusBar.getInstance().getText());
    }

    public static void show(String text) {
        inWork = true;
        RootPanel.get("ajax-loader").setVisible(true);
        StatusBar.getInstance().setText(text);
        workTimer = new Timer() {

            int dots = 0;

            String oldText = StatusBar.getInstance().getText();

            @Override
            public void run() {
                dots = (dots + 1) % 4;
                String myDots = oldText + " ";
                for (int i = 0; i < dots; i++) myDots += ".";
                StatusBar.getInstance().setText(myDots);
            }

            @Override
            public void cancel() {
                super.cancel();
                StatusBar.getInstance().setText(" ");
            }
        };
        workTimer.scheduleRepeating(1000);
    }

    public static void hide() {
        RootPanel.get("ajax-loader").setVisible(false);
        workTimer.cancel();
        inWork = false;
    }

    public static boolean working() {
        return inWork;
    }
}
