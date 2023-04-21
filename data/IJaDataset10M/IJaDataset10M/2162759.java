package mfinder.interceptor;

import mfinder.annotation.InterceptorStack;

/**
 * 内置拦截栈。
 */
public class DefaultInterceptorStack {

    /**
     * 空拦截栈名称，不包含任何拦截器。
     */
    @InterceptorStack(interceptors = {  })
    public static final String EMPTY_INTERCEPTOR_STACK = "empty";

    /**
     * 示例拦截栈名称，包含日志拦截器和计时拦截器。
     *
     * @see SampleInterceptor#logging
     * @see SampleInterceptor#timer
     */
    @InterceptorStack(interceptors = { SampleInterceptor.LOGGING, SampleInterceptor.TIMER })
    public static final String SAMPLE_INTERCEPTOR_STACK = "sample";
}
