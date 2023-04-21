package com.director.hibernate;

import org.hibernate.SessionFactory;

/**
 * @author Simone Ricciardi
 * @version 1.0, 12/04/2011
 */
public class LoadByStringIdParameterFactory extends LoadByIdParameterFactory<String> {

    public LoadByStringIdParameterFactory(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Class<String> getInputType() {
        return String.class;
    }
}
