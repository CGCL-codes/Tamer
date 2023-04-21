package com.hyper9.uvapi.impls.virt;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.hyper9.common.utils.ThrowableUtils;
import com.hyper9.uvapi.Log;
import com.hyper9.uvapi.impls.UniqueBeanImpl;
import com.hyper9.uvapi.types.ConnectorBean;
import com.hyper9.uvapi.types.virt.ConnectedBean;

/**
 * The base class for virtualization beans.
 * 
 * @author akutz
 * 
 */
public abstract class VirtBeanImpl extends UniqueBeanImpl<String> implements ConnectedBean {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = -5177280631120388164L;

    /**
     * The bean's connector.
     */
    private ConnectorBean connector;

    /**
     * The bean's properties. This should only be used to store properties as
     * defined in the VirtProperties enumeration. All other properties should be
     * defined in fields.
     */
    protected Map<String, Object> properties = new HashMap<String, Object>();

    private List<String> propertyNames;

    protected VirtBeanImpl() {
        super();
    }

    protected VirtBeanImpl(String id, ConnectorBean connector) {
        super(id);
        this.connector = connector;
    }

    @Override
    public ConnectorBean getConnector() {
        return this.connector;
    }

    @Override
    public void setConnector(ConnectorBean toSet) {
        this.connector = toSet;
    }

    @Override
    public boolean equals(Object toCompare) {
        if (!this.getClass().equals(toCompare.getClass())) {
            return false;
        }
        String id1 = getID();
        String id2 = ((ConnectedBean) toCompare).getID();
        if (!equals(id1, id2)) {
            return false;
        }
        try {
            BeanInfo bi = Introspector.getBeanInfo(this.getClass());
            PropertyDescriptor[] pdArr = bi.getPropertyDescriptors();
            Object[] args = new Object[0];
            for (PropertyDescriptor pd : pdArr) {
                if (pd.getReadMethod().getParameterTypes().length != 0) {
                    continue;
                }
                Class<?> pdt = pd.getPropertyType();
                if (pdt.isPrimitive() || Number.class.isAssignableFrom(pdt) || String.class.isAssignableFrom(pdt)) {
                    Object v1 = pd.getReadMethod().invoke(this, args);
                    Object v2 = pd.getReadMethod().invoke(toCompare, args);
                    if (!equals(v1, v2)) {
                        return false;
                    }
                } else if (ConnectedBean.class.isAssignableFrom(pdt)) {
                    Object v1 = pd.getReadMethod().invoke(this, args);
                    Object v2 = pd.getReadMethod().invoke(toCompare, args);
                    if (!equals(v1, v2)) {
                        return false;
                    }
                } else if (pdt.isArray() && ConnectedBean.class.isAssignableFrom(pdt.getComponentType())) {
                    Object v1 = pd.getReadMethod().invoke(this, args);
                    Object v2 = pd.getReadMethod().invoke(toCompare, args);
                    if (!equals(v1, v2)) {
                        return false;
                    }
                } else if (Collection.class.isAssignableFrom(pdt)) {
                    Type grt = pd.getReadMethod().getGenericReturnType();
                    if (grt instanceof ParameterizedType) {
                        ParameterizedType pt = (ParameterizedType) grt;
                        Type[] at = pt.getActualTypeArguments();
                        if (at.length == 1) {
                            Class<?> atc = (Class<?>) at[0];
                            boolean isCollOfVirtBeans = ConnectedBean.class.isAssignableFrom(atc);
                            if (isCollOfVirtBeans) {
                                Object v1 = pd.getReadMethod().invoke(this, args);
                                Object v2 = pd.getReadMethod().invoke(toCompare, args);
                                if (!equals(v1, v2)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.error(getConnector(), "Error comparing objects", e);
            System.out.println(ThrowableUtils.getStackTrace(e));
            return false;
        }
        return true;
    }

    private boolean equals(Object o1, Object o2) {
        if (o1 == null && o2 == null) {
            return true;
        }
        if (o1 == null || o2 == null) {
            return false;
        }
        return o1.equals(o2);
    }

    @Override
    public void reload() throws Exception {
        ConnectedBean vb = loadSelf(getPropertyNames());
        BeanInfo info = Introspector.getBeanInfo(this.getClass());
        PropertyDescriptor[] pdArr = info.getPropertyDescriptors();
        Object[] args = new Object[0];
        for (PropertyDescriptor pd : pdArr) {
            Object v = pd.getReadMethod().invoke(vb, args);
            pd.getWriteMethod().invoke(this, new Object[] { v });
        }
    }

    /**
     * Load this object from the server.
     * 
     * @param propertiesToGet The properties to get.
     * @return This object.
     * @throws Exception When an error occurs.
     */
    protected abstract ConnectedBean loadSelf(List<String> propertiesToGet) throws Exception;

    @Override
    public List<String> getPropertyNames() {
        return this.propertyNames;
    }

    @Override
    public void setPropertyNames(List<String> toSet) {
        this.propertyNames = toSet;
    }
}
