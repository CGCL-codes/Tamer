package com.sisioh.erp.core.entity.shipping;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import com.sisioh.erp.core.entity.AbstractEntity;

/**
 * 出荷先設定
 * 
 * @author j5ik2o
 */
@Entity
@SuppressWarnings("serial")
public class ShippingTargetConfig extends AbstractEntity {

    /**
	 * 出荷先設定ID(PK/FK)
	 */
    @Id
    @Column(precision = 19, nullable = false, unique = true)
    public Long shippingTargetId;

    /**
	 * 削除区分 (0=非削除,1=削除)
	 */
    @Column(precision = 10, nullable = false, unique = false)
    public boolean deleteType;

    /**
	 * 無効区分 (0=有効,1=無効)
	 */
    @Column(precision = 10, nullable = false, unique = false)
    public boolean disableType;

    /**
	 * 出荷先 関連プロパティ
	 */
    @OneToOne
    @JoinColumn(name = "shipping_target_id", referencedColumnName = "shipping_target_id")
    public ShippingTarget shippingTarget;

    @Override
    public String toString() {
        return org.apache.commons.lang.builder.ToStringBuilder.reflectionToString(this);
    }
}
