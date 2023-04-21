package org.xdoclet.plugin.web.qtags;

import com.thoughtworks.qdox.model.DocletTag;

public interface JspValidatorInitParamTag extends DocletTag {

    public abstract String getName_();

    public abstract String getValue_();

    public abstract String getDescription();
}
