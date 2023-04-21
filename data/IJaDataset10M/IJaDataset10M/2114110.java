package dsb.bar.tks.client.admin.customer;

import java.awt.Component;
import javax.swing.JScrollPane;

/**
 * A {@link JScrollPane} containing a {@link CustomerTable}.
 */
public class CustomerTableScrollPane extends JScrollPane {

    private static final long serialVersionUID = 1L;

    /**
	 * Create a new {@link CustomerTableScrollPane} with an empty
	 * {@link CustomerTable}.
	 */
    public CustomerTableScrollPane() {
        this(new CustomerTable());
    }

    /**
	 * Create a new {@link CustomerTableScrollPane} with a given
	 * {@link CustomerTable}.
	 * 
	 * @param table
	 *            The table to be displayed.
	 */
    public CustomerTableScrollPane(CustomerTable table) {
        super(table);
    }

    /**
	 * Get the underlying {@link CustomerTable}.
	 * 
	 * @return A CustomerTable reference.
	 */
    public CustomerTable getCustomerTable() {
        Component c = this.getViewport().getView();
        if (!(c instanceof CustomerTable)) throw new RuntimeException("Viewport view component is not a CustomerTable");
        return (CustomerTable) c;
    }

    /**
	 * Set the displayed {@link CustomerTable}.
	 * 
	 * @param table
	 *            The table to display.
	 */
    public void setCustomerTable(CustomerTable table) {
        this.setViewportView(table);
    }

    @Override
    public void setViewportView(Component view) {
        if (!(view instanceof CustomerTable)) throw new IllegalArgumentException("view should be a CustomerTable");
        super.setViewportView(view);
    }
}
