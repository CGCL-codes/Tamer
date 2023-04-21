package net.sf.hdkp.ui.updater;

public interface UpdaterController {

    public boolean hasError();

    public void displayError();

    public void displayButtons();

    public void displayDone();

    public void hideView();

    public void showView();
}
