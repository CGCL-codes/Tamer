package com.pinae.zazu.aop;

import java.lang.reflect.Method;

import org.aopalliance.aop.Advice;

/**
 * AfterAdvice在指定方法返回值后执行
 * 
 * @author 惠毓赓
 *
 */
public interface AfterAdvice extends Advice{
	/**
	 * 在指定方法成功调用后进行回调
	 * 
	 * @param target 包含指定方法的对象 
	 * @param method 指定调用的方法
	 * @param args 指定方法的参数值
	 */
	public void after(Object target, Method method, Object[] args);
}
