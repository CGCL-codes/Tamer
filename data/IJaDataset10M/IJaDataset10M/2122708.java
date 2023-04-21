package org.apache.myfaces.view.facelets.compiler;

import java.io.IOException;
import java.io.Writer;
import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UniqueIdVendor;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.FaceletException;
import org.apache.myfaces.view.facelets.AbstractFaceletContext;
import org.apache.myfaces.view.facelets.el.ELText;
import org.apache.myfaces.view.facelets.tag.jsf.ComponentSupport;
import org.apache.myfaces.view.facelets.util.FastWriter;

/**
 * @author Jacob Hookom
 * @version $Id: UITextHandler.java,v 1.10 2008/07/13 19:01:33 rlubke Exp $
 */
final class UITextHandler extends AbstractUIHandler {

    private final ELText txt;

    private final String alias;

    private final int length;

    public UITextHandler(String alias, ELText txt) {
        this.alias = alias;
        this.txt = txt;
        this.length = txt.toString().length();
    }

    public void apply(FaceletContext ctx, UIComponent parent) throws IOException, FacesException, FaceletException, ELException {
        if (parent != null) {
            try {
                ELText nt = this.txt.apply(ctx.getExpressionFactory(), ctx);
                UIComponent c = new UIText(this.alias, nt);
                AbstractFaceletContext actx = (AbstractFaceletContext) ctx;
                UniqueIdVendor uniqueIdVendor = actx.getUniqueIdVendorFromStack();
                if (uniqueIdVendor == null) {
                    uniqueIdVendor = ComponentSupport.getViewRoot(ctx, parent);
                }
                if (uniqueIdVendor != null) {
                    String uid = uniqueIdVendor.createUniqueId(ctx.getFacesContext(), null);
                    c.setId(uid);
                }
                this.addComponent(ctx, parent, c);
            } catch (Exception e) {
                throw new ELException(this.alias + ": " + e.getMessage(), e.getCause());
            }
        }
    }

    public String toString() {
        return this.txt.toString();
    }

    public String getText() {
        return this.txt.toString();
    }

    public String getText(FaceletContext ctx) {
        Writer writer = new FastWriter(this.length);
        try {
            this.txt.apply(ctx.getExpressionFactory(), ctx).write(writer, ctx);
        } catch (IOException e) {
            throw new ELException(this.alias + ": " + e.getMessage(), e.getCause());
        }
        return writer.toString();
    }
}
