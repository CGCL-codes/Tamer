package org.appspy.server.bo;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Olivier HEDIN / olivier@appspy.org
 */
@Entity
@Table(name = "METHOD")
public class Method extends Feature {

    @Basic
    @Column(name = "className")
    protected String mClassName;

    @Basic
    @Column(name = "methodName")
    protected String mMethodName;

    /**
	 * @return the className
	 */
    public String getClassName() {
        return mClassName;
    }

    /**
	 * @param className the className to set
	 */
    public void setClassName(String className) {
        this.mClassName = className;
    }

    /**
	 * @return the methodName
	 */
    public String getMethodName() {
        return mMethodName;
    }

    /**
	 * @param methodName the methodName to set
	 */
    public void setMethodName(String methodName) {
        this.mMethodName = methodName;
    }
}
