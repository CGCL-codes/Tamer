package org.guiceyfruit.testing.junit3.example;

import com.google.inject.AbstractModule;

/** @version $Revision: 1.1 $ */
public class EdamModule extends AbstractModule {

    protected void configure() {
        bind(Cheese.class).to(Edam.class);
    }
}
