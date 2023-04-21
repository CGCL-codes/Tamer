package net.mogray.quezen.ui.view.views;

import net.mogray.quezen.core.QZ;
import net.mogray.quezen.core.QueueServer;
import net.mogray.quezen.ui.view.toolbars.QZConsoleToolbar;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class QZConsoleView extends QZView {

    private Shell sh;

    private QZConsoleToolbar tbMain;

    private Text txtConsole;

    private Color bgColor;

    private Label lblConsole;

    private String sStatus = new String("");

    private int iLines = 0;

    public QZConsoleView(Shell shMain) {
        super(shMain, SWT.BORDER);
        sh = shMain;
        LoadUI();
        LoadLayout();
    }

    public QZConsoleView() {
        super(QZ.shMain, SWT.BORDER);
        sh = QZ.shMain;
        LoadUI();
        LoadLayout();
    }

    protected void LoadUI() {
        tbMain = new QZConsoleToolbar(this);
        txtConsole = new Text(this, SWT.WRAP | SWT.READ_ONLY | SWT.V_SCROLL | SWT.BORDER);
        bgColor = sh.getDisplay().getSystemColor(SWT.COLOR_WHITE);
        try {
            txtConsole.setBackground(bgColor);
        } catch (IllegalArgumentException iae) {
            QZ.LogMessage(iae.getMessage());
        }
        lblConsole = new Label(this, SWT.NONE);
        lblConsole.setText(QZ.PHRASES.getPhrase("65") + sStatus.toString());
    }

    protected void LoadLayout() {
        this.setLayout(new FormLayout());
        FormData tbdata = new FormData();
        tbdata.right = new FormAttachment(100, 0);
        tbMain.setLayoutDat(tbdata);
        FormData txtdata = new FormData();
        txtdata.top = new FormAttachment(tbMain.getToolbar(), 0);
        txtdata.right = new FormAttachment(100, -3);
        txtdata.left = new FormAttachment(0, 0);
        txtdata.bottom = new FormAttachment(100, -3);
        txtConsole.setLayoutData(txtdata);
        FormData lblConsoledata = new FormData();
        lblConsoledata.top = new FormAttachment(0, 3);
        lblConsoledata.left = new FormAttachment(0, 3);
        lblConsole.setLayoutData(lblConsoledata);
    }

    public void ClearConsole() {
        txtConsole.setText("");
        iLines = 0;
    }

    public void AppendMsg(String sMsg) {
        try {
            if (iLines > 0) {
                txtConsole.append("\n" + sMsg);
            } else {
                txtConsole.append(sMsg);
            }
            iLines++;
        } catch (Exception e) {
            System.err.println(sMsg);
        }
    }

    public void setStatus(String sMsg) {
        sStatus = sMsg;
        lblConsole.setText(QZ.PHRASES.getPhrase("65") + sStatus.toString());
    }

    public void QZDispose() {
        tbMain.QZDispose();
        if (!txtConsole.isDisposed()) {
            txtConsole.dispose();
        }
        if (!bgColor.isDisposed()) {
            bgColor.dispose();
        }
        if (!this.isDisposed()) {
            this.dispose();
        }
    }

    public QueueServer getQueueServer() {
        return null;
    }

    public void setQueueServer(QueueServer queueServer) {
    }
}
