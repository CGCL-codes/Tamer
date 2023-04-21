package com.sisioh.erp.core.entity.accounting;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import com.sisioh.erp.core.entity.AbstractEntity;
import com.sisioh.erp.core.entity.member.Member;

/**
 * 課税取引
 * 
 * @author j5ik2o
 */
@Entity
@SuppressWarnings("serial")
public class TaxTrade extends AbstractEntity {

    /**
	 * 課税取引ID(PK)
	 */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TAX_TRADE_GEN")
    @SequenceGenerator(name = "TAX_TRADE_GEN", sequenceName = "TAX_TRADE_SEQ", allocationSize = 1)
    @Column(precision = 19, nullable = false, unique = true)
    public Long taxTradeId;

    /**
	 * 課税取引システムコード(UK)
	 */
    @Column(length = 20, nullable = false, unique = true)
    public String taxTradeSystemCode;

    /**
	 * 課税取引コード(UK)
	 */
    @Column(length = 255, nullable = true, unique = true)
    public String taxTradeCode;

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
	 * 加盟店ID(FK)
	 */
    @Column(precision = 19, nullable = false, unique = false)
    public Long memberId;

    /**
	 * 課税取引名
	 */
    @Column(length = 20, nullable = false, unique = false)
    public String taxTradeName;

    /**
	 * 税率
	 */
    @Column(precision = 8, scale = 8, nullable = false, unique = false)
    public Float taxRate;

    /**
	 * 勘定科目リスト 関連プロパティ
	 */
    @OneToMany(mappedBy = "taxTrade")
    public List<AccountCode> accountCodeList;

    /**
	 * 加盟店 関連プロパティ
	 */
    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
    public Member member;

    @Override
    public String toString() {
        return org.apache.commons.lang.builder.ToStringBuilder.reflectionToString(this);
    }
}
