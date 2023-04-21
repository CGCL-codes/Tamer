package com.kxproweb.kxprowebgwt.client.resources;

import com.google.gwt.core.client.GWT;

public interface Messages extends com.google.gwt.i18n.client.Messages {

    public static final Messages INSTANCE = GWT.create(Messages.class);

    String disconnect();
}
