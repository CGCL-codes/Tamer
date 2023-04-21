package org.hrodberaht.inject.internal;

import org.hrodberaht.inject.SimpleInjection;
import org.hrodberaht.inject.register.RegistrationModule;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 *         2010-maj-29 12:48:43
 * @version 1.0
 * @since 1.0
 */
public interface RegistrationInjectionContainer {

    void register(InjectionKey key, Class service, SimpleInjection.Scope scope, SimpleInjection.RegisterType type, boolean throwError);

    void register(RegistrationModule... modules);
}
