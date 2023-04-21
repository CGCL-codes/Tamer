package net.community.chest.jmx;

import javax.management.MBeanParameterInfo;
import net.community.chest.reflect.ClassUtil;

/**
 * <P>Copyright 2007 as per GPLv2</P>
 *
 * @author Lyor G.
 * @since Aug 19, 2007 11:56:12 AM
 */
public class ReflectiveMBeanParameterInfo extends MBeanParameterInfo {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2469810142557520222L;

    private final Class<?> _paramType;

    public final Class<?> getParameterClass() {
        return _paramType;
    }

    public ReflectiveMBeanParameterInfo(String pName, Class<?> type, String pDesc) throws IllegalArgumentException {
        super(pName, (null == type) ? null : type.getName(), pDesc);
        if (null == (_paramType = type)) throw new IllegalArgumentException(ClassUtil.getConstructorExceptionLocation(getClass()) + " no parameter type class specified");
    }
}
