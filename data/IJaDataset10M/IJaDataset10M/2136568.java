package org.heartstorming.bada.timebar;

import java.util.List;
import java.util.Iterator;
import javax.swing.JPanel;
import javax.swing.DefaultListModel;
import org.heartstorming.bada.util.*;
import org.heartstorming.bada.swingui.TimeBarGUI;

public class TimeBarObserver implements Observer {

    private TimeBar timeBar;

    private TimeBarGUI gui;

    public TimeBarObserver() {
    }

    public TimeBarObserver(TimeBar timeBar) {
        this.timeBar = timeBar;
        timeBar.registerObserver(this);
    }

    public void setTimeBar(TimeBar bar) {
        System.out.println("[DEBUG] TimeBarObserver::setTimeBar() entrance.");
        timeBar = bar;
        if (gui != null) gui.setThisIsFirstDisplay();
        timeBar.registerObserver(this);
        System.out.println("[DEBUG] TimeBarObserver::setTimeBar() exit...");
    }

    public void setGUI(TimeBarGUI gui) {
        this.gui = gui;
    }

    public void update(List phrase, int index) {
        if (gui == null) return;
        if (timeBar != null) {
            DefaultListModel listModel = gui.getListModel();
            listModel.clear();
            for (Iterator i = phrase.iterator(); i.hasNext(); ) {
                listModel.addElement(i.next());
            }
            gui.getList().setSelectedIndex(index);
        }
    }
}
