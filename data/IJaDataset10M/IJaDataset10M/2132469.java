package graphic;

import bgpanalyzer.base.ControllerBase;
import bgpanalyzer.main.MainController;

/**
 *
 * @author paco
 */
public class CorenessController implements ControllerBase {

    static MainController mainController = null;

    static BrowseCorenessView bview = null;

    /** Creates a new instance of CorenessController */
    public CorenessController(MainController mainController) {
        this.mainController = mainController;
    }

    public void clearViews() {
    }

    public void showCorenessView() {
        bview = new BrowseCorenessView(this);
        bview.setVisible(true);
    }
}
