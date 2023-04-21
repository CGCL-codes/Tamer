package com.mw3d.swt.component;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import com.mw3d.swt.EditorGUI;
import com.mw3d.swt.util.AppProperties;

/**
 * @author Outrunner.
 * Created on Apr 27, 2005
 */
public class SplashScreen {

    private static Shell shell;

    private static Display display;

    public SplashScreen(Display dis) {
        display = dis;
        init();
    }

    public void init() {
        shell = new Shell(display, SWT.NO_TRIM | SWT.NO_BACKGROUND | SWT.ON_TOP);
        shell.setLayout(new FillLayout());
        Image splashImage = ImageDescriptor.createFromURL(SplashScreen.class.getClassLoader().getResource(AppProperties.PATH_LOGO)).createImage();
        Rectangle displayBounds = display.getPrimaryMonitor().getBounds();
        Rectangle imageBounds = splashImage.getBounds();
        shell.setBounds(displayBounds.x + ((displayBounds.width - imageBounds.width) / 2), displayBounds.y + ((displayBounds.height - imageBounds.height) / 2), imageBounds.width, imageBounds.height);
        shell.open();
        shell.update();
        update("Starting up");
    }

    public static void update(String text) {
        if (shell == null || shell.isDisposed()) return;
        Image image = new Image(shell.getDisplay(), shell.getBounds());
        GC gc = new GC(image);
        gc.drawImage(ImageDescriptor.createFromURL(SplashScreen.class.getClassLoader().getResource(AppProperties.PATH_LOGO)).createImage(), 0, 0);
        gc.setForeground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
        gc.drawText(text + "...", 15, shell.getBounds().height - 25, true);
        GC shellGc = new GC(shell);
        shellGc.drawImage(image, 0, 0);
        shellGc.dispose();
        image.dispose();
        gc.dispose();
    }

    public static void dispose() {
        if (shell == null || shell.isDisposed()) return;
        shell.dispose();
        shell = null;
    }

    public static void setVisible(final boolean visible) {
        if (shell == null) return;
        shell.setVisible(visible);
    }
}
