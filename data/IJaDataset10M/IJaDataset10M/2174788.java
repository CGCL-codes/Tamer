package vehikel.ide;

import java.io.FileNotFoundException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import vehikel.ide.actions.ActionEnabler;
import vehikel.ide.actions.Autorun;
import vehikel.ide.preferences.VehikelIdePreference;
import vehikel.util.Console;
import vehikel.util.OS;
import vehikel.util.VehikelPreference;

public class VehikelIdeApplication implements IApplication {

    private static Autorun autorun;

    private static VehikelIdeApplication application;

    private VehikelIdePreference vehikelIdePreference;

    public Object start(IApplicationContext context) throws Exception {
        application = this;
        Console.activate();
        OS.list();
        vehikelIdePreference = new VehikelIdePreference();
        vehikelIdePreference.load();
        new VehikelIdeInstaller().afterInstall();
        String[] applicationArgs = Platform.getCommandLineArgs();
        for (int ax = 0; ax < applicationArgs.length; ax++) {
            String arg = applicationArgs[ax];
            if (arg.equals("-unittesting")) {
                vehikel.testing.TestsRunner.runTests();
                break;
            }
        }
        autorun = new Autorun();
        new Thread(autorun).start();
        new ActionEnabler().start();
        Display display = PlatformUI.createDisplay();
        Console.console.startMonitor();
        try {
            int returnCode = PlatformUI.createAndRunWorkbench(display, new VehikelIdeWorkbenchAdvisor());
            if (returnCode == PlatformUI.RETURN_RESTART) {
                return IApplication.EXIT_RESTART;
            }
            return IApplication.EXIT_OK;
        } finally {
            display.dispose();
        }
    }

    public void stop() {
        final IWorkbench workbench = PlatformUI.getWorkbench();
        if (workbench == null) return;
        final Display display = workbench.getDisplay();
        display.syncExec(new Runnable() {

            public void run() {
                if (!display.isDisposed()) workbench.close();
            }
        });
        application = null;
    }

    /**
	 * Returns the shared preferences
	 * 
	 * @return "vehikel.properties"
	 */
    public static VehikelIdePreference getVehikelIdePreference() {
        return application != null ? application.vehikelIdePreference : null;
    }
}
