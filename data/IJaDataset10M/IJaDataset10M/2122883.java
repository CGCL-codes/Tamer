package ch.uzh.ifi.attempto.aceview.ui.view;

import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import org.jdesktop.swingx.JXTable;
import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;
import ch.uzh.ifi.attempto.aceview.model.MetricsTableModel;
import ch.uzh.ifi.attempto.aceview.ui.Colors;
import ch.uzh.ifi.attempto.aceview.ui.util.TableColumnHelper;

/**
 * <p>This view component shows some metrics of the ACE text.</p>
 * 
 * @author Kaarel Kaljurand
 */
public class ACEMetricsViewComponent extends AbstractOWLViewComponent {

    private final JXTable tableMetrics = new JXTable(new MetricsTableModel());

    @Override
    protected void disposeOWLView() {
        ((MetricsTableModel) tableMetrics.getModel()).dispose();
    }

    @Override
    protected void initialiseOWLView() throws Exception {
        tableMetrics.setShowGrid(true);
        tableMetrics.setGridColor(Colors.GRID_COLOR);
        tableMetrics.setRowHeight(tableMetrics.getRowHeight() + 1);
        tableMetrics.setSortable(false);
        tableMetrics.setTableHeader(null);
        TableColumnHelper.configureColumns(tableMetrics, MetricsTableModel.Column.values());
        setLayout(new BorderLayout());
        add(new JScrollPane(tableMetrics));
    }
}
