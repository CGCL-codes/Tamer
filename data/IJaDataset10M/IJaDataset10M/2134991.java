package org.apache.commons.beanutils.priv;

/**
 * Interface that is indirectly implemented by PrivateBean.
 *
 * @author Craig R. McClanahan
 * @version $Revision: 1.6 $ $Date: 2004/02/28 13:18:37 $
 */
public interface PrivateIndirect {

    /**
     * A property accessible via an indirectly implemented interface.
     */
    public String getBaz();

    /**
     * A method accessible via an indirectly implemented interface.
     */
    public String methodBaz(String in);
}
