package test.crispy.example;

import net.sf.crispy.InterceptorHandler;
import net.sf.crispy.interceptor.StopWatchInterceptor;
import net.sf.crispy.server.InterceptorHandlerCreator;

public class InterceptorHandlerCreatorExample implements InterceptorHandlerCreator {

    private StopWatchInterceptor stopWatch = null;

    private InterceptorHandler interceptorHandler = null;

    public InterceptorHandlerCreatorExample() {
    }

    public InterceptorHandlerCreatorExample(StopWatchInterceptor pvStopWatch) {
        setStopWatch(pvStopWatch);
    }

    public void setInterceptorHandler(InterceptorHandler pvInterceptorHandler) {
        interceptorHandler = pvInterceptorHandler;
    }

    public InterceptorHandler getInterceptorHandler() {
        return interceptorHandler;
    }

    /**
	 * This method create a new <code>InterceptorHandler</code> instance.
	 * If set a <code>InterceptorHandler</code> (with setInterceptorHandler), than
	 * is by every call from this method the return object the same. This behavior is
	 * for tests suitable. For production is it nor suittable. 
	 * 
	 * @return The <code>InterceptorHandler</code> object.
	 */
    public InterceptorHandler createNewInterceptorHandlerInstance() {
        if (interceptorHandler == null) {
            interceptorHandler = new InterceptorHandler();
        }
        return interceptorHandler;
    }

    public StopWatchInterceptor getStopWatch() {
        return stopWatch;
    }

    public void setStopWatch(StopWatchInterceptor pvStopWatch) {
        interceptorHandler = new InterceptorHandler();
        stopWatch = pvStopWatch;
        interceptorHandler.addInterceptor(stopWatch);
    }
}
