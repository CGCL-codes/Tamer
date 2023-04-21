package externalSources;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;

public class Snippet9 {

    public static void main(String[] args) {
        Display display = new Display();
        final Shell shell = new Shell(display, SWT.SHELL_TRIM | SWT.H_SCROLL | SWT.V_SCROLL);
        final Composite composite = new Composite(shell, SWT.BORDER);
        composite.setSize(200, 400);
        final ScrollBar hBar = shell.getHorizontalBar();
        hBar.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                Point location = composite.getLocation();
                location.x = -hBar.getSelection();
                composite.setLocation(location);
            }
        });
        final ScrollBar vBar = shell.getVerticalBar();
        vBar.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                Point location = composite.getLocation();
                location.y = -vBar.getSelection();
                composite.setLocation(location);
            }
        });
        shell.addListener(SWT.Resize, new Listener() {

            public void handleEvent(Event e) {
                Point size = composite.getSize();
                Rectangle rect = shell.getClientArea();
                hBar.setMaximum(size.x);
                vBar.setMaximum(size.y);
                hBar.setThumb(Math.min(size.x, rect.width));
                vBar.setThumb(Math.min(size.y, rect.height));
                int hPage = size.x - rect.width;
                int vPage = size.y - rect.height;
                int hSelection = hBar.getSelection();
                int vSelection = vBar.getSelection();
                Point location = composite.getLocation();
                if (hSelection >= hPage) {
                    if (hPage <= 0) hSelection = 0;
                    location.x = -hSelection;
                }
                if (vSelection >= vPage) {
                    if (vPage <= 0) vSelection = 0;
                    location.y = -vSelection;
                }
                composite.setLocation(location);
            }
        });
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }
}
