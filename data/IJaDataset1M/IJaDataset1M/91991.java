package org.ironrhino.core.aop;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.ironrhino.core.event.EntityOperationEvent;
import org.ironrhino.core.event.EntityOperationType;
import org.ironrhino.core.event.EventPublisher;
import org.ironrhino.core.model.Persistable;

@Aspect
@Singleton
@Named
public class PublishAspect extends BaseAspect {

    public PublishAspect() {
        order = -1;
    }

    @Inject
    private EventPublisher eventPublisher;

    @Around("execution(* org.ironrhino..service.*Manager.save*(*)) and args(entity) and @args(publishAware)")
    public Object save(ProceedingJoinPoint call, Persistable<?> entity, PublishAware publishAware) throws Throwable {
        if (isBypass()) return call.proceed();
        boolean isNew = entity.isNew();
        Object result = call.proceed();
        if (eventPublisher != null) eventPublisher.publish(new EntityOperationEvent(entity, isNew ? EntityOperationType.CREATE : EntityOperationType.UPDATE), publishAware.global());
        return result;
    }

    @AfterReturning("execution(* org.ironrhino..service.*Manager.delete*(*)) and args(entity) and @args(publishAware)")
    public void delete(Persistable<?> entity, PublishAware publishAware) {
        if (isBypass()) return;
        if (eventPublisher != null) eventPublisher.publish(new EntityOperationEvent(entity, EntityOperationType.DELETE), publishAware.global());
    }
}
