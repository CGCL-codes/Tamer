package com.rapidminer.gui.new_plotter;

import java.util.LinkedList;
import java.util.List;
import com.rapidminer.gui.new_plotter.listener.events.ConfigurationChangeEvent;

/**
 * @author Marius Helf
 *
 */
public class PlotConfigurationQuickFix {

    private List<ConfigurationChangeEvent> changeList = new LinkedList<ConfigurationChangeEvent>();

    public PlotConfigurationQuickFix() {
    }

    public PlotConfigurationQuickFix(ConfigurationChangeEvent change) {
        addChange(change);
    }

    public List<ConfigurationChangeEvent> getChanges() {
        return changeList;
    }

    public void addChange(ConfigurationChangeEvent change) {
        changeList.add(change);
    }
}
