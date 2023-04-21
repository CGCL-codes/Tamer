package net.flysource.client.gui.transfers;

import fi.mmm.yhteinen.swing.core.YController;

public class DownloadOptionsController extends YController {

    DownloadOptionsModel model = new DownloadOptionsModel();

    DownloadOptionsView view = new DownloadOptionsView();

    public DownloadOptionsController() {
        super();
        setUpMVC(model, view);
        resetViewChanges();
        view.setVisible(true);
    }

    public void okButtonPressed() {
        model.setCancelled(false);
        view.dispose();
    }

    public void cancelButtonPressed() {
        model.setCancelled(true);
        view.dispose();
    }
}
