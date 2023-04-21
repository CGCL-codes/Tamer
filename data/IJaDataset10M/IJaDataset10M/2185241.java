package org.shopformat.controller.admin.salesOrders.delivery;

import java.util.ArrayList;
import java.util.List;
import org.shopformat.controller.admin.BaseViewController;
import org.shopformat.domain.order.sales.delivery.DeliveryOption;
import org.shopformat.domain.order.sales.delivery.DeliverySize;
import org.shopformat.domain.order.sales.delivery.DeliveryZone;

/**
 * 
 * @author Stephen Vangasse
 *
 * @version $Id$
 */
public class DeliveryMatrix extends BaseViewController {

    private List<List<List<DeliveryOption>>> matrix;

    private List<DeliveryZone> deliveryZones;

    private List<DeliverySize> deliverySizes;

    @Override
    public void prerender() {
        matrix = new ArrayList<List<List<DeliveryOption>>>();
        deliveryZones = getShopService().findAll(DeliveryZone.class);
        deliverySizes = getShopService().findAll(DeliverySize.class);
        for (DeliverySize size : deliverySizes) {
            List<List<DeliveryOption>> row = new ArrayList<List<DeliveryOption>>();
            for (DeliveryZone zone : deliveryZones) {
                List<DeliveryOption> cell = getShopService().getDeliveryOptionsForZoneAndSize(zone, size);
                row.add(cell);
            }
            matrix.add(row);
        }
    }

    /**
	 * @return the matrix
	 */
    public List<List<List<DeliveryOption>>> getMatrix() {
        return matrix;
    }

    /**
	 * @return the deliveryZones
	 */
    public List<DeliveryZone> getDeliveryZones() {
        return deliveryZones;
    }

    /**
	 * @return the deliverySizes
	 */
    public List<DeliverySize> getDeliverySizes() {
        return deliverySizes;
    }
}
