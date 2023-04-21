package net.pleso.framework.client.ui.custom.controls.datagrid;

import net.pleso.framework.client.dal.SelectParams;
import net.pleso.framework.client.localization.FrameworkLocale;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 *  Pager widget used in {@link DataGridWrapper}
 *  
 * <h3>CSS Style Rules</h3>
 * <ul>
 * <li>.pf-pager { the pager itself }</li>
 * <li>.pf-pager-status { pager's status text }</li>
 * <ul>
 * 
 * @author Scater
 * 
 */
public class Pager extends Composite implements ClickListener {

    private final DockPanel mainPanel = new DockPanel();

    private final Button gotoNext = new Button("&gt;", this);

    private final Button gotoPrev = new Button("&lt;", this);

    private final HTML status = new HTML(FrameworkLocale.messages().no_data());

    /**
	 * Constructor
	 * @param params initialization {@link SelectParams} instance
	 */
    public Pager(SelectParams params) {
        initWidget(mainPanel);
        setStyleName("pf-pager");
        status.setStyleName("pf-pager-status");
        HorizontalPanel buttons = new HorizontalPanel();
        buttons.add(gotoPrev);
        buttons.add(gotoNext);
        mainPanel.add(buttons, DockPanel.EAST);
        mainPanel.setCellHorizontalAlignment(buttons, DockPanel.ALIGN_RIGHT);
        mainPanel.add(status, DockPanel.CENTER);
        mainPanel.setVerticalAlignment(DockPanel.ALIGN_MIDDLE);
        mainPanel.setCellHorizontalAlignment(status, HasAlignment.ALIGN_RIGHT);
        mainPanel.setCellVerticalAlignment(status, HasAlignment.ALIGN_MIDDLE);
        mainPanel.setCellWidth(status, "100%");
        gotoPrev.setEnabled(false);
        gotoNext.setEnabled(false);
        this.params = params;
    }

    private SelectParams params;

    /**
	 * Total count of rows (records) in data source
	 */
    private int totalCount = 0;

    /**
	 * Gets count of rows
	 * @return count of rows
	 */
    public int getTotalCount() {
        return totalCount;
    }

    /**
	 * Sets count of rows
	 * @param totalCount count of rows
	 */
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    /**
	 * Gets select parameters
	 * @return select parameters
	 */
    public SelectParams getParams() {
        return params;
    }

    public void onClick(Widget sender) {
        if (sender == gotoPrev) {
            params.setOffset(params.getOffset() - params.getLimit());
            if (eventListener != null) eventListener.pagerParamsChanged();
        }
        if (sender == gotoNext) {
            params.setOffset(params.getOffset() + params.getLimit());
            if (eventListener != null) eventListener.pagerParamsChanged();
        }
    }

    /**
	 * Refresh pager view with current {@link SelectParams} and total count of rows.
	 * Sets status text and buttons "enable/disable" state
	 */
    public void refresh() {
        if (totalCount > 0) {
            int pagesCount = (int) Math.ceil((float) totalCount / params.getLimit());
            int pageNumber = (int) Math.ceil((float) params.getOffset() / params.getLimit()) + 1;
            status.setText(FrameworkLocale.messages().pagenumber_from_pagescount(pageNumber, pagesCount));
        } else {
            status.setText(FrameworkLocale.messages().no_data());
        }
        gotoPrev.setEnabled(params.getOffset() != 0);
        gotoNext.setEnabled(params.getOffset() + params.getLimit() < totalCount);
    }

    /**
	 * Listener of events in pager
	 */
    private IPagerEventListener eventListener;

    /**
	 * Gets events listener instance
	 * @return events listener instance
	 */
    public IPagerEventListener getEventListener() {
        return eventListener;
    }

    /**
	 * Sets events listener instance
	 * @param eventListener events listener instance
	 */
    public void setEventListener(IPagerEventListener eventListener) {
        this.eventListener = eventListener;
    }

    /**
	 * Sets external error message
	 * @param text error text
	 */
    public void setErrorMessage(String text) {
        status.setText(text);
    }
}
