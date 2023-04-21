package hu.scytha.gui.text;

import hu.scytha.common.Messages;
import hu.scytha.common.Util;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

/**
 * 'Set encoding' dialog.
 * 
 * @author Bertalan Lacza
 */
public class EncodingDialog extends Dialog {

    private String fEncoding;

    private Combo combo;

    private String[] fDefaultEncodings = new String[] { System.getProperty("file.encoding"), "ISO-8859-1", "US-ASCII", "UTF-8", "UTF-16", "UTF-16BE", "UTF-16LE" };

    public EncodingDialog(Shell shell) {
        this(shell, System.getProperty("file.encoding"));
    }

    /**
    * Constructor.
    * @param shell
    */
    public EncodingDialog(Shell shell, String pInitialEncoding) {
        super(shell);
        this.fEncoding = pInitialEncoding;
    }

    /**
    * Constructor.
    * @param shell
    */
    public EncodingDialog(Shell shell, String[] pEncodings) {
        this(shell, System.getProperty("file.encoding"));
        this.fDefaultEncodings = pEncodings;
    }

    protected Control createDialogArea(Composite parent) {
        getShell().setText(Messages.getString("TextViewer.set.encoding2"));
        getShell().setImage(Util.getImageRegistry().get("scytha"));
        Composite comp = new Composite(parent, SWT.NONE);
        GridLayout gl = new GridLayout();
        gl.numColumns = 2;
        gl.horizontalSpacing = 10;
        comp.setLayout(gl);
        Label enc = new Label(comp, SWT.NONE);
        enc.setText(Messages.getString("TextViewer.encoding") + ":");
        this.combo = new Combo(comp, SWT.NONE);
        this.combo.setText(this.fEncoding);
        for (int i = 0; i < fDefaultEncodings.length; i++) {
            this.combo.add(fDefaultEncodings[i]);
        }
        return comp;
    }

    protected void okPressed() {
        this.fEncoding = this.combo.getText();
        super.okPressed();
    }

    public String getEncoding() {
        return this.fEncoding;
    }
}
