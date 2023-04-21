package org.ogce.schemas.gfac.beans.utils;

/**
 * @author Ye Fan
 */
public class ServiceParam {

    public String paramName;

    public String paramValue;

    public String paramDesc;

    /**
	 * @return the paramName
	 */
    public String getParamName() {
        return paramName;
    }

    /**
	 * @param paramName
	 *            the paramName to set
	 */
    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    /**
	 * @return the paramValue
	 */
    public String getParamValue() {
        return paramValue;
    }

    /**
	 * @param paramValue
	 *            the paramValue to set
	 */
    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public void setParamDesc(String paramDesc) {
        this.paramDesc = paramDesc;
    }

    public String getParamDesc() {
        return paramDesc;
    }
}
