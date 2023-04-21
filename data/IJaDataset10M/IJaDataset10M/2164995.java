package org.viewaframework.core;

import org.viewaframework.common.MyTrayView;
import org.viewaframework.common.TestApplication;
import org.viewaframework.common.TestView;
import org.viewaframework.test.ApplicationTrapper;
import junit.framework.TestCase;

/**
 * @author Mario Garcia
 * @since 1.0.4
 *
 */
public class DefaultApplicationTest extends TestCase {

    private ApplicationTrapper app;

    public void setUp() throws Exception {
        app = new ApplicationTrapper(new DefaultApplicationLauncher().execute(TestApplication.class));
    }

    /**
	 *  This test checks the method responsible for hiding and
	 *  restoring the root view.
	 */
    public void testSetVisible() {
        app.view(MyTrayView.ID).menuItem("hiderestore").click(3000).applicationTrapper().requireVisible(false).view(MyTrayView.ID).menuItem("hiderestore").click(3000).applicationTrapper().requireVisible().view(TestView.ID).button("failureButton").click(5000).view().text("text").requireText("Bad situation!");
    }

    public void tearDown() {
        app.close();
    }
}
