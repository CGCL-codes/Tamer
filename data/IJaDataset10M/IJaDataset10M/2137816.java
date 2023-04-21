package org.ztemplates.web.request;

import org.ztemplates.web.ZIEncryptionService;
import org.ztemplates.web.ZIExceptionService;
import org.ztemplates.web.ZIMessageService;
import org.ztemplates.web.ZISecurityService;

/**
 * The application request-time environment, these services are fixed and cannot
 * be changed by application
 * 
 * @author gerdziegler.de
 * 
 */
public interface ZIApplicationServiceRepository {

    public ZIEncryptionService getEncryptionService();

    public ZISecurityService getSecurityService();

    public ZIExceptionService getExceptionService();

    public ZIMessageService getMessageService();
}
