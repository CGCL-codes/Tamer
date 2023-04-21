package org.eclipse.swt.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet281 {

    public static void main(String[] args) {
        final Display display = new Display();
        final Shell shell = new Shell(display, SWT.SHELL_TRIM | SWT.NO_BACKGROUND);
        shell.setLayout(new FillLayout());
        shell.addListener(SWT.Paint, new Listener() {

            public void handleEvent(Event e) {
                GC gc = e.gc;
                Rectangle rect = shell.getClientArea();
                Color color1 = new Color(display, 234, 246, 253);
                Color color2 = new Color(display, 217, 240, 252);
                Color color3 = new Color(display, 190, 230, 253);
                Color color4 = new Color(display, 167, 217, 245);
                Pattern p1 = new Pattern(display, 0, 0, 0, rect.height / 2, color1, color2);
                gc.setBackgroundPattern(p1);
                gc.fillRectangle(rect.x, rect.y, rect.width, rect.height / 2);
                Pattern p2 = new Pattern(display, 0, rect.height / 2, 0, rect.height, color3, color4);
                gc.setBackgroundPattern(p2);
                gc.fillRectangle(rect.x, rect.y + (rect.height / 2), rect.width, rect.height / 2 + 1);
                p1.dispose();
                p2.dispose();
                color1.dispose();
                color2.dispose();
                color3.dispose();
                color4.dispose();
            }
        });
        shell.setSize(200, 64);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }
}
