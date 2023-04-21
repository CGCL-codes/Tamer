package customComponents;

import java.util.List;
import javax.swing.JComboBox;
import utils.EventOperations;

public class TimesComboBox extends JComboBox {

    private static final long serialVersionUID = 1L;

    private EventOperations evOperations = new EventOperations();

    /**
	 * Adding default minutes values
	 */
    public TimesComboBox(String type) {
        List<String> minutes = evOperations.getMinutes();
        List<String> seconds = evOperations.getSeconds();
        List<String> mSeconde = evOperations.getmSecondes();
        if (type.toLowerCase().equals("minutes")) {
            for (String m : minutes) {
                this.addItem(m);
            }
        } else if (type.toLowerCase().equals("seconds")) {
            for (String m : seconds) {
                this.addItem(m);
            }
        } else if (type.toLowerCase().equals("mseconds")) {
            for (String m : mSeconde) {
                this.addItem(m);
            }
        }
    }
}
