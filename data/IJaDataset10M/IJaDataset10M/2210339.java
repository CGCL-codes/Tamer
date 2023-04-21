package net.shopxx.entity;

import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;
import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.shopxx.bean.ProductImage;
import org.apache.commons.lang.StringUtils;
import org.compass.annotations.Index;
import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableProperty;
import org.compass.annotations.Store;

/**
 * 实体类 - 公司
 * ============================================================================
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 提示：在未取得SHOP++商业授权之前，您不能将本软件应用于商业用途，否则SHOP++将保留追究的权力。
 * ----------------------------------------------------------------------------
 * 官方网站：http://www.shopxx.net
 * ----------------------------------------------------------------------------
 * KEY: SHOPXXA422025AB3BAEE5940EB4488D12B6691
 * ============================================================================
 */
@Entity
@Searchable
public class Company extends BaseEntity {

    private static final long serialVersionUID = 4858058186018438872L;

    public static final int MAX_BEST_PRODUCT_LIST_COUNT = 20;

    public static final int MAX_NEW_PRODUCT_LIST_COUNT = 20;

    public static final int MAX_HOT_PRODUCT_LIST_COUNT = 20;

    public static final int DEFAULT_PRODUCT_LIST_PAGE_SIZE = 12;

    private String people;

    private String name;

    private Integer point;

    private Boolean isBest;

    private Boolean isNew;

    private Boolean isHot;

    private Boolean isMarketable;

    private String address;

    private String hangye;

    private String phone;

    private String fix;

    private String email;

    private String website;

    private String description;

    private String options;

    private String metaKeywords;

    private String metaDescription;

    private String htmlFilePath;

    private String productImageListStore;

    private CompanyCategory companyCategory;

    private Member member;

    private Brand brand;

    private Set<Product> productSet;

    private String chart;

    @ManyToOne(fetch = FetchType.LAZY)
    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public String getChart() {
        return chart;
    }

    public void setChart(String chart) {
        this.chart = chart;
    }

    public String getHangye() {
        return hangye;
    }

    public void setHangye(String hangye) {
        this.hangye = hangye;
    }

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    public Set<Product> getProductSet() {
        return productSet;
    }

    public void setProductSet(Set<Product> productSet) {
        this.productSet = productSet;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    @SearchableProperty(store = Store.NO)
    @Column(nullable = false)
    public Boolean getIsMarketable() {
        return isMarketable;
    }

    public void setIsMarketable(Boolean isMarketable) {
        this.isMarketable = isMarketable;
    }

    @SearchableProperty(store = Store.YES)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @SearchableProperty(store = Store.YES)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFix() {
        return fix;
    }

    public void setFix(String fix) {
        this.fix = fix;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @SearchableProperty(store = Store.YES)
    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    @SearchableProperty(store = Store.YES)
    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(nullable = false)
    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        if (point == null || point < 0) {
            point = 0;
        }
        this.point = point;
    }

    @SearchableProperty(store = Store.NO)
    @Column(nullable = false)
    public Boolean getIsBest() {
        return isBest;
    }

    public void setIsBest(Boolean isBest) {
        this.isBest = isBest;
    }

    @SearchableProperty(store = Store.NO)
    @Column(nullable = false)
    public Boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }

    @SearchableProperty(store = Store.NO)
    @Column(nullable = false)
    public Boolean getIsHot() {
        return isHot;
    }

    public void setIsHot(Boolean isHot) {
        this.isHot = isHot;
    }

    @Column(length = 10000)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(length = 5000)
    public String getMetaKeywords() {
        return metaKeywords;
    }

    public void setMetaKeywords(String metaKeywords) {
        this.metaKeywords = metaKeywords;
    }

    @Column(length = 5000)
    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    @SearchableProperty(index = Index.NO, store = Store.YES)
    @Column(nullable = false, updatable = false)
    public String getHtmlFilePath() {
        return htmlFilePath;
    }

    public void setHtmlFilePath(String htmlFilePath) {
        this.htmlFilePath = htmlFilePath;
    }

    @SearchableProperty(store = Store.YES)
    @Column(length = 10000)
    public String getProductImageListStore() {
        return productImageListStore;
    }

    public void setProductImageListStore(String productImageListStore) {
        this.productImageListStore = productImageListStore;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public CompanyCategory getCompanyCategory() {
        return companyCategory;
    }

    public void setCompanyCategory(CompanyCategory companyCategory) {
        this.companyCategory = companyCategory;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    @SuppressWarnings("unchecked")
    @Transient
    public List<ProductImage> getProductImageList() {
        if (StringUtils.isEmpty(productImageListStore)) {
            return null;
        }
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setRootClass(ProductImage.class);
        JSONArray jsonArray = JSONArray.fromObject(productImageListStore);
        return (List<ProductImage>) JSONSerializer.toJava(jsonArray, jsonConfig);
    }

    @Transient
    public void setProductImageList(List<ProductImage> productImageList) {
        if (productImageList == null || productImageList.size() == 0) {
            productImageListStore = null;
            return;
        }
        JSONArray jsonArray = JSONArray.fromObject(productImageList);
        productImageListStore = jsonArray.toString();
    }

    /**
	 * 根据商品图片ID获取商品图片，未找到则返回null
	 * 
	 * @param ProductImage
	 *            ProductImage对象
	 */
    @Transient
    public ProductImage getProductImage(String productImageId) {
        List<ProductImage> productImageList = getProductImageList();
        for (ProductImage productImage : productImageList) {
            if (StringUtils.equals(productImageId, productImage.getId())) {
                return productImage;
            }
        }
        return null;
    }
}
