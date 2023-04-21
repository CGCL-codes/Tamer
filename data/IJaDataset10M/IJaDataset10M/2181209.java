package org.zkoss.spring.security.ui.webapp;

import org.springframework.security.ui.AccessDeniedHandlerImpl;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;

/**
 * Controller codes for /web/zul/zkspring/security/errorTemplate.zul
 * @author henrichen
 * @since 1.0
 */
public class ErrorTemplateComposer extends GenericForwardComposer {

    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        session.setAttribute(AccessDeniedHandlerImpl.SPRING_SECURITY_ACCESS_DENIED_EXCEPTION_KEY, arg.get(AccessDeniedHandlerImpl.SPRING_SECURITY_ACCESS_DENIED_EXCEPTION_KEY));
    }

    public void onClose() {
        session.removeAttribute(AccessDeniedHandlerImpl.SPRING_SECURITY_ACCESS_DENIED_EXCEPTION_KEY);
    }
}
