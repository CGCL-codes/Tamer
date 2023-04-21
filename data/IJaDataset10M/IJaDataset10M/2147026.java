package ossobook.client.gui.update.elements.eingabefelder;

import ossobook.client.gui.update.components.other.AbstraktesSelbstkorrekturTextFeld;

/**
 *
 * @author  ali
 */
public class ObjektNrFeld extends AbstraktesSelbstkorrekturTextFeld {

    /** Creates a new instance of ItemNrField */
    public ObjektNrFeld() {
        super();
    }

    public boolean condition() {
        boolean result = false;
        try {
            Integer.parseInt(getText());
            result = true;
        } catch (NumberFormatException e) {
            result = false;
        }
        if (this.getText().equals("")) {
            result = true;
        }
        return result;
    }

    public void useDefaultValue() {
        this.setText("");
    }
}
