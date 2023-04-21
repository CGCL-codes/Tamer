package org.tolven.naming;

/**
 * This class allows the parameters to be configured externally via JNDI
 * 
 * @author Joseph Isaac
 *
 */
public interface RealmContext extends TolvenJndiContext {

    public Object getRealmClass();
}
