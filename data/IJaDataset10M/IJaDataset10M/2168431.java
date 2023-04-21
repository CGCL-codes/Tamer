package tufts.vue.beans;

import tufts.vue.*;
import java.util.*;
import java.beans.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/** This class describes a DynamicPropertyMapper: an object that
* can get and set dynamic properties of dynamic beans belonging to
* Java classes or interfaces for which it has registered.
*
* This is still original S.B. code and could use reworking & better integration.
*
* Is this code still needed?
*
**/
public class VueLWCPropertyMapper implements VuePropertyMapper {

    static final String[] sNodeProperties = { LWKey.FillColor.name, LWKey.StrokeColor.name, LWKey.StrokeWidth.name, LWKey.TextColor.name, LWKey.Shape.name, LWKey.Font.name };

    static final String[] sLinkProperties = { LWKey.StrokeColor.name, LWKey.StrokeWidth.name, LWKey.TextColor.name, LWKey.Font.name, LWKey.LinkArrows.name };

    static final String[] sTextProperties = { LWKey.TextColor.name, LWKey.Font.name };

    /**
     * getPropertyValue
     * Gets the value of the property from the specified object.
     * @param Object - the object
     * @param String name
     * @return Object the value
     **/
    public Object getPropertyValue(Object pBean, String key) {
        if (pBean instanceof LWComponent) {
            return ((LWComponent) pBean).getPropertyValue(key);
        } else {
            System.out.println(this + " getPropertyValue: unhandled class for " + key + " on " + pBean);
        }
        return null;
    }

    /**
     * setPropertyValue
     * This sets the property with of the object with the passed value
     * @param OpObject - the object
     * @param String pName the proeprty name
     * @param pValue - the value of the named property
     **/
    public void setPropertyValue(Object pBean, String pName, Object pValue) {
        if (pBean instanceof LWComponent) {
            setProperty((LWComponent) pBean, pName, pValue);
        } else throw new IllegalArgumentException("VueLWCPropertyMapper: can't handle class " + pBean + " name=" + pName + " val=" + pValue);
    }

    public static void setProperty(LWComponent c, Object key, Object val) {
        c.setProperty(key, val);
    }

    /**
     * getBeanInfo
     * Returns the VueBeanInfo for the object.
     * @param Object - the object
     * @return VueBeanInfo the info for the object.
     **/
    public VueBeanInfo getBeanInfo(Object pObject) {
        VueBeanInfo beanInfo = null;
        if (pObject instanceof LWComponent) beanInfo = new LWCBeanInfo((LWComponent) pObject);
        return beanInfo;
    }

    public class LWCBeanInfo implements VueBeanInfo {

        String[] mPropertyNames = null;

        VuePropertyDescriptor[] mDescriptors = null;

        Map mMap = null;

        LWCBeanInfo(LWComponent pLWC) {
            if (pLWC instanceof LWNode) {
                if (((LWNode) pLWC).isTextNode()) mPropertyNames = sTextProperties; else mPropertyNames = sNodeProperties;
            } else if (pLWC instanceof LWLink) {
                mPropertyNames = sLinkProperties;
            }
            if (mPropertyNames != null) {
                mDescriptors = new VuePropertyDescriptor[mPropertyNames.length];
                mMap = new HashMap();
                VuePropertyDescriptor desc = null;
                for (int i = 0; i < mPropertyNames.length; i++) {
                    desc = createDescriptor(mPropertyNames[i]);
                    mDescriptors[i] = desc;
                    mMap.put(mPropertyNames[i], desc);
                }
            }
        }

        public VuePropertyDescriptor[] getPropertyDescriptors() {
            return mDescriptors;
        }

        public boolean hasProperty(String pName) {
            boolean hasKey = mMap.containsKey(pName);
            return hasKey;
        }

        public String[] getPropertyNames() {
            return mPropertyNames;
        }

        public VuePropertyDescriptor getPropertyDescriptor(String pName) {
            return (VuePropertyDescriptor) mMap.get(pName);
        }

        private VuePropertyDescriptor createDescriptor(String pName) {
            VuePropertyDescriptor desc = null;
            String str = new String();
            Font font = new Font("Default", 1, Font.PLAIN);
            Integer i = new Integer(1);
            Color color = new Color(0, 0, 0);
            ;
            Class theClass = null;
            if (pName.equals(LWKey.FillColor.name) || pName.equals(LWKey.StrokeColor.name) || pName.equals(LWKey.TextColor.name)) {
                theClass = color.getClass();
            } else if (pName.equals(LWKey.StrokeWidth)) {
                Float thefloat = new Float(0);
                theClass = thefloat.getClass();
            } else if (pName.equals(LWKey.LinkArrows)) {
                theClass = i.getClass();
            } else if (pName.equals(LWKey.Font)) {
                theClass = font.getClass();
            }
            desc = new VuePropertyDescriptor(pName, theClass, null);
            return desc;
        }
    }

    boolean sDebug = true;

    private void debug(String s) {
        if (sDebug) System.out.println(s);
    }
}
