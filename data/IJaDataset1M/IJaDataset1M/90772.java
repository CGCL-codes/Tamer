package net.sourceforge.etsysync.utils.etsy.api.commands;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.sourceforge.etsysync.utils.TestPropertiesLoader;
import net.sourceforge.etsysync.utils.etsy.api.EtsyApi;
import net.sourceforge.etsysync.utils.etsy.api.connectors.EtsyOauthConnectorInterface;
import net.sourceforge.etsysync.utils.etsy.api.connectors.EtsyOauthConnectorSignpostImpl;
import net.sourceforge.etsysync.utils.etsy.api.connectors.EtsyOauthSandboxConnectorSignpostImpl;
import net.sourceforge.etsysync.utils.etsy.api.connectors.EtsyPublicConnector;
import net.sourceforge.etsysync.utils.etsy.api.connectors.EtsyPublicSandboxConnector;
import net.sourceforge.etsysync.utils.etsy.api.resources.FeedbackInfo;
import net.sourceforge.etsysync.utils.etsy.api.resources.User;
import net.sourceforge.etsysync.utils.event.exceptions.unchecked.GeneralHttpResponseException;
import junit.framework.TestCase;

public class GetUserEndToEndTest extends TestCase {

    private String applicationKey;

    private String applicationSecret;

    private String oauthKey;

    private String oauthSecret;

    private EtsyApi etsyApiInstancePublicSandbox;

    private EtsyApi etsyApiInstancePrivateSandbox;

    private EtsyApi etsyApiInstancePublicProduction;

    private EtsyApi etsyApiInstancePrivateProduction;

    private GetUser controlGetUser;

    private String expectedUserName;

    private List<User> expectedUserListPublic;

    private List<User> expectedUserListPrivate;

    private List<User> testUserList;

    public void setUp() {
        User expectedUser = new User(10316168, "northernartist", new Date(1277410224000L), null, new FeedbackInfo(0, 0));
        this.expectedUserListPublic = new ArrayList<User>();
        this.expectedUserListPublic.add(expectedUser);
        expectedUser = new User(10316168, "northernartist", "creative_artistic1@yahoo.com", new Date(1277410224000L), null, new FeedbackInfo(0, 0));
        this.expectedUserListPrivate = new ArrayList<User>();
        this.expectedUserListPrivate.add(expectedUser);
        this.applicationKey = TestPropertiesLoader.getApplicationKey();
        this.applicationSecret = TestPropertiesLoader.getApplicationSecret();
        this.oauthKey = TestPropertiesLoader.getOauthKey();
        this.oauthSecret = TestPropertiesLoader.getOauthSecret();
        etsyApiInstancePublicSandbox = new EtsyApi(new EtsyPublicSandboxConnector(applicationKey, applicationSecret));
        etsyApiInstancePublicProduction = new EtsyApi(new EtsyPublicConnector(applicationKey, applicationSecret));
        EtsyOauthConnectorInterface oauthInterface;
        oauthInterface = new EtsyOauthSandboxConnectorSignpostImpl(applicationKey, applicationSecret);
        oauthInterface.init(oauthKey, oauthSecret);
        etsyApiInstancePrivateSandbox = new EtsyApi(oauthInterface);
        oauthInterface = new EtsyOauthConnectorSignpostImpl(applicationKey, applicationSecret);
        oauthInterface.init(oauthKey, oauthSecret);
        etsyApiInstancePrivateProduction = new EtsyApi(oauthInterface);
        this.expectedUserName = "northernartist";
        this.controlGetUser = new GetUser(expectedUserName);
    }

    public void testPublicGetUser() {
        try {
            testUserList = etsyApiInstancePublicProduction.get(controlGetUser);
            fail("This test will fail when production keys are loaded, change then");
            assertEquals(expectedUserListPublic, testUserList);
        } catch (GeneralHttpResponseException e) {
        }
    }

    public void testPrivateGetUser() {
        try {
            testUserList = etsyApiInstancePrivateProduction.get(controlGetUser);
            fail("This test will fail when production keys are loaded, change then");
            assertEquals(expectedUserListPublic, testUserList);
        } catch (GeneralHttpResponseException e) {
        }
    }

    public void testPublicGetUserInSandbox() {
        testUserList = etsyApiInstancePublicSandbox.get(controlGetUser);
        assertEquals(expectedUserListPublic, testUserList);
    }

    public void testPrivateGetUserInSandbox() {
        testUserList = etsyApiInstancePrivateSandbox.get(controlGetUser);
        assertEquals(expectedUserListPrivate, testUserList);
    }
}
