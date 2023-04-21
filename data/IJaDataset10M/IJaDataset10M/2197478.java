package net.sf.irunninglog.service;

/**
 * Generic service interface.  Any service within the application should
 * implement this interface.
 *
 * <p/>
 *
 * <code>IService</code> instances may be retrieved from the
 * <code>ServiceFactory</code>'s <code>newService</code> method.  In order to
 * accomplish this, any implementing class that can be accessed via the
 * factory must provide a zero-argument constructor (althought the constructor
 * need not be public).
 *
 * @author <a href="mailto:allan_e_lewis@yahoo.com">Allan Lewis</a>
 * @version $Revision: 1.1.1.1 $ $Date: 2005/06/23 01:48:57 $
 * @since iRunningLog 1.0
 * @see net.sf.irunninglog.service.ServiceFactory#newService(String)
 */
public interface IService {
}
