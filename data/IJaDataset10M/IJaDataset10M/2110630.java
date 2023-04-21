package com.sisioh.erp.core.entity.partner.customer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import com.sisioh.erp.core.entity.AbstractEntity;

/**
 * 顧客履歴詳細
 * 
 * @author j5ik2o
 */
@Entity
@SuppressWarnings("serial")
public class CustomerHistoryDetail extends AbstractEntity {

    /**
	 * 顧客履歴詳細ID(PK)
	 */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUSTOMER_HISTORY_DETAIL_GEN")
    @SequenceGenerator(name = "CUSTOMER_HISTORY_DETAIL_GEN", sequenceName = "CUSTOMER_HISTORY_DETAIL_SEQ", allocationSize = 1)
    @Column(precision = 19, nullable = false, unique = true)
    public Long customerHistoryDetailId;

    /**
	 * 顧客履歴詳細システムコード(UK)
	 */
    @Column(length = 20, nullable = false, unique = true)
    public String customerHistoryDetailSystemCode;

    /**
	 * 顧客履歴詳細コード(UK)
	 */
    @Column(length = 255, nullable = true, unique = true)
    public String customerHistoryDetailCode;

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
	 * 顧客履歴ID(FK)
	 */
    @Column(precision = 19, nullable = false, unique = false)
    public Long customerHistoryId;

    /**
	 * 履歴定義詳細ID(FK)
	 */
    @Column(precision = 19, nullable = false, unique = false)
    public Long customerHistoryDefineDetailId;

    /**
	 * 値
	 */
    @Column(length = 255, nullable = true, unique = false)
    public String value;

    /**
	 * 顧客履歴 関連プロパティ
	 */
    @ManyToOne
    @JoinColumn(name = "customer_history_id", referencedColumnName = "customer_history_id")
    public CustomerHistory customerHistory;

    /**
	 * 顧客履歴定義詳細 関連プロパティ
	 */
    @ManyToOne
    @JoinColumn(name = "customer_history_define_detail_id", referencedColumnName = "customer_history_define_detail_id")
    public CustomerHistoryDefineDetail customerHistoryDefineDetail;

    @Override
    public String toString() {
        return org.apache.commons.lang.builder.ToStringBuilder.reflectionToString(this);
    }
}
