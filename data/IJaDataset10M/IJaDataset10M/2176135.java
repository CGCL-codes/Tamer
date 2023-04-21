package com.jaspersoft.jasperserver.api.common.domain;

import com.jaspersoft.jasperserver.api.common.domain.Id;

/**
 * 
 * @author tkavanagh
 * @version $Id: ValidationDetail.java 3 2006-04-30 18:09:42Z sgwood $
 *
 */
public interface ValidationDetail {

    public Id getId();

    /**
	 * 
	 * @return Class - Class of Resource being validated
	 */
    public Class getValidationClass();

    public String getName();

    public String getLabel();

    /**
	 * 
	 * @return String - such as VALID, VALID_STATIC, VALID_DYNAMIC, ERROR
	 */
    public String getResult();

    /**
	 * 
	 * @return String - holds value if result in non-valid
	 */
    public String getMessage();

    /**
	 * 
	 * @return Exception - potentially holds value if result if non-valid
	 */
    public Exception getException();
}
