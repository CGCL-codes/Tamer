package com.sisioh.erp.core.entity.partner.supplier;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import com.sisioh.erp.core.entity.AbstractEntity;
import com.sisioh.erp.core.entity.member.Member;
import com.sisioh.erp.core.entity.partner.Partner;
import com.sisioh.erp.core.entity.purchase.Pay;
import com.sisioh.erp.core.entity.purchase.Purchase;
import com.sisioh.erp.core.entity.purchase.PurchaseOrder;
import com.sisioh.erp.core.entity.sales.product.Product;

/**
 * 仕入先
 * 
 * @author j5ik2o
 */
@Entity
@SuppressWarnings("serial")
public class Supplier extends AbstractEntity {

    /**
	 * 仕入先ID(PK/FK)
	 */
    @Id
    @Column(precision = 19, nullable = false, unique = true)
    public Long supplierId;

    /**
	 * 仕入先システムコード(UK)
	 */
    @Column(length = 20, nullable = false, unique = true)
    public String supplierSystemCode;

    /**
	 * 仕入先コード(UK)
	 */
    @Column(length = 255, nullable = true, unique = true)
    public String supplierCode;

    /**
	 * 加盟店ID(FK)
	 */
    @Column(precision = 19, nullable = false, unique = false)
    public Long memberId;

    /**
	 * 備考
	 */
    @Column(length = 255, nullable = true, unique = false)
    public String remarks;

    /**
	 * 支払リスト 関連プロパティ
	 */
    @OneToMany(mappedBy = "supplier")
    public List<Pay> payList;

    /**
	 * 商品リスト 関連プロパティ
	 */
    @OneToMany(mappedBy = "supplier")
    public List<Product> productList;

    /**
	 * 仕入リスト 関連プロパティ
	 */
    @OneToMany(mappedBy = "supplier")
    public List<Purchase> purchaseList;

    /**
	 * 発注リスト 関連プロパティ
	 */
    @OneToMany(mappedBy = "supplier")
    public List<PurchaseOrder> purchaseOrderList;

    /**
	 * 加盟店 関連プロパティ
	 */
    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
    public Member member;

    /**
	 * 取引先 関連プロパティ
	 */
    @OneToOne
    @JoinColumn(name = "supplier_id", referencedColumnName = "partner_id")
    public Partner partner;

    /**
	 * 仕入先請求設定 関連プロパティ
	 */
    @OneToOne(mappedBy = "supplier")
    public SupplierBillingConfig supplierBillingConfig;

    /**
	 * 仕入先設定 関連プロパティ
	 */
    @OneToOne(mappedBy = "supplier")
    public SupplierConfig supplierConfig;

    /**
	 * 仕入先プロファイル 関連プロパティ
	 */
    @OneToOne(mappedBy = "supplier")
    public SupplierProfile supplierProfile;

    @Override
    public String toString() {
        return org.apache.commons.lang.builder.ToStringBuilder.reflectionToString(this);
    }
}
