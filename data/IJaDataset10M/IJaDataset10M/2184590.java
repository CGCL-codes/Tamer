package org.plazmaforge.studio.reportdesigner.dialogs.chart.series;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.plazmaforge.studio.reportdesigner.dialogs.AbstractRDEditDialog;
import org.plazmaforge.studio.reportdesigner.dialogs.chart.HyperlinkProvider;

/** 
 * @author Oleh Hapon
 * $Id: AbstractSeriesEditDialog.java,v 1.1 2010/08/23 09:49:48 ohapon Exp $
 */
public abstract class AbstractSeriesEditDialog extends AbstractRDEditDialog {

    protected HyperlinkProvider hyperlinkProvider;

    public AbstractSeriesEditDialog(Shell parentShell, int command) {
        super(parentShell);
        this.command = command;
    }

    protected Control createDialogArea(Composite parent) {
        Composite parentComposite = (Composite) super.createDialogArea(parent);
        TabFolder tabFolder = new TabFolder(parentComposite, SWT.NULL);
        createItems(tabFolder);
        doLoadData();
        return parentComposite;
    }

    protected abstract void createItems(TabFolder tabFolder);
}
