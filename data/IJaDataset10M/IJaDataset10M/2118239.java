package com.sisioh.erp.core.entity.partner.customer;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import com.sisioh.erp.core.entity.AbstractEntity;
import com.sisioh.erp.core.entity.member.Member;

/**
 * 顧客側担当者
 * 
 * @author j5ik2o
 */
@Entity
@SuppressWarnings("serial")
public class CustomerInCharge extends AbstractEntity {

    /**
	 * 顧客側担当者ID(PK)
	 */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUSTOMER_IN_CHARGE_GEN")
    @SequenceGenerator(name = "CUSTOMER_IN_CHARGE_GEN", sequenceName = "CUSTOMER_IN_CHARGE_SEQ", allocationSize = 1)
    @Column(precision = 19, nullable = false, unique = true)
    public Long customerInChargeId;

    /**
	 * 顧客側担当者システムコード(UK)
	 */
    @Column(length = 20, nullable = false, unique = true)
    public String customerInChargeSystemCode;

    /**
	 * 顧客側担当者コード(UK)
	 */
    @Column(length = 20, nullable = true, unique = true)
    public String customerInChargeCode;

    /**
	 * 加盟店ID(FK)
	 */
    @Column(precision = 19, nullable = false, unique = false)
    public Long memberId;

    /**
	 * 顧客ID(FK)
	 */
    @Column(precision = 19, nullable = false, unique = false)
    public Long customerId;

    /**
	 * 顧客枝番(FK)
	 */
    @Column(precision = 10, nullable = false, unique = false)
    public Integer customerSubId;

    /**
	 * 備考
	 */
    @Column(length = 255, nullable = true, unique = false)
    public String remarks;

    /**
	 * 加盟店 関連プロパティ
	 */
    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
    public Member member;

    /**
	 * 顧客側担当者設定 関連プロパティ
	 */
    @OneToOne(mappedBy = "customerInCharge")
    public CustomerInChargeConfig customerInChargeConfig;

    /**
	 * 顧客側担当者画像 関連プロパティ
	 */
    @OneToMany(mappedBy = "customerInCharge")
    public List<CustomerInChargeImage> customerInChargeImageList;

    /**
	 * 顧客側担当者プロファイル 関連プロパティ
	 */
    @OneToOne(mappedBy = "customerInCharge")
    public CustomerInChargeProfile customerInChargeProfile;

    /**
	 * 顧客側担当者プロファイル連絡先 関連プロパティ
	 */
    @OneToOne(mappedBy = "customerInCharge")
    public CustomerInChargeProfileContact customerInChargeProfileContact;

    @Override
    public String toString() {
        return org.apache.commons.lang.builder.ToStringBuilder.reflectionToString(this);
    }
}
