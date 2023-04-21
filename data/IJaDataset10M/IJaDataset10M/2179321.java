package br.gov.frameworkdemoiselle.authorization;

import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import javax.servlet.http.HttpServletRequest;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import br.gov.frameworkdemoiselle.DemoiselleException;
import br.gov.frameworkdemoiselle.security.RequiredRole;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.ResourceBundle;

/**
 * @author SERPRO
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Beans.class)
public class JaasAuthorizerTest {

    private JaasAuthorizer authorizer;

    @Before
    public void setUp() throws Exception {
        authorizer = new JaasAuthorizer();
    }

    @Test
    public void testHasPermission() {
        ResourceBundle bundle = new ResourceBundle(ResourceBundle.getBundle("demoiselle-authorization-bundle"));
        Whitebox.setInternalState(authorizer, "bundle", bundle);
        try {
            authorizer.hasPermission("", "");
            Assert.fail();
        } catch (DemoiselleException e) {
            Assert.assertTrue(e.getMessage().equals(bundle.getString("permission-not-defined-for-jaas", RequiredRole.class.getSimpleName())));
        }
    }

    @Test
    public void testHasRole() {
        HttpServletRequest request = PowerMock.createMock(HttpServletRequest.class);
        expect(request.isUserInRole((String) EasyMock.anyObject())).andReturn(true);
        mockStatic(Beans.class);
        expect(Beans.getReference(HttpServletRequest.class)).andReturn(request).anyTimes();
        PowerMock.replay(request, Beans.class);
        Assert.assertTrue(authorizer.hasRole(""));
    }
}
