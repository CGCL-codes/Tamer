package org.nakedobjects.runtime.session;

import java.util.List;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.runtime.authentication.AuthenticationManager;
import org.nakedobjects.runtime.authorization.AuthorizationManager;
import org.nakedobjects.runtime.imageloader.TemplateImageLoader;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactory;
import org.nakedobjects.runtime.system.DeploymentType;
import org.nakedobjects.runtime.userprofile.UserProfileLoader;

/**
 * As its superclass, but provides a default for some of more basic components (that
 * is, where the core framework offers only a single implementation). 
 */
public class NakedObjectSessionFactoryDefault extends NakedObjectSessionFactoryAbstract {

    public NakedObjectSessionFactoryDefault(final DeploymentType deploymentType, final NakedObjectConfiguration configuration, final TemplateImageLoader templateImageLoader, final SpecificationLoader specificationLoader, final AuthenticationManager authenticationManager, AuthorizationManager authorizationManager, final UserProfileLoader userProfileLoader, final PersistenceSessionFactory persistenceSessionFactory, final List<Object> servicesList) {
        super(deploymentType, configuration, specificationLoader, templateImageLoader, authenticationManager, authorizationManager, userProfileLoader, persistenceSessionFactory, servicesList);
    }
}
