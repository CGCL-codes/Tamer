package com.gvecom.cihai.resolver.bean;

import org.apache.commons.lang.StringUtils;

/**
 * 企业联系人数据封装 Create by 2011-12-3
 * 
 * @author liuweijava
 * @copyright Copyright (c) 2011-2013 Gvecom
 */
public class ContacterBean {

    /** 联系人名字 */
    private String contacterName;

    /** 联系人职位 */
    private String position;

    /** 移动电话 */
    private String mobile;

    /** 联系电话 */
    private String phone;

    /** 传真 */
    private String fax;

    /** 企业网站 */
    private String webSite;

    /** 邮编 */
    private String post;

    /** 电子邮箱 */
    private String email;

    /** 区号 */
    private String preNum;

    /** Default construct method */
    public ContacterBean() {
    }

    /** Full construct method */
    public ContacterBean(String contacterName, String position, String mobile, String phone, String fax, String webSite, String post, String email, String preNum) {
        this.contacterName = contacterName;
        this.position = position;
        this.mobile = mobile;
        this.phone = phone;
        this.fax = fax;
        this.webSite = webSite;
        this.post = post;
        this.email = email;
        this.preNum = preNum;
    }

    /**
	 * Get name
	 * Create by 2011-12-3
	 * @author liuweijava
	 * @return
	 */
    public String getContacterName() {
        return this.contacterName;
    }

    /**
	 * Set name
	 * Create by 2011-12-3
	 * @author liuweijava
	 * @param name
	 */
    public void setContacterName(String contacterName) {
        this.contacterName = contacterName;
    }

    /**
	 * Get position
	 * Create by 2011-12-3
	 * @author liuweijava
	 * @return
	 */
    public String getPosition() {
        return this.position;
    }

    /**
	 * Set position
	 * Create by 2011-12-3
	 * @author liuweijava
	 * @param position
	 */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
	 * Get mobile
	 * Create by 2011-12-3
	 * @author liuweijava
	 * @return
	 */
    public String getMobile() {
        return this.mobile;
    }

    /**
	 * Set mobile
	 * Create by 2011-12-3
	 * @author liuweijava
	 * @param mobile
	 */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
	 * Get phone
	 * Create by 2011-12-3
	 * @author liuweijava
	 * @return
	 */
    public String getPhone() {
        return this.phone;
    }

    /**
	 * Set phone
	 * Create by 2011-12-3
	 * @author liuweijava
	 * @param phone
	 */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
	 * Get fax
	 * Create by 2011-12-3
	 * @author liuweijava
	 * @return
	 */
    public String getFax() {
        return this.fax;
    }

    /**
	 * Set fax
	 * Create by 2011-12-3
	 * @author liuweijava
	 * @param fax
	 */
    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
	 * Get webSite
	 * Create by 2011-12-3
	 * @author liuweijava
	 * @return
	 */
    public String getWebSite() {
        return this.webSite;
    }

    /**
	 * Set webSite
	 * Create by 2011-12-3
	 * @author liuweijava
	 * @param webSite
	 */
    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    /**
	 * Get post
	 * Create by 2011-12-3
	 * @author liuweijava
	 * @return
	 */
    public String getPost() {
        return this.post;
    }

    /**
	 * Set post
	 * Create by 2011-12-3
	 * @author liuweijava
	 * @param post
	 */
    public void setPost(String post) {
        this.post = post;
    }

    /**
	 * Get email
	 * Create by 2011-12-3
	 * @author liuweijava
	 * @return
	 */
    public String getEmail() {
        return this.email;
    }

    /**
	 * Set email
	 * Create by 2011-12-3
	 * @author liuweijava
	 * @param email
	 */
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPreNum() {
        return this.preNum;
    }

    public void setPreNum(String preNum) {
        this.preNum = preNum;
    }
}
