package com.google.code.japa.interceptor.autopersist;

import org.springframework.aop.*;
import org.springframework.aop.support.*;

/**
 * Spring advisor to declare the AutomaticPersistence pointcut.
 * 
 * @author Leandro Aparecido
 * @since 1.0
 * @see com.google.code.japa.interceptor.autopersist.AutomaticPersistencePointcut
 * @see com.google.code.japa.annotation.AutomaticPersistence
 */
public class AutomaticPersistenceAdvisor extends AbstractBeanFactoryPointcutAdvisor {

    private static final long serialVersionUID = -3258189736749140214L;

    public static final int DEFAULT_ORDER = 0xFFFFF;

    /**
	 * Pointcut the advisor is declaring.
	 */
    private Pointcut pointcut;

    /**
	 * Creates the advisor setting its order to {@link #DEFAULT_ORDER}.
	 */
    public AutomaticPersistenceAdvisor() {
        pointcut = new AutomaticPersistencePointcut();
        setOrder(DEFAULT_ORDER);
    }

    /**
	 * {@inheritDoc}
	 */
    public Pointcut getPointcut() {
        return pointcut;
    }
}
