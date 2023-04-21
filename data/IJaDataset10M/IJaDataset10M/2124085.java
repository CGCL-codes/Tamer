package org.inigma.iniglet.about;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.inigma.iniglet.utils.DynamicListener;

public class AboutWindow {

    private static final String FONT_FAMILY = "Arial";

    private static Log logger = LogFactory.getLog(AboutWindow.class);

    private Shell shell;

    private AboutData data;

    public AboutWindow(Display display, AboutData data) {
        this.data = data;
        shell = new Shell(display, SWT.BORDER | SWT.CLOSE);
        shell.setLayout(new GridLayout(3, false));
        shell.setText("About " + data.getName());
        shell.setImage(data.getIcon());
        int width = 250;
        GridData gridData = null;
        Image image = data.getImage();
        if (image != null) {
            Canvas canvas = new Canvas(shell, SWT.NONE);
            canvas.addPaintListener(new AboutImage(image));
            gridData = new GridData(SWT.CENTER, SWT.FILL, true, true, 3, 1);
            gridData.minimumHeight = image.getBounds().height;
            gridData.minimumWidth = image.getBounds().width;
            canvas.setLayoutData(gridData);
            if (gridData.minimumWidth > width) {
                width = gridData.minimumWidth;
            }
        }
        Label name = new Label(shell, SWT.SHADOW_OUT | SWT.CENTER);
        name.setText(data.getName() + " " + data.getVerison());
        name.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 3, 1));
        name.setFont(new Font(display, FONT_FAMILY, 16, SWT.BOLD));
        if (data.getDescription() != null) {
            Label description = new Label(shell, SWT.CENTER | SWT.WRAP);
            description.setText(data.getDescription());
            GridData layoutData = new GridData(SWT.CENTER, SWT.CENTER, true, true, 3, 1);
            layoutData.widthHint = width;
            description.setLayoutData(layoutData);
        }
        if (data.getCopyright() != null) {
            Label copyright = new Label(shell, SWT.CENTER);
            copyright.setText("Copyright © " + data.getCopyright());
            copyright.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, true, 3, 1));
            copyright.setFont(new Font(display, FONT_FAMILY, 10, SWT.NORMAL));
        }
        if (data.getUrl() != null) {
            Button url = new Button(shell, SWT.PUSH);
            url.setText(data.getUrl());
            url.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false, 3, 1));
            url.addListener(SWT.Selection, new DynamicListener(this, "handleEventUrl"));
        }
        if (data.getCredits().size() > 0) {
            Button credits = new Button(shell, SWT.PUSH);
            credits.setText("Credits");
            gridData = new GridData(SWT.CENTER, SWT.FILL, true, true, 1, 1);
            gridData.minimumWidth = width / 4;
            credits.setLayoutData(gridData);
            credits.addListener(SWT.Selection, new DynamicListener(this, "handleEventCredits"));
        }
        if (data.getLicense() != null) {
            Button license = new Button(shell, SWT.PUSH);
            license.setText("License");
            gridData = new GridData(SWT.CENTER, SWT.FILL, true, true, 1, 1);
            gridData.minimumWidth = width / 4;
            license.setLayoutData(gridData);
            license.addListener(SWT.Selection, new DynamicListener(this, "handleEventLicense"));
        }
        Button ok = new Button(shell, SWT.PUSH);
        ok.setText("Close");
        gridData = new GridData(SWT.CENTER, SWT.FILL, true, true, 1, 1);
        gridData.minimumWidth = width / 4;
        ok.setLayoutData(gridData);
        DynamicListener close = new DynamicListener(this, "handleEventClose");
        ok.addListener(SWT.Selection, close);
        shell.addListener(SWT.Close, close);
        shell.pack();
    }

    public void setVisible(boolean visible) {
        shell.setVisible(visible);
    }

    @SuppressWarnings("unused")
    private void handleEventUrl(Event event) {
        try {
            Browser browser = new Browser(shell, SWT.NONE);
            browser.setUrl(data.getUrl());
        } catch (SWTError e) {
            logger.warn("System does not support a default browser!", e);
            MessageBox box = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK | SWT.APPLICATION_MODAL);
            box.setText("Browser Error");
            box.setMessage("Default brower support not setup!\n" + e.getMessage());
            box.open();
        }
    }

    @SuppressWarnings("unused")
    private void handleEventCredits(Event event) {
        CreditWindow window = new CreditWindow(data);
    }

    @SuppressWarnings("unused")
    private void handleEventLicense(Event event) {
        LicenseWindow window = new LicenseWindow(data);
    }

    @SuppressWarnings("unused")
    private void handleEventClose(Event event) {
        shell.setVisible(false);
        event.doit = false;
    }
}
