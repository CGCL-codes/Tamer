package net.sourceforge.processdash.tool.quicklauncher;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import net.sourceforge.processdash.util.LightweightSet;

public class DropTransferHandler extends TransferHandler {

    QuickLauncher launcher;

    InstanceLauncherFactory launcherFactory;

    public DropTransferHandler(QuickLauncher launcher, InstanceLauncherFactory launcherFactory) {
        this.launcher = launcher;
        this.launcherFactory = launcherFactory;
    }

    public boolean canImport(JComponent comp, DataFlavor[] flavors) {
        for (int i = 0; i < flavors.length; i++) {
            if (DataFlavor.javaFileListFlavor.equals(flavors[i])) return true;
        }
        return false;
    }

    public boolean importData(JComponent comp, Transferable t) {
        List files = null;
        try {
            files = (List) t.getTransferData(DataFlavor.javaFileListFlavor);
        } catch (Exception ex) {
        }
        if (files == null || files.isEmpty()) return false;
        Set targets = new LightweightSet();
        for (Iterator i = files.iterator(); i.hasNext(); ) {
            File f = (File) i.next();
            if ("pspdash.jar".equals(f.getName())) {
                launcher.useDashboardJarFile(f);
                continue;
            }
            DashboardInstance l = launcherFactory.getLauncher(comp, f);
            if (l != null) targets.add(l);
        }
        launcher.launchInstances(targets);
        return true;
    }
}
