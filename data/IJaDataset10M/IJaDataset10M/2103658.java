package ibcontroller;

import java.awt.Window;
import java.awt.event.WindowEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JFrame;

class LoginFrameHandler implements WindowHandler {

    public void handleWindow(Window window, int eventID) {
        if (eventID != WindowEvent.WINDOW_OPENED) return;
        TwsListener.setLoginFrame((JFrame) window);
        if (!setFieldsAndClick(window)) {
            System.err.println("IBController: could not login because we could not find one of the controls.");
        }
    }

    public boolean recogniseWindow(Window window) {
        if (!(window instanceof JFrame)) return false;
        return (Utils.titleEquals(window, "New Login") || Utils.titleEquals(window, "Login"));
    }

    private boolean setFieldsAndClick(final Window window) {
        if (!Utils.setTextField(window, 0, TwsListener.getUserName())) return false;
        if (!Utils.setTextField(window, 1, TwsListener.getPassword())) return false;
        if (!Utils.setCheckBoxSelected(window, "Use/store settings on server", Settings.getBoolean("StoreSettingsOnServer", false))) return false;
        if (TwsListener.getUserName().length() == 0) {
            Utils.findTextField(window, 0).requestFocus();
            return true;
        }
        if (TwsListener.getPassword().length() == 0) {
            Utils.findTextField(window, 1).requestFocus();
            return true;
        }
        if (Utils.findButton(window, "Login") == null) return false;
        final Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {

            public void run() {
                final AtomicBoolean done = new AtomicBoolean(false);
                do {
                    GuiSynchronousExecutor.instance().execute(new Runnable() {

                        public void run() {
                            Utils.clickButton(window, "Login");
                            done.set(!Utils.isButtonEnabled(window, "Login"));
                        }
                    });
                    Utils.pause(500);
                } while (!done.get());
            }
        }, 10);
        return true;
    }
}
