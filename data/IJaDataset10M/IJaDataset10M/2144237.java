package lv.ante.xwiki.testdigester.http;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class HttpDownloaderImplTest {

    private HttpDownloader httpDownloader;

    @Before
    public void setUp() {
        httpDownloader = new HttpDownloaderImpl();
        ProxySettings settings = new ProxySettings();
        settings.setEnabled(false);
        ((HttpDownloaderImpl) httpDownloader).setProxySettings(settings);
    }

    @Test
    public void testGet() {
        String result = httpDownloader.downloadPage("http://del.icio.us/rss/kalvis/xwiki-test-xml");
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }
}
