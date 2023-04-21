package org.bfc.inventory.gwt.client.view.impl;

import org.bfc.inventory.gwt.client.view.BfcNavigationView;
import org.bfc.inventory.gwt.client.view.SimpleLayoutPanel;
import org.bfc.inventory.gwt.shared.NavigationListType;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class BfcNavigationViewImpl implements BfcNavigationView {

    private NavigationListPanel _navigationListView;

    private SimpleLayoutPanel _contentsTablePanel;

    private LayoutPanel _widgetPanel;

    private BfcNavigationPresenter _presenter;

    public BfcNavigationViewImpl() {
        super();
        _navigationListView = new NavigationListPanel();
        _contentsTablePanel = new SimpleLayoutPanel();
        _widgetPanel = new LayoutPanel();
        _widgetPanel.setSize("800", "600");
        TabLayoutPanel dashboardLayoutPanel = new TabLayoutPanel(1.5, Unit.EM);
        SplitLayoutPanel mappingIssuesComposite = new SplitLayoutPanel();
        dashboardLayoutPanel.add(mappingIssuesComposite, "Bushwick Food Coop Inventory Admin", false);
        mappingIssuesComposite.addWest(_navigationListView, 200.0);
        mappingIssuesComposite.add(_contentsTablePanel);
        _widgetPanel.add(dashboardLayoutPanel);
    }

    @Override
    public SimpleLayoutPanel getContentsTablePanel() {
        return _contentsTablePanel;
    }

    @Override
    public void setPresenter(BfcNavigationPresenter presenter) {
        _presenter = presenter;
    }

    @Override
    public Widget asWidget() {
        return _widgetPanel;
    }

    @Override
    public SingleSelectionModel<NavigationListType> getNavigationPanelSelectionModel() {
        return _navigationListView == null ? null : _navigationListView.getSelectionModel();
    }

    private class NavigationListPanel extends LayoutPanel {

        private CellList<NavigationListType> _cellList;

        private ListDataProvider<NavigationListType> _dataProvider;

        private SingleSelectionModel<NavigationListType> _selectionModel;

        public NavigationListPanel() {
            super();
            _selectionModel = new SingleSelectionModel<NavigationListType>();
            _selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

                public void onSelectionChange(SelectionChangeEvent event) {
                    NavigationListType selected = _selectionModel.getSelectedObject();
                    if (selected != null) {
                        _presenter.handleNavListTypeSelected(selected);
                    }
                }
            });
            NavigationListTypeCell typeCell = new NavigationListTypeCell();
            _cellList = new CellList<NavigationListType>(typeCell);
            _cellList.setSelectionModel(_selectionModel);
            _dataProvider = new ListDataProvider<NavigationListType>();
            _dataProvider.addDataDisplay(_cellList);
            for (NavigationListType curType : NavigationListType.values()) {
                _dataProvider.getList().add(curType);
            }
            _cellList.setRowCount(NavigationListType.values().length, true);
            ScrollPanel sPanel = new ScrollPanel(_cellList);
            this.add(sPanel);
        }

        public SingleSelectionModel<NavigationListType> getSelectionModel() {
            return _selectionModel;
        }
    }

    public static class NavigationListTypeCell extends AbstractCell<NavigationListType> {

        @Override
        public void render(Context context, NavigationListType value, SafeHtmlBuilder sb) {
            if (value != null) {
                sb.appendEscaped(value.getTitle());
            }
        }
    }
}
