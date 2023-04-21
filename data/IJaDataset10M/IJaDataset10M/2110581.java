package org.shopformat.controller.admin.salesOrders.tax;

import java.util.ArrayList;
import java.util.List;
import org.shopformat.controller.admin.BaseViewController;
import org.shopformat.domain.order.sales.tax.TaxClass;
import org.shopformat.domain.order.sales.tax.TaxRate;
import org.shopformat.domain.order.sales.tax.TaxZone;

/**
 * 
 * @author Stephen Vangasse
 * 
 * @version $Id$
 */
public class TaxMatrix extends BaseViewController {

    private List<List<TaxRate>> matrix;

    private List<TaxZone> taxZones;

    private List<TaxClass> taxClasses;

    @Override
    public void prerender() {
        matrix = new ArrayList<List<TaxRate>>();
        taxZones = getShopService().findAll(TaxZone.class);
        taxClasses = getShopService().findAll(TaxClass.class);
        for (TaxClass taxClass : taxClasses) {
            List<TaxRate> row = new ArrayList<TaxRate>();
            for (TaxZone zone : taxZones) {
                row.add(getShopService().findRateForZoneAndClass(zone, taxClass));
            }
            matrix.add(row);
        }
    }

    /**
	 * @return the matrix
	 */
    public List<List<TaxRate>> getMatrix() {
        return matrix;
    }

    /**
	 * @return the taxZones
	 */
    public List<TaxZone> getTaxZones() {
        return taxZones;
    }

    /**
	 * @return the taxClasses
	 */
    public List<TaxClass> getTaxClasses() {
        return taxClasses;
    }
}
