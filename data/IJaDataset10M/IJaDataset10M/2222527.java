package org.j2eebuilder.view;

import org.j2eebuilder.license.LicenseViolationException;
import java.beans.PropertyChangeListener;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import org.j2eebuilder.BuilderException;
import org.j2eebuilder.model.OrganizationVO;
import org.j2eebuilder.model.TransientObject;

/**
 * @(#)Session.java	1.350 01/12/03
 * Session interface
 * @see java.beans.PropertyChangeListener
 */
public interface Session extends StatelessSession, TransientObject, PropertyChangeListener {

    public static final String INSTANCE_NAME = "sessionBean";

    public static final String IS_MECHANISM_VALID = "getIsMechanismValid";

    public static final String IS_LICENSE_VALID = "getIsLicenseValid";

    /**
     * get/set servletContext
     */
    public ServletContext getServletContext() throws SessionException;

    public void setServletContext(ServletContext ctx) throws SessionException;

    public String getInitParameter(String initParam);

    public java.net.URL getResource(String resourceName) throws java.net.MalformedURLException;

    public java.io.InputStream getResourceAsStream(String resourceName);

    public Object getAttributeFromSession(String name) throws org.j2eebuilder.view.SessionException;

    public void addAttributeToSession(String name, Object value) throws org.j2eebuilder.view.SessionException;

    public void removeAttributeFromSession(String name) throws org.j2eebuilder.view.SessionException;

    public Object getAttributeFromApplication(String name) throws org.j2eebuilder.view.SessionException;

    public void addAttributeToApplication(String name, Object value) throws org.j2eebuilder.view.SessionException;

    public void removeAttributeFromApplication(String name) throws org.j2eebuilder.view.SessionException;

    public String getSessionID() throws SessionException;

    public String getScheme();

    public String getProtocol();

    public String getRemoteHost();

    public String getRemoteAddr();

    public Cookie[] getCookies();

    public String getServerName();

    public Integer getServerPort();

    public String getContextPath();

    public String getCreationTime() throws SessionException;

    public String getLastAccessedTime() throws SessionException;

    public void invalidate() throws SessionException;

    public Boolean getIsLicenseValid() throws LicenseViolationException;

    public org.j2eebuilder.license.LicenseBean getLicense() throws LicenseViolationException;

    public Boolean validateLicense(org.j2eebuilder.license.LicenseBean license);

    public void checkLicenseViolation(org.j2eebuilder.license.LicenseBean license) throws LicenseViolationException;

    /**
     * find user
     */
    public org.j2eebuilder.model.MechanismVO findByLoginInfo(String username, String password, org.j2eebuilder.view.Request requestHelperBean) throws SessionException;

    public org.j2eebuilder.model.MechanismVO findMechanismVO(Integer mechanismID, org.j2eebuilder.view.Request requestHelperBean) throws SessionException;

    public String validate(Request requestHelperBean) throws SessionException;
}
