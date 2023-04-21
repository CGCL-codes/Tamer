package orcajo.azada.core;

import orcajo.azada.commoms.icons.IconProvider;
import orcajo.azada.core.datasources.DataSourcesManager;
import orcajo.azada.core.handlers.DrillPositionHandler;
import orcajo.azada.core.handlers.NonEmptyHandler;
import orcajo.azada.core.olap.Setting;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.varia.NullAppender;
import org.eclipse.core.commands.Command;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.StatusLineContributionItem;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.progress.UIJob;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "orcajo.azada.core";

    private static Activator plugin;

    private IWindowListener windowListener;

    private IPropertyChangeListener preferenceListener;

    /**
	 * The constructor
	 */
    public Activator() {
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        plugin.getWorkbench().addWindowListener(getWindowListener());
        plugin.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        initLogge4j();
        UIJob job = new UIJob("InitCommandsWorkaround") {

            @SuppressWarnings("restriction")
            public IStatus runInUIThread(IProgressMonitor monitor) {
                new TitleUpdater();
                IStatusLineManager slm = ((org.eclipse.ui.internal.WorkbenchWindow) plugin.getWorkbench().getActiveWorkbenchWindow()).getStatusLineManager();
                if (slm != null) {
                    StatusLineContributionItem statusItem = new StatusLineContributionItem("Olap_Filter_Group");
                    slm.add(statusItem);
                    new StatusLineUpdater(statusItem);
                }
                ICommandService commandService = (ICommandService) plugin.getWorkbench().getActiveWorkbenchWindow().getService(ICommandService.class);
                Command command = commandService.getCommand(DrillPositionHandler.ID);
                command.isEnabled();
                command = commandService.getCommand(NonEmptyHandler.ID);
                command.isEnabled();
                return new Status(IStatus.OK, PLUGIN_ID, "Init commands workaround performed succesfully");
            }
        };
        job.schedule();
        preferenceListener = new PropertyChangeListener();
        getPreferenceStore().addPropertyChangeListener(preferenceListener);
    }

    private void initLogge4j() {
        Logger logger = Logger.getRootLogger();
        ConsoleAppender consoleAppender = new ConsoleAppender(new PatternLayout("%m%n"), "System.out");
        consoleAppender.setThreshold(Level.FATAL);
        logger.addAppender(new NullAppender());
    }

    public void stop(BundleContext context) throws Exception {
        if (windowListener != null) {
            plugin.getWorkbench().removeWindowListener(windowListener);
        }
        plugin = null;
        getPreferenceStore().removePropertyChangeListener(preferenceListener);
        preferenceListener = null;
        DataSourcesManager.closeAllDatasources();
        IconProvider.dispose();
        super.stop(context);
    }

    /**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

    private IWindowListener getWindowListener() {
        if (windowListener == null) {
            windowListener = new WindowListener();
        }
        return windowListener;
    }

    /**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
    public static Activator getDefault() {
        return plugin;
    }

    private class WindowListener implements IWindowListener {

        public void windowActivated(IWorkbenchWindow window) {
        }

        public void windowClosed(IWorkbenchWindow window) {
            if (Setting.getInstance().isRestoreLastQuery()) {
                Configuration.save();
            }
        }

        public void windowDeactivated(IWorkbenchWindow window) {
        }

        public void windowOpened(IWorkbenchWindow window) {
            if (Setting.getInstance().isRestoreLastQuery()) {
                Configuration.read();
            }
        }
    }
}
