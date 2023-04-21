package samples.swing.combobox;

import org.scopemvc.controller.basic.BasicController;

/**
 * @author <A HREF="mailto:daniel.michalik@autel.cz">Daniel Michalik</A>
 * @created 05 September 2002
 * @version $Revision: 1.4 $ $Date: 2002/09/05 15:41:47 $
 */
public class ComboDemoController extends BasicController {

    /**
     * Constructor for the ComboDemoController object
     */
    public ComboDemoController() {
        ComboDemoModel model = new ComboDemoModel();
        setModel(model);
        ComboDemoView view = new ComboDemoView();
        model.setRootComponent(view);
        setView(view);
    }
}
