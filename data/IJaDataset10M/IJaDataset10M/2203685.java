package de.janbusch.jhashpassword.gui;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Canvas;

public class BackgroundActionDialog extends Dialog {

    protected Object result;

    protected Shell shlTitle;

    private String text;

    private String message;

    /**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
    public BackgroundActionDialog(Shell parent, int style, String text, String message) {
        super(parent, style);
        this.text = text;
        this.message = message;
        setText(text);
    }

    private void setMessage(String message) {
    }

    /**
	 * Open the dialog.
	 * @return the result
	 */
    public Object open() {
        createContents();
        shlTitle.open();
        shlTitle.layout();
        Display display = getParent().getDisplay();
        while (!shlTitle.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        return result;
    }

    /**
	 * Create contents of the dialog.
	 */
    private void createContents() {
        shlTitle = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shlTitle.setSize(450, 300);
        shlTitle.setText(text);
        Label lblNewLabel = new Label(shlTitle, SWT.NONE);
        lblNewLabel.setBounds(10, 10, 428, 135);
        lblNewLabel.setText(message);
        final Image loadingImg = SWTResourceManager.getImage(SyncDialog.class, "/de/janbusch/jhashpassword/images/snake_transparent.gif");
        Canvas canvas = new Canvas(shlTitle, SWT.NO_BACKGROUND | SWT.NO_FOCUS);
        canvas.setBounds(196, 196, 64, 64);
        canvas.addPaintListener(new PaintListener() {

            @Override
            public void paintControl(PaintEvent e) {
                e.gc.drawImage(loadingImg, 0, 0);
            }
        });
    }
}
