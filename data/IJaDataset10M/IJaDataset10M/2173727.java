package de.lema.client.view.system;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IEditorInput;
import de.lema.bo.ServerAction;
import de.lema.client.request.Session;
import de.lema.client.view.template.LemaSucheView;

public class ServerActionView extends LemaSucheView<ServerAction, ServerActionTableColumnEnum> {

    public static final String ID = "de.lema.client.view.system.ServerActionView";

    public ServerActionView() {
        super(ServerAction.class, null, new ServerActionTableComparator());
    }

    @Override
    public void createColumns() {
        TableViewer tableViewer = getTableViewer();
        ServerActionTableColumnEnum columnEnum = ServerActionTableColumnEnum.ID;
        TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        tableViewerColumn.setLabelProvider(new ServerActionTableLabelProvider(columnEnum));
        TableColumn tblclmn = tableViewerColumn.getColumn();
        tblclmn.setWidth(columnEnum.getWidth());
        tblclmn.setText(columnEnum.getColumnName());
        tblclmn.setToolTipText(columnEnum.getToolTip());
        tblclmn.addSelectionListener(getComparator().getSelectionAdapter(tableViewer, tblclmn, columnEnum));
        columnEnum = ServerActionTableColumnEnum.Action;
        tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        tableViewerColumn.setLabelProvider(new ServerActionTableLabelProvider(columnEnum));
        tblclmn = tableViewerColumn.getColumn();
        tblclmn.setWidth(columnEnum.getWidth());
        tblclmn.setText(columnEnum.getColumnName());
        tblclmn.setToolTipText(columnEnum.getToolTip());
        tblclmn.addSelectionListener(getComparator().getSelectionAdapter(tableViewer, tblclmn, columnEnum));
        columnEnum = ServerActionTableColumnEnum.Dauer;
        tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        tableViewerColumn.setLabelProvider(new ServerActionTableLabelProvider(columnEnum));
        tblclmn = tableViewerColumn.getColumn();
        tblclmn.setWidth(columnEnum.getWidth());
        tblclmn.setText(columnEnum.getColumnName());
        tblclmn.setToolTipText(columnEnum.getToolTip());
        tblclmn.addSelectionListener(getComparator().getSelectionAdapter(tableViewer, tblclmn, columnEnum));
        columnEnum = ServerActionTableColumnEnum.Zeitpunkt;
        tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        tableViewerColumn.setLabelProvider(new ServerActionTableLabelProvider(columnEnum));
        tblclmn = tableViewerColumn.getColumn();
        tblclmn.setWidth(columnEnum.getWidth());
        tblclmn.setText(columnEnum.getColumnName());
        tblclmn.setToolTipText(columnEnum.getToolTip());
        tblclmn.addSelectionListener(getComparator().getSelectionAdapter(tableViewer, tblclmn, columnEnum));
        columnEnum = ServerActionTableColumnEnum.Client;
        tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        tableViewerColumn.setLabelProvider(new ServerActionTableLabelProvider(columnEnum));
        tblclmn = tableViewerColumn.getColumn();
        tblclmn.setWidth(columnEnum.getWidth());
        tblclmn.setText(columnEnum.getColumnName());
        tblclmn.setToolTipText(columnEnum.getToolTip());
        tblclmn.addSelectionListener(getComparator().getSelectionAdapter(tableViewer, tblclmn, columnEnum));
        columnEnum = ServerActionTableColumnEnum.Parameter;
        tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        tableViewerColumn.setLabelProvider(new ServerActionTableLabelProvider(columnEnum));
        tblclmn = tableViewerColumn.getColumn();
        tblclmn.setWidth(columnEnum.getWidth());
        tblclmn.setText(columnEnum.getColumnName());
        tblclmn.setToolTipText(columnEnum.getToolTip());
        tblclmn.addSelectionListener(getComparator().getSelectionAdapter(tableViewer, tblclmn, columnEnum));
    }

    @Override
    public IEditorInput createUpdateIEditorInput(Session session, ServerAction bo) {
        return null;
    }

    @Override
    public boolean equals(ServerAction alt, ServerAction neu) {
        return alt.getId().equals(neu.getId());
    }

    @Override
    public IEditorInput createNewIEditorInput(Session session) {
        return null;
    }

    @Override
    public boolean hasEditPermission(Session session) {
        return false;
    }
}
