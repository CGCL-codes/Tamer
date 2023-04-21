package org.sf.pkb.gui.importpkb;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class DialogViewContent extends Dialog {

    private Shell m_dlgShell;

    private StyledText m_styledText = null;

    private String m_content = "";

    private Control m_cover_control = null;

    public DialogViewContent(Shell shell) {
        super(shell, SWT.DIALOG_TRIM | SWT.MODELESS);
    }

    /**
	 * Opens the dialog box
	 */
    public void open(Control coverControl, String subject, String content) {
        m_cover_control = coverControl;
        m_dlgShell = new Shell(getParent(), SWT.TITLE | SWT.PRIMARY_MODAL | SWT.DIALOG_TRIM);
        if (m_cover_control != null) {
            m_dlgShell.setLocation(m_cover_control.toDisplay(-2, -47));
            m_dlgShell.setSize(m_cover_control.getSize().x, m_cover_control.getSize().y + 90);
        } else {
            m_dlgShell.setLocation(100, 100);
            m_dlgShell.setSize(500, 400);
        }
        m_dlgShell.setText(subject);
        m_content = content;
        createContents(m_dlgShell);
        m_dlgShell.layout();
        m_dlgShell.open();
        Display display = getParent().getDisplay();
        while (!m_dlgShell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    public void forceActive() {
        m_dlgShell.forceActive();
    }

    protected void createContents(final Shell shell) {
        shell.setLayout(new FormLayout());
        FormData data = null;
        int rowDistance = 2;
        int colDistance = 2;
        m_styledText = new StyledText(shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        m_styledText.setEditable(false);
        data = new FormData();
        data.top = new FormAttachment(0, rowDistance);
        data.left = new FormAttachment(0, colDistance);
        data.right = new FormAttachment(100, -colDistance);
        data.bottom = new FormAttachment(100, -rowDistance);
        m_styledText.setLayoutData(data);
        m_styledText.setText(m_content);
    }
}
