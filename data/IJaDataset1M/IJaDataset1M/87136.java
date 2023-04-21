package org.xaware.ide.xadev.table.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.xaware.ide.xadev.table.contentprovider.TableContentProvider;

/**
 * Default editor for Combobox.
 * 
 * @author T Vasu
 * @version 1.0
 */
public class ComboEditor extends TableCellEditor {

    /** Combo control. */
    private Combo comboControl;

    /** Flag for checking whether the Control is Read-only or not */
    private boolean isEditable;

    /**
     * Creates a new ComboEditor object.
     * 
     * @param parent
     *            parent composite instance.
     * @param items
     *            values to be displayed in combobox.
     * @param aIsEditable
     *            Control is read-only or not
     */
    public ComboEditor(final Composite parent, final Object[] items, final boolean aIsEditable) {
        super(parent);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.marginWidth = -2;
        gridLayout.marginHeight = -4;
        setLayout(gridLayout);
        isEditable = aIsEditable;
        if (isEditable) {
            comboControl = new Combo(this, SWT.NONE);
        } else {
            comboControl = new Combo(this, SWT.READ_ONLY);
        }
        if (items != null) {
            for (int i = 0; i < items.length; i++) {
                comboControl.add(items[i].toString());
            }
        }
        final GridData gData = new GridData(GridData.FILL_HORIZONTAL);
        comboControl.setLayoutData(gData);
    }

    @Override
    public String getText() {
        return comboControl.getText();
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.xaware.ide.xadev.datamodel.editor.TableCellEditor#setEditorText(java.lang.String)
     */
    @Override
    public void setText(final String string) {
        comboControl.setText(string);
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Widget#addListener(int, org.eclipse.swt.widgets.Listener)
     */
    @Override
    public void addListener(final int eventType, final Listener listener) {
        comboControl.addListener(eventType, listener);
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Widget#dispose()
     */
    @Override
    public void dispose() {
        comboControl.dispose();
        super.dispose();
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.xaware.ide.xadev.datamodel.editor.TableCellEditor#focusComponent()
     */
    @Override
    public boolean focusComponent() {
        return false;
    }

    /**
     * Sets row and Column.
     * 
     * @param row
     *            row index.
     * @param col
     *            col index.
     * @param provider
     *            provider instance.
     */
    @Override
    public void setRowAndColoumn(final int row, final int col, final TableContentProvider provider) {
        super.setRowAndColoumn(row, col, provider);
        comboControl.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent evt) {
                updateValue(((Combo) (evt.getSource())).getText());
            }
        });
        if (isEditable) {
            comboControl.addFocusListener(new FocusAdapter() {

                @Override
                public void focusLost(final FocusEvent fe) {
                    updateValue(((Combo) (fe.getSource())).getText());
                }
            });
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.xaware.ide.xadev.datamodel.editor.TableCellEditor#setData(java.lang.Object)
     */
    @Override
    public void setData(final Object item) {
        if (item != null) {
            comboControl.removeAll();
            final Object[] items = (Object[]) item;
            for (int i = 0; i < items.length; i++) {
                comboControl.add(items[i].toString());
            }
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.xaware.ide.xadev.datamodel.editor.TableCellEditor#isEnabled()
     */
    @Override
    public boolean isControlEnabled() {
        return comboControl.isEnabled();
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.xaware.ide.xadev.datamodel.editor.TableCellEditor#setEnabled(boolean)
     */
    @Override
    public void setEnabled(final boolean enable) {
        comboControl.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
        comboControl.setEnabled(enable);
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.xaware.ide.xadev.datamodel.editor.TableCellEditor#getEditorControl()
     */
    @Override
    public Control getEditorControl() {
        return comboControl;
    }
}
