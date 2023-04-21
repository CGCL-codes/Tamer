package com.sisioh.erp.core.entity.partner.customer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import com.sisioh.erp.core.entity.AbstractEntity;
import com.sisioh.erp.core.type.BillingType;
import com.sisioh.erp.core.type.PayType;

/**
 * 顧客請求設定
 * 
 * @author j5ik2o
 */
@Entity
@SuppressWarnings("serial")
public class CustomerBillingConfig extends AbstractEntity {

    /**
	 * 顧客ID(PK/FK)
	 */
    @Id
    @Column(precision = 19, nullable = false, unique = true)
    public Long customerId;

    /**
	 * 無効区分 (0=有効,1=無効)
	 */
    @Column(precision = 10, nullable = false, unique = false)
    public boolean disableType;

    /**
	 * 請求区分 (1=都度請求,2=締請求)
	 */
    @Column(precision = 10, nullable = false, unique = false)
    public BillingType billingType;

    /**
	 * 締日 (99=末日)
	 */
    @Column(precision = 10, nullable = true, unique = false)
    public Integer deadlineDay;

    /**
	 * 支払月区分 (0=当月,1=翌月,2=翌々月)
	 */
    @Column(precision = 10, nullable = true, unique = false)
    public Integer payMonthType;

    /**
	 * 支払日 (99=末日)
	 */
    @Column(precision = 10, nullable = true, unique = false)
    public Integer payDay;

    /**
	 * 支払方法区分
	 */
    @Column(precision = 10, nullable = true, unique = false)
    public PayType payType;

    /**
	 * 顧客 関連プロパティ
	 */
    @OneToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    public Customer customer;

    @Override
    public String toString() {
        return org.apache.commons.lang.builder.ToStringBuilder.reflectionToString(this);
    }
}
