package com.jguild.devportal.web.security;

import com.sun.facelets.FaceletContext;
import com.sun.facelets.FaceletException;
import com.sun.facelets.tag.TagAttribute;
import com.sun.facelets.tag.TagConfig;
import com.sun.facelets.tag.TagHandler;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.jsf.FacesContextUtils;
import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * Tag used that filters the body based on security ACLs
 */
public class AclTag extends TagHandler {

    private final TagAttribute target;

    public AclTag(final TagConfig tagConfig) {
        super(tagConfig);
        final FacesContext ctx = FacesContext.getCurrentInstance();
        final WebApplicationContext context = FacesContextUtils.getRequiredWebApplicationContext(ctx);
        target = getAttribute("target");
    }

    public void apply(final FaceletContext faceletContext, final UIComponent parent) throws IOException, FacesException, FaceletException, ELException {
        final Object v = target.getObject(faceletContext);
        System.out.println(v);
        if (true) {
            nextHandler.apply(faceletContext, parent);
        }
    }
}
