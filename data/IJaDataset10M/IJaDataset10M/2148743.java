package hoplugins.pluginIFA;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

public class RendererSorter extends MouseAdapter {

    private JTable table;

    private SortedTableModel sortedTableModel;

    private int sortColumn;

    private int oldIconColumn;

    private ImageIcon sortAZ = new ImageIcon(RendererSorter.class.getResource("image/ArrowTop.gif"));

    private ImageIcon sortZA = new ImageIcon(RendererSorter.class.getResource("image/ArrowUnder.gif"));

    public RendererSorter(JTable table, SortedTableModel sortedTableModel, int sortStartColumn) {
        this.table = table;
        this.sortedTableModel = sortedTableModel;
        this.oldIconColumn = sortStartColumn;
        this.sortColumn = sortStartColumn;
        ((RendererDecorator) table.getColumnModel().getColumn(sortStartColumn).getHeaderRenderer()).setLabelIcon(this.sortAZ);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.sortColumn = this.table.columnAtPoint(e.getPoint());
        TableColumn column = this.table.getColumnModel().getColumn(this.sortColumn);
        if (this.oldIconColumn != this.sortColumn) ((RendererDecorator) this.table.getColumnModel().getColumn(this.oldIconColumn).getHeaderRenderer()).setLabelIcon(null);
        this.oldIconColumn = this.sortColumn;
        RendererDecorator rendererDecorator = (RendererDecorator) column.getHeaderRenderer();
        if (rendererDecorator.getLabelIcon() == null) {
            rendererDecorator.setLabelIcon(this.sortAZ);
            this.sortedTableModel.setAlfa(true);
        } else if (rendererDecorator.getLabelIcon() == this.sortZA) {
            rendererDecorator.setLabelIcon(this.sortAZ);
            this.sortedTableModel.setAlfa(true);
        } else {
            rendererDecorator.setLabelIcon(this.sortZA);
            this.sortedTableModel.setAlfa(false);
        }
        this.sortedTableModel.sort(this.sortColumn);
        if (this.table.getRowCount() > 0) this.table.setRowSelectionInterval(0, 0);
        this.table.getTopLevelAncestor().repaint();
    }

    public int getSortColumn() {
        return this.sortColumn;
    }
}
