package de.fau.cs.dosis.acceptance.feature48;

import java.io.IOException;
import java.net.MalformedURLException;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import utils.ServerConfiguration;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import de.fau.cs.dosis.acceptance.AbstractAcceptanceTest;
import de.fau.cs.dosis.model.Role;
import de.fau.cs.dosis.model.User;
import de.fau.cs.dosis.model.UserState;

public class UserSignsUpForReviewer extends AbstractAcceptanceTest {

    @Override
    protected ServerConfiguration getServerConfig() {
        return ServerConfiguration.BASIC;
    }

    @Before
    public void setupClass() throws Exception {
        initServer();
        initDbUnit();
    }

    @After
    public void tearClass() throws Exception {
        stopDbUnit();
        stopServer();
    }

    @Test
    public void testContactButton() throws MalformedURLException, IOException {
        String name = "testReqRev3";
        User tmp = createTestUser(name, name, Role.EDITOR);
        tmp.setState(UserState.ACTIVE);
        tmp.setRequestedReviewerStatus(true);
        getUserManager().storeUser(tmp);
        HtmlPage index = login(name, name);
        HtmlPage edit = getBot().clickToPage(index, "userEdit.do");
        Assert.assertTrue(pageContainsString(edit, "mailto:stefan.bernitzki@mydosis.de"));
    }

    @Test
    @Ignore("sign up is no longer supported with the current gui")
    public void testSignUp() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
        String name = "testReqRev1";
        User tmp = createTestUser(name, name, Role.EDITOR);
        tmp.setState(UserState.ACTIVE);
        tmp.setRequestedReviewerStatus(true);
        getUserManager().storeUser(tmp);
        HtmlPage index = login(name, name);
        HtmlPage edit = getBot().clickToPage(index, "userEdit.do");
        getBot().clickInputByName(edit, "becomeReviewer");
        User u = getUserManager().getUserByUsername(name);
        Assert.assertNotNull(u);
        Assert.assertTrue(u.isRequestedReviewerStatus());
        getUserManager().deleteUser(u);
    }

    @Test
    public void testApprove() throws MalformedURLException, IOException {
        String name = "testReqRev2";
        User tmp = createTestUser(name, name, Role.EDITOR);
        tmp.setRequestedReviewerStatus(true);
        getUserManager().storeUser(tmp);
        HtmlPage index = login("admin", "admin");
        HtmlPage userList = getBot().clickToPage(index, "becomeReviewerList.do");
        HtmlPage adminUser = getBot().clickToPage(userList, "adminUser.do?user=" + name);
        HtmlSelect select = getBot().getSelectByName(adminUser, "role");
        getBot().setSelectOptionByText(select, "Vidierer");
        getBot().getInputByName(adminUser, "submit").click();
        User u = getUserManager().getUserByUsername(name);
        Assert.assertNotNull(u);
        Assert.assertFalse(u.isRequestedReviewerStatus());
        Assert.assertTrue(u.getRole() == Role.REVIEWER);
        getUserManager().deleteUser(u);
    }
}
