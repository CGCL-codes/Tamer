package com.antilia.demo;

import com.antilia.web.WebApplication;
import com.antilia.web.osgi.WebApplicationActivator;

public class Activator extends WebApplicationActivator {

    public Activator() {
    }

    @Override
    protected Class<? extends WebApplication> getApplicationClass() {
        return DemoApplication.class;
    }

    @Override
    protected String getServletAlias() {
        return "demo";
    }
}
