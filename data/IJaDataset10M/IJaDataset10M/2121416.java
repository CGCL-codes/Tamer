package calclipse.lib.lcd.display.actions;

import javax.swing.Icon;
import calclipse.lib.lcd.display.Display;
import calclipse.lib.lcd.display.DisplayAction;
import calclipse.lib.lcd.display.DisplayModel;

public class EndAction extends DisplayAction {

    private static final long serialVersionUID = 1L;

    public EndAction(final Display display) {
        super(display);
    }

    public EndAction(final Display display, final String name) {
        super(display, name);
    }

    public EndAction(final Display display, final String name, final Icon icon) {
        super(display, name, icon);
    }

    @Override
    public void execute() {
        final DisplayModel model = display.getModel();
        final int line = model.getLineCount() - 1;
        model.setPosition(model.getCharCount(line), line, true);
    }
}
