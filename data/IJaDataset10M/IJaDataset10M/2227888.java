package org.nakedobjects.runtime.remoting;

import org.nakedobjects.metamodel.specloader.FacetDecoratorInstaller;
import org.nakedobjects.runtime.authentication.AuthenticationManagerInstaller;
import org.nakedobjects.runtime.authorization.AuthorizationManagerInstaller;
import org.nakedobjects.runtime.persistence.PersistenceMechanismInstaller;

public interface ClientConnectionInstaller extends PersistenceMechanismInstaller, FacetDecoratorInstaller, AuthenticationManagerInstaller, AuthorizationManagerInstaller {

    static String TYPE = "connector";
}
