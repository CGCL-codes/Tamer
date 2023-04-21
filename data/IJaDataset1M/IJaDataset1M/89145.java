package org.objectwiz.testmodel;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 *
 * @author Benoît Del Basso <benoit.delbasso at helmet.fr>
 */
@Entity
public class OnlineOrder extends org.objectwiz.model.EntityBase {

    private Client client;

    private String orderReference;

    private Address deliveryAddress;

    public OnlineOrder() {
    }

    public OnlineOrder(Client client, String orderReference, Address deliveryAddress) {
        this.client = client;
        this.orderReference = orderReference;
        this.deliveryAddress = deliveryAddress;
    }

    public String getOrderReference() {
        return orderReference;
    }

    public void setOrderReference(String orderReference) {
        this.orderReference = orderReference;
    }

    @ManyToOne
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @ManyToOne
    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
}
