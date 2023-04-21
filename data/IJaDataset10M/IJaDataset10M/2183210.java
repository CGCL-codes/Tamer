package org.springframework.aop;

import org.aopalliance.aop.Advice;

/**
 * Superinterface for all before advice. Spring supports only method before
 * advice. Although this is unlikely to change, this API is designed to
 * allow field advice in future if desired.
 *
 * @author Rod Johnson
 * @see org.springframework.aop.MethodBeforeAdvice
 */
public interface BeforeAdvice extends Advice {
}
