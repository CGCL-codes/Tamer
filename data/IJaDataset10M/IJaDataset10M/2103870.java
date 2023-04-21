package net.sf.echobinding.table;

import java.util.ArrayList;
import java.util.List;
import net.sf.echobinding.binding.BindingContext;
import net.sf.echobinding.event.SelectionChangeListener;
import nextapp.echo2.app.Color;
import nextapp.echo2.app.list.ListSelectionModel;

/**
 * A data bound table with selectable rows. On selection a
 * <code>SelectionChangeEvent</code> is send to all registered
 * <code>SelectionChangeListener</code>. The <code>BindingContext</code>
 * for the selected row can be retrieved through this event.
 * 
 */
public class SelectableTable<T> extends BoundTable<T> implements TableSelectionListener<T> {

    private static final long serialVersionUID = 887162060539185694L;

    private List<RowSelectionListener> _selectionListener = new ArrayList<RowSelectionListener>();

    private T _selectedItem;

    /**
	 * Creates a new <code>SelectableTable</code> from a list and a binding
	 * context.
	 * 
	 * @param list
	 * @param ctx
	 */
    public SelectableTable(List<T> list, BindingContext ctx) {
        super(list, ctx);
    }

    /**
	 * Creates a new <code>SelectableTable</code> from a data bound list and a
	 * binding context.
	 * 
	 * @param listBindingId
	 * @param ctx
	 */
    public SelectableTable(String listBindingId, BindingContext ctx) {
        super(listBindingId, ctx);
    }

    @Override
    protected void initialize() {
        super.initialize();
        ListSelectionModel selectionModel = getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectionModel.addChangeListener(new SelectionChangeListener(this));
        setSelectionModel(selectionModel);
        setSelectionBackground(Color.CYAN);
        setSelectionEnabled(true);
        setRolloverBackground(Color.GREEN);
        setRolloverEnabled(true);
        addActionListener(this);
    }

    public void rowIndexChanged(int rowIndex) {
        setSelectedItem(getItem(rowIndex));
        fireRowSelectionChanged(rowIndex);
    }

    /**
	 * Returns the currently selected item.
	 * 
	 * @return the selected item
	 */
    public T getSelectedItem() {
        return _selectedItem;
    }

    /**
	 * Sets the selected item.
	 * 
	 */
    protected void setSelectedItem(T selectedItem) {
        _selectedItem = selectedItem;
    }

    public void addRowSelectionListener(RowSelectionListener listener) {
        _selectionListener.add(listener);
    }

    private void fireRowSelectionChanged(int row) {
        for (RowSelectionListener listener : _selectionListener) {
            listener.rowSelectionChanged(getBindingContext(row), row);
        }
    }
}
