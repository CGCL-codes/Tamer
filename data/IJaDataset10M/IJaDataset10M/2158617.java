package cn.mmbook.platform.model.tag;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import java.util.*;
import javacommon.base.*;
import javacommon.util.*;
import cn.org.rapid_framework.util.*;
import cn.org.rapid_framework.web.util.*;
import cn.org.rapid_framework.page.*;
import cn.org.rapid_framework.page.impl.*;
import cn.mmbook.platform.model.tag.*;
import cn.mmbook.platform.dao.tag.*;
import cn.mmbook.platform.service.tag.impl.*;
import cn.mmbook.platform.service.tag.*;

/**
 * 
 * @author 自强  zqiangzhang@gmail.com
 *
 */
public class CmsTagContent extends BaseEntity {

    public static final String FORMAT_INSERT_TIME_ = DATE_TIME_FORMAT;

    public static final String FORMAT_UPDATE_TIME_ = DATE_TIME_FORMAT;

    public static final String FORMAT_MAKE_TIME_ = DATE_TIME_FORMAT;

    private java.lang.String id;

    private java.lang.String templetId;

    private java.lang.String categoryId;

    private java.lang.String tagName;

    private java.lang.String tagNameString;

    private java.lang.String tagNameValue;

    private java.lang.String ifPage;

    private java.lang.Integer rowNums;

    private java.sql.Timestamp insertTime;

    private java.lang.String insertTimeString;

    private java.lang.String posId;

    private java.lang.String keyId;

    private java.lang.String modelId;

    private java.sql.Timestamp updateTime;

    private java.lang.String updateTimeString;

    private java.lang.String tagExp;

    private java.lang.String orderType;

    private java.lang.String versionId;

    private java.lang.String companyId;

    private java.lang.String fieldsCode;

    private java.lang.String fieldsCodeString;

    private java.lang.String makePeople;

    private java.sql.Timestamp makeTime;

    private java.lang.String makeTimeString;

    private java.lang.String ifsale;

    private java.lang.String cmsModelStore;

    private java.lang.String posStore;

    private java.lang.String continueModeNo;

    public CmsTagContent() {
    }

    public CmsTagContent(java.lang.String id) {
        this.id = id;
    }

    public void setId(java.lang.String value) {
        this.id = value;
    }

    public java.lang.String getId() {
        return this.id;
    }

    public void setTempletId(java.lang.String value) {
        this.templetId = value;
    }

    public java.lang.String getTempletId() {
        return this.templetId;
    }

    public void setCategoryId(java.lang.String value) {
        this.categoryId = value;
    }

    public java.lang.String getCategoryId() {
        return this.categoryId;
    }

    public void setTagName(java.lang.String value) {
        this.tagName = value;
    }

    public java.lang.String getTagName() {
        return this.tagName;
    }

    public void setIfPage(java.lang.String value) {
        this.ifPage = value;
    }

    public java.lang.String getIfPage() {
        return this.ifPage;
    }

    public void setRowNums(java.lang.Integer value) {
        this.rowNums = value;
    }

    public java.lang.Integer getRowNums() {
        return this.rowNums;
    }

    public void setInsertTime(java.sql.Timestamp value) {
        this.insertTime = value;
        setInsertTimeString(date2String(value, FORMAT_INSERT_TIME_));
    }

    public java.sql.Timestamp getInsertTime() {
        return this.insertTime;
    }

    public void setPosId(java.lang.String value) {
        this.posId = value;
    }

    public java.lang.String getPosId() {
        return this.posId;
    }

    public void setKeyId(java.lang.String value) {
        this.keyId = value;
    }

    public java.lang.String getKeyId() {
        return this.keyId;
    }

    public void setModelId(java.lang.String value) {
        this.modelId = value;
    }

    public java.lang.String getModelId() {
        return this.modelId;
    }

    public void setUpdateTime(java.sql.Timestamp value) {
        this.updateTime = value;
        setUpdateTimeString(date2String(value, FORMAT_UPDATE_TIME_));
    }

    public java.sql.Timestamp getUpdateTime() {
        return this.updateTime;
    }

    public void setTagExp(java.lang.String value) {
        this.tagExp = value;
    }

    public java.lang.String getTagExp() {
        return this.tagExp;
    }

    public void setOrderType(java.lang.String value) {
        this.orderType = value;
    }

    public java.lang.String getOrderType() {
        return this.orderType;
    }

    public void setVersionId(java.lang.String value) {
        this.versionId = value;
    }

    public java.lang.String getVersionId() {
        return this.versionId;
    }

    public void setCompanyId(java.lang.String value) {
        this.companyId = value;
    }

    public java.lang.String getCompanyId() {
        return this.companyId;
    }

    public void setFieldsCode(java.lang.String value) {
        this.fieldsCode = value;
    }

    public java.lang.String getFieldsCode() {
        return this.fieldsCode;
    }

    public java.lang.String getFieldsCodeString() {
        return fieldsCodeString;
    }

    public void setFieldsCodeString(java.lang.String fieldsCodeString) {
        this.fieldsCodeString = fieldsCodeString;
        setFieldsCode(fieldsCodeString);
    }

    public void setMakePeople(java.lang.String value) {
        this.makePeople = value;
    }

    public java.lang.String getMakePeople() {
        return this.makePeople;
    }

    public void setMakeTime(java.sql.Timestamp value) {
        this.makeTime = value;
        setMakeTimeString(date2String(value, FORMAT_MAKE_TIME_));
    }

    public java.sql.Timestamp getMakeTime() {
        return this.makeTime;
    }

    public void setIfsale(java.lang.String value) {
        this.ifsale = value;
    }

    public java.lang.String getIfsale() {
        return this.ifsale;
    }

    public String toString() {
        return new ToStringBuilder(this).append("Id", getId()).append("TempletId", getTempletId()).append("CategoryId", getCategoryId()).append("TagName", getTagName()).append("IfPage", getIfPage()).append("RowNum", getRowNums()).append("InsertTime", getInsertTime()).append("PosId", getPosId()).append("KeyId", getKeyId()).append("ModelId", getModelId()).append("UpdateTime", getUpdateTime()).append("TagExp", getTagExp()).append("OrderType", getOrderType()).append("VersionId", getVersionId()).append("CompanyId", getCompanyId()).append("FieldsCode", getFieldsCode()).append("MakePeople", getMakePeople()).append("MakeTime", getMakeTime()).append("Ifsale", getIfsale()).toString();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getId()).append(getTempletId()).append(getCategoryId()).append(getTagName()).append(getIfPage()).append(getRowNums()).append(getInsertTime()).append(getPosId()).append(getKeyId()).append(getModelId()).append(getUpdateTime()).append(getTagExp()).append(getOrderType()).append(getVersionId()).append(getCompanyId()).append(getFieldsCode()).append(getMakePeople()).append(getMakeTime()).append(getIfsale()).toHashCode();
    }

    public boolean equals(Object obj) {
        if (obj instanceof CmsTagContent == false) return false;
        if (this == obj) return true;
        CmsTagContent other = (CmsTagContent) obj;
        return new EqualsBuilder().append(getId(), other.getId()).append(getTempletId(), other.getTempletId()).append(getCategoryId(), other.getCategoryId()).append(getTagName(), other.getTagName()).append(getIfPage(), other.getIfPage()).append(getRowNums(), other.getRowNums()).append(getInsertTime(), other.getInsertTime()).append(getPosId(), other.getPosId()).append(getKeyId(), other.getKeyId()).append(getModelId(), other.getModelId()).append(getUpdateTime(), other.getUpdateTime()).append(getTagExp(), other.getTagExp()).append(getOrderType(), other.getOrderType()).append(getVersionId(), other.getVersionId()).append(getCompanyId(), other.getCompanyId()).append(getFieldsCode(), other.getFieldsCode()).append(getMakePeople(), other.getMakePeople()).append(getMakeTime(), other.getMakeTime()).append(getIfsale(), other.getIfsale()).isEquals();
    }

    public java.lang.String getCmsModelStore() {
        return cmsModelStore;
    }

    public void setCmsModelStore(java.lang.String cmsModelStore) {
        this.cmsModelStore = cmsModelStore;
    }

    public java.lang.String getPosStore() {
        return posStore;
    }

    public void setPosStore(java.lang.String posStore) {
        this.posStore = posStore;
    }

    public java.lang.String getTagNameString() {
        return tagName;
    }

    public void setTagNameString(java.lang.String tagNameString) {
        this.tagNameString = tagNameString;
        this.tagName = tagNameString;
    }

    public java.lang.String getInsertTimeString() {
        return insertTimeString;
    }

    public void setInsertTimeString(java.lang.String insertTimeString) {
        this.insertTimeString = insertTimeString;
    }

    public java.lang.String getUpdateTimeString() {
        return updateTimeString;
    }

    public void setUpdateTimeString(java.lang.String updateTimeString) {
        this.updateTimeString = updateTimeString;
    }

    public java.lang.String getMakeTimeString() {
        return makeTimeString;
    }

    public void setMakeTimeString(java.lang.String makeTimeString) {
        this.makeTimeString = makeTimeString;
    }

    public java.lang.String getTagNameValue() {
        this.setTagNameValue("");
        return this.tagNameValue;
    }

    public void setTagNameValue(java.lang.String tagNameValue) {
        this.tagNameValue = "{" + this.tagName + "}";
    }

    public java.lang.String getContinueModeNo() {
        return continueModeNo;
    }

    public void setContinueModeNo(java.lang.String continueModeNo) {
        this.continueModeNo = continueModeNo;
    }
}
