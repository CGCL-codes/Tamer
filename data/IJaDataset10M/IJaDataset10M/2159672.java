package org.jazzteam.androidframework.appconstructor;

import org.junit.Before;
import org.junit.Test;

public class ApplicationCreatorTest {

    private ApplicationCreator creator;

    @Before
    public void setUp() {
        creator = new ApplicationCreator("");
        creator.setAppName("Conference");
        creator.setMainTitle("Conference Guide");
        creator.setMenuButtonConferenceSchedule("Conference Schedule");
        creator.setMenuButtonMapOfCity("City map");
    }

    @Test
    public void testRewriteXML() {
        creator.rewriteStringsXml("D:\\Workspace\\Kessler\\AndroidWizard\\resources\\strings.xml");
    }
}
