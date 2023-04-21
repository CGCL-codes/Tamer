package net.algid.purchase.gui.records;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import net.algid.purchase.interfaces.IGoodLogic;
import net.algid.purchase.interfaces.ISupplierLogic;
import net.algid.purchase.valueObject.GoodData;
import net.algid.purchase.valueObject.Supplier;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class GoodListDialog extends Dialog implements IConstants {

    private final IGoodLogic goodLogic;

    private final ISupplierLogic supplierLogic;

    private TableViewer goodDataTv;

    private final Image shellImg;

    public static class GoodDataLabelProvider implements ITableLabelProvider {

        @Override
        public void addListener(ILabelProviderListener arg0) {
        }

        @Override
        public void dispose() {
        }

        @Override
        public boolean isLabelProperty(Object arg0, String arg1) {
            return false;
        }

        @Override
        public void removeListener(ILabelProviderListener arg0) {
        }

        @Override
        public Image getColumnImage(Object arg0, int arg1) {
            return null;
        }

        @Override
        public String getColumnText(Object arg0, int arg1) {
            if (arg0 instanceof GoodData && arg0 != null) {
                GoodData _goodData = (GoodData) arg0;
                switch(arg1) {
                    case 0:
                        return _goodData.good != null ? String.valueOf(_goodData.good.code) : null;
                    case 1:
                        return _goodData.good != null ? _goodData.good.name : null;
                    case 2:
                        return _goodData.good != null ? _goodData.good.nomarticle : null;
                    case 3:
                        return _goodData.supplier != null ? new StringBuilder().append("[").append(_goodData.supplier.code).append("] ").append(_goodData.supplier.name).toString() : null;
                }
            }
            return null;
        }
    }

    public static class GoodDataContentProvider implements IStructuredContentProvider {

        private GoodData[] elements;

        @Override
        public void dispose() {
        }

        @Override
        public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
            if (arg2 instanceof GoodData[]) {
                elements = (GoodData[]) arg2;
            } else {
                elements = null;
            }
        }

        @Override
        public Object[] getElements(Object arg0) {
            return elements;
        }
    }

    /**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
    public GoodListDialog(Shell parentShell, Image img, IGoodLogic logic1, ISupplierLogic logic2) {
        super(parentShell);
        setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
        this.shellImg = img;
        this.goodLogic = logic1;
        this.supplierLogic = logic2;
    }

    /**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        container.setLayout(new GridLayout(1, false));
        goodDataTv = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
        goodDataTv.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent arg0) {
                updateButtonState(arg0.getSelection() != null && !arg0.getSelection().isEmpty());
            }
        });
        final Table table = goodDataTv.getTable();
        table.setHeaderVisible(true);
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        TableColumn column1 = new TableColumn(table, SWT.NONE);
        column1.setWidth(100);
        column1.setText(Messages.getString("Good.code.text"));
        TableColumn column2 = new TableColumn(table, SWT.NONE);
        column2.setWidth(100);
        column2.setText(Messages.getString("Good.name.text"));
        TableColumn column3 = new TableColumn(table, SWT.NONE);
        column3.setWidth(100);
        column3.setText(Messages.getString("Good.normaticle.text"));
        TableColumn column4 = new TableColumn(table, SWT.NONE);
        column4.setWidth(100);
        column4.setText(Messages.getString("Good.supplier.text"));
        goodDataTv.setLabelProvider(new GoodDataLabelProvider());
        goodDataTv.setContentProvider(new GoodDataContentProvider());
        initData();
        return container;
    }

    private void initData() {
        List<GoodData> list = null;
        try {
            list = goodLogic.getDataList(null);
        } catch (SQLException e) {
            MessageDialog.openError(getShell(), "������", e.getMessage());
            e.printStackTrace();
        }
        if (list != null) {
            goodDataTv.setInput(list.toArray(new GoodData[list.size()]));
        } else {
            goodDataTv.setInput(new GoodData[0]);
        }
    }

    /**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, ADD_ID, Messages.getString("Dialog.addBtn.text"), false);
        createButton(parent, CHANGE_ID, Messages.getString("Dialog.changeBtn.text"), false);
        createButton(parent, REMOVE_ID, Messages.getString("Dialog.removeBtn.text"), false);
        updateButtonState(goodDataTv.getSelection() != null && !goodDataTv.getSelection().isEmpty());
    }

    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.getString("GoodListDialog.title"));
        newShell.setImage(shellImg);
    }

    /**
	 * Return the initial size of the dialog.
	 */
    @Override
    protected Point getInitialSize() {
        return new Point(450, 300);
    }

    @Override
    protected void buttonPressed(int buttonId) {
        List<Supplier> supplierList = supplierLogic.getList(null);
        if (buttonId == ADD_ID) {
            GoodData _data = new GoodData();
            if (new GoodDialog(getShell(), _data, supplierList).open() == IDialogConstants.OK_ID && _data.good != null) {
                goodLogic.add(_data.good);
                initData();
            }
        }
        if (buttonId == CHANGE_ID) {
            GoodData _data = (GoodData) ((IStructuredSelection) goodDataTv.getSelection()).getFirstElement();
            if (_data.supplier != null) {
                for (Iterator<Supplier> iter = supplierList.iterator(); iter.hasNext(); ) {
                    Supplier _supplier = iter.next();
                    if (_supplier.code == _data.supplier.code) {
                        _data.supplier = _supplier;
                        break;
                    }
                }
            }
            if (new GoodDialog(getShell(), _data, supplierList).open() == IDialogConstants.OK_ID) {
                goodLogic.save(_data.good);
                initData();
            }
        }
        if (buttonId == REMOVE_ID) {
            GoodData _data = (GoodData) ((IStructuredSelection) goodDataTv.getSelection()).getFirstElement();
            goodLogic.remove(_data.good.code);
            initData();
        }
    }

    protected void updateButtonState(boolean isGoodSelected) {
        final Button changeBnt = getButton(CHANGE_ID);
        if (changeBnt != null) {
            changeBnt.setEnabled(isGoodSelected);
        }
        final Button removeBnt = getButton(REMOVE_ID);
        if (removeBnt != null) {
            removeBnt.setEnabled(isGoodSelected);
        }
    }
}
