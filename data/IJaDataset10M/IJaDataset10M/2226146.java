package net.sf.lightbound.components.links;

import java.io.IOException;
import net.sf.lightbound.Request;
import net.sf.lightbound.components.Component;
import net.sf.lightbound.controller.InternalPageResolver;
import net.sf.lightbound.exceptions.TranslationException;
import net.sf.lightbound.extend.ConstructedURL;

/**
 * A link which leads to a page handled by this LightBound instance.
 * <br/><br/>
 * For external links, see {@link WebLink}.
 * 
 * @author esa
 *
 */
public class PageLink extends Link {

    private final Class<?> pageClass;

    /**
   * Constructs a new link which leads to a page
   * 
   * @param pageClass the class of the page that this link leads to
   */
    public PageLink(Class<?> pageClass) {
        this.pageClass = pageClass;
    }

    /**
   * Constructs a new link which leads to a page
   * 
   * @param pageClass the class of the page that this link leads to
   * @param insideContext the ID resolving context (the object from which to
   *  resolve the IDs inside the document tag which this object is associated
   *  to, see {@link Component})
   */
    public PageLink(Class<?> pageClass, Object insideContext) {
        super(insideContext);
        this.pageClass = pageClass;
    }

    /**
   * Constructs a new link which takes to a page
   * 
   * @param associated rendering object the class of the page that
   *  this link leads to. May not be null.
   */
    public PageLink(Object pageObject) {
        this(pageObject.getClass());
    }

    /**
   * Constructs a new link which takes to a page
   * 
   * @param associated rendering object the class of the page that
   *  this link leads to. May not be null.
   * @param insideContext the ID resolving context (the object from which to
   *  resolve the IDs inside the document tag which this object is associated
   *  to, see {@link Component})
   */
    public PageLink(Object pageObject, Object insideContext) {
        this(pageObject.getClass(), insideContext);
    }

    /**
   * Returns the class of the page which this link takes to
   * @return the class of the page which this link takes to
   */
    public Class<?> getPageClass() {
        return pageClass;
    }

    @Override
    protected ConstructedURL toURL(InternalPageResolver pageResolver, Request request) {
        ConstructedURL url;
        try {
            url = new ConstructedURL(pageResolver.getRelativeURLPath(pageClass, request));
        } catch (IOException e) {
            throw new TranslationException("can't get relative path for request", e);
        }
        addParams(url);
        return url;
    }

    @Override
    public Object clone() {
        Link clone = new PageLink(pageClass);
        clone.addParams(this);
        return clone;
    }
}
