package com.gorillalogic.faces.beans;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import com.gorillalogic.dal.AccessException;
import com.gorillalogic.dal.Table;
import com.gorillalogic.faces.FacesException;
import com.gorillalogic.faces.FacesRuntimeException;
import com.gorillalogic.faces.util.FacesUtils;
import com.gorillalogic.glob.GLException;

/**
 * Referenced in a value binding expression for eval'ing gcl using []'s. <@link
 * #get> implementation differs from <@link GlSession#get> in that it always
 * returns a GLObject. GlSession.get returns a GLObjectList if the gcl
 * expression evaluates to a table with zero or more than one row.
 */
public class GlObject extends AbstractMap {

    static Logger logger = Logger.getLogger(GlObject.class);

    public static final String SESSION_ATTR = "glObject";

    public Object get(Object gcl) {
        try {
            return GlSession.getCurrentInstance().new MapImpl(FacesUtils.gclToTable((String) gcl).asRow());
        } catch (FacesException e) {
            throw new FacesRuntimeException(e.getMessage());
        } catch (AccessException e) {
            throw new FacesRuntimeException(logger, "Error converting table to row: " + e.getMessage(), e);
        }
    }

    public Set entrySet() {
        logger.warn("entrySet called for GlContext and it's not implemented!");
        return new HashMap().entrySet();
    }
}
