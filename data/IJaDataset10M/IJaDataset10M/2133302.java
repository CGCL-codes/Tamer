package vbullmin.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;

/**
 * Liddle splash for vbullmin
 * @author Onur Aslan
 */
public class Splash {

    /** Shell for Splash */
    private Shell shell;

    /** Splash image */
    private Image image;

    /** Constructor opening splash */
    public Splash() {
        open();
    }

    public void open() {
        shell = new Shell(GUI.display, SWT.NO_TRIM);
        image = new Image(GUI.display, "images/emblem-system.png");
        ImageData imagedata = image.getImageData();
        shell.setSize(imagedata.width, imagedata.height);
        shell.setText("vbullmin loading...");
        Rectangle r = GUI.display.getBounds();
        int shellX = (r.width - imagedata.width) / 2;
        int shellY = (r.height - imagedata.height) / 2;
        shell.setLocation(shellX, shellY);
        shell.open();
        GC gc = new GC(shell);
        gc.drawImage(image, 0, 0);
    }

    public void close() {
        image.dispose();
        shell.dispose();
    }
}
