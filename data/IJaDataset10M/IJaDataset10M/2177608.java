package Sale.Display;

import java.awt.Component;
import java.awt.TextArea;

/**
  * A static text control.
  *
  * <p>A static text control will simply print out a message to the user. It will not
  * react to user input and its return data will be <i>null</i>.</p>
  *
  * <p>To use the static text control in your user IO use code like the following: <hr>
  *
  * <PRE>
  * ...
  * FormSheet fs = getDisplayManager().createFormSheet();
  *
  * ...
  *
  * fs.addItem (new StaticItemDescriptor("Hallo Welt!"));
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
  * @see StaticItemDescriptor
  * @see DisplayManager#fillFormSheet
  *
  * @author Steffen Zschaler
  * @version 0.5
  */
public class Static extends FormSheetItem implements ConsoleFormSheetItem, AWTFormSheetItem {

    /**
    * The message to be displayed.
    */
    protected String sMsg;

    /**
    * Construct a new Static control.
    *
    * <p>You will never call this constructor directly as it is called in
    * <a href=FormSheet#addItem>FormSheet.addItem</a>. But you must declare it to make
    * this class a valid FormSheetItem.</p>
    *
    * @see FormSheetItem
    */
    public Static(StaticItemDescriptor sid) {
        super();
        sMsg = sid.getMessage();
    }

    /**
    * Return the data contained in this Static.
    *
    * <p>After the FormSheet was handed to the
    * <a href=DisplayManager#fillFormSheet>fillFormSheet()</a>-method of the
    * DisplayManager the getData()-method of each FormSheetItem will return the data
    * entered by the user.</p>
    *
    * <p>Returns null, as a static text control does not take any user input.</p>
    */
    public Object getData() {
        return null;
    }

    /**
    * Does the actual user IO on a console.
    *
    * <p>To give you an example of how to redefine this method the code for the static text
    * is included here: 
    *
    * <hr><PRE>
    *
    * System.out.println (sMsg);
    * </PRE><hr>
    */
    public void doConsoleIO() {
        System.out.println(sMsg);
    }

    public Component getAWTControl() {
        TextArea ta = new TextArea(sMsg);
        ta.setEditable(false);
        return ta;
    }
}
