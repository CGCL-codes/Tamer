package com.sun.facelets.tag.ui;

import java.io.IOException;
import javax.el.ELException;
import javax.el.VariableMapper;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import com.sun.facelets.FaceletContext;
import com.sun.facelets.FaceletException;
import com.sun.facelets.el.VariableMapperWrapper;
import com.sun.facelets.tag.TagAttribute;
import com.sun.facelets.tag.TagConfig;
import com.sun.facelets.tag.TagHandler;

/**
 * @author Jacob Hookom
 * @version $Id: IncludeHandler.java,v 1.3 2005/08/24 04:38:55 jhook Exp $
 */
public final class IncludeHandler extends TagHandler {

    private final TagAttribute src;

    /**
     * @param config
     */
    public IncludeHandler(TagConfig config) {
        super(config);
        this.src = this.getRequiredAttribute("src");
    }

    public void apply(FaceletContext ctx, UIComponent parent) throws IOException, FacesException, FaceletException, ELException {
        String path = this.src.getValue(ctx);
        VariableMapper orig = ctx.getVariableMapper();
        ctx.setVariableMapper(new VariableMapperWrapper(orig));
        try {
            this.nextHandler.apply(ctx, null);
            ctx.includeFacelet(parent, path);
        } finally {
            ctx.setVariableMapper(orig);
        }
    }
}
