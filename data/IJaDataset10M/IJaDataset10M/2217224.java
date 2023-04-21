package com.frameworkset.sqlexecutor;

import com.frameworkset.orm.annotation.PrimaryKey;

public class ParentListBean {

    @PrimaryKey(pkname = "ListBean", auto = true)
    public int id;

    public String fieldName;

    public String fieldLable;

    public String fieldType;

    public String sortorder;

    public boolean isprimaryKey;

    public boolean required;

    public int fieldLength;

    public int isvalidated;

    /**
	 * @return the fieldName
	 */
    public String getFieldName() {
        return fieldName;
    }

    /**
	 * @param fieldName the fieldName to set
	 */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
	 * @return the fieldLable
	 */
    public String getFieldLable() {
        return fieldLable;
    }

    /**
	 * @param fieldLable the fieldLable to set
	 */
    public void setFieldLable(String fieldLable) {
        this.fieldLable = fieldLable;
    }

    /**
	 * @return the fieldType
	 */
    public String getFieldType() {
        return fieldType;
    }

    /**
	 * @param fieldType the fieldType to set
	 */
    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    /**
	 * @return the isprimaryKey
	 */
    public boolean isIsprimaryKey() {
        return isprimaryKey;
    }

    /**
	 * @param isprimaryKey the isprimaryKey to set
	 */
    public void setIsprimaryKey(boolean isprimaryKey) {
        this.isprimaryKey = isprimaryKey;
    }

    /**
	 * @return the required
	 */
    public boolean isRequired() {
        return required;
    }

    /**
	 * @param required the required to set
	 */
    public void setRequired(boolean required) {
        this.required = required;
    }

    /**
	 * @return the fieldLength
	 */
    public int getFieldLength() {
        return fieldLength;
    }

    /**
	 * @param fieldLength the fieldLength to set
	 */
    public void setFieldLength(int fieldLength) {
        this.fieldLength = fieldLength;
    }

    /**
	 * @return the isvalidated
	 */
    public int getIsvalidated() {
        return isvalidated;
    }

    /**
	 * @param isvalidated the isvalidated to set
	 */
    public void setIsvalidated(int isvalidated) {
        this.isvalidated = isvalidated;
    }

    /**
	 * @return the id
	 */
    public int getId() {
        return id;
    }

    /**
	 * @param id the id to set
	 */
    public void setId(int id) {
        this.id = id;
    }

    /**
	 * @return the sortorder
	 */
    public String getSortorder() {
        return sortorder;
    }

    /**
	 * @param sortorder the sortorder to set
	 */
    public void setSortorder(String sortorder) {
        this.sortorder = sortorder;
    }
}
