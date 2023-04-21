package swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class Browser {

    public String run(Display display) {
        Shell shell = new Shell(display);
        FileDialog dialog = new FileDialog(shell, SWT.NULL);
        String[] extension = { "*.xml" };
        dialog.setFilterExtensions(extension);
        String path = dialog.open();
        return path;
    }
}
