package net.sf.jmoney;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;

/**
 * The "main program" for JMoney RCP.
 * 
 * @author Nigel Westbury
 * @author Johann Gyger
 */
public class JMoneyApplication implements IApplication {

    public Object start(IApplicationContext context) throws Exception {
        Display display = PlatformUI.createDisplay();
        try {
            Shell shell = new Shell(display, SWT.ON_TOP);
            try {
                initializeInstanceLocation(shell);
            } finally {
                shell.dispose();
            }
            WorkbenchAdvisor workbenchAdvisor = new JMoneyWorkbenchAdvisor();
            int returnCode = PlatformUI.createAndRunWorkbench(display, workbenchAdvisor);
            if (returnCode == PlatformUI.RETURN_RESTART) {
                return IApplication.EXIT_RESTART;
            } else {
                return IApplication.EXIT_OK;
            }
        } finally {
            if (display != null) display.dispose();
        }
    }

    public void stop() {
    }

    private void initializeInstanceLocation(Shell shell) {
        Location instanceLoc = Platform.getInstanceLocation();
        if (instanceLoc.isSet()) {
            return;
        }
        String os = Platform.getOS();
        String pathname = null;
        if (Platform.OS_WIN32.equals(os)) {
            pathname = "Application Data" + File.separator + "JMoney";
        } else if (Platform.OS_MACOSX.equals(os)) {
            pathname = "Library" + File.separator + "JMoney";
        } else if (Platform.OS_LINUX.equals(os)) {
            pathname = ".jmoney";
        } else {
            return;
        }
        pathname = System.getProperty("user.home") + File.separator + pathname;
        File workspace = new File(pathname);
        if (!workspace.exists()) workspace.mkdir();
        pathname = workspace.getAbsolutePath().replace(File.separatorChar, '/');
        try {
            URL url = new URL("file", null, pathname);
            instanceLoc.setURL(url, false);
        } catch (MalformedURLException e) {
            MessageDialog.openError(shell, "Invalid Workspace", "Invalid pathname for JMoney workspace: " + pathname);
        }
    }
}
