package net.sf.jguard.ext.authentication;

import javax.inject.Inject;
import com.google.inject.Module;
import com.mycila.testing.plugin.guice.Bind;
import com.mycila.testing.plugin.guice.ModuleProvider;
import net.sf.jguard.core.authentication.AuthenticationScope;
import net.sf.jguard.core.authentication.configuration.AppConfigurationEntryFilter;
import net.sf.jguard.core.authentication.configuration.FilteredConfiguration;
import net.sf.jguard.core.authentication.configuration.GuestAppConfigurationEntryFilter;
import net.sf.jguard.core.authentication.configuration.JGuardConfiguration;
import net.sf.jguard.core.authentication.loginmodules.UserLoginModule;
import net.sf.jguard.core.authentication.manager.AuthenticationManagerModule;
import net.sf.jguard.core.authorization.policy.SingleAppPolicy;
import net.sf.jguard.core.lifecycle.*;
import net.sf.jguard.core.test.JGuardTest;
import net.sf.jguard.core.test.JGuardTestFiles;
import net.sf.jguard.core.test.MockModule;
import net.sf.jguard.ext.authentication.loginmodules.AuditLoginModule;
import net.sf.jguard.ext.authentication.loginmodules.XmlLoginModule;
import net.sf.jguard.ext.authentication.manager.XmlAuthenticationManager;
import net.sf.jguard.ext.authorization.manager.XmlAuthorizationManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.AppConfigurationEntry;
import java.net.URL;
import java.util.*;

/**
 * @author <a href="mailto:diabolo512@users.sourceforge.net">Charles Gay</a>
 */
public class FilteredConfigurationWithRealExtClassesTest extends JGuardTest {

    @Inject
    public JGuardConfiguration configuration;

    private static final String MY_APPLICATION_NAME = "myApplication";

    private static final String JGUARD_AUTHENTICATION_XML = JGuardTestFiles.J_GUARD_AUTHENTICATION_XML.toString();

    @Bind
    Request request = new MockRequestAdapter(new MockRequest());

    @Bind
    Response response = new MockResponseAdapter(new MockResponse());

    private static final String ADMIN = "admin";

    @Bind
    List<Callback> providesCallbacks() {
        List<Callback> callbacks = new ArrayList<Callback>();
        NameCallback nameCallback = new NameCallback(ADMIN);
        callbacks.add(nameCallback);
        PasswordCallback passwordCallback = new PasswordCallback("", true);
        passwordCallback.setPassword(ADMIN.toCharArray());
        callbacks.add(passwordCallback);
        return callbacks;
    }

    @ModuleProvider
    public Iterable<Module> providesModules() {
        URL applicationPath = Thread.currentThread().getContextClassLoader().getResource(".");
        return super.providesModules(AuthenticationScope.LOCAL, true, applicationPath, XmlAuthorizationManager.class);
    }

    @ModuleProvider
    public Module getDummyModule() {
        return new MockModule();
    }

    @Before
    public void setUp() {
        URL authenticationConfigurationLocation = Thread.currentThread().getContextClassLoader().getResource(JGUARD_AUTHENTICATION_XML);
        System.setProperty(SingleAppPolicy.APPLICATION_NAME_SYSTEM_PROPERTY, MY_APPLICATION_NAME);
        List<AppConfigurationEntry> entries = new ArrayList<AppConfigurationEntry>();
        Map<String, ?> options = new HashMap<String, Object>();
        AppConfigurationEntry entry = new AppConfigurationEntry(XmlLoginModule.class.getName(), AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, options);
        entries.add(entry);
        Map<String, ?> options2 = new HashMap<String, Object>();
        AppConfigurationEntry entry2 = new AppConfigurationEntry(AuditLoginModule.class.getName(), AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, options2);
        entries.add(entry2);
        configuration.addConfigEntriesForApplication(MY_APPLICATION_NAME, entries);
    }

    @Test
    public void testGetAppConfigurationEntry() {
        List<AppConfigurationEntryFilter> filters = new ArrayList<AppConfigurationEntryFilter>();
        filters.add(new GuestAppConfigurationEntryFilter());
        FilteredConfiguration filteredConfiguration = new FilteredConfiguration(configuration, filters);
        List<AppConfigurationEntry> filteredEntries = Arrays.asList(filteredConfiguration.getAppConfigurationEntry(MY_APPLICATION_NAME));
        Assert.assertTrue(filteredEntries.size() == 2);
        AppConfigurationEntry entry = filteredEntries.get(0);
        Assert.assertEquals(XmlLoginModule.class.getName(), entry.getLoginModuleName());
        Map options = entry.getOptions();
        Assert.assertTrue("skip challenge option is not set", options.containsKey(UserLoginModule.SKIP_CREDENTIAL_CHECK));
        Assert.assertTrue("skip option challenge is not set to true", options.get(UserLoginModule.SKIP_CREDENTIAL_CHECK).equals("true"));
    }

    @Override
    protected AuthenticationManagerModule buildAuthenticationManagerModule() {
        return new AuthenticationManagerModule(APPLICATION_NAME, authenticationXmlFileLocation, XmlAuthenticationManager.class);
    }
}
