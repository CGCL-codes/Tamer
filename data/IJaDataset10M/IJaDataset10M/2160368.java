package net.mogray.infinitypfm.ui.view.views;

import net.mogray.infinitypfm.client.InfinityPfm;
import net.mogray.infinitypfm.core.conf.MM;
import net.mogray.infinitypfm.ui.view.toolbars.ConsoleToolbar;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ConsoleView extends BaseView {

    private Shell sh;

    private ConsoleToolbar tbMain;

    private Text txtConsole;

    private Color bgColor;

    private Label lblConsole;

    private String sStatus = "";

    private int iLines = 0;

    public ConsoleView(Shell shMain) {
        super(shMain, SWT.BORDER);
        sh = shMain;
        LoadUI();
        LoadLayout();
    }

    public ConsoleView() {
        super(InfinityPfm.shMain, SWT.BORDER);
        sh = InfinityPfm.shMain;
        LoadUI();
        LoadLayout();
    }

    protected void LoadUI() {
        tbMain = new ConsoleToolbar(this);
        txtConsole = new Text(this, SWT.WRAP | SWT.READ_ONLY | SWT.V_SCROLL | SWT.BORDER);
        bgColor = sh.getDisplay().getSystemColor(SWT.COLOR_WHITE);
        try {
            txtConsole.setBackground(bgColor);
        } catch (IllegalArgumentException iae) {
            InfinityPfm.LogMessage(iae.getMessage());
        }
        lblConsole = new Label(this, SWT.NONE);
        lblConsole.setText(MM.PHRASES.getPhrase("65") + sStatus.toString());
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
        lblConsole.setText(MM.PHRASES.getPhrase("65") + sStatus.toString());
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
}
