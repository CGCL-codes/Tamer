package org.jSyncManager.SJS.webstart;

import java.io.File;

public class Launcher {

    private Model model;

    private LauncherView view;

    public Launcher() {
        model = new Model();
        view = new LauncherView(this);
    }

    public void setSelectedAdapter(int pos) {
        model.getAdapter(pos);
        view.getAdapterPanel().setAdapter(model.getAdapter(pos));
    }

    public void addNewAdapter() {
        Adapter adapter = new Adapter();
        model.addAdapter(adapter);
        view.updateAdapterList(model);
        view.setAdapter(adapter);
    }

    public void saveAdapter(Adapter adapter) {
        view.updateAdapterList(model);
    }

    public void launch() {
    }

    public void setFile(File file) {
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        new Launcher();
    }
}
