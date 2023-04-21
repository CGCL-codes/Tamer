package javax.naming.directory;

import java.io.Serializable;
import javax.naming.NamingEnumeration;

/**
 * @author Warren Levy (warrenl@redhat.com)
 * @date June 14, 2001
 */
public interface Attributes extends Cloneable, Serializable {

    boolean isCaseIgnored();

    int size();

    Attribute get(String attrID);

    NamingEnumeration getAll();

    NamingEnumeration getIDs();

    Attribute put(String attrID, Object val);

    Attribute put(Attribute attr);

    Attribute remove(String attrID);

    Object clone();
}
