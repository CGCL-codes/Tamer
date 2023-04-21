package com.gargoylesoftware.htmlunit.javascript.host.html;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Unit tests for {@link HTMLMapElement}.
 *
 * @version $Revision: 6701 $
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HTMLMapElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "3", "true", "true", "true" })
    public void areas() throws Exception {
        final String html = "<html>\n" + "<head>\n" + "  <script>\n" + "    function test() {\n" + "      var map = document.createElement('map');\n" + "      var area0 = document.createElement('area');\n" + "      var area1 = document.createElement('area');\n" + "      var area2 = document.createElement('area');\n" + "      map.appendChild(area0);\n" + "      map.appendChild(area1);\n" + "      map.appendChild(area2);\n" + "      var areaElems = map.areas;\n" + "      alert(areaElems.length);\n" + "      alert(area0 === areaElems[0]);\n" + "      alert(area1 === areaElems[1]);\n" + "      alert(area2 === areaElems[2]);\n" + "    }\n" + "  </script>\n" + "</head><body onload='test()'>\n" + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
