package yaw.core.swt.util;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SWTRuntime {

    private Display display;

    private Shell shell;

    private static SWTRuntime instance;

    public static SWTRuntime instance() {
        if (instance == null) instance = new SWTRuntime();
        return instance;
    }

    protected SWTRuntime() {
        this.display = new Display();
    }

    public Display getDisplay() {
        return this.display;
    }

    public Shell getShell() {
        if (this.shell == null) this.shell = new Shell(display);
        return shell;
    }

    public void messagepump() {
        while (display.getShells().length > (this.shell != null ? 1 : 0)) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }

    public void wait(Shell shell) {
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
    }
}
