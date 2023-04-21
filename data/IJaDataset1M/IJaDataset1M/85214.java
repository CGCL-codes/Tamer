package org.contextor.resource.io.loader.web;

import org.contextor.content.context.term.provider.TermContextProviderAware;
import org.contextor.content.io.ContentResourceLoaderAware;
import org.contextor.content.io.loader.web.content.WebContentParserAware;
import org.contextor.resource.context.web.WebPageContext;
import org.contextor.resource.io.ResourceLoader;
import org.contextor.resource.web.WebPage;
import org.contextor.tag.context.provider.TagContextProviderAware;
import org.springframework.beans.factory.InitializingBean;

/**
 * An interface to load and create instances of {@link WebPage} having their
 * URL's.
 * 
 * 
 * @author Behrooz Nobakht [behrooz dot nobakht at gmail dot com]
 **/
public interface WebPageFactory extends ResourceLoader, ContentResourceLoaderAware, TermContextProviderAware, TagContextProviderAware, WebContentParserAware, InitializingBean {

    WebPageContext getObject() throws Exception;

    Class<? extends WebPageContext> getObjectType();

    boolean isSingleton();
}
