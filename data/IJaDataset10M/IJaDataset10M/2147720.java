package com.rapidminer.gui.new_plotter.listener;

import com.rapidminer.gui.new_plotter.listener.events.LegendConfigurationChangeEvent;

/**
 * 
 * @author Nils Woehler
 *
 */
public interface LegendConfigurationListener {

    public void legendConfigurationChanged(LegendConfigurationChangeEvent change);
}
