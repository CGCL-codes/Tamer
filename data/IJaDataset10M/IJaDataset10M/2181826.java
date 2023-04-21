package de.mindmatters.faces.taglib.facelets;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.component.ActionSource;
import javax.faces.component.ActionSource2;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import com.sun.facelets.FaceletContext;
import com.sun.facelets.tag.TagAttribute;
import com.sun.facelets.tag.jsf.ComponentConfig;
import de.mindmatters.faces.el.ConstantMethodExpression;

/**
 * <strong>HtmlSimpleOutcomeActionSourceComponentHandler</strong> is a
 * TagHandler for {@link ActionSource} components .
 * 
 * @author Andreas Kuhrwahl
 * @see ActionSource
 * 
 */
public class HtmlSimpleOutcomeActionSource2ComponentHandler extends HtmlSimpleOutcomeActionSourceComponentHandler {

    /**
     * Default facelet constructor.
     * 
     * @param config
     *            The component configuration
     */
    public HtmlSimpleOutcomeActionSource2ComponentHandler(final ComponentConfig config) {
        super(config);
    }

    /**
     * Sets the action property on the associated {@link ActionSource}
     * actionSource.
     * 
     * @param ctx
     *            the {@link FaceletContext}
     * @param actionSource
     *            the {@link ActionSource}
     * @param actionAttribute
     *            the action {@link TagAttribute}
     * @deprecated
     */
    protected void setActionProperty(final FaceletContext ctx, final ActionSource actionSource, final TagAttribute actionAttribute) {
        if (actionSource instanceof ActionSource2) {
            setActionProperty(ctx, (ActionSource2) actionSource, actionAttribute);
        } else {
            super.setActionProperty(ctx, actionSource, actionAttribute);
        }
    }

    /**
     * Sets the action property on the associated {@link ActionSource2}
     * actionSource.
     * 
     * @param ctx
     *            the {@link FaceletContext}
     * @param actionSource
     *            the {@link ActionSource2}
     * @param actionAttribute
     *            the action {@link TagAttribute}
     */
    protected void setActionProperty(final FaceletContext ctx, final ActionSource2 actionSource, final TagAttribute actionAttribute) {
        if (getAction().isLiteral()) {
            actionSource.setActionExpression(new ConstantMethodExpression(getAction().getValue()));
        } else {
            FacesContext facesContext = ctx.getFacesContext();
            try {
                ValueExpression expression = facesContext.getApplication().getExpressionFactory().createValueExpression(facesContext.getELContext(), getAction().getValue(), String.class);
                ((UIComponent) actionSource).setValueExpression("action", expression);
            } catch (ELException ex) {
                throw new IllegalArgumentException("Only plain string outcomes are allowed for this action attribute");
            }
        }
    }
}
