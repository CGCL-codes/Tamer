package org.yes.cart.service.domain;

import org.yes.cart.domain.entity.Address;
import org.yes.cart.domain.entity.CarrierSla;
import org.yes.cart.domain.entity.CustomerOrderDeliveryDet;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * User: Igor Azarny iazarny@yahoo.com
 * Date: 07-May-2011
 * Time: 11:13:01
 */
public interface CarrierSlaService extends GenericService<CarrierSla> {

    /**
     * Get shipping SLA by carrier Id.
     *
     * @param carrierId given carrier id
     * @return list of SLA, that belongs to given carrier id
     */
    List<CarrierSla> findByCarrier(long carrierId);

    /**
     * Get sla by name. Sla name is unique
     * @param slaName given sla name.
     * @return {@link CarrierSla}
     */
    CarrierSla finaByName(final String slaName);

    /**
     * Get the price of delivery.
     *
     * @param carrierSla     carries sla
     * @param items          items to deliver
     * @param defaultAddress deliver to address.
     * @return price of delivery
     */
    BigDecimal getDeliveryPrice(CarrierSla carrierSla, Collection<CustomerOrderDeliveryDet> items, Address defaultAddress);

    /**
     * Fin all sla for given currency.
     * @param currency fiven currency.
     * @return   list of found currency
     */
    List<CarrierSla> findByCurrency(String currency);
}
