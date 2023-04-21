package com.peterhi.ui.controls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import com.peterhi.resource.ResourceManager;
import com.peterhi.ui.Toolkit;

public class CommonControlsTest {

    private static final Class type = CommonControlsTest.class;

    private static final String APP_NAME = "Common Controls Test";

    private static Display display;

    private static XShell shell;

    private static XLabel staticLabel;

    private static XLabel dynamicLabel;

    private static XText styledText;

    private static XFlip xFlip;

    private static Label flipLabel;

    private static Text flipText;

    private static Button flipButton;

    private static Button dialogButton;

    public static void main(String[] args) throws Exception {
        Display.setAppName(APP_NAME);
        display = Display.getDefault();
        shell = new XShell(display);
        shell.setImage(ResourceManager.getImage("ui.png"));
        shell.addListener(SWT.Dispose, new Listener() {

            public void handleEvent(Event e) {
                ResourceManager.returnImage("ui.png");
            }
        });
        initShell(shell);
        shell.open();
        shell.getContentPanel().layout();
        while (!shell.isDisposed()) if (!display.readAndDispatch()) display.sleep();
        display.dispose();
    }

    private static void initShell(final XShell shell) {
        shell.setText(APP_NAME);
        shell.setSize(640, 480);
        staticLabel = new XLabel(shell.getContentPanel(), SWT.NONE);
        dynamicLabel = new XLabel(shell.getContentPanel(), SWT.NONE);
        styledText = new XText(shell.getContentPanel(), SWT.BORDER);
        xFlip = new XFlip(shell.getContentPanel(), SWT.NONE);
        flipLabel = new Label(xFlip, SWT.NONE);
        flipText = new Text(xFlip, SWT.BORDER);
        flipButton = new Button(xFlip, SWT.NONE);
        dialogButton = new Button(shell.getContentPanel(), SWT.NONE);
        staticLabel.setText("Static label with image.");
        dynamicLabel.setText("Dynamic label with image.");
        styledText.setText("Styled Text!");
        flipLabel.setText("Flip Label");
        flipText.setText("Flip Text");
        flipButton.setText("Flip Button");
        dialogButton.setText("Dialog");
        staticLabel.setProgress(50);
        dynamicLabel.setProgress(60);
        styledText.setProgress(70);
        staticLabel.setImage(ResourceManager.getImage("ok.png"));
        staticLabel.addListener(SWT.Dispose, new Listener() {

            public void handleEvent(Event e) {
                ResourceManager.returnImage("ok.png");
            }
        });
        final ImageLoader loader = ResourceManager.getPlatformWaitImage();
        dynamicLabel.setAnimatedImage(loader);
        dynamicLabel.addListener(SWT.Dispose, new Listener() {

            public void handleEvent(Event e) {
                ResourceManager.returnImageData(loader);
            }
        });
        dialogButton.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                XDialog d = new XDialog(shell, SWT.APPLICATION_MODAL | SWT.SHEET);
                Toolkit.center(shell, d);
                d.setProgress(50);
                d.open();
                System.out.println("Done!");
            }
        });
        GridData data = new GridData();
        data.widthHint = 320;
        data.heightHint = 160;
        xFlip.setLayoutData(data);
        xFlip.setInterval(3000);
    }
}
