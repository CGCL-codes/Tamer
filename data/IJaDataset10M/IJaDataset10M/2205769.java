package net.woodstock.rockapi.validation.aop;

import net.woodstock.rockapi.pojo.Pojo;
import net.woodstock.rockapi.validation.ObjectValidator;
import net.woodstock.rockapi.validation.Operation;
import net.woodstock.rockapi.validation.ValidationException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class AnnotedUpdateAdvice {

    @Before(value = "target(net.woodstock.rockapi.persistence.DAO) && execution(* update(..)) && args(pojo)")
    public void validate(Pojo pojo) throws ValidationException {
        ObjectValidator.validate(pojo, Operation.UPDATE);
    }
}
