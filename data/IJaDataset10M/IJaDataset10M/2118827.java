package de.javatt.data.scenario.swt;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

public class DummyMenuItemListener implements Listener, Runnable {

    private Display myDisplay;

    public DummyMenuItemListener(Display disp) {
        myDisplay = disp;
    }

    public void handleEvent(Event event) {
        Thread showPopup = new Thread(this);
        showPopup.start();
    }

    public void run() {
        try {
            Thread.sleep(3000);
        } catch (Exception ex) {
        }
        myDisplay.syncExec(new Runnable() {

            public void run() {
                Shell myShell = new Shell(myDisplay);
                myShell.setText("Popup");
                myShell.setSize(200, 200);
                myShell.open();
            }
        });
    }
}
