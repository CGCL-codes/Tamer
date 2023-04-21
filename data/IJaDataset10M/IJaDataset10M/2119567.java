package com.gargoylesoftware.htmlunit.javascript.host;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Unit tests for {@link BoxObject}.
 *
 * @version $Revision: 6701 $
 * @author Daniel Gredler
 */
@RunWith(BrowserRunner.class)
public class BoxObjectTest extends WebDriverTestCase {

    /**
     * Tests box object attributes relating to HTML elements: firstChild, lastChild, previousSibling, etc.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3 = { "true", "true", "true", "true", "true" }, DEFAULT = "exception")
    public void testElementAttributes() throws Exception {
        final String html = "<html>\n" + "  <body onload='test()'>\n" + "    <span id='foo'>foo</span><div id='d'><span id='a'>a</span><span id='b'>b</span></div><span id='bar'>bar</span>\n" + "    <script>\n" + "      function test() {\n" + "        try {\n" + "          var div = document.getElementById('d');\n" + "          var spanFoo = document.getElementById('foo');\n" + "          var spanA = document.getElementById('a');\n" + "          var spanB = document.getElementById('b');\n" + "          var spanBar = document.getElementById('bar');\n" + "          var box = document.getBoxObjectFor(div);\n" + "          alert(box.element == div);\n" + "          alert(box.firstChild == spanA);\n" + "          alert(box.lastChild == spanB);\n" + "          alert(box.previousSibling == spanFoo);\n" + "          alert(box.nextSibling == spanBar);\n" + "        } catch (e) {alert('exception')}\n" + "      }\n" + "    </script>\n" + "  </body>\n" + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Tests box object attributes relating to position and size: x, y, screenX, screenY, etc.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3 = { "73-123", "73-244", "510-410" }, DEFAULT = "exception")
    public void testPositionAndSizeAttributes() throws Exception {
        final String html = "<html>\n" + "  <body onload='test()'>\n" + "    <style>#d { position:absolute; left:50px; top:100px; width:500px; height:400px; border:3px; padding: 5px; margin: 23px; }</style>\n" + "    <div id='d'>daniel</div>\n" + "    <script>\n" + "      function test() {\n" + "        try {\n" + "          var div = document.getElementById('d');\n" + "          var box = document.getBoxObjectFor(div);\n" + "          alert(box.x + '-' + box.y);\n" + "          alert(box.screenX + '-' + box.screenY);\n" + "          alert(box.width + '-' + box.height);\n" + "        } catch (e) {alert('exception')}\n" + "      }\n" + "    </script>\n" + "  </body>\n" + "</html>";
        loadPageWithAlerts2(html);
    }
}
