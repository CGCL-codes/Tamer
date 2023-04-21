package net.sf.clearwork.service.axis2.server.transaction;

import java.util.List;
import org.springframework.transaction.support.ResourceHolderSupport;
import org.springframework.transaction.support.TransactionSynchronization;

/**
 * Holder for suspended resources. Used by suspend and resume.
 *
 * @author huqi
 * @see #suspend
 * @see #resume
 */
public interface IWebServiceResourcesHolder {

    /**
	 *
	 * @return
	 * @see TransactionSynchronization
	 */
    public List getWebServiceSynchronizations();

    /**
	 *
	 * @return
	 * @see org.springframework.jdbc.datasource.ConnectionHolder
	 * @see org.springframework.orm.hibernate3.SessionHolder
	 */
    public ResourceHolderSupport getWebServiceResources();

    /**
	 * such as hibernate resources like ... <br>
	 *
	 * @return
	 */
    public Object getOtherResources();
}
