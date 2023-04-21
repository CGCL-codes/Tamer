package com.jspx.core.license.core;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User:chenYuan (mail:cayurain@21cn.com)
 * Date: 2005-1-5
 * Time: 10:30:34
 *
 */
public interface LicenseBean extends Externalizable {

    /**
     * Get the start date.
     *
     * @return The start date.
     */
    Date getStartDate();

    /**
     * Get the expired date.
     *
     * @return The expired date.
     */
    Date getEndDate();

    /**
     * Get the assistant key string to generate the license.
     *
     * @return The assistant key string to generate the license.
     */
    String getAssistantKey();

    String Checkout();

    String getKey();

    String getUser();
}
