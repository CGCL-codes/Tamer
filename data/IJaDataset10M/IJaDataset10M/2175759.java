package org.plazmaforge.framework.client.swt.dialogs.dataexport;

import org.eclipse.swt.widgets.Shell;

/** 
 * @author Oleh Hapon
 * $Id: ODTExportDialog.java,v 1.4 2010/05/26 17:21:54 ohapon Exp $
 */
public class ODTExportDialog extends AbstractExportDialog {

    private static final String[] FILTER = { "*.odt" };

    private static final int FLAGS = OPT_HDR | FMT_NULL | PATTERN_DATE | PATTERN_TIME | PATTERN_NUMBER;

    public ODTExportDialog(Shell parentShell) {
        super(parentShell);
    }

    public String[] getFileFilter() {
        return FILTER;
    }

    public int getFlags() {
        return FLAGS;
    }

    public String getMessage() {
        return Messages.getString("ExportDialog.odt.message");
    }

    public String getTitle() {
        return Messages.getString("ExportDialog.odt.title");
    }

    protected boolean isCheckFile() {
        return true;
    }
}
