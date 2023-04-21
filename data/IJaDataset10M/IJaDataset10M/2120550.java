package ca.ucalgary.cpsc.ase.productLineDesigner;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class ProductLineDesigner extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "Test";

    private static ProductLineDesigner plugin;

    /**
	 * The constructor
	 */
    public ProductLineDesigner() {
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
    public static ProductLineDesigner getDefault() {
        return plugin;
    }

    public static void showInformationDialog(final String title, final String message) {
        final Display display = plugin.getWorkbench().getDisplay();
        display.asyncExec(new Runnable() {

            public void run() {
                MessageDialog.openInformation(display.getShells()[0], title, message);
            }
        });
    }

    public static void showInformationDialog(final String message) {
        final Display display = plugin.getWorkbench().getDisplay();
        display.asyncExec(new Runnable() {

            public void run() {
                MessageDialog.openInformation(display.getShells()[0], "ProductLineDesigner", message);
            }
        });
    }

    public static void showErrorDialog(final String title, final String message) {
        final Display display = plugin.getWorkbench().getDisplay();
        display.asyncExec(new Runnable() {

            public void run() {
                MessageDialog.openError(display.getShells()[0], title, message);
            }
        });
    }

    public static void showErrorDialog(final String message) {
        final Display display = plugin.getWorkbench().getDisplay();
        display.asyncExec(new Runnable() {

            public void run() {
                MessageDialog.openError(display.getShells()[0], "ProductLineDesigner", message);
            }
        });
    }
}
