package org.rascalli.framework.core;

import java.util.Map;

/**
 * <p>
 * 
 * </p>
 * 
 * <p>
 * <b>Company:&nbsp;</b> SAT, Research Studios Austria
 * </p>
 * 
 * <p>
 * <b>Copyright:&nbsp;</b> (c) 2007
 * </p>
 * 
 * <p>
 * <b>last modified:</b><br/> $Author: christian $<br/> $Date: 2008-08-26
 * 16:27:29 +0200 (Di, 26 Aug 2008) $<br/> $Revision: 2446 $
 * </p>
 * 
 * @author Christian Schollum
 */
public final class PropertyCreated extends PropertyChange {

    /**
     * @param type
     * @param key
     * @param oldValue
     * @param newValue
     */
    public PropertyCreated(String key, Object value) {
        super(Type.CREATED, key, null, value);
    }

    @Override
    public void applyTo(Map<String, Object> properties) {
        properties.put(getKey(), getNewValue());
    }
}
