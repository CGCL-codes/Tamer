package com.eric.simms.view.components;

import java.io.IOException;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IconAndMessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import com.eric.simms.common.util.ResourceUtil;
import com.eric.simms.view.nls.ViewMessages;

public class AboutDialog extends IconAndMessageDialog {

    public static final int DEFAULT_WIDTH = 300;

    public static final int DEFAULT_HEIGHT = 200;

    public static final int LABEL_WIDTH = 45;

    public static final int DEFAULT_GAP = 5;

    public static final int LEFT_GAP = 5;

    public static final int RIGHT_GAP = 5;

    private Image icon;

    private Image image;

    private Font font;

    public AboutDialog(Shell parent) {
        super(parent);
    }

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        icon = ResourceUtil.getImage(Display.getCurrent(), "about.gif");
        shell.setImage(icon);
        shell.setText(ViewMessages.get(ViewMessages.Dialog_About));
        Shell parent = (Shell) shell.getParent();
        Rectangle displayBounds = parent != null ? parent.getBounds() : Display.getCurrent().getPrimaryMonitor().getBounds();
        int w = displayBounds.width < DEFAULT_WIDTH ? displayBounds.width : DEFAULT_WIDTH;
        int h = displayBounds.height < DEFAULT_HEIGHT ? displayBounds.height : DEFAULT_HEIGHT;
        shell.setSize(w, h);
        Rectangle shellBounds = shell.getBounds();
        int x = displayBounds.x + (displayBounds.width - shellBounds.width) >> 1;
        int y = displayBounds.y + (displayBounds.height - shellBounds.height) >> 1;
        shell.setLocation(x, y);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        createMessageArea(parent);
        Composite composite = new Composite(parent, SWT.NONE);
        GridData data = new GridData(GridData.FILL_BOTH);
        data.horizontalSpan = 2;
        composite.setLayoutData(data);
        GridLayout layout = new GridLayout(1, true);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.verticalSpacing = 6;
        composite.setLayout(layout);
        FontData[] fontDatas = parent.getFont().getFontData();
        FontData fontData = fontDatas[0];
        fontData.setHeight(12);
        fontData.setStyle(SWT.BOLD);
        font = new Font(Display.getCurrent(), fontData);
        CLabel appTitle = new CLabel(composite, SWT.CENTER | SWT.BOLD);
        appTitle.setText(ViewMessages.get(ViewMessages.App_Title));
        appTitle.setFont(font);
        GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
        appTitle.setLayoutData(layoutData);
        CLabel version = new CLabel(composite, SWT.BOLD | SWT.CENTER);
        version.setText(ViewMessages.get(ViewMessages.App_Version));
        version.setFont(parent.getFont());
        layoutData = new GridData(GridData.FILL_HORIZONTAL);
        version.setLayoutData(layoutData);
        Composite authorLine = new Composite(composite, SWT.NONE);
        layoutData = new GridData(GridData.FILL_HORIZONTAL);
        authorLine.setLayoutData(layoutData);
        FormLayout fLayout = new FormLayout();
        authorLine.setLayout(fLayout);
        CLabel authorLabel = new CLabel(authorLine, SWT.NONE);
        authorLabel.setText(ViewMessages.get(ViewMessages.Dialog_About_Author_Label));
        authorLabel.setAlignment(SWT.RIGHT);
        FormData fData = new FormData();
        fData.left = new FormAttachment(0, LEFT_GAP);
        fData.width = LABEL_WIDTH;
        authorLabel.setLayoutData(fData);
        CLabel authorName = new CLabel(authorLine, SWT.NONE);
        authorName.setText(ViewMessages.get(ViewMessages.Author));
        authorName.setAlignment(SWT.LEFT);
        fData = new FormData();
        fData.left = new FormAttachment(authorLabel, DEFAULT_GAP);
        fData.right = new FormAttachment(100, 0 - RIGHT_GAP);
        authorName.setLayoutData(fData);
        Composite mailLine = new Composite(composite, SWT.NONE);
        layoutData = new GridData(GridData.FILL_HORIZONTAL);
        mailLine.setLayoutData(layoutData);
        fLayout = new FormLayout();
        mailLine.setLayout(fLayout);
        Link mail = new Link(mailLine, SWT.NONE);
        mail.setText(ViewMessages.get(ViewMessages.Dialog_About_Author_Mail));
        mail.addSelectionListener(new SIMMSMailLinkListener());
        fData = new FormData();
        fData.left = new FormAttachment(0, LEFT_GAP + LABEL_WIDTH + RIGHT_GAP);
        fData.right = new FormAttachment(100, 0 - RIGHT_GAP);
        mail.setLayoutData(fData);
        return composite;
    }

    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, ViewMessages.get(ViewMessages.Dialog_OK_Button), true);
    }

    @Override
    protected Image getImage() {
        return image;
    }

    public void setMessage(String msg) {
        this.message = msg;
    }

    @Override
    public boolean close() {
        if (image != null) {
            image.dispose();
        }
        if (icon != null) {
            icon.dispose();
        }
        if (font != null) {
            font.dispose();
        }
        return super.close();
    }

    private class SIMMSMailLinkListener implements SelectionListener {

        @Override
        public void widgetDefaultSelected(SelectionEvent e) {
        }

        @Override
        public void widgetSelected(SelectionEvent e) {
            try {
                Runtime.getRuntime().exec(ViewMessages.get(ViewMessages.MailTo_Author));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
