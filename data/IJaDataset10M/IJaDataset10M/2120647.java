package org.openedc.aop;

import java.text.MessageFormat;
import org.openedc.domain.AuditableEvent;
import org.openedc.service.MessageResourceResolver;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.MissingResourceException;
import javax.inject.Inject;
import javax.inject.Named;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.openedc.domain.Auditable;
import org.openedc.domain.AuditableEventType;
import org.openedc.service.AuditableEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author peter
 */
@Aspect
public class Auditor {

    private Logger logger = LoggerFactory.getLogger(Auditor.class);

    private AuditableEventService auditableEventService;

    @Inject
    @Named("messageResourceResolver")
    private MessageResourceResolver messageResourceResolver;

    @Pointcut("execution(* org.openedc.service.*.*(..))")
    public void serviceMethods() {
    }

    @Pointcut("@annotation(org.openedc.domain.Auditable)")
    public void annotatedMethods() {
    }

    @Around("annotatedMethods()")
    public Object audit(ProceedingJoinPoint pjp) throws Throwable {
        Object retVal = null;
        retVal = pjp.proceed();
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        String name = methodSignature.getName();
        List<String> parameterNames = Arrays.asList(methodSignature.getParameterNames());
        Auditable annotation = methodSignature.getMethod().getAnnotation(Auditable.class);
        AuditableEventType type = annotation.type();
        String msg = annotation.msg();
        if (msg == null || msg.length() == 0) {
            String msgid = annotation.msgid();
            if (msgid == null || msg.length() == 0) {
                msgid = type.toString();
            }
            String[] msgparams = annotation.msgparams();
            if (msgparams == null) {
                msgparams = new String[] {};
            }
            msg = retrieveMessage(msgid, msgparams);
        }
        Date now = GregorianCalendar.getInstance().getTime();
        auditableEventService.handleAuditableEvent(type, msg, now);
        final String afterMethodCallMessage = "audit AFTER! - name = " + name + " param names: " + parameterNames + " type: " + annotation.type().toString();
        logger.info(afterMethodCallMessage);
        return retVal;
    }

    private String retrieveMessage(String msgid, String[] msgparams) {
        String message;
        try {
            message = MessageFormat.format(messageResourceResolver.getBundle().getString(msgid), (Object[]) msgparams);
        } catch (MissingResourceException e) {
            message = '!' + msgid + '!';
        }
        return message;
    }

    public void setAuditableEventService(AuditableEventService auditableEventService) {
        this.auditableEventService = auditableEventService;
    }

    public void setMessageResourceResolver(MessageResourceResolver messageResourceResolver) {
        this.messageResourceResolver = messageResourceResolver;
    }
}
