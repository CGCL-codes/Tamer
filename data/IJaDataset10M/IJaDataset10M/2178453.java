package org.riverock.interfaces.portal.bean;

import java.util.Date;

/**
 * @author Sergei Maslyukov
 *         Date: 29.05.2006
 *         Time: 15:22:19
 */
public interface User {

    public Long getUserId();

    public Long getCompanyId();

    public String getFirstName();

    public String getMiddleName();

    public String getLastName();

    public Date getCreatedDate();

    public Date getDeletedDate();

    public String getAddress();

    public String getPhone();

    public String getEmail();

    public boolean isDeleted();
}
