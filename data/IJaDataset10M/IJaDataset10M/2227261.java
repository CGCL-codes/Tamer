package org.zkoss.zktest.test2;

import java.io.IOException;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import com.thoughtworks.selenium.Selenium;

/**
 * 
 * @author sam
 * 			
 */
public class B30_1486556Test extends ZKTestCase {

    private String _target = "B30-1486556.zul";

    private List<Selenium> _browsers;

    private String _url;

    @Override
    @Before
    public void setUp() {
        _browsers = getBrowsers(_target);
        _url = getUrl(_target);
    }

    @Test(expected = AssertionError.class)
    public void test1() {
        String inputComp = uuid(4);
        String clickComp = uuid(2);
        String buttonComp = uuid(5);
        for (Selenium browser : _browsers) {
            try {
                browser.start();
                browser.open(_url);
                browser.windowFocus();
                browser.focus(inputComp);
                browser.fireEvent(inputComp, "blur");
                assertTrue("true".equals(browser.getEval("jq('#zk_comp_4').hasClass('z-textbox-text-invalid')")));
                browser.refresh();
                browser.windowFocus();
                browser.focus(inputComp);
                browser.click(buttonComp);
                assertTrue("true".equals(browser.getEval("jq('#zk_comp_4').hasClass('z-textbox-text-invalid')")));
                browser.close();
                break;
            } finally {
                browser.stop();
            }
        }
    }
}
