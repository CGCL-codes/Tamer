package br.com.rapidrest.chain;

import java.lang.reflect.Method;
import br.com.rapidrest.annotation.Response;
import br.com.rapidrest.exception.ChainException;
import br.com.rapidrest.response.ResponseGenerator;

public class ResponseGeneratorCommand implements Command {

    @Override
    public void execute(Chain chain) throws ChainException {
        BasicChainContext context = (BasicChainContext) chain.getContext();
        Method method = context.getMethod();
        if (method != null && method.isAnnotationPresent(Response.class)) {
            Response annotation = method.getAnnotation(Response.class);
            ResponseGenerator responseGenerator;
            try {
                responseGenerator = annotation.value().newInstance();
            } catch (InstantiationException e) {
                throw new ChainException(e);
            } catch (IllegalAccessException e) {
                throw new ChainException(e);
            }
            responseGenerator.generate(context);
        } else {
            chain.proceed();
        }
    }
}
