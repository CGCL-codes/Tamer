package net.sourceforge.keepassj2me;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Gauge;
import javax.microedition.lcdui.Item;

/**
 * Form for display current progress a process 
 * @author Stepan Strelets
 */
public class ProgressForm extends Form implements IProgressListener, CommandListener {

    private Gauge bar = null;

    private Command cmdCancel = null;

    private boolean cancel = false;

    /**
	 * Construct form
	 * @param title Form title
	 * @param cancelable
	 */
    public ProgressForm(String title, boolean cancelable) {
        super(title);
        bar = new Gauge("", false, 100, 0);
        bar.setLayout(Item.LAYOUT_CENTER | Item.LAYOUT_VCENTER | Item.LAYOUT_EXPAND);
        if (cancelable) {
            this.cmdCancel = new Command("Cancel", Command.CANCEL, 1);
            this.addCommand(this.cmdCancel);
            this.setCommandListener(this);
        }
        this.append(bar);
    }

    public void setProgress(int procent, String message) throws KeePassException {
        if (this.cancel) {
            throw new KeePassException("Cancel by user");
        } else {
            bar.setValue(procent);
            if (message != null) bar.setLabel(message);
        }
        ;
    }

    public void commandAction(Command cmd, Displayable dsp) {
        if (cmd == this.cmdCancel) {
            this.cancel = true;
            this.bar.setLabel("Canceling");
        }
    }
}
