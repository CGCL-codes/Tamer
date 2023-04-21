package net.sf.beanshield.aop;

import net.sf.beanshield.session.ShieldSessionImplementor;

/**
 *
 */
public interface ShieldProxy {

    void init(ShieldSessionImplementor session);
}
