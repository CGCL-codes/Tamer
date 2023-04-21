package org.nexopenframework.engine.exception;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nexopenframework.core.ServiceComponent;
import org.nexopenframework.core.annotations.ExceptionHandler;
import org.nexopenframework.core.annotations.ExceptionHandlers;
import org.nexopenframework.core.annotations.SeverityType;
import org.springframework.util.StringUtils;

/**
 * <p>NexTReT Open Framework</p>
 * 
 * <p>Comment here</p>
 * 
 * @author <a href="mailto:fme@nextret.net">Francesc Xavier Magdaleno</a>
 * @version 1.0
 * @since 1.0
 */
public class AnnotationEngineExceptionListener extends EngineExceptionListener {

    /**holder of exception handled*/
    private static ExceptionHandlerHolder handled = new ExceptionHandlerHolder();

    /**
	 * 
	 * @see ExceptionHandlers
	 * @see ExceptionHandler
	 * @see org.nexopenframework.engine.exception.EngineExceptionListener#logException(org.nexopenframework.engine.exception.EngineExceptionEvent)
	 */
    @Override
    protected void logException(EngineExceptionEvent _event) {
        Method m = _event.getServiceMethod();
        if (m.isAnnotationPresent(ExceptionHandlers.class)) {
            ExceptionHandlers handlers = m.getAnnotation(ExceptionHandlers.class);
            for (ExceptionHandler handler : handlers.value()) {
                logExceptionInternal(_event, handler);
            }
        }
        if (m.isAnnotationPresent(ExceptionHandler.class)) {
            ExceptionHandler handler = m.getAnnotation(ExceptionHandler.class);
            logExceptionInternal(_event, handler);
        }
        try {
            if (!handled.isHandled()) {
                super.logException(_event);
            }
        } finally {
            handled.reset();
        }
    }

    @SuppressWarnings("unchecked")
    private void logExceptionInternal(EngineExceptionEvent _event, ExceptionHandler handler) {
        Throwable thr = _event.getException();
        if (handler.type().isAssignableFrom(thr.getClass())) {
            try {
                ServiceComponent service = _event.getService();
                Log logger = LogFactory.getLog(service.getClass());
                String message = handler.message();
                if (!StringUtils.hasText(message)) {
                    StringBuilder sb = new StringBuilder();
                    {
                        sb.append("Raised an exception in ").append(service.getName());
                        sb.append(" in method ").append(_event.getServiceMethod());
                        sb.append(".\n Request transactional ID :: ").append(_event.getXid());
                        sb.append(".\n Time :: ").append(new Date(_event.getTimestamp()));
                    }
                    message = sb.toString();
                }
                SeverityType st = handler.severity();
                switch(st) {
                    case DEBUG:
                        logger.debug(message, thr);
                        break;
                    case INFO:
                        logger.info(message, thr);
                        break;
                    case WARN:
                        logger.warn(message, thr);
                        break;
                    case ERROR:
                        logger.error(message, thr);
                        break;
                    case FATAL:
                        logger.fatal(message, thr);
                        break;
                }
            } finally {
                handled.addExceptionHandler();
            }
        }
    }

    private static class ExceptionHandlerHolder extends ThreadLocal<List<Character>> {

        public boolean isHandled() {
            return this.get() != null && !this.get().isEmpty();
        }

        public void addExceptionHandler() {
            this.get().add('1');
        }

        public void reset() {
            this.get().clear();
        }

        @Override
        protected List<Character> initialValue() {
            return new ArrayList<Character>();
        }
    }

    ;
}
