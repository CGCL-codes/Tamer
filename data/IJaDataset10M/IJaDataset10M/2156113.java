package org.shopformat.controller.admin.purchaseOrders;

import java.util.ArrayList;
import java.util.List;
import org.jfree.data.general.AbstractDataset;
import org.shopformat.controller.admin.BaseViewController;
import org.shopformat.controller.admin.crud.list.AlternativeReturnCrudListImpl;
import org.shopformat.controller.admin.crud.list.CrudList;
import org.shopformat.controller.admin.reports.ChartService;

public class PurchaseOrderSummary extends BaseViewController {

    private CrudList overdueOrderList;

    private List neededProducts;

    public void init() {
        super.init();
        overdueOrderList = new AlternativeReturnCrudListImpl("summary");
        neededProducts = new ArrayList();
    }

    public void preprocess() {
        super.preprocess();
        loadLists();
    }

    public void prerender() {
        super.prerender();
        if (!isPostBack()) {
            loadLists();
        }
    }

    private void loadLists() {
    }

    public List getNeededProducts() {
        return neededProducts;
    }

    public CrudList getOverdueOrderList() {
        return overdueOrderList;
    }

    public AbstractDataset getStockChartBySupplier() {
        return ChartService.getStockChartBySupplier(4);
    }

    public AbstractDataset getStockChartByBrand() {
        return ChartService.getStockChartByBrand(4);
    }
}
