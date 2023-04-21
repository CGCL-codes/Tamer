package com.trapezium.vrml.fields;

import com.trapezium.parse.TokenEnumerator;
import com.trapezium.vrml.VrmlElement;
import com.trapezium.vrml.Scene;
import com.trapezium.vrml.grammar.Table7;

/**
 *  Scene graph component for sequence of vec3f values.
 *
 *  Value objects are not added to scene graph, unless they contain errors.
 *  
 *  @author          Johannes N. Johannsen
 *  @version         1.12, 25 Feb 1998, added base profile warning
 *  @version         1.1, 15 Jan 1998
 *
 *  @since           1.0
 */
public class MFVec3fValue extends MFFieldValue {

    public MFVec3fValue() {
        super();
    }

    public VrmlElement subclassFactory(int tokenOffset, TokenEnumerator v, Scene scene) {
        if (!SFVec3fValue.valid(tokenOffset, v)) {
            SFVec3fValue sfvec = new SFVec3fValue(tokenOffset, v);
            optimizedValueCount += sfvec.numberValidChildren();
            return (sfvec);
        } else {
            optimizedValueCount += 3;
            if ((optimizedValueCount / 3) == (Table7.MFVec3fLimit + 1)) {
                v.setState(tokenOffset);
                SFVec3fValue sfvec = new SFVec3fValue(tokenOffset, v);
                sfvec.setError("Nonconformance, base profile limit of " + Table7.MFVec3fLimit + " exceeded here");
                return (sfvec);
            }
            return (null);
        }
    }

    /** Generate extreme values */
    transient ExtremeValues ev;

    public void genExtremes(TokenEnumerator dataSource) {
        ev = new ExtremeValues();
        int scanner = getFirstTokenOffset();
        int last = getLastTokenOffset();
        dataSource.setState(scanner);
        while ((scanner != -1) && (scanner <= last)) {
            scanner = dataSource.skipToNumber(0);
            if (scanner != -1) {
                ev.putFloat(dataSource.getFloat(scanner));
            }
            scanner = dataSource.getNextToken();
        }
    }

    public boolean hasExtremes() {
        if (ev != null) {
            return (ev.hasValues());
        } else {
            return (false);
        }
    }

    public float getXsize() {
        if (ev != null) {
            return (ev.getXsize());
        } else {
            return (0f);
        }
    }

    public float getYsize() {
        if (ev != null) {
            return (ev.getYsize());
        } else {
            return (0f);
        }
    }

    public float getZsize() {
        if (ev != null) {
            return (ev.getZsize());
        } else {
            return (0f);
        }
    }

    public float getXcenter() {
        if (ev != null) {
            return (ev.getXcenter());
        } else {
            return (0f);
        }
    }

    public float getYcenter() {
        if (ev != null) {
            return (ev.getYcenter());
        } else {
            return (0f);
        }
    }

    public float getZcenter() {
        if (ev != null) {
            return (ev.getZcenter());
        } else {
            return (0f);
        }
    }
}
