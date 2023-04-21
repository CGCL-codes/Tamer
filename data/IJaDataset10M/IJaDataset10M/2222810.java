package jmax.editors.console;

import java.awt.*;
import javax.swing.*;
import jmax.commons.*;
import jmax.ui.*;
import jmax.toolkit.*;

/** The console module; the initModule function is called at init time
 *  by jmax, and install module related things
 */
public class ConsoleTool extends AbstractTool {

    ConsoleWindow console;

    ConsoleTool(UIContext context) {
        super(context);
        console = new ConsoleWindow(this);
    }

    public JComponent getMainComponent() {
        return console;
    }

    public MenuProvider getMenuProvider() {
        return console;
    }

    public String getTitle() {
        return "jMax Console";
    }

    public void postString(String s) {
        console.getPrintStream().println(s);
    }

    public Rectangle getPreferredBounds() {
        return null;
    }

    public Icon getIcon() throws MaxError {
        return Icons.get("jmax://core/images/console.gif");
    }
}
