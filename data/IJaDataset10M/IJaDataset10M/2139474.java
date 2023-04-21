package royere.cwi.util;

import java.util.*;

/**
* Class Visuals. This is a simple data structure to encapsulate all the data
* which can be made available when the user has performed a node pick on the screen.
*
* @author Ivan Herman
*/
public class ClassVisuals implements java.io.Serializable {

    public Visuals defaultNodeVisuals = null;

    public Visuals defaultEdgeVisuals = null;

    public HashMap classNodeVisuals = new HashMap();

    public HashMap classEdgeVisuals = new HashMap();

    public double defaultNodeSize = 0.0;

    public ClassVisuals() {
        ;
    }

    public ClassVisuals(ClassVisuals theOther) {
        copy(theOther);
    }

    public boolean isEmpty() {
        return (defaultNodeVisuals == null || defaultNodeVisuals.isEmpty()) && (defaultEdgeVisuals == null || defaultEdgeVisuals.isEmpty()) && classNodeVisuals.size() == 0 && classEdgeVisuals.size() == 0;
    }

    /**
    * A simple copy of the content, without any further check. Beware: the object <em>references</em>
    * and not their values are copied!
    *
    * @param theOther the other visual to copy from. If null, the method is void.
    */
    protected void copy(ClassVisuals theOther) {
        if (theOther == null) return;
        if (theOther.defaultNodeVisuals != null) {
            defaultNodeVisuals = new Visuals();
            defaultNodeVisuals.copy(theOther.defaultNodeVisuals);
        }
        if (theOther.defaultEdgeVisuals != null) {
            defaultEdgeVisuals = new Visuals();
            defaultEdgeVisuals.copy(theOther.defaultEdgeVisuals);
        }
        if (theOther.classNodeVisuals != null) {
            Iterator keys = theOther.classNodeVisuals.keySet().iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                Visuals n = new Visuals();
                n.copy((Visuals) theOther.classNodeVisuals.get(key));
                classNodeVisuals.put(key, n);
            }
        }
        if (theOther.classEdgeVisuals != null) {
            Iterator keys = theOther.classEdgeVisuals.keySet().iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                Visuals n = new Visuals();
                n.copy((Visuals) theOther.classEdgeVisuals.get(key));
                classEdgeVisuals.put(key, n);
            }
        }
    }

    /**
    * Merge of the instance with another, <em>lower priority</em> instance.
    * This means that a value in the lower priority instance is considered
    * <em>if and only if</em> the value is not set in this instance. Even if the
    * lower priority instance value prevails, only object references are copied and not
    * the values themselves.
    *
    * @param theOther the lower priority visual property instance to merge with.
    * If null, the method is void.
    */
    public void merge(ClassVisuals theOther) {
        if (theOther == null) return;
        if (theOther.defaultNodeVisuals != null) {
            if (defaultNodeVisuals == null) defaultNodeVisuals = new Visuals();
            defaultNodeVisuals.merge(theOther.defaultNodeVisuals);
        }
        if (theOther.defaultEdgeVisuals != null) {
            if (defaultEdgeVisuals == null) defaultEdgeVisuals = new Visuals();
            defaultEdgeVisuals.merge(theOther.defaultEdgeVisuals);
        }
        if (theOther.classNodeVisuals != null) {
            Iterator keys = theOther.classNodeVisuals.keySet().iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                Visuals n;
                {
                    Object oo = classNodeVisuals.get(key);
                    if (oo == null) {
                        n = new Visuals();
                        classNodeVisuals.put(key, n);
                    } else {
                        n = (Visuals) oo;
                    }
                }
                n.merge((Visuals) theOther.classNodeVisuals.get(key));
            }
        }
        if (theOther.classEdgeVisuals != null) {
            Iterator keys = theOther.classEdgeVisuals.keySet().iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                Visuals n;
                {
                    Object oo = classEdgeVisuals.get(key);
                    if (oo == null) {
                        n = new Visuals();
                        classEdgeVisuals.put(key, n);
                    } else {
                        n = (Visuals) oo;
                    }
                }
                n.merge((Visuals) theOther.classEdgeVisuals.get(key));
            }
        }
    }

    public String toString() {
        StringBuffer retval = new StringBuffer();
        if (defaultNodeVisuals != null) {
            retval.append("   = Default node visuals:\n");
            retval.append(defaultNodeVisuals.toString());
        } else {
            retval.append("   = No default node visuals\n");
        }
        if (defaultEdgeVisuals != null) {
            retval.append("   = Default edge visuals:\n");
            retval.append(defaultEdgeVisuals.toString());
        } else {
            retval.append("   = No default edge visuals\n");
        }
        if (classNodeVisuals.size() != 0) {
            retval.append("   = Class node visuals\n");
            Iterator keys = classNodeVisuals.keySet().iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                retval.append("   = For class " + key + "\n");
                retval.append(((Visuals) classNodeVisuals.get(key)).toString());
            }
        } else {
            retval.append("   = No class node visuals\n");
        }
        if (classEdgeVisuals.size() != 0) {
            retval.append("   = Class edge visuals\n");
            Iterator keys = classEdgeVisuals.keySet().iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                retval.append("   = For class " + key + "\n");
                retval.append(((Visuals) classEdgeVisuals.get(key)).toString());
            }
        } else {
            retval.append("   = No class edge visuals\n");
        }
        return retval.toString();
    }
}
