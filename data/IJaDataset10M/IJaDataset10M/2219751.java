package org.codehaus.groovy.grails.plugins.acegi;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.User;

/**
 * extends acegi's User class 
 * to can set Grails Domain Class at login,
 * for able to load auth class from context
 * @author T.Yamamoto
 *
 */
public class GrailsUser extends User {

    private static final long serialVersionUID = 6089520028448407157L;

    /** Grails Domain class Object */
    private Object domainClass;

    public GrailsUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, GrantedAuthority[] authorities) throws IllegalArgumentException {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    /**
	 * Returns Domain class Object
	 * @return
	 */
    public Object getDomainClass() {
        return domainClass;
    }

    /**
	 * set Domain class Object
	 * @param domainClass
	 */
    public void setDomainClass(Object domainClass) {
        this.domainClass = domainClass;
    }
}
