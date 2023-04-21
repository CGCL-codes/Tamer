package net.sf.woko.test.controller.stripes;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;

@FacetKey(name = "validation", profileId = "ROLE_WOKO_ALL")
public class MyFacetActionValidation implements ActionBean {

    private ActionBeanContext context;

    private boolean saved = false;

    private boolean cancelled = false;

    public ActionBeanContext getContext() {
        return context;
    }

    public void setContext(ActionBeanContext context) {
        this.context = context;
    }

    @Validate(required = true)
    private String prop;

    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    @DefaultHandler
    public Resolution save() {
        saved = true;
        return new ForwardResolution("/toto.jsp");
    }

    @DontValidate
    public Resolution cancel() {
        cancelled = true;
        return new ForwardResolution("/toto.jsp");
    }

    public boolean isSaved() {
        return saved;
    }

    public boolean isCancelled() {
        return cancelled;
    }
}
