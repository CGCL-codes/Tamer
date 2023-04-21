package org.jampa.gui.dialogs;

import java.util.Iterator;
import java.util.Map;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.util.Util;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.jampa.gui.translations.Messages;
import org.jampa.model.playlists.AudioItem;

public class TagUpdateProblemDialog extends TitleAreaDialog {

    private Table viewer;

    private TableColumn _fileCol;

    private TableColumn _errorCol;

    private Map<AudioItem, String> _items;

    public TagUpdateProblemDialog(Shell parentShell, Map<AudioItem, String> items) {
        super(parentShell);
        _items = items;
    }

    protected Control createContents(Composite parent) {
        Control contents = super.createContents(parent);
        setTitle(Messages.getString("TagUpdateProblemDialog.Title"));
        setMessage(Messages.getString("TagUpdateProblemDialog.TitleArea"), IMessageProvider.INFORMATION);
        return contents;
    }

    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        viewer = new Table(composite, SWT.FULL_SELECTION | SWT.BORDER);
        viewer.setLayoutData(new GridData(GridData.FILL_BOTH));
        TableLayout layout = new TableLayout();
        layout.addColumnData(new ColumnWeightData(80, 10, true));
        layout.addColumnData(new ColumnWeightData(20, 10, true));
        viewer.setLayout(layout);
        _fileCol = new TableColumn(viewer, SWT.LEFT);
        _fileCol.setText(Messages.getString("TagUpdateProblemDialog.ColHeaderFile"));
        _errorCol = new TableColumn(viewer, SWT.LEFT);
        _errorCol.setText(Messages.getString("TagUpdateProblemDialog.ColHeaderError"));
        if (!Util.isWindows()) {
            viewer.setLinesVisible(true);
        }
        viewer.setHeaderVisible(true);
        fillList();
        return parent;
    }

    private void fillList() {
        TableItem tableItem;
        viewer.removeAll();
        AudioItem item;
        Iterator<AudioItem> iter = _items.keySet().iterator();
        while (iter.hasNext()) {
            item = iter.next();
            tableItem = new TableItem(viewer, SWT.NONE);
            tableItem.setText(0, item.getFileName());
            tableItem.setText(1, _items.get(item));
        }
    }

    protected void createButtonsForButtonBar(Composite parent) {
        Button closeBtn = createButton(parent, IDialogConstants.CLOSE_ID, IDialogConstants.CLOSE_LABEL, true);
        closeBtn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                close();
            }
        });
    }

    protected Point getInitialSize() {
        return new Point(600, 300);
    }
}
