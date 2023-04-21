package example.chapter4;

import java.lang.reflect.Method;
import org.springframework.aop.ThrowsAdvice;

/**
 * Execute when exception occurs.
 * 
 * @author Xuefeng
 */
public class LoginThrowsAdvice implements ThrowsAdvice {

    public void afterThrowing(Method m, Object[] args, Object target, Throwable subclass) {
        System.out.println("[LoginThrowsAdvice] An exception occur: " + subclass.getClass().getSimpleName());
    }
}
