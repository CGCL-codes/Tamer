package org.apache.myfaces.trinidadinternal.taglib.listener;

import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentClassicTagBase;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.myfaces.trinidad.logging.TrinidadLogger;

/**
 *
 */
public class ResetActionListenerTag extends TagSupport {

    @Override
    public int doStartTag() throws JspException {
        UIComponentClassicTagBase tag = UIComponentClassicTagBase.getParentUIComponentClassicTagBase(pageContext);
        if (tag == null) {
            throw new JspException(_LOG.getMessage("RESETACTIONLISTENER_MUST_INSIDE_UICOMPONENT_TAG"));
        }
        if (!tag.getCreated()) return SKIP_BODY;
        UIComponent component = tag.getComponentInstance();
        if (!(component instanceof ActionSource)) {
            throw new JspException(_LOG.getMessage("RESETACTIONlISTENER_MUST_INSIDE_UICOMPONENT_TAG"));
        }
        ResetActionListener listener = new ResetActionListener();
        ((ActionSource) component).addActionListener(listener);
        return super.doStartTag();
    }

    @Override
    public void release() {
        super.release();
    }

    private static final TrinidadLogger _LOG = TrinidadLogger.createTrinidadLogger(ResetActionListenerTag.class);
}
