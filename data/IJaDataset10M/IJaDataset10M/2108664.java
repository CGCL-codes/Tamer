package org.contextor.resource.context.web;

import org.contextor.content.context.term.TermContext;
import org.contextor.content.context.term.WeightedTermContext;
import org.contextor.content.web.WebContent;
import org.contextor.resource.Resource;
import org.contextor.resource.context.ResourceContext;
import org.contextor.resource.web.DefaultWebPage;
import org.contextor.resource.web.WebPage;
import org.contextor.tag.context.TagContext;
import org.contextor.tag.context.WeightedTagContext;

/**
 * 
 * 
 * 
 * @author Behrooz Nobakht [behrooz dot nobakht at gmail dot com]
 **/
public class DefaultWebPageContext implements WebPageContext {

    private static final long serialVersionUID = 4203391583654105272L;

    protected WebPage webPageResource;

    protected WeightedTagContext tagContext;

    protected WeightedTermContext termContext;

    public DefaultWebPageContext(String url, WebContent webContent, WeightedTermContext termContext, WeightedTagContext tagContext) {
        this.webPageResource = new DefaultWebPage(url, webContent);
        this.tagContext = tagContext;
        this.termContext = termContext;
    }

    protected DefaultWebPageContext() {
    }

    @Override
    public WebContent getRawContent() {
        return webPageResource.getContent();
    }

    @Override
    public TagContext getTagContext() {
        return tagContext;
    }

    @Override
    public TermContext getTermContext() {
        return termContext;
    }

    @Override
    public Resource getResource() {
        return webPageResource;
    }

    @Override
    public WebContent getContent() {
        return getRawContent();
    }

    @Override
    public String toString() {
        return "Web Page Context for [" + webPageResource + "]";
    }

    @Override
    public int hashCode() {
        return getResource().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ResourceContext)) {
            return false;
        }
        ResourceContext rc = (ResourceContext) obj;
        return getResource().getUri().equals(rc.getResource().getUri());
    }
}
