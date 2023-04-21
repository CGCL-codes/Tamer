package org.webiyo.examples.sourceforge;

import junit.framework.TestCase;
import org.webiyo.util.test.ElementChecker;
import org.webiyo.xml.XmlException;
import java.io.File;
import java.util.Iterator;

public class HomePageTest extends TestCase {

    private HomePage homePage;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Project project = new FakeProject(new File("."), Project.WEBIYO_SOURCE_DIRS);
        homePage = new HomePage(project);
    }

    public void testRender() throws Exception {
        SourceForgePageChecker page = new SourceForgePageChecker(homePage);
        page.checkBasics("Webiyo", "");
        page.checkSeeSource("src/org/webiyo/examples/sourceforge/HomePage.html", "src/org/webiyo/examples/sourceforge/HomePageTest.html");
        page.checkText("Webiyo (pronounced \"webby-O\") is a small Java 1.5 library blah blah blah...", "/html/body//div[@class='project-description']");
        checkProjectNews(page);
    }

    private void checkProjectNews(SourceForgePageChecker page) throws XmlException {
        ElementChecker newsDiv = page.navigateTo("/html/body//div[@class='news']");
        Iterator<ElementChecker> it = newsDiv.select("//div[@class='news-item']").iterator();
        ElementChecker item = it.next();
        item.checkText("More to come...", "h3");
        item.checkText("Still setting up. \n            a link", "p");
        assertFalse(it.hasNext());
    }
}
