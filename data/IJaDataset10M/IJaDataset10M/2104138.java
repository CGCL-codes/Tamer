package ibcontroller;

import java.awt.Window;
import java.awt.event.WindowEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JFrame;

class GatewayLoginFrameHandler implements WindowHandler {

    public void handleWindow(final Window window, int eventID) {
        if (eventID != WindowEvent.WINDOW_OPENED) return;
        if (!setFieldsAndClick(window)) {
            System.err.println("IBController: could not login because we could not find one of the controls.");
        }
    }

    public boolean recogniseWindow(Window window) {
        if (!(window instanceof JFrame)) return false;
        return (Utils.titleContains(window, "IB Gateway") && (Utils.findButton(window, "Login") != null));
    }

    private boolean setFieldsAndClick(final Window window) {
        if (Utils.findRadioButton(window, "IB API") != null) {
            if (!Utils.isRadioButtonSelected(window, "IB API")) {
                if (!Utils.setRadioButtonSelected(window, "IB API")) return false;
            }
        } else if (Utils.findRadioButton(window, "TWS/API") != null) {
            if (!Utils.isRadioButtonSelected(window, "TWS/API")) {
                if (!Utils.setRadioButtonSelected(window, "TWS/API")) return false;
            }
        } else return false;
        if (!Utils.setTextField(window, 0, TwsListener.getUserName())) return false;
        if (!Utils.setTextField(window, 1, TwsListener.getPassword())) return false;
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
                            done.set(!window.isVisible());
                        }
                    });
                    Utils.pause(500);
                } while (!done.get());
            }
        }, 10);
        return true;
    }
}
