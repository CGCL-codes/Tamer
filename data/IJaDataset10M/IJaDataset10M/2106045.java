package ossobook.client.gui.update.elements.inputfields;

import ossobook.client.gui.update.components.other.AbstraktesSelbstkorrekturKodeFeld;
import ossobook.client.gui.update.components.window.ProjektFenster;

/**
 * @author ali
 */
public class ErhaltungFeld extends AbstraktesSelbstkorrekturKodeFeld {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /** Creates a new instance of SkelCodeField */
    public ErhaltungFeld() {
        super();
    }

    @Override
    public boolean condition() {
        boolean result;
        try {
            Integer i = Integer.parseInt(this.getText());
            String s = ProjektFenster.erhaltunglist.getTeil(i);
            if (!s.equals("")) {
                titledBorder.setTitle(s);
                result = true;
            } else {
                titledBorder.setTitle("");
                result = false;
            }
        } catch (Exception e) {
            titledBorder.setTitle("");
            result = false;
        }
        if (getText().equals("")) {
            result = true;
        }
        return result;
    }
}
