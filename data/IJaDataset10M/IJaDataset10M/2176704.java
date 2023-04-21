package lucy.examples.example1.impl;

import lucy.examples.example1.Greeting;
import org.t2framework.commons.annotation.composite.SingletonScope;

/**
 * <#if locale="en">
 * <p>
 * 
 * @SingletonScope is a scope annotation for Lucy.This class is singleton. *
 *                 </p>
 *                 <#else>
 *                 <p>
 * @SingletonScope はLucyのためのアノテーションで、
 *                 このインスタンスはシングルトン(=Lucyから取得すると毎回同じインスタンスが返ってくる)であることを示します．
 *                 </p>
 *                 </#if>
 */
@SingletonScope
public class GreetingImpl implements Greeting {

    /**
	 * <#if locale="en">
	 * <p>
	 * hello().
	 * </p>
	 * <#else>
	 * <p>
	 * helloメソッドです．
	 * </p>
	 * </#if>
	 */
    public String hello() {
        return "Hello Lucy!";
    }
}
