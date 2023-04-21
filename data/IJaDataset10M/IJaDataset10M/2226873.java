package net.taylor.selenium.dsl;

import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.javascript.DebuggerImpl;

public class BrowserSession {

    public static final String URL_ROOT_PROPERTY = "net.taylor.selenium.url.root";

    public static final String BROWSER_VERSION_PROPERTY = "net.taylor.selenium.browser.version";

    public static final String IE6 = "ie6";

    public static final String IE7 = "ie7";

    public static final String FIREFOX = "firefox";

    protected ExtHtmlUnitDriver driver;

    protected String urlRoot;

    public BrowserSession() {
        initBrowserVersion();
        initDriver();
        initUrlRoot();
    }

    public static void setUrlRoot(String url) {
        System.setProperty(URL_ROOT_PROPERTY, url);
    }

    public static void setBrowserVersion(String value) {
        System.setProperty(BROWSER_VERSION_PROPERTY, value);
    }

    public ExtHtmlUnitDriver getDriver() {
        return driver;
    }

    public void getPage(String path) {
        driver.get(urlRoot + path);
    }

    public String getUrlRoot() {
        return urlRoot;
    }

    protected void initBrowserVersion() {
        String browser = System.getProperty(BROWSER_VERSION_PROPERTY, IE7);
        if (IE7.equals(browser)) {
            BrowserVersion.setDefault(BrowserVersion.INTERNET_EXPLORER_7);
        } else if (IE6.equals(browser)) {
            BrowserVersion.setDefault(BrowserVersion.INTERNET_EXPLORER_6);
        } else if (FIREFOX.equals(browser)) {
            BrowserVersion.setDefault(BrowserVersion.FIREFOX_3);
        }
    }

    protected void initUrlRoot() {
        urlRoot = System.getProperty(URL_ROOT_PROPERTY);
        if (urlRoot == null) {
            throw new RuntimeException("System Property '" + URL_ROOT_PROPERTY + "' must be set.");
        }
    }

    protected void initDriver() {
        driver = new ExtHtmlUnitDriver();
    }

    public class ExtHtmlUnitDriver extends HtmlUnitDriver {

        public ExtHtmlUnitDriver() {
            super();
            WebClient client = getWebClient();
            client.setCssErrorHandler(new SilentCssErrorHandler());
            client.setJavaScriptEnabled(true);
            client.getJavaScriptEngine().getContextFactory().setDebugger(new DebuggerImpl());
        }

        public String getContentType() {
            return getWebClient().getCurrentWindow().getEnclosedPage().getWebResponse().getContentType();
        }
    }
}
