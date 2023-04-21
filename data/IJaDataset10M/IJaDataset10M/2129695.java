package net.sf.balm.common.validation;

/**
 * @author dz
 */
public interface ValidationTarget {

    public Class getTargetClass();

    public Object getTarget();

    public String getTargetAlias();
}
