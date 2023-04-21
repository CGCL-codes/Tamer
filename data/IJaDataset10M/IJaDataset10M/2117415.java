package net.sf.iqser.plugin.linkedin;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;
import java.util.Properties;
import junit.framework.TestCase;
import net.sf.iqser.plugin.linkedin.test.MockAnalyzerTaskStarter;
import net.sf.iqser.plugin.linkedin.test.MockRepository;
import net.sf.iqser.plugin.linkedin.test.TestServiceLocator;
import net.sf.iqser.plugin.linkedin.test.UnitTestUtils;
import net.sf.iqser.plugin.linkedin.utils.ContentURLUtils;
import net.sf.iqser.plugin.linkedin.test.MockContentProviderFacade;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;
import com.google.code.linkedinapi.client.LinkedInApiClient;
import com.google.code.linkedinapi.client.LinkedInApiClientFactory;
import com.google.code.linkedinapi.client.enumeration.ProfileField;
import com.google.code.linkedinapi.client.oauth.LinkedInAccessToken;
import com.google.code.linkedinapi.schema.Person;
import com.google.code.linkedinapi.schema.Update;
import com.iqser.core.config.Configuration;
import com.iqser.core.exception.IQserTechnicalException;
import com.iqser.core.model.Attribute;
import com.iqser.core.model.Content;
import com.iqser.core.repository.Repository;
import edu.emory.mathcs.backport.java.util.Arrays;

public class LinkedInContentProviderTestOnline extends TestCase {

    private LinkedInContentProvider licp;

    private String apiKey = "w0BCQkXr846DoIebHYiNu9ZR5aykWd9dINk8AjTfithCysgvQVVnLInQ0zfwn9WP";

    private String apiSecret = "t8L0U_XmdnjfzqMb810lwLUidRoVMrDqXG7jp8ALCyT4_Vycaq6qC2w71MmZ8P0k";

    private String token = "09d3623a-e56c-497d-964b-1a5922e378d9";

    private String tokenSecret = "f7fb8991-a1dd-4d94-b961-26bd3350e595";

    private LinkedInApiClientFactory factory;

    @Override
    protected void setUp() throws Exception {
        PropertyConfigurator.configure("src/test/resources/log4j.properties");
        Properties initParams = new Properties();
        initParams.setProperty("api-key", apiKey);
        initParams.setProperty("api-secret", apiSecret);
        initParams.setProperty("token", token);
        initParams.setProperty("secret-token", tokenSecret);
        initParams.setProperty("attribute-mappings", "[UPDATE_COMMENT=COMMENT_UPDATE][SCHOOL=SCHULE][TITLE=TITLE]");
        initParams.setProperty("content-type-mappings", "[POSTING=UPDATE][PROFILE=PERSON]");
        initParams.setProperty("key-attributes", "[SCHULE]");
        licp = new LinkedInContentProvider();
        licp.setInitParams(initParams);
        licp.setId("net.sf.iqser.plugin.linkedin");
        licp.init();
        Configuration.configure(getClass().getResourceAsStream("/iqser-config.xml"));
        TestServiceLocator sl = (TestServiceLocator) Configuration.getConfiguration().getServiceLocator();
        MockRepository rep = new MockRepository();
        rep.init();
        sl.setRepository(rep);
        sl.setAnalyzerTaskStarter(new MockAnalyzerTaskStarter());
        MockContentProviderFacade cpFacade = new MockContentProviderFacade();
        cpFacade.setRepo(rep);
        sl.setFacade(cpFacade);
        factory = LinkedInApiClientFactory.newInstance(apiKey, apiSecret);
    }

    public void testGetContent() {
        LinkedInAccessToken liat = new LinkedInAccessToken(token, tokenSecret);
        LinkedInApiClient apiClient = factory.createLinkedInApiClient(liat);
        Person currentUser = apiClient.getProfileForCurrentUser(EnumSet.allOf(ProfileField.class));
        String id = currentUser.getId();
        String type = LinkedInContentProvider.TYPE_PROFILE;
        String contentURL = ContentURLUtils.createContentURL(type, id);
        Content content = licp.getContent(contentURL);
        assertNotNull(content);
        Attribute attributeByName = content.getAttributeByName("FIRST_NAME");
        String value = attributeByName.getValue();
        assertEquals("Adi", value);
    }

    public void testInit() throws Exception {
        Field field;
        field = licp.getClass().getDeclaredField("attributeMappings");
        field.setAccessible(true);
        Map<String, String> attributeMappings = (Map<String, String>) field.get(licp);
        assertNotNull(attributeMappings);
        assertTrue(attributeMappings.size() > 0);
        assertEquals("COMMENT_UPDATE", attributeMappings.get("UPDATE_COMMENT"));
        assertEquals("SCHULE", attributeMappings.get("SCHOOL"));
        field = licp.getClass().getDeclaredField("contentTypeMappings");
        field.setAccessible(true);
        Map<String, String> typesMap = (Map<String, String>) field.get(licp);
        assertNotNull(typesMap);
        assertTrue(typesMap.size() > 0);
        assertEquals("UPDATE", typesMap.get("POSTING"));
        assertEquals("PERSON", typesMap.get("PROFILE"));
    }

    public void testPerformPostAction() {
    }

    public void testDoSynchronization() throws IQserTechnicalException {
        Repository repository = Configuration.getConfiguration().getServiceLocator().getRepository();
        Collection contentByProvider = repository.getContentByProvider(licp.getId(), false);
        assertEquals(0, contentByProvider.size());
        licp.doSynchonization();
        contentByProvider = repository.getContentByProvider(licp.getId(), false);
        System.out.println(contentByProvider.size());
        assertTrue(contentByProvider.size() > 0);
    }

    public void testDoHousekeeping() throws IQserTechnicalException {
        Repository repository = Configuration.getConfiguration().getServiceLocator().getRepository();
        Collection contentByProvider = repository.getContentByProvider(licp.getId(), false);
        assertEquals(0, contentByProvider.size());
        Content content = new Content();
        content.setContentUrl("someContent");
        content.setProvider(licp.getId());
        repository.addContent(content);
        contentByProvider = repository.getContentByProvider(licp.getId(), false);
        assertEquals(1, contentByProvider.size());
        licp.doHousekeeping();
        contentByProvider = repository.getContentByProvider(licp.getId(), false);
        assertEquals(0, contentByProvider.size());
    }

    @Test
    public void testGetBinaryData() {
        Content content;
        String[] profileIds = new String[] { "1Vc8iynbaL", "HlJlDx6epM" };
        for (String profileId : profileIds) {
            String contentUrl = ContentURLUtils.createContentURL(licp.TYPE_PROFILE, profileId);
            content = createDummyContent(licp.TYPE_PROFILE, contentUrl);
            assertNotNull(licp.getBinaryData(content));
        }
        String[] updateKeys = new String[] { "UNIU-112734140-5495740676614139904-SHARE", "UNIU-112734140-5492171737300742144-SHARE", "UNIU-155040-5484392187368050688-SHARE" };
        for (String updateKey : updateKeys) {
            String contentUrl = ContentURLUtils.createContentURL(licp.TYPE_POSTING, updateKey);
            content = createDummyContent(licp.TYPE_POSTING, contentUrl);
            assertNotNull(licp.getBinaryData(content));
        }
        content = createDummyContent(null, null);
        assertNull(licp.getBinaryData(content));
    }

    private Content createDummyContent(String type, String contentUrl) {
        Content c = new Content();
        c.setProvider(licp.getId());
        c.setType(type);
        c.setContentUrl(contentUrl);
        return c;
    }
}
