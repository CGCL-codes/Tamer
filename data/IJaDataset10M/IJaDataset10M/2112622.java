package com.dsp.bean;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.AccessType;
import com.core.util.BaseBean;

@Entity
@Table(name = "company_accounts")
public class CompanyAccount extends BaseBean {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @AccessType(value = "property")
    @Column(name = "id")
    private Long id;

    @AccessType(value = "property")
    @Column(name = "company_type")
    private Integer companyType;

    @AccessType(value = "property")
    @Column(name = "fix_value")
    private BigDecimal fixValue;

    @AccessType(value = "property")
    @Column(name = "rate")
    private BigDecimal rate;

    @AccessType(value = "property")
    @Column(name = "company_lower")
    private BigDecimal companyLower = new BigDecimal(0);

    @AccessType(value = "property")
    @Column(name = "company_limit")
    private BigDecimal companyLimit = new BigDecimal(99999);

    @AccessType(value = "property")
    @Column(name = "company_round")
    private Integer companyRound;

    @AccessType(value = "property")
    @Column(name = "province")
    private String province;

    @AccessType(value = "property")
    @Column(name = "city")
    private String city;

    @AccessType(value = "property")
    @Column(name = "area")
    private String area;

    @AccessType(value = "property")
    @Column(name = "hukou")
    private Integer hukou;

    @AccessType(value = "property")
    @Column(name = "item_id")
    private Integer itemId;

    /**
	 * @return the id
	 */
    public Long getId() {
        return id;
    }

    /**
	 * @return the companyType
	 */
    public Integer getCompanyType() {
        return companyType;
    }

    /**
	 * @return the fixValue
	 */
    public BigDecimal getFixValue() {
        return fixValue;
    }

    /**
	 * @return the rate
	 */
    public BigDecimal getRate() {
        return rate;
    }

    /**
	 * @return the companyLower
	 */
    public BigDecimal getCompanyLower() {
        return companyLower;
    }

    /**
	 * @return the companyLimit
	 */
    public BigDecimal getCompanyLimit() {
        return companyLimit;
    }

    /**
	 * @return the companyRound
	 */
    public Integer getCompanyRound() {
        return companyRound;
    }

    /**
	 * @param id the id to set
	 */
    public void setId(Long id) {
        this.id = id;
    }

    /**
	 * @param companyType the companyType to set
	 */
    public void setCompanyType(Integer companyType) {
        this.companyType = companyType;
    }

    /**
	 * @param fixValue the fixValue to set
	 */
    public void setFixValue(BigDecimal fixValue) {
        this.fixValue = fixValue;
    }

    /**
	 * @param rate the rate to set
	 */
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    /**
	 * @param companyLower the companyLower to set
	 */
    public void setCompanyLower(BigDecimal companyLower) {
        this.companyLower = companyLower;
    }

    /**
	 * @param companyLimit the companyLimit to set
	 */
    public void setCompanyLimit(BigDecimal companyLimit) {
        this.companyLimit = companyLimit;
    }

    /**
	 * @param companyRound the companyRound to set
	 */
    public void setCompanyRound(Integer companyRound) {
        this.companyRound = companyRound;
    }

    /**
	 * @return the province
	 */
    public String getProvince() {
        return province;
    }

    /**
	 * @return the city
	 */
    public String getCity() {
        return city;
    }

    /**
	 * @return the area
	 */
    public String getArea() {
        return area;
    }

    /**
	 * @return the hukou
	 */
    public Integer getHukou() {
        return hukou;
    }

    /**
	 * @return the itemId
	 */
    public Integer getItemId() {
        return itemId;
    }

    /**
	 * @param province the province to set
	 */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
	 * @param city the city to set
	 */
    public void setCity(String city) {
        this.city = city;
    }

    /**
	 * @param area the area to set
	 */
    public void setArea(String area) {
        this.area = area;
    }

    /**
	 * @param hukou the hukou to set
	 */
    public void setHukou(Integer hukou) {
        this.hukou = hukou;
    }

    /**
	 * @param itemId the itemId to set
	 */
    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }
}
