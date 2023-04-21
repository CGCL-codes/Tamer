package org.nexopenframework.ide.eclipse.model;

import org.dom4j.Document;

/**
 * <p>NexOpen Framework</p>
 *  
 * <p></p>
 * 
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
public class JTAResourceName extends NamedObject implements IJTAResource {

    private String transactionManager;

    private String tmfb = "org.springframework.transaction.jta.JtaTransactionManager";

    private String userTransactionName;

    /***/
    private Document doc;

    public JTAResourceName() {
        super("JTAResource");
    }

    public String getTransactionManager() {
        return transactionManager;
    }

    public String getUserTransactionName() {
        return userTransactionName;
    }

    /**
	 * 
	 * @see org.nexopenframework.ide.eclipse.model.IJTAResource#setTransactionManager(java.lang.String)
	 */
    public void setTransactionManager(final String tm) {
        this.transactionManager = tm;
    }

    public void setUserTransactionName(final String utn) {
        this.userTransactionName = utn;
    }

    /**
	 * 
	 * @see org.nexopenframework.ide.eclipse.model.IJTAResource#getDocument()
	 */
    public Document getDocument() {
        return doc;
    }

    public void setDocument(final Document doc) {
        this.doc = doc;
    }

    public String getTransactionManagerFactoryBean() {
        return tmfb;
    }

    public void setTransactionManagerFactoryBean(final String tmfb) {
        this.tmfb = tmfb;
    }
}
