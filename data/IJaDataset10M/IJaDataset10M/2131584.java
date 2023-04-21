package net.sourceforge.javautil.common.api;

/**
 * An object that wraps/proxies another object.
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public interface IWrapper<T> {

    /**
	 * @return The original object being wrapped.
	 */
    T getWrapped();
}
