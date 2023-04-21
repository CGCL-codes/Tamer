package Sale.Display;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.awt.Component;
import java.awt.Container;
import java.awt.TextField;

/**
  * A password input line control.
  *
  * <p>A password input line control will prompt the user to enter one line of text which can be
  * interpreted as an password.</p>
  *
  * <p><STRONG>Attention:</STRONG> On console like systems it is impossible to catch user input
  * before it was fully entered. For this reason on console like systems password input lines work
  * the same way as standard input lines. THEY WILL PERFORM NO HIDING OF USER INPUT!</p>
  * 
  * <p>For another flaw on some console systems see <a href="Sale.Display.InputLine.html">InputLine</a>.</p>
  *
  * <p>To use the password input line control in your user IO use code like the following: <hr>
  *
  * <PRE>
  * ...
  * FormSheet fs = getDisplayManager().createFormSheet();
  * fs.setFlags (FormSheet.FORCE_CONFIRM_INPUT, true);
  *
  * ...
  *
  * fs.addItem (new PassWDInputLineDescriptor("Enter your name please: "));
  *
  * ...
  *
  * getDisplayManager().fillFormSheet();
  *
  * ...
  *
  * </PRE><hr>
  *
  * @see DisplayManager#createFormSheet
  * @see FormSheet#addItem
  * @see InputLineDescriptor
  * @see DisplayManager#fillFormSheet
  *
  * @author Steffen Zschaler
  * @version 0.5
  */
public class PassWDInputLine extends InputLine implements ConsoleFormSheetItem, AWTFormSheetItem {

    /**
    * Construct a new password input line control.
    *
    * <p>You will never call this constructor directly as it is called in
    * <a href=FormSheet#addItem>FormSheet.addItem</a>. But you must declare it to make
    * this class a valid FormSheetItem.</p>
    *
    * @see FormSheetItem
    */
    public PassWDInputLine(PassWDInputLineDescriptor pwild) {
        super(pwild);
    }

    /**
    * Create and return an AWT Control for this FormSheetItem.
    *
    * @return an AWT Control for this FormSheetItem.
    */
    public Component getAWTControl() {
        Container c = (Container) super.getAWTControl();
        Component[] caComponents = c.getComponents();
        for (int i = 0; i < caComponents.length; i++) {
            if (caComponents[i] instanceof TextField) {
                ((TextField) caComponents[i]).setEchoChar('*');
                break;
            }
        }
        return c;
    }
}
