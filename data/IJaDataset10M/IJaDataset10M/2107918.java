package edu.ucla.cs.typecast.discovery;

import java.io.Serializable;
import java.util.Collection;
import edu.ucla.cs.typecast.util.TypeUtil;

/**
 *
 * @date Aug 7, 2007
 */
public class ServiceTemplate implements Serializable {

    private Class[] types;

    private volatile Collection<Class> allTypes;

    private long seqno;

    public ServiceTemplate(Class[] types) {
        this.types = types;
        allTypes = TypeUtil.getAllTypes(types);
    }

    public ServiceTemplate(Class[] types, long seqno) {
        this.types = types;
        allTypes = TypeUtil.getAllTypes(types);
        this.seqno = seqno;
    }

    public long getSeqno() {
        return this.seqno;
    }

    /**
	 * @return the types
	 */
    public Class[] getTypes() {
        return types;
    }

    /**
	 * @param types the types to set
	 */
    public void setTypes(Class[] types) {
        this.types = types;
    }

    public boolean match(ServiceAdvertisement sa) {
        Collection<Class> allAdvTypes = TypeUtil.getAllTypes(sa.getTypes());
        for (int i = 0; i < types.length; i++) {
            if (!allAdvTypes.contains(types[i])) {
                return false;
            }
        }
        return true;
    }
}
