package org.nakedobjects.runtime.system;

import org.nakedobjects.metamodel.config.ConfigurationConstants;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.specloader.NakedObjectReflector;
import org.nakedobjects.metamodel.specloader.NakedObjectReflectorInstaller;
import org.nakedobjects.runtime.authentication.AuthenticationManager;
import org.nakedobjects.runtime.authentication.AuthenticationManagerInstaller;
import org.nakedobjects.runtime.authorization.AuthorizationManager;
import org.nakedobjects.runtime.authorization.AuthorizationManagerInstaller;
import org.nakedobjects.runtime.fixturesinstaller.FixturesInstaller;
import org.nakedobjects.runtime.imageloader.TemplateImageLoaderInstaller;
import org.nakedobjects.runtime.persistence.PersistenceMechanismInstaller;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactory;
import org.nakedobjects.runtime.persistence.services.ServicesInstaller;
import org.nakedobjects.runtime.remoting.ClientConnectionInstaller;
import org.nakedobjects.runtime.userprofile.UserProfileStore;
import org.nakedobjects.runtime.userprofile.UserProfileStoreInstaller;
import org.nakedobjects.runtime.viewer.NakedObjectsViewer;
import org.nakedobjects.runtime.viewer.NakedObjectsViewerInstaller;
import org.nakedobjects.runtime.web.EmbeddedWebServerInstaller;

public final class SystemConstants {

    /**
     * Key used to lookup {@link DeploymentType} (eg via command line) in {@link NakedObjectConfiguration}.
     * 
     * <p>
     * Use {@link DeploymentType#valueOf(String)} to decode.
     */
    public static final String DEPLOYMENT_TYPE_KEY = ConfigurationConstants.ROOT + "deploymentType";

    /**
     * Key used to lookup {@link NakedObjectReflector reflector} in {@link NakedObjectConfiguration}, and root
     * for any {@link NakedObjectReflectorInstaller reflector}-specific configuration keys.
     */
    public static final String REFLECTOR_KEY = ConfigurationConstants.ROOT + NakedObjectReflectorInstaller.TYPE;

    /**
     * Default for {@link #REFLECTOR_KEY}
     */
    public static final String REFLECTOR_DEFAULT = "java";

    /**
     * Key used to lookup {@link NakedObjectsViewer viewer} in {@link NakedObjectConfiguration}, and root for
     * any {@link NakedObjectsViewerInstaller viewer}-specific configuration keys.
     */
    public static final String VIEWER_KEY = ConfigurationConstants.ROOT + NakedObjectsViewerInstaller.TYPE;

    /**
     * Default for {@link #VIEWER_KEY}.
     */
    public static final String VIEWER_DEFAULT = "dnd";

    /**
     * Key used to lookup {@link PersistenceSessionFactory persistor} in {@link NakedObjectConfiguration}, and
     * root for any {@link PersistenceMechanismInstaller persistor}-specific configuration keys.
     */
    public static final String OBJECT_PERSISTOR_INSTALLER_KEY = ConfigurationConstants.ROOT + PersistenceMechanismInstaller.TYPE;

    public static final String OBJECT_PERSISTOR_KEY = OBJECT_PERSISTOR_INSTALLER_KEY;

    public static final String OBJECT_PERSISTOR_NON_PRODUCTION_DEFAULT = "in-memory";

    public static final String OBJECT_PERSISTOR_PRODUCTION_DEFAULT = "xml";

    /**
     * Key used to lookup {@link UserProfileStore user profile store} (via command line) in
     * {@link NakedObjectConfiguration}, and root for any {@link UserProfileStoreInstaller user profile store}
     * -specific configuration keys.
     */
    public static final String PROFILE_PERSISTOR_INSTALLER_KEY = ConfigurationConstants.ROOT + UserProfileStoreInstaller.TYPE;

    public static final String USER_PROFILE_STORE_KEY = PROFILE_PERSISTOR_INSTALLER_KEY;

    public static final String USER_PROFILE_STORE_NON_PRODUCTION_DEFAULT = "in-memory";

    public static final String USER_PROFILE_STORE_PRODUCTION_DEFAULT = "xml";

    /**
     * Key used to lookup {@link AuthenticationManager authentication manager} in
     * {@link NakedObjectConfiguration}, and root for any {@link AuthenticationManagerInstaller authentication
     * manager}-specific configuration keys.
     */
    public static final String AUTHENTICATION_INSTALLER_KEY = ConfigurationConstants.ROOT + AuthenticationManagerInstaller.TYPE;

    /**
     * Default for {@link #AUTHENTICATION_INSTALLER_KEY}
     */
    public static final String AUTHENTICATION_DEFAULT = "file";

    /**
     * Key used to lookup {@link AuthorizationManager authorization manager} in
     * {@link NakedObjectConfiguration}, and root for any {@link AuthorizationManagerInstaller authorization
     * manager}-specific configuration keys.
     */
    public static final String AUTHORIZATION_INSTALLER_KEY = ConfigurationConstants.ROOT + AuthorizationManagerInstaller.TYPE;

    /**
     * Default for {@link #AUTHORIZATION_DEFAULT}
     */
    public static final String AUTHORIZATION_DEFAULT = "file";

    /**
     * Key used to lookup {@link ServicesInstaller services installer} in {@link NakedObjectConfiguration},
     * and root for any {@link ServicesInstaller services installer}-specific configuration keys.
     */
    public static final String SERVICES_INSTALLER_KEY = ConfigurationConstants.ROOT + ServicesInstaller.TYPE;

    /**
     * Default for {@link #SERVICES_INSTALLER_KEY}
     */
    public static final String SERVICES_INSTALLER_DEFAULT = "configuration";

    /**
     * Key used to lookup {@link FixturesInstaller fixtures installer} in {@link NakedObjectConfiguration},
     * and root for any {@link FixturesInstaller fixtures installer}-specific configuration keys.
     */
    public static final String FIXTURES_INSTALLER_KEY = ConfigurationConstants.ROOT + FixturesInstaller.TYPE;

    /**
     * Default for {@link #FIXTURES_INSTALLER_KEY}
     */
    public static final String FIXTURES_INSTALLER_DEFAULT = "configuration";

    /**
     * Key used to lookup {@link TemplateImageLoaderInstaller template image loader} in
     * {@link NakedObjectConfiguration}, and root for any {@link TemplateImageLoaderInstaller template image
     * loader}-specific configuration keys.
     */
    public static final String IMAGE_LOADER_KEY = ConfigurationConstants.ROOT + TemplateImageLoaderInstaller.TYPE;

    /**
     * Default for {@link #IMAGE_LOADER_KEY}
     */
    public static final String IMAGE_LOADER_DEFAULT = "awt";

    /**
     * Key used to lookup {@link ClientConnectionInstaller client connection installer} in
     * {@link NakedObjectConfiguration}, and root for any {@link ClientConnectionInstaller client connection
     * installer}-specific configuration keys.
     */
    public static final String CLIENT_CONNECTION_KEY = ConfigurationConstants.ROOT + ClientConnectionInstaller.TYPE;

    /**
     * Default for {@link #CLIENT_CONNECTION_KEY}
     */
    public static final String CLIENT_CONNECTION_DEFAULT = "encoding_socket";

    /**
     * Key used to lookup {@link EmbeddedWebServerInstaller embedded web installer} in
     * {@link NakedObjectConfiguration}, and root for any {@link EmbeddedWebServerInstaller embedded web
     * server installer}-specific configuration keys.
     */
    public static final String WEBSERVER_KEY = ConfigurationConstants.ROOT + EmbeddedWebServerInstaller.TYPE;

    /**
     * Default for {@link #WEBSERVER_KEY}
     */
    public static final String WEBSERVER_DEFAULT = "jetty";

    /**
     * Key by which requested fixture (eg via command line) is made available in
     * {@link NakedObjectConfiguration}.
     */
    public static final String FIXTURE_KEY = ConfigurationConstants.ROOT + "fixture";

    /**
     * Key by which requested user (eg via command line) is made available in {@link NakedObjectConfiguration}
     * .
     */
    public static final String USER_KEY = ConfigurationConstants.ROOT + "user";

    /**
     * Key by which requested password (eg via command line) is made available in
     * {@link NakedObjectConfiguration}.
     */
    public static final String PASSWORD_KEY = ConfigurationConstants.ROOT + "password";

    /**
     * Key as to whether to show splash (eg via command line) is made available in
     * {@link NakedObjectConfiguration}.
     * 
     * <p>
     * Use {@link Splash#valueOf(String)} to decode.
     */
    public static final String NOSPLASH_KEY = ConfigurationConstants.ROOT + "nosplash";

    public static final boolean NOSPLASH_DEFAULT = false;

    public static final String LOCALE_KEY = ConfigurationConstants.ROOT + "locale";

    private SystemConstants() {
    }
}
