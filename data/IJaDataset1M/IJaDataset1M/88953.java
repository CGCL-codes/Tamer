package org.tamacat.httpd.monitor;

/**
 * <p>It is interface to express that I support a monitoring target function.
 * 
 * @param <T> target of check object.
 */
public interface MonitorEvent<T> {

    /**
	 * <p>When there was a problem by a monitoring check, it is carried out.
	 * @param target remove the check target
	 */
    void removeTarget(T target);

    /**
	 * <p>When I restored by a monitoring check normally, it is carried out.
	 * @param target add the check target
	 */
    void addTarget(T target);
}
