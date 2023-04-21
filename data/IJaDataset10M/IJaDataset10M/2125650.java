package jhelpgenerator.utils.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import jhelpgenerator.utils.*;

/**
 *
 * @author v3r5_u5
 */
public class MapItemRemAction extends AbstractAction {

    private Global global;

    public MapItemRemAction(Global global) {
        super();
        this.global = global;
        putValue(NAME, "Remove Map item");
        putValue(SHORT_DESCRIPTION, "Remove Map item");
        putValue(SMALL_ICON, Global.createImageIcon("icons/16/remove.png"));
    }

    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
