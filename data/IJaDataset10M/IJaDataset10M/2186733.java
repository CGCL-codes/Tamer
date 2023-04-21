package org.aspectj.lang.reflect;

/**
 * AspectJ runtime representation of a declare soft member within an aspect.
 */
public interface DeclareSoft {

    /**
	 * The aspect that declared this member
	 */
    AjType getDeclaringType();

    /**
	 * The softened exception type
	 */
    AjType getSoftenedExceptionType() throws ClassNotFoundException;

    /**
	 * The pointcut determining the join points at which the exception is to be softened.
	 */
    PointcutExpression getPointcutExpression();
}
