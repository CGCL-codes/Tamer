package br.com.rapidrest.ioc;

import java.lang.reflect.Field;
import javax.servlet.http.HttpServletRequest;
import br.com.rapidrest.annotation.Inject;
import br.com.rapidrest.chain.BasicChainContext;
import br.com.rapidrest.exception.InjectionException;

public class RequestFieldHandler implements FieldHandler {

    @Override
    public void handle(Field field, Object target, BasicChainContext context) throws InjectionException {
        if (field.isAnnotationPresent(Inject.class) && field.getType().equals(HttpServletRequest.class)) {
            try {
                field.setAccessible(true);
                field.set(target, context.getRequest());
            } catch (IllegalArgumentException e) {
                throw new InjectionException(e);
            } catch (IllegalAccessException e) {
                throw new InjectionException(e);
            }
        }
    }

    @Override
    public void end(Field field, Object target, BasicChainContext context) throws InjectionException {
        if (field.isAnnotationPresent(Inject.class) && field.getType().equals(HttpServletRequest.class)) {
            try {
                field.setAccessible(true);
                field.set(target, null);
            } catch (IllegalArgumentException e) {
                throw new InjectionException(e);
            } catch (IllegalAccessException e) {
                throw new InjectionException(e);
            }
        }
    }
}
