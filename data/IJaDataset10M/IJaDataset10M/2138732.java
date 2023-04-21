package jp.hpl.common.thread;

import src.gui.Model;

public class ProgressWriteThread extends Thread {

    Model model;

    String filePath;

    public ProgressWriteThread(Model model) {
        this.model = model;
        filePath = null;
    }

    public ProgressWriteThread(Model model, String path) {
        this.model = model;
        filePath = path;
    }

    public void run() {
        try {
            if (filePath != null) {
                model.writeNew(filePath);
            } else {
                model.writeData();
            }
            model.turnProgOff();
        } catch (Exception e) {
            System.out.println(e);
            model.turnProgOff();
        }
    }
}
