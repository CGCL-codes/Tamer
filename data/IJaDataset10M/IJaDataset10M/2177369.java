package com.chunayev.numerology.gui.table;

import static com.chunayev.numerology.util.Constants.RESOURCE_BUNDLE;
import javax.swing.table.AbstractTableModel;
import com.chunayev.numerology.domain.Chakra;
import com.chunayev.numerology.domain.Plan;
import com.chunayev.numerology.util.Constants;
import com.chunayev.numerology.util.db.DBInitializer;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.PrimaryIndex;

/**
 * @author Sergey Chunayev
 */
public class NumerologicTableModel extends AbstractTableModel {

    private static final char[] ALPHABET = Constants.ALPHABETS_PROPERTIES.getProperty("ru.alphabet").toCharArray();

    private static final int COLUMN_COUNT = 8;

    private static final String[] COLUMN_NAMES = new String[Constants.ROW_COUNT + 1];

    /**
     * 
     */
    private static final long serialVersionUID = 3752739965319689318L;

    private static final char[] VALUES = new char[49];

    static {
        try {
            final PrimaryIndex<Long, Chakra> chakraPrimary = DBInitializer.CHAKRA_ENTITY_SOTRE.getPrimaryIndex(Long.class, Chakra.class);
            COLUMN_NAMES[0] = RESOURCE_BUNDLE.getString("table.column.plan.title");
            final long count = chakraPrimary.count();
            for (int i = 0; i < count; i++) {
                final Chakra c = chakraPrimary.get(new Long(i));
                final String name = c.getName().toLowerCase();
                final String localName = RESOURCE_BUNDLE.getString(String.format("table.column.%s", name));
                COLUMN_NAMES[i + 1] = localName;
            }
        } catch (final DatabaseException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < VALUES.length; i++) {
            VALUES[i] = ' ';
        }
        for (int i = 0; i < ALPHABET.length; i++) {
            VALUES[i] = ALPHABET[i];
        }
    }

    @Override
    public Class<?> getColumnClass(final int columnIndex) {
        return String.class;
    }

    @Override
    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    @Override
    public String getColumnName(final int column) {
        return COLUMN_NAMES[column];
    }

    @Override
    public int getRowCount() {
        return Constants.ROW_COUNT;
    }

    @Override
    public Object getValueAt(final int row, final int column) {
        if (column == 0) {
            try {
                final PrimaryIndex<Long, Plan> pi = DBInitializer.PLAN_ENTITY_STORE.getPrimaryIndex(Long.class, Plan.class);
                final Plan plan = pi.get((long) (6 - row));
                return RESOURCE_BUNDLE.getString(String.format("table.column.plan.%s", plan.getName().toLowerCase()));
            } catch (final DatabaseException e) {
                throw new RuntimeException(e);
            }
        }
        final int position = TablePositionCalculator.calculateOrdinal(row, column);
        return new Character(VALUES[position]).toString().intern().toUpperCase();
    }
}
