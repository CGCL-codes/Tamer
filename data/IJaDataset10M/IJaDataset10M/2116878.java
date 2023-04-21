package org.rob.confjsflistener.simplelistener.configuration;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Roberto
 *
 */
public class SimpleListenerConfigurationImpl implements SimpleListenerConfiguration {

    /**
	 * Lists of uriMask. Global and per phase
	 */
    private List<String> uriMask = new LinkedList<String>();

    /**
	 * Check by View Id flag. Global and per Phase
	 */
    private Boolean checkByViewId = Boolean.FALSE;

    /**
	 * Indicates if uriPattern should be checked by JSF uri. Global and per phase
	 */
    private Boolean checkByJSFName = Boolean.TRUE;

    /**
	 * Indicates if PhaseListener will be executed in GET requests 
	 */
    private Boolean allowGetMethod = Boolean.TRUE;

    /**
	 * Indicates if PhaseListener will be executed in POST requests
	 */
    private Boolean allowPostMethod = Boolean.TRUE;

    public Boolean getAllowGetMethod() {
        return this.allowGetMethod;
    }

    public Boolean getAllowPostMethod() {
        return this.allowPostMethod;
    }

    public void setAllowGetMethod(Boolean allowGetMethod) {
        this.allowGetMethod = allowGetMethod;
    }

    public void setAllowPostMethod(Boolean allowPostMethod) {
        this.allowPostMethod = allowPostMethod;
    }

    public List<String> getUriMask() {
        return this.uriMask;
    }

    public Boolean getCheckByViewId() {
        return this.checkByViewId;
    }

    public Boolean getCheckByJSFName() {
        return this.checkByJSFName;
    }

    public void setUriMask(List<String> uriMask) {
        this.uriMask = uriMask;
    }

    public void setCheckByViewId(Boolean checkByViewId) {
        this.checkByViewId = checkByViewId;
    }

    public void setCheckByJSFName(Boolean checkByJSFName) {
        this.checkByJSFName = checkByJSFName;
    }
}
