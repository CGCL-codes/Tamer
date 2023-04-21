package com.pinae.zazu.aop.test;

import com.pinae.zazu.aop.Pointcut;
import com.pinae.zazu.aop.advice.MyAfterAdvice;
import com.pinae.zazu.aop.advice.MyAfterAdvice2;
import com.pinae.zazu.aop.advice.MyAroundAdvice;
import com.pinae.zazu.aop.advice.MyAroundAdvice2;
import com.pinae.zazu.aop.advice.MyBeforeAdvice;
import com.pinae.zazu.aop.advice.MyClassAdvice;
import com.pinae.zazu.aop.pointcut.DefaultPointcutAdvisor;
import com.pinae.zazu.aop.pointcut.NameMatcherPointcutAdvisor;
import com.pinae.zazu.aop.pointcut.ParameterTypesPointcutAdvisor;
import com.pinae.zazu.aop.pointcut.PointcutUtil;
import com.pinae.zazu.aop.pointcut.RegexPointcutAdvisor;
import com.pinae.zazu.aop.pointcut.TrackTracePointcutAdvisor;
import com.pinae.zazu.aop.proxy.ProxyFactory;
import com.pinae.zazu.aop.resource.ITarget;
import com.pinae.zazu.aop.resource.MyTarget;
import com.pinae.zazu.aop.target.DefaultTarget;
import com.pinae.zazu.aop.target.Target;

import junit.framework.TestCase;

/**
 * 代理工厂测试
 * 
 * @author 惠毓赓
 *
 */
public class ProxyFactoryBeanTest extends TestCase {

	/**
	 * 测试代理工厂
	 */
	public void testProxyFactory() {
		Object[] intercepyor = new Object[6];
		//由参数匹配驱动的AfterAdvice
		ParameterTypesPointcutAdvisor after= new ParameterTypesPointcutAdvisor();
		after.setAdvice(new MyAfterAdvice());
		after.setClass("java.lang.String");
		intercepyor[0] = after;
		//由调用栈驱动的BforeAdvice
		TrackTracePointcutAdvisor before = new TrackTracePointcutAdvisor();
		before.setAdvice(new MyBeforeAdvice());
		before.setClassName("com.pinae.zazu.aop.test.ProxyFactoryBeanTest");
		intercepyor[1] = before;
		//由正则表达式驱动的
		RegexPointcutAdvisor around1 = new RegexPointcutAdvisor();
		around1.setAdvice(new MyAroundAdvice());
		around1.setPattern("ayHell*");
		intercepyor[2] = around1
		;
		NameMatcherPointcutAdvisor around2 = new NameMatcherPointcutAdvisor();
		around2.setAdvice(new MyAroundAdvice2());
		around2.setMappedName("sayHello");
		intercepyor[3] = around2;
		
		NameMatcherPointcutAdvisor after2_1 = new NameMatcherPointcutAdvisor();
		after2_1.setAdvice(new MyAfterAdvice2());
		after2_1.setMappedName("sayHello");
		Pointcut point1 = after2_1.getPointcut();
	
		ParameterTypesPointcutAdvisor after2_2 = new ParameterTypesPointcutAdvisor();
		after2_2.setAdvice(new MyAfterAdvice2());
		after2_2.setClass("java.lang.String");
		Pointcut point2 = after2_2.getPointcut();
		
		DefaultPointcutAdvisor after2 = new DefaultPointcutAdvisor();
		after2.setAdvice(new MyAfterAdvice2());
		after2.setPointcut(PointcutUtil.union(point1, point2));
		intercepyor[4] = after2;
		
		intercepyor[5] = new MyClassAdvice();
		
		ProxyFactory proxyFactory= new ProxyFactory();
		Target myTarget = new DefaultTarget();
		myTarget.setTarget(new MyTarget());
		proxyFactory.setTarget(myTarget);
		proxyFactory.setIntercepyor(intercepyor);
		ITarget target = (ITarget)proxyFactory.getObject();
		assertEquals(target.sayHello("Hui"), "Hello Hui");
	}

}
