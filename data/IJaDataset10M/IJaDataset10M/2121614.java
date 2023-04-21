package com.aop.demo;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class RepositoryTrackar {

    @Before("execution(* com.aop.demo.PersonRepsitory.*(..))")
    public void logInfo(JoinPoint join) {
        System.out.println("METHOD : " + join.getSignature().getDeclaringType().getName() + " : " + join.getSignature().getName());
    }
}
