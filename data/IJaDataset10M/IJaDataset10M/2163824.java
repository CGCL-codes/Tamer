package org.t2framework.it;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.logging.Logger;
import junit.framework.TestCase;
import org.openqa.selenium.server.SeleniumServer;
import org.t2framework.it.annotation.Browser;
import org.t2framework.it.annotation.Capture;
import org.t2framework.it.annotation.Port;
import org.t2framework.it.annotation.Url;
import org.t2framework.it.common.BrowserType;
import org.t2framework.it.common.Default;
import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

/**
 * Selenium TestCase
 * 
 * @author yone098
 */
public abstract class SeleniumTestCase extends TestCase {

    protected static final Logger logger = Logger.getLogger(SeleniumTestCase.class.getName());

    protected SeleniumServer seleniumServer;

    protected Selenium selenium;

    private static int emptyPort = -1;

    protected String fileSeparator = System.getProperty("file.separator");

    protected IntegrationTestInfo info;

    public SeleniumTestCase() {
        createIntegrationTestInfo();
    }

    /**
	 * setUp
	 */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        startSeleniumServer();
        startSelenium();
    }

    protected void createIntegrationTestInfo() {
        info = new IntegrationTestInfo();
        Class<? extends SeleniumTestCase> clazz = getClass();
        info.browser = getBrowser(clazz);
        info.capturePath = getCaptureSaveDir(clazz);
        info.port = getPort(clazz);
        info.url = getUrl(clazz);
        logger.info("Default info browser[" + info.browser + "] port[" + info.port + "] url[" + info.url + "] capturePath[" + info.capturePath + "]");
    }

    /**
	 * tearDown
	 */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        stopSelenium();
        stopSeleniumServer();
    }

    /**
	 * start selenium server
	 * 
	 * @throws Exception
	 */
    private void startSeleniumServer() throws Exception {
        searchEmptyPort();
        seleniumServer = new SeleniumServer(SeleniumTestCase.emptyPort);
        seleniumServer.start();
    }

    /**
	 * stop selenium server
	 */
    private void stopSeleniumServer() {
        seleniumServer.stop();
    }

    /**
	 * start selenium
	 */
    private void startSelenium() {
        final String url = createUrl();
        logger.info("SeleniumTestCase Browser[" + info.browser + "]");
        selenium = new DefaultSelenium(Default.HOST, SeleniumTestCase.emptyPort, info.browser, url);
        selenium.setSpeed(Default.SPEED);
        selenium.start();
    }

    /**
	 * stop selenium
	 */
    private void stopSelenium() {
        selenium.stop();
    }

    /**
	 * pause command
	 * 
	 * @param millis
	 *            miliseconds
	 */
    protected void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Captures a PNG screenshot to the specified file
	 * 
	 * @param file
	 *            fileName or filename the absolute path to the file to be
	 *            written, ex. "c:\\tmp\\screenshot.png" or "test.png"
	 */
    protected void captureScreen(final String file) {
        final String filename = getCaptureScreenshotPath(file);
        windowMaximize();
        pause(3000);
        selenium.captureScreenshot(filename);
    }

    /**
	 * window size maximize
	 */
    protected void windowMaximize() {
        selenium.getEval("moveTo(0,0);resizeTo(screen.width,screen.height);window.focus();");
    }

    /**
	 * create capture path
	 * 
	 * @param filepath
	 *            fileName or filename the absolute path to the file to be
	 *            written, ex. "c:\\tmp\\screenshot.png" or "test.png"
	 */
    protected String getCaptureScreenshotPath(final String filepath) {
        String savePath = filepath;
        final String saveDir = info.capturePath;
        if (saveDir != null) {
            if (!saveDir.endsWith(fileSeparator)) {
                savePath = saveDir + fileSeparator + filepath;
            } else {
                savePath = saveDir + filepath;
            }
        }
        return savePath;
    }

    /**
	 * It looks for an empty port.
	 */
    protected void searchEmptyPort() {
        if (SeleniumTestCase.emptyPort != -1) {
            return;
        }
        int localPort = SeleniumServer.DEFAULT_PORT;
        ServerSocket socket;
        try {
            socket = new ServerSocket();
            socket.bind(new InetSocketAddress(0));
            localPort = socket.getLocalPort();
            socket.close();
        } catch (IOException e) {
        }
        SeleniumTestCase.emptyPort = localPort;
    }

    /**
	 * create url
	 * 
	 * @return url string
	 */
    protected String createUrl() {
        Class<? extends SeleniumTestCase> clazz = getClass();
        String url = getUrl(clazz);
        int port = getPort(clazz);
        if (port == -1) port = info.port;
        logger.info("SeleniumTestCase Url[" + url + "] port[" + port + "]");
        url += ":" + String.valueOf(port);
        return url;
    }

    /**
	 * get browser from class annotation
	 * 
	 * @return browser
	 */
    protected String getBrowser(Class<?> clazz) {
        Browser browser = clazz.getAnnotation(Browser.class);
        String ret = Default.BROWSER;
        if (browser != null) {
            BrowserType type = browser.value();
            String path = browser.path();
            if (type != null) {
                ret = type.type();
            }
            if (path != null) {
                ret += " " + path;
            }
        }
        return ret.trim();
    }

    /**
	 * get Url from class annotation
	 * 
	 * @return url
	 */
    protected String getUrl(Class<?> clazz) {
        Url url = clazz.getAnnotation(Url.class);
        if (url == null) {
            return Default.URL;
        }
        final String value = url.value();
        return (!"".equals(value)) ? value : Default.URL;
    }

    /**
	 * get Port number from class annotation
	 * 
	 * @return port
	 */
    protected int getPort(Class<?> clazz) {
        Port port = clazz.getAnnotation(Port.class);
        if (port == null) {
            return Integer.valueOf(Default.PORT).intValue();
        }
        final int value = port.value();
        return (value != -1) ? value : Integer.valueOf(Default.PORT).intValue();
    }

    /**
	 * get Capture save directory from annotation
	 * 
	 * @param clazz
	 * @return save dirrectory
	 */
    private String getCaptureSaveDir(Class<?> clazz) {
        Capture capture = clazz.getAnnotation(Capture.class);
        return (capture != null) ? capture.path() : null;
    }

    /**
	 * FlashObject for selenium test
	 * 
	 * @author yone098
	 */
    protected class FlashObject {

        private Selenium selenium;

        private String id;

        /**
		 * constructor
		 * 
		 * @param selenium
		 *            {@link Selenium} object
		 * @param id
		 *            flash object id in HTML
		 */
        public FlashObject(Selenium selenium, String id) {
            this.selenium = selenium;
            this.id = id;
        }

        /**
		 * call flash function
		 * 
		 * @param functionName
		 *            function name
		 * @param args
		 *            function args
		 * @return
		 */
        public String call(String functionName, String... args) {
            return selenium.getEval(this.jsFunction(functionName, args));
        }

        private String jsFunction(String functionName, String... args) {
            String functionArgs = "";
            if (args.length > 0) {
                for (int i = 0, len = args.length; i < len; i++) {
                    functionArgs = functionArgs + "'" + args[i] + "',";
                }
                functionArgs = functionArgs.substring(0, functionArgs.length() - 1);
            }
            return "window.document['" + id + "']." + functionName + "(" + functionArgs + ");";
        }
    }

    protected static class IntegrationTestInfo {

        public String browser;

        public String capturePath;

        public int port;

        public String url;
    }
}
