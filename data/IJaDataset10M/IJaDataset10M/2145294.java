package com.nodeshop.entity;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

/**
 * 实体类 - 物流公司
 
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 
 
 
 
 
 * KEY: nodeshop59CDA5BD112307CE31AA07638136F25A
 
 */
@Entity
public class DeliveryCorp extends BaseEntity {

    private static final long serialVersionUID = 10595703086045998L;

    private String name;

    private String url;

    private Integer orderList;

    private Set<DeliveryType> deliveryTypeSet;

    @Column(nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column(nullable = false)
    public Integer getOrderList() {
        return orderList;
    }

    public void setOrderList(Integer orderList) {
        this.orderList = orderList;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "defaultDeliveryCorp")
    public Set<DeliveryType> getDeliveryTypeSet() {
        return deliveryTypeSet;
    }

    public void setDeliveryTypeSet(Set<DeliveryType> deliveryTypeSet) {
        this.deliveryTypeSet = deliveryTypeSet;
    }
}
