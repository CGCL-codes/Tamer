package org.dolmen.swing.panels;

import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

/**
 * Specialized {@link BasePanel} to render a {@link JTable}
 * 
 * @since 0.0.1
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte
 * 
 */
public class TablePanel extends BasePanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private JTable fTable;

    public TablePanel() {
        this(new JTable());
    }

    public TablePanel(JTable aTable) {
        super();
        setTable(aTable);
    }

    public JTable getTable() {
        return fTable;
    }

    /**
	 * set the {@link JTable}.
	 * <p>
	 * when set the AutoResizeMode is set to AUTO_RESIZE_OFF and SelectionMode to
	 * SINGLE_SELECTION
	 * 
	 * @param aTable
	 *          the table
	 * @return the configured JTable
	 */
    public JTable setTable(JTable aTable) {
        assert aTable != null;
        fTable = aTable;
        fTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        fTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fTable = aTable;
        setLayout(new BorderLayout());
        add(new JScrollPane(fTable), BorderLayout.CENTER);
        return fTable;
    }
}
