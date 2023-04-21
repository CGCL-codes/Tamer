package com.sharetime.appengine.shared;

import com.google.web.bindery.requestfactory.shared.RequestFactory;

public interface ShareTimeServerRequestFactory extends RequestFactory {

    ShareTimeServerRequest shareTimeServerRequest();

    ShareTimeServerRequest context();
}
