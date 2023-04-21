package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet92 {

    public static void main(String[] args) {
        Display display = new Display();
        Color white = display.getSystemColor(SWT.COLOR_WHITE);
        Color black = display.getSystemColor(SWT.COLOR_BLACK);
        PaletteData palette = new PaletteData(new RGB[] { white.getRGB(), black.getRGB() });
        ImageData sourceData = new ImageData(20, 20, 1, palette);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 20; j++) {
                sourceData.setPixel(i, j, 1);
            }
        }
        palette = new PaletteData(new RGB[] { white.getRGB(), black.getRGB() });
        ImageData maskData = new ImageData(20, 20, 1, palette);
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                maskData.setPixel(i, j, 1);
            }
        }
        Cursor cursor = new Cursor(display, sourceData, maskData, 10, 10);
        Shell shell = new Shell(display);
        final Image source = new Image(display, sourceData);
        final Image mask = new Image(display, maskData);
        shell.addPaintListener(new PaintListener() {

            public void paintControl(PaintEvent e) {
                GC gc = e.gc;
                gc.drawString("source: ", 10, 10);
                gc.drawImage(source, 0, 0, 20, 20, 60, 10, 20, 20);
                gc.drawString("mask: ", 10, 40);
                gc.drawImage(mask, 0, 0, 20, 20, 60, 40, 20, 20);
            }
        });
        shell.setSize(150, 150);
        shell.open();
        shell.setCursor(cursor);
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        cursor.dispose();
        source.dispose();
        mask.dispose();
        display.dispose();
    }
}
