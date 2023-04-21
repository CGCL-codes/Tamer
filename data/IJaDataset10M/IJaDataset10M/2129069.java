package mswing.table;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

/**
 * Ce renderer est un renderer de rupture. Dans une colonne, quand des valeurs qui se suivent sont
 * �gales, seule la premi�re est affich�e. Exemple de rupture sur l'ann�e :
 *
 * 2001 1er semestre 1000<br/>
 * 2nd semestre 1000<br/>
 * 2002 1er semestre 1200<br/>
 * 2nd semestre 1300<br/>
 *
 * Ce renderer peut d�l�guer le rendu quand n�cessaire � un renderer d�l�gu�.
 *
 * @author Emeric Vernat
 */
class MRuptureTableCellRenderer extends MDefaultTableCellRenderer {

    private static final long serialVersionUID = 1L;

    private static class EmptyTableCellRenderer extends MDefaultTableCellRenderer {

        private static final long serialVersionUID = 1L;

        EmptyTableCellRenderer() {
            super();
        }

        /** {@inheritDoc} */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            final Component component = super.getTableCellRendererComponent(table, null, isSelected, hasFocus, row, column);
            if (component instanceof JLabel) {
                final JLabel label = (JLabel) component;
                label.setText("\"");
                label.setHorizontalAlignment(SwingConstants.CENTER);
            }
            return component;
        }
    }

    /** Renderer quand le texte est identique. */
    private final TableCellRenderer emptyRenderer = new EmptyTableCellRenderer();

    /** Renderer d�l�gu� affichant le texte normalement. */
    private TableCellRenderer delegateRenderer;

    /**
	 * Constructeur.
	 */
    MRuptureTableCellRenderer() {
        super();
    }

    /**
	 * Constructeur.
	 *
	 * @param delegateRenderer javax.swing.table.TableCellRenderer
	 */
    MRuptureTableCellRenderer(TableCellRenderer delegateRenderer) {
        super();
        this.delegateRenderer = delegateRenderer;
    }

    /** {@inheritDoc} */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (table == null) {
            return super.getTableCellRendererComponent(null, value, isSelected, hasFocus, row, column);
        }
        final Object currentValue = value;
        final boolean currentSelected = isSelected;
        Object upperValue = null;
        if (row > 0) {
            upperValue = table.getValueAt(row - 1, column);
        }
        TableCellRenderer renderer;
        if (upperValue != null && upperValue.equals(value)) {
            renderer = emptyRenderer;
        } else if (delegateRenderer != null) {
            renderer = delegateRenderer;
        } else {
            renderer = table.getDefaultRenderer(table.getColumnClass(column));
        }
        return renderer.getTableCellRendererComponent(table, currentValue, currentSelected, hasFocus, row, column);
    }
}
