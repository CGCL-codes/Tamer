package mobi.ilabs.html;

import javax.microedition.lcdui.Display;

/**
* Simple embeded html-browser.
* <p>
* @author �ystein Myhre
*/
public interface HtmlPlugin {

    public void treat(Display display, String url, String contentType) throws Throwable;
}
