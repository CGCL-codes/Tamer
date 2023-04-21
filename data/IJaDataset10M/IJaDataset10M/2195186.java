package watij.runtime.ie;

import com.jniwrapper.win32.ui.Wnd;
import watij.dialogs.Dialog;

public abstract class IEBaseDialog implements Dialog {

    Wnd dialog;

    IE ie;

    public IEBaseDialog(Wnd dialog, IE ie) {
        this.dialog = dialog;
        this.ie = ie;
    }

    public String text() throws Exception {
        return IEUtil.getStaticText(dialog);
    }

    public String title() throws Exception {
        return dialog.getWindowText();
    }

    public void quit() throws Exception {
        int wm_quit = 0x0012;
        dialog.postMessage(wm_quit, 0, 0);
    }

    public boolean exists() throws Exception {
        return dialog.isWindow();
    }
}
