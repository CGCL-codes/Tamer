package logrus.beanmock.invoker;

public interface CounterAccessor {

    Counter getCounter(MethodInvocationInfo info);
}
