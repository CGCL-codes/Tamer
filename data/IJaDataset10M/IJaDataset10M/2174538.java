package fr.itris.glips.extension.jwidget.trends.runtime.configuration;

/**
 * the class of the listeners to the changes on the configuration objects
 * @author ITRIS, Jordi SUC
 */
public abstract class ConfigurationChangeListener {

    /**
	 * notifies that the mode or the sub mode has been changed
	 */
    public void modeOrSubModeChanged() {
    }

    /**
	 * notifies that the duration of the visible part of the curves has been modified
	 */
    public void durationChanged() {
    }

    /**
	 * notifies that the vertical zoom has changed
	 */
    public void verticalZoomChanged() {
    }
}
