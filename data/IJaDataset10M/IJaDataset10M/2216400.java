package com.sisioh.erp.core.entity.sales.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import com.sisioh.erp.core.entity.AbstractEntity;

/**
 * 商品設定
 * 
 * @author j5ik2o
 */
@Entity
@SuppressWarnings("serial")
public class ProductConfig extends AbstractEntity {

    /** 商品ID(PK/FK) */
    @Id
    @Column(precision = 19, nullable = false, unique = true)
    public Long productId;

    /**
	 * 削除区分 (0=非削除,1=削除,)
	 */
    @Column(precision = 10, nullable = false, unique = false)
    public boolean deleteType;

    /**
	 * 無効区分 (0=有効,1=無効)
	 */
    @Column(precision = 10, nullable = false, unique = false)
    public boolean disableType;

    /**
	 * 商品 関連プロパティ
	 */
    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    public Product product;

    @Override
    public String toString() {
        return org.apache.commons.lang.builder.ToStringBuilder.reflectionToString(this);
    }
}
