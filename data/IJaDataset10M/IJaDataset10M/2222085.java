package de.tobiasmaasland.voctrain.client.tag;

import org.apache.log4j.Logger;
import de.tobiasmaasland.voctrain.client.about.AboutController;
import de.tobiasmaasland.voctrain.client.about.AboutView;
import fi.mmm.yhteinen.swing.core.YController;
import fi.mmm.yhteinen.swing.core.YModel;

public class TagController extends YController {

    private static Logger log = Logger.getLogger(TagController.class);

    private AboutController aboutController = null;

    private TagView view = new TagView();

    private YModel model = new TagModel();

    public TagController() {
        this.setUpMVC(model, view);
    }

    public void btnNewPressed() {
        aboutController = new AboutController();
        this.addChild(aboutController);
        ((AboutView) aboutController.getView()).setVisible(true);
    }

    public void btnClosePressed() {
        log.debug("Close!!");
        view.setVisible(false);
        view.dispose();
        this.nullifyModel();
        this.nullifyView();
    }
}
