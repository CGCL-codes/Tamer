package com.liferay.portalweb.portlet.unitconverter;

import com.liferay.portalweb.portal.BaseTestCase;
import com.liferay.portalweb.portal.util.RuntimeVariables;

/**
 * <a href="ConvertUnitTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class ConvertUnitTest extends BaseTestCase {

    public void testConvertUnit() throws Exception {
        selenium.type("_27_fromValue", RuntimeVariables.replace("1.0"));
        selenium.select("_27_fromId", RuntimeVariables.replace("label=Inch"));
        selenium.select("_27_toId", RuntimeVariables.replace("label=Centimeter"));
        selenium.click("//input[@value='Convert']");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("_27_to_value")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
    }
}
