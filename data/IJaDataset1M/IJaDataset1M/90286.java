package calclipse.lib.lcd.display.actions;

import javax.swing.Icon;
import calclipse.lib.lcd.display.Display;
import calclipse.lib.lcd.display.DisplayAction;
import calclipse.lib.lcd.display.DisplayModel;

public class SelectEndAction extends DisplayAction {

    private static final long serialVersionUID = 1L;

    public SelectEndAction(final Display display) {
        super(display);
    }

    public SelectEndAction(final Display display, final String name) {
        super(display, name);
    }

    public SelectEndAction(final Display display, final String name, final Icon icon) {
        super(display, name, icon);
    }

    @Override
    public void execute() {
        display.setCancelSelection(false);
        final DisplayModel model = display.getModel();
        if (!model.isSelecting()) {
            model.setSelection(model.getX(), model.getY(), false);
            model.setSelecting(true, false);
        }
        final int line = model.getLineCount() - 1;
        model.setPosition(model.getCharCount(line), line, false);
        model.fireDisplayModelChanged();
        display.setCancelSelection(true);
    }
}
