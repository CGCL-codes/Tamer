package com.kongur.network.erp.domain.sc;

/***
 *
 */
public class ConsignInfoDO {

    private Long id;

    private Long groupId;

    private Long shopId;

    private String sellerNick;

    private String contactPerson;

    private String province;

    private String city;

    private String district;

    private String address;

    private String postCode;

    private String phone;

    private String mobile;

    private String companyName;

    private String description;

    /***
	 * Get id
	 */
    public Long getId() {
        return id;
    }

    /***
	 * Set id
	 * @param id id
	 */
    public void setId(Long id) {
        this.id = id;
    }

    /***
	 * Get groupId
	 */
    public Long getGroupId() {
        return groupId;
    }

    /***
	 * Set groupId
	 * @param groupId groupId
	 */
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    /***
	 * Get shopId
	 */
    public Long getShopId() {
        return shopId;
    }

    /***
	 * Set shopId
	 * @param shopId shopId
	 */
    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    /***
	 * Get sellerNick
	 */
    public String getSellerNick() {
        return sellerNick;
    }

    /***
	 * Set sellerNick
	 * @param sellerNick sellerNick
	 */
    public void setSellerNick(String sellerNick) {
        this.sellerNick = sellerNick;
    }

    /***
	 * Get contactPerson
	 */
    public String getContactPerson() {
        return contactPerson;
    }

    /***
	 * Set contactPerson
	 * @param contactPerson contactPerson
	 */
    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    /***
	 * Get province
	 */
    public String getProvince() {
        return province;
    }

    /***
	 * Set province
	 * @param province province
	 */
    public void setProvince(String province) {
        this.province = province;
    }

    /***
	 * Get city
	 */
    public String getCity() {
        return city;
    }

    /***
	 * Set city
	 * @param city city
	 */
    public void setCity(String city) {
        this.city = city;
    }

    /***
	 * Get district
	 */
    public String getDistrict() {
        return district;
    }

    /***
	 * Set district
	 * @param district district
	 */
    public void setDistrict(String district) {
        this.district = district;
    }

    /***
	 * Get address
	 */
    public String getAddress() {
        return address;
    }

    /***
	 * Set address
	 * @param address address
	 */
    public void setAddress(String address) {
        this.address = address;
    }

    /***
	 * Get postCode
	 */
    public String getPostCode() {
        return postCode;
    }

    /***
	 * Set postCode
	 * @param postCode postCode
	 */
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    /***
	 * Get phone
	 */
    public String getPhone() {
        return phone;
    }

    /***
	 * Set phone
	 * @param phone phone
	 */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /***
	 * Get mobile
	 */
    public String getMobile() {
        return mobile;
    }

    /***
	 * Set mobile
	 * @param mobile mobile
	 */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /***
	 * Get companyName
	 */
    public String getCompanyName() {
        return companyName;
    }

    /***
	 * Set companyName
	 * @param companyName companyName
	 */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /***
	 * Get description
	 */
    public String getDescription() {
        return description;
    }

    /***
	 * Set description
	 * @param description description
	 */
    public void setDescription(String description) {
        this.description = description;
    }

    /***
	 * 
	 */
    @Override
    public String toString() {
        StringBuffer mb = new StringBuffer();
        mb.append("id" + id);
        mb.append("groupId" + groupId);
        mb.append("shopId" + shopId);
        mb.append("sellerNick" + sellerNick);
        mb.append("contactPerson" + contactPerson);
        mb.append("province" + province);
        mb.append("city" + city);
        mb.append("district" + district);
        mb.append("address" + address);
        mb.append("postCode" + postCode);
        mb.append("phone" + phone);
        mb.append("mobile" + mobile);
        mb.append("companyName" + companyName);
        mb.append("description" + description);
        return new StringBuilder().append("ConsignInfoDO").append(mb).toString();
    }
}
