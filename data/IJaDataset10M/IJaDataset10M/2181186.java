package com.tscribble.bitleech.ui.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import com.tscribble.bitleech.core.download.DownloadManager;
import com.tscribble.bitleech.ui.util.SWTUtil;

public class URLDlg extends Dialog {

    private String url;

    private Shell shell;

    private DownloadManager man;

    public URLDlg(Shell parent) {
        super(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
    }

    public String open() {
        man = DownloadManager.getInst();
        createContents();
        SWTUtil.open(shell, true);
        return url;
    }

    private void createContents() {
        shell = new Shell(getParent(), getStyle());
        shell.setText("Enter URL");
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        shell.setLayout(gridLayout);
        new Label(shell, SWT.NONE).setText("URL:");
        final Combo cbUrl = new Combo(shell, SWT.NONE);
        cbUrl.setLayoutData(new GridData(400, SWT.DEFAULT));
        cbUrl.add("http://cdimage.ubuntu.com/cdimage/daily-live/current/jaunty-desktop-i386.iso");
        cbUrl.add("http://www.limewire.com/LimeWireWinBoth");
        cbUrl.add("http://ufpr.dl.sourceforge.net/sourceforge/azureus/Vuze_4.0.0.4b_windows.exe");
        cbUrl.setText(SWTUtil.getClipboardText().trim());
        Button cmdOk = new Button(shell, SWT.NONE);
        cmdOk.setLayoutData(new GridData(75, SWT.DEFAULT));
        cmdOk.setText("OK");
        shell.setDefaultButton(cmdOk);
        cmdOk.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                url = cbUrl.getText().trim();
                url = (url != null && (url.length() > 0)) ? url : null;
                shell.close();
            }
        });
    }
}
