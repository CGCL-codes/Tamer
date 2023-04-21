package jp.dodododo.aop.interceptors;

import jp.dodododo.aop.annotation.Enhance;

@Enhance(false)
public abstract class AbstractMethodInterceptor extends AbstractInterceptor implements MethodInterceptor {
}
