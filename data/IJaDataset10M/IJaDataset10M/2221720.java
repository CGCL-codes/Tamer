package com.antilia.web.layout;

import org.apache.wicket.markup.html.WebMarkupContainer;

/**
 * 
 *
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public interface IContainer {

    public static final String BODY_ID = "body";

    public static final String BODY_CONTENT_ID = "content";

    WebMarkupContainer getBody();

    WebMarkupContainer getContent();
}
