package com.idna.trace.domain.face;

import java.util.Collection;
import java.util.Date;
import net.icdpublishing.dream.domain.DomainField;
import net.icdpublishing.dream.domain.proxying.DomainFieldGetter;
import net.icdpublishing.dream.domain.proxying.DomainFieldSetter;
import net.icdpublishing.dream.domain.proxying.ProxyListGetter;

public interface LandRegistry extends DomainProxyInterface {

    @DomainFieldGetter(field = DomainField.PRICE)
    Double getPrice();

    @DomainFieldGetter(field = DomainField.PROPERTY_TYPE)
    String getPropertyType();

    @DomainFieldGetter(field = DomainField.SALE_DATE)
    Date getSaleDate();

    @ProxyListGetter(field = DomainField.SALE, face = LandRegistry.class)
    Collection<LandRegistry> getSales();

    @DomainFieldSetter(field = DomainField.POSITION)
    void setPosition(Integer position);

    @DomainFieldSetter(field = DomainField.PRICE)
    void setPrice(Double price);

    @DomainFieldSetter(field = DomainField.PROPERTY_TYPE)
    void setPropertyType(String string);

    @DomainFieldSetter(field = DomainField.SALE_DATE)
    void setSaleDate(Date date);

    @DomainFieldSetter(field = DomainField.SALE)
    void setSales(Collection<LandRegistry> sales);
}
