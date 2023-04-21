package fr.amille.animebrowser.control.controlview;

import fr.amille.animebrowser.control.InitControl;
import fr.amille.animebrowser.view.central.newepisodes.NewSerieView;

public class NewSerieViewControl implements InitControl {

    private NewSerieView newSerieView;

    public NewSerieView getNewSerieView() {
        return this.newSerieView;
    }

    @Override
    public void initControl() {
        this.newSerieView = new NewSerieView();
    }

    public void setNewSerieView(final NewSerieView newSerieView) {
        this.newSerieView = newSerieView;
    }
}
