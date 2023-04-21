package jacky.lanlan.song.aop;

import jacky.lanlan.song.aop.advice.AroundAdvice;
import jacky.lanlan.song.util.Assert;
import java.lang.reflect.Method;

public class TestAroundAdvice implements AroundAdvice {

    public Object process(Object target, Method method, Object... args) throws Exception {
        Assert.notNull(method);
        Assert.isTrue(method.getName().matches(".*[Hh]ello.*"));
        Object returnValue = method.invoke(target, args);
        Assert.isTrue(returnValue instanceof String);
        Assert.isTrue(((String) returnValue).matches("^(Test)+.+"));
        return returnValue + "[Intercepted]";
    }
}
