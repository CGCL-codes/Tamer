package kenwudi.java.msn;

import org.eclipse.swt.widgets.Shell;

public class ShellInOtherPosition {

    ShellInOtherPosition(Shell shell, int offWidth, int offHeight) {
        int screenH = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
        int screenW = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
        int shellH = shell.getBounds().height;
        int shellW = shell.getBounds().width;
        if (shellH > screenH) shellH = screenH;
        if (shellW > screenW) shellW = screenW;
        shell.setLocation(((screenW - shellW) / offWidth), ((screenH - shellH) / offHeight));
    }
}
