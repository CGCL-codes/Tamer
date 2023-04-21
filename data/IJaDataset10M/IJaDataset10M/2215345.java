package javax.faces.component;

import java.io.Serializable;

/**
 * @author Simon Lessard (latest modification by $Author: slessard $)
 * @version $Revision: 696523 $ $Date: 2009-03-21 10:40:14 -0400 (mer., 17 sept. 2008) $
 * 
 * @since 2.0
 */
public interface StateHelper extends StateHolder {

    public void add(Serializable key, Object value);

    public Object eval(Serializable key);

    public Object eval(Serializable key, Object defaultValue);

    public Object get(Serializable key);

    public Object put(Serializable key, Object value);

    public Object put(Serializable key, String mapKey, Object value);

    public Object remove(Serializable key);

    public Object remove(Serializable key, Object valueOrKey);
}
