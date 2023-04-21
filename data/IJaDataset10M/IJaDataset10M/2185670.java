package com.liferay.portal.mirage.aop;

import com.sun.portal.cms.mirage.exception.CMSException;
import com.sun.portal.cms.mirage.model.custom.BinaryContent;
import com.sun.portal.cms.mirage.model.custom.OptionalCriteria;
import java.util.Map;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * <a href="ArticleImageInvoker.java.html"><b><i>View Source</i></b></a>
 *
 * @author Karthik Sudarshan
 *
 */
public class ArticleImageInvoker extends BinaryContent implements OptionalCriteria {

    public ArticleImageInvoker(ProceedingJoinPoint proceedingJoinPoint) {
        _invoker = new MirageInvoker(proceedingJoinPoint);
    }

    public Map<Object, Object> getOptions() {
        return null;
    }

    public Object getReturnValue() {
        return _invoker.getReturnValue();
    }

    public Object invoke() throws CMSException {
        return _invoker.invoke();
    }

    private MirageInvoker _invoker;
}
