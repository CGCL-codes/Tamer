package net.sf.rcpforms.experimenting.rcp_base.widgets.table;

import net.sf.rcpforms.experimenting.rcp_base.widgets.table.helper.AbstractAdvTableResizeAdapter;
import net.sf.rcpforms.experimenting.rcp_base.widgets.table.helper.AdvancedTableUtil;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class RCPAdvTableResizeAdapter extends AbstractAdvTableResizeAdapter {

    public RCPAdvTableResizeAdapter(final RCPAdvancedTable advTable) {
        super(advTable);
    }

    @Override
    protected void proColumnResized(final TableColumn widget) {
        final RCPAdvancedTableColumn advColumn = AdvancedTableUtil.getTableColumnsConfigurations2(widget);
        if (advColumn == null) {
            throw new IllegalStateException("link from TableColumn to RCPAdvancedTableColumn no property set up (swt.Widget.setData() )");
        }
    }

    @Override
    protected void proTableResized(final Table table) {
        m_advancedTable.triggerColumnRelayout(ETableColumnResizePolicy.PACK_ON_REFRESH);
    }
}
