package net.sf.rcpforms.experimenting.rcp_base.widgets.table;

import java.util.List;
import net.sf.rcpforms.experimenting.java.base.ReflectionUtil;
import net.sf.rcpforms.experimenting.model.bean.BeanAdapter2;
import net.sf.rcpforms.experimenting.rcp_base.event.IRCPTableInputChangedListener;
import net.sf.rcpforms.experimenting.rcp_base.event.RCPBaseInputChangedEvent;
import net.sf.rcpforms.experimenting.rcp_base.event.RCPTableInputChangedEvent;
import net.sf.rcpforms.experimenting.rcp_base.widgets.table.helper.AdvancedTableUtil;
import net.sf.rcpforms.tablesupport.tables.SizeFixedTable;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class RCPAdvancedTable<ROW_TYPE> extends RCPAdvancedViewer<ROW_TYPE, RCPAdvancedTable<ROW_TYPE>> implements IRCPAdvancedTable<ROW_TYPE, RCPAdvancedTable<ROW_TYPE>> {

    private static BeanAdapter2 dummy = BeanAdapter2.getInstance2();

    private String m_fontProperty;

    private boolean m_isSizeFixedTable;

    private volatile Cursor m_orgCursor = null;

    public RCPAdvancedTable(final Class<ROW_TYPE> rowClass, final RCPAdvancedTableColumn<ROW_TYPE>... columnConfigs) {
        this(rowClass, SWT.DEFAULT, columnConfigs);
    }

    public RCPAdvancedTable(final Class<ROW_TYPE> rowClass, final int style, final RCPAdvancedTableColumn<ROW_TYPE>... columnConfigs) {
        super(rowClass, style, columnConfigs);
    }

    /**
	 * creates the table and an appropriate viewer
	 */
    @Override
    protected Widget createWrappedWidget(final FormToolkit toolkit) {
        Table result;
        if (getStyle() == SWT.DEFAULT) {
            result = getFormToolkitEx().createTable(getSWTParent());
        } else {
            result = getFormToolkit().createTable(getSWTParent(), getStyle());
        }
        createViewer(result);
        m_isCheckedTable = (getStyle() & SWT.CHECK) != 0 && m_columnViewer instanceof CheckboxTableViewer;
        final Table swtTable = getSWTTable();
        m_isSizeFixedTable = swtTable instanceof SizeFixedTable;
        if (m_columnViewer instanceof IRCPAdvancedTableViewer) {
            final IRCPAdvancedTableViewer advViewer = (IRCPAdvancedTableViewer) m_columnViewer;
            advViewer.addTableInputChangedListener(new IRCPTableInputChangedListener() {

                @Override
                public void viewerInputChanged(final RCPTableInputChangedEvent event) {
                    proTableInputChanged(event);
                }
            });
        }
        return result;
    }

    @Override
    protected void postSpawnConfigureTableAndViewer() {
        final AdvancedTableUtil util = AdvancedTableUtil.newInstance();
        util.configureTableViewer(this, getColumns(), m_isCheckedTable, true, false, null);
        triggerColumnRelayout(ETableColumnResizePolicy.PACK_INITIALLY);
    }

    @Override
    public final RCPAdvancedTableColumn<ROW_TYPE>[] getColumns() {
        return (RCPAdvancedTableColumn<ROW_TYPE>[]) m_columnConfigs;
    }

    /**
	 * creates the viewer for the table
	 * 
	 * @param widget
	 */
    protected void createViewer(final Table widget) {
        m_columnViewer = getFormToolkitEx().createTableViewer(widget);
    }

    @Override
    public final Table getSWTTable() {
        return getTypedWidget();
    }

    @Override
    public TableViewer getViewer() {
        return getTableViewer();
    }

    /** See {@link IRCPAdvancedTable#getTableViewer()} */
    @Override
    public TableViewer getTableViewer() {
        return (TableViewer) m_columnViewer;
    }

    @Override
    public int getItemCount() {
        return getSWTTable().getItemCount();
    }

    @Override
    public int getSelectedCount() {
        return getSWTTable().getSelectionCount();
    }

    @Override
    public boolean getLinesVisible() {
        return getSWTTable().getLinesVisible();
    }

    @Override
    protected void setLinesVisible_noEv(final boolean linesVisible) {
        getSWTTable().setLinesVisible(linesVisible);
    }

    @Override
    protected final void proViewerInputChanged(final RCPBaseInputChangedEvent event) {
        proTableInputChanged((RCPTableInputChangedEvent) event);
    }

    protected final void proTableInputChanged(final RCPTableInputChangedEvent event) {
    }

    @Override
    protected void proSetInput(final List<ROW_TYPE> input) {
        super.proSetInput(input);
        AdvancedTableUtil.doHandleListChange(this, input == null ? 0 : input.size());
    }

    @Override
    public ROW_TYPE getModelAt(final int mouseY) {
        final TableItem item = AdvancedTableUtil.getPixelCoordYToItem(this, mouseY);
        return item == null ? null : (ROW_TYPE) item.getData();
    }

    @Override
    public TableItem getItemAtPixelY(final int pixelY) {
        return AdvancedTableUtil.getPixelCoordYToItem(this, pixelY);
    }

    @Override
    protected Item getItemAtIndex(final int rowIndex) {
        return getSWTTable().getItem(rowIndex);
    }

    @Override
    protected void subUnlinkBeanSupport() {
        final Table swtTable = getSWTTable();
        if (m_selectionListener != null) {
            swtTable.removeSelectionListener(m_selectionListener);
        }
        super.subUnlinkBeanSupport();
    }

    @Override
    protected void postSpawnInitBeanSupport() {
        super.postSpawnInitBeanSupport();
        final Table swtTable = getSWTTable();
        if (swtTable != null && !swtTable.isDisposed()) {
            m_selectionListener = new SelectionListener() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    proWidgetSelected0(e);
                }

                @Override
                public void widgetDefaultSelected(final SelectionEvent e) {
                }
            };
            swtTable.addSelectionListener(m_selectionListener);
        }
    }

    @Override
    public IRCPAdvancedTableColumn<ROW_TYPE, ? extends IRCPAdvancedTableColumn>[] getIColumns() {
        return ReflectionUtil.castArray(IRCPAdvancedTableColumn.class, super.getIColumns(), false);
    }

    @Override
    protected void repaintAll() {
        getTableViewer().refresh();
    }

    /**
	 * Experimental activate height resizing.
	 */
    public void experimentalActivateHeightResizing() {
        AdvancedTableUtil.experimentTableHeightAdapter(this);
    }
}
