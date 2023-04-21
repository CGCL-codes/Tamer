package org.ieducnews.view.component;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.TestPanelSource;
import org.apache.wicket.util.tester.WicketTester;
import org.ieducnews.model.DomainModel;
import org.ieducnews.model.config.ModelProperties;
import org.ieducnews.view.AboutPage;
import org.ieducnews.view.HomePage;
import org.ieducnews.view.WebApp;
import org.ieducnews.view.concept.contribution.AddSubmissionPage;
import org.ieducnews.view.concept.member.SignInPage;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FooterPanelTest {

    private static WebApp webApp;

    private static WicketTester tester;

    @BeforeClass
    public static void beforeTests() {
        ModelProperties modelProperties = new ModelProperties(FooterPanelTest.class);
        DomainModel domainModel = new DomainModel(modelProperties);
        domainModel = domainModel.load();
        webApp = new WebApp();
        webApp.setDomainModel(domainModel);
        tester = new WicketTester(webApp);
        tester.setupRequestAndResponse();
        tester.startPage(SignInPage.class);
        FormTester formTester = tester.newFormTester("signIn");
        formTester.setValue("account", "pascald");
        formTester.setValue("password", "pd");
        formTester.submit();
    }

    @Before
    public void beforeTest() {
        tester.startPanel(new FooterPanelSource());
    }

    @Test
    public void containComponents() {
        tester.assertComponent("panel:new", Link.class);
        tester.assertComponent("panel:submit", SubmissionLink.class);
        tester.assertComponent("panel:about", BookmarkablePageLink.class);
    }

    @Test
    public void navigateToHome() {
        tester.clickLink("panel:new");
        tester.assertRenderedPage(HomePage.class);
    }

    @Test
    public void navigateToSubmitLink() {
        tester.clickLink("panel:submit");
        tester.assertRenderedPage(AddSubmissionPage.class);
    }

    @Test
    public void navigateToAbout() {
        tester.clickLink("panel:about");
        tester.assertRenderedPage(AboutPage.class);
    }

    private class FooterPanelSource implements TestPanelSource {

        private static final long serialVersionUID = 1;

        public Panel getTestPanel(String panelId) {
            return new FooterPanel(panelId);
        }
    }
}
