package prove;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public final class FadePanel {

    static Display display;

    static Shell shell;

    static int alpha;

    static Image image;

    static Rectangle imageBounds;

    public static void main(String[] args) {
        display = new Display();
        shell = new Shell(display, SWT.NO_TRIM | SWT.ON_TOP);
        Composite composite = new Composite(shell, SWT.NONE);
        composite.setLayout(new GridLayout(2, false));
        new Label(composite, SWT.NONE).setText("Name:");
        new Text(composite, SWT.SINGLE | SWT.BORDER);
        new Label(composite, SWT.NONE).setText("Address:");
        new Text(composite, SWT.SINGLE | SWT.BORDER);
        Button button = new Button(composite, SWT.PUSH);
        button.setText("Fade Away");
        button.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1));
        final StackLayout stackLayout = new StackLayout();
        shell.setLayout(stackLayout);
        stackLayout.topControl = composite;
        final Canvas canvas = new Canvas(shell, SWT.DOUBLE_BUFFERED);
        canvas.addPaintListener(new PaintListener() {

            public void paintControl(final PaintEvent e) {
                e.gc.setAlpha(alpha);
                e.gc.drawImage(image, 0, 0);
            }
        });
        button.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                imageBounds = shell.getClientArea();
                image = new Image(display, imageBounds.width, imageBounds.height);
                GC gc = new GC(shell);
                gc.copyArea(image, 0, 0);
                gc.dispose();
                stackLayout.topControl = canvas;
                shell.layout();
                alpha = 100;
                while (alpha > 0) {
                    canvas.redraw();
                    canvas.update();
                    try {
                        Thread.sleep(10);
                    } catch (final InterruptedException ex) {
                    }
                    alpha--;
                }
                shell.close();
            }
        });
        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        image.dispose();
        display.dispose();
    }
}
