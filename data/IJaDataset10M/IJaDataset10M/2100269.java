package de.hpi.eworld.scenarios;

import org.apache.log4j.Logger;
import org.java.plugin.Plugin;
import de.hpi.eworld.core.ui.MainWindow;
import de.hpi.eworld.networkview.NetworkView;
import de.hpi.eworld.networkview.model.ViewItem;
import de.hpi.eworld.observer.NotificationType;
import de.hpi.eworld.observer.ObserverNotification;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JCheckBoxMenuItem;

public class ScenariosDockWidgetPlugin extends Plugin implements Observer {

    private final transient Logger logger = Logger.getLogger(this.getClass());

    /**
	 * The ID of this plug-in.
	 */
    public static final String PLUGIN_ID = "de.hpi.eworld.scenarios.scenariosdockwidget";

    /**
	 * The dock widget.
	 */
    private ScenariosDockWidget scenariosDockWidget;

    /**
	 * Indicates, whether the connection between the network view and the scenarios is already done.
	 */
    private boolean networkViewConnectedWithScenarios = false;

    /**
	 * A reference to the networkview.
	 */
    private NetworkView nwView;

    /**
	 * A reference to the parent window.
	 */
    private MainWindow parentWindow = null;

    /**
	 * An action, which toggles the display of the dock widget.
	 */
    private JCheckBoxMenuItem viewAction;

    /**
	 * Executed, when the plugin is loaded.
	 */
    @Override
    protected void doStart() throws Exception {
    }

    /**
	 * Executed, when the plugin is suspended.
	 */
    @Override
    protected void doStop() throws Exception {
    }

    /**
	 * Initializes this plugin.
	 * 
	 * @param parentWindow
	 *            The parent window.
	 * @param viewAction
	 *            The action to toggle the display of the dock widget.
	 * @param title
	 *            The title of the dock widget.
	 */
    public void initialize(final MainWindow parentWindow, final JCheckBoxMenuItem viewAction, final String title) {
        if (scenariosDockWidget == null) {
            scenariosDockWidget = new ScenariosDockWidget();
        }
        scenariosDockWidget.setNetworkView(nwView);
        this.parentWindow = parentWindow;
        this.viewAction = viewAction;
        this.viewAction.setSelected(true);
        this.viewAction.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                scenariosDockWidget.setVisible(ScenariosDockWidgetPlugin.this.viewAction.getState());
            }
        });
        scenariosDockWidget.startUp(parentWindow);
        scenariosDockWidget.setVisible(true);
        tryToConnectNetworkViewWithScenarios();
    }

    /**
	 * Initializes the reference to the network view and tries to connect to scenarios.
	 * @param networkView Reference to the network view.
	 */
    public void initializeEventHandler(final NetworkView networkView) {
        nwView = networkView;
        if (scenariosDockWidget == null) {
            scenariosDockWidget = new ScenariosDockWidget();
        }
        scenariosDockWidget.setNetworkView(nwView);
        scenariosDockWidget.initialize();
    }

    /**
	 * Connects signals from network view with methods in scenarios.
	 */
    private void tryToConnectNetworkViewWithScenarios() {
        if (!networkViewConnectedWithScenarios && nwView != null) {
            nwView.provideObservable().addObserver(this);
            networkViewConnectedWithScenarios = true;
        }
    }

    @Override
    public void update(final Observable o, final Object arg) {
        if (arg instanceof ObserverNotification) {
            final ObserverNotification notification = (ObserverNotification) arg;
            final NotificationType type = notification.getType();
            logger.debug("Received notification: " + type.toString());
            switch(type) {
                case freeSpaceClicked:
                    scenariosDockWidget.onFreeSpaceClicked();
                    break;
                case itemClicked:
                    scenariosDockWidget.onViewItemClicked((ViewItem) notification.getObj1());
                    break;
            }
        } else {
            throw new IllegalArgumentException("Notification from Observable is of unknown type.");
        }
    }
}
