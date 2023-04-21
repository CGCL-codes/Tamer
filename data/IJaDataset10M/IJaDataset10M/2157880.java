package org.ztemplates.web.spring;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.ztemplates.render.ZIRenderedObject;
import org.ztemplates.web.ZIActiveView;
import org.ztemplates.web.ZIRenderService;
import org.ztemplates.web.ZTemplates;

/**
 * Spring proxy for ZIRenderService
 * 
 * @author gerd
 * 
 */
@Component(ZIRenderService.SPRING_NAME)
@Scope("request")
public class ZRenderServiceSpring implements ZIRenderService {

    static final Logger log = Logger.getLogger(ZRenderServiceSpring.class);

    private final ZIRenderService service;

    public ZRenderServiceSpring() {
        this.service = ZTemplates.getRenderService();
    }

    public String render(Object obj) throws Exception {
        return service.render(obj);
    }

    public ZIRenderedObject prerender(Object obj) throws Exception {
        return service.prerender(obj);
    }

    public String renderZtemplatesCss() throws Exception {
        return service.renderZtemplatesCss();
    }

    public String createJavaScriptId() {
        return service.createJavaScriptId();
    }

    public String createJavaScriptId(String prefix) {
        return service.createJavaScriptId(prefix);
    }

    public void setJavaScriptIdPrefix(String javaScriptIdPrefix) {
        service.setJavaScriptIdPrefix(javaScriptIdPrefix);
    }

    public String getJavaScriptIdPrefix() {
        return service.getJavaScriptIdPrefix();
    }

    public String getCssId(Class clazz) {
        return service.getCssId(clazz);
    }

    public <T extends ZIActiveView> T createActiveView(Class<T> clazz) throws Exception {
        return service.createActiveView(clazz);
    }
}
