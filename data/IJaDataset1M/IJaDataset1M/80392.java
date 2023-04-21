package edu.univalle.lingweb.persistence;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CoDeliveryDate2 entity.
 * 
 * @author LingWeb
 */
@Entity
@Table(name = "co_delivery_date2", schema = "public", uniqueConstraints = {  })
public class CoDeliveryDate2 extends AbstractCoDeliveryDate2 implements java.io.Serializable {

    /** default constructor */
    public CoDeliveryDate2() {
    }

    /** minimal constructor */
    public CoDeliveryDate2(Long deliveryDateId, Long deliveryDateNum, Date deliveryDate) {
        super(deliveryDateId, deliveryDateNum, deliveryDate);
    }

    /** full constructor */
    public CoDeliveryDate2(Long deliveryDateId, CoExercises2 coExercises2, Long deliveryDateNum, Date deliveryDate) {
        super(deliveryDateId, coExercises2, deliveryDateNum, deliveryDate);
    }
}
