package com.google.gwt.inject.client;

import com.google.inject.Provider;

/**
 * Provider to call static getter on {@link MyProvided}.
 */
public class MyProvidedProvider implements Provider<MyProvided> {

    public MyProvided get() {
        return MyProvided.getInstance();
    }
}
