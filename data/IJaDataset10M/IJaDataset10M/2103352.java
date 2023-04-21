package nl.utwente.ewi.hmi.deira.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class AboutDialog extends org.eclipse.swt.widgets.Dialog implements SelectionListener {

    private Shell dialogShell;

    private Label aboutLabel;

    private Label label1;

    private Button ok;

    public AboutDialog(Shell parent, int style) {
        super(parent, style);
    }

    public void open() {
        try {
            Shell parent = getParent();
            dialogShell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
            {
                SWTResourceManager.registerResourceUser(dialogShell);
            }
            FormLayout dialogShellLayout = new FormLayout();
            dialogShell.setLayout(dialogShellLayout);
            dialogShell.layout();
            dialogShell.pack();
            dialogShell.setSize(494, 247);
            dialogShell.setBackground(SWTResourceManager.getColor(255, 255, 255));
            dialogShell.setForeground(SWTResourceManager.getColor(214, 214, 214));
            {
                ok = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
                FormData okLData = new FormData();
                okLData.width = 71;
                okLData.height = 23;
                okLData.left = new FormAttachment(0, 1000, 282);
                okLData.top = new FormAttachment(0, 1000, 185);
                ok.setLayoutData(okLData);
                ok.setText("Close");
                ok.addSelectionListener(this);
            }
            {
                label1 = new Label(dialogShell, SWT.NONE);
                FormData label1LData = new FormData();
                label1LData.width = 126;
                label1LData.height = 196;
                label1LData.left = new FormAttachment(0, 1000, 22);
                label1LData.top = new FormAttachment(0, 1000, 12);
                label1.setLayoutData(label1LData);
                label1.setImage(new Image(Display.getDefault(), "Horsie_smaller.png"));
                label1.setBackground(SWTResourceManager.getColor(255, 255, 255));
            }
            {
                FormData imageContainerLData = new FormData();
                imageContainerLData.width = 0;
                imageContainerLData.height = 13;
                imageContainerLData.left = new FormAttachment(0, 1000, 105);
                imageContainerLData.top = new FormAttachment(0, 1000, 68);
            }
            {
                aboutLabel = new Label(dialogShell, SWT.CENTER);
                FormData aboutLabelLData = new FormData();
                aboutLabelLData.width = 314;
                aboutLabelLData.height = 128;
                aboutLabelLData.left = new FormAttachment(0, 1000, 160);
                aboutLabelLData.top = new FormAttachment(0, 1000, 31);
                aboutLabel.setLayoutData(aboutLabelLData);
                aboutLabel.setText("DEIRA GUI v1.1\n Fran�ois L.A. Knoppel\n\n DEIRA: Dynamic Engaging Intelligent Reporter Agent\n Fran�ois L.A. Knoppel\n Almer S. Tigelaar \n Danny Oude Bos \n Thijs Alofs");
                aboutLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
                aboutLabel.setFont(SWTResourceManager.getFont("Tahoma", 9, 0, false, false));
            }
            dialogShell.setLocation(getParent().toDisplay(100, 100));
            dialogShell.open();
            Display display = dialogShell.getDisplay();
            while (!dialogShell.isDisposed()) {
                if (!display.readAndDispatch()) display.sleep();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent arg0) {
        widgetSelected(arg0);
    }

    @Override
    public void widgetSelected(SelectionEvent arg0) {
        dialogShell.dispose();
    }
}
