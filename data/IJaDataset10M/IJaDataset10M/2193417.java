package br.mps.eti.siljac.plugins;

import java.io.FileInputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import org.apache.log4j.Logger;
import HTTPClient.HTTPConnection;
import HTTPClient.HTTPResponse;
import br.mps.eti.siljac.dataprovider.DataProvider;
import br.mps.eti.siljac.util.PropertyManager;
import de.nava.informa.core.ChannelIF;
import de.nava.informa.core.ItemIF;
import de.nava.informa.impl.basic.ChannelBuilder;
import de.nava.informa.parsers.FeedParser;

/**
 * Siljac plugin to read RSS feeds.
 * 
 * @author <a href="mailto:mpserafim@yahoo.com.br">Marcos Paulo Serafim</a>
 * @since 0.0.1
 */
public class Rss implements DataProvider {

    private static final Logger _log = Logger.getLogger(Rss.class);

    private static final String RSS_FILE = "rss.properties";

    private static final String RSS_KEY = "rss.";

    private static final String REFRESH = "refresh";

    private List urls;

    private int atual = 0;

    private String texts[];

    private Calendar refresh;

    private int interval;

    public void initialize() {
        if (_log.isDebugEnabled()) _log.debug(PropertyManager.getInstance().getProperty("siljac.plugin.initialization"));
        Properties p = new Properties();
        try {
            p.load(new FileInputStream(RSS_FILE));
            urls = new ArrayList();
            for (int ind = 0; p.getProperty(RSS_KEY + ind) != null; ind++) {
                urls.add(p.getProperty(RSS_KEY + ind));
            }
            texts = new String[urls.size()];
            refresh = Calendar.getInstance();
            interval = Integer.parseInt(p.getProperty(REFRESH));
            refreshRss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getData() {
        if (_log.isDebugEnabled()) _log.debug(PropertyManager.getInstance().getProperty("siljac.plugin.getting.data"));
        try {
            Calendar temp = Calendar.getInstance();
            if (temp.after(refresh)) {
                refreshRss();
            }
            String rss = texts[atual];
            Random r = new Random();
            ChannelIF channel = FeedParser.parse(new ChannelBuilder(), new StringReader(rss));
            Object os[] = channel.getItems().toArray();
            int posicaoRss = r.nextInt(os.length);
            ItemIF item = (ItemIF) os[posicaoRss];
            atual++;
            if (atual >= urls.size()) atual = 0;
            return new StringBuffer("\"*").append(item.getTitle().replaceAll("\"", "")).append("\"").toString();
        } catch (Exception e) {
            _log.error(PropertyManager.getInstance().getProperty("siljac.plugin.error"), e);
            e.printStackTrace();
        }
        return PropertyManager.getInstance().getProperty("siljac.plugin.error");
    }

    private String getRss(String url) throws Exception {
        String rss;
        if (_log.isDebugEnabled()) _log.debug("URL: " + url);
        URL u = new URL(url);
        HTTPConnection hc = new HTTPConnection(u);
        HTTPResponse hr = hc.Get(u.getFile());
        rss = hr.getText();
        return rss;
    }

    private void refreshRss() throws Exception {
        if (_log.isDebugEnabled()) _log.debug("Refreshing rss");
        for (int ind = 0; ind < texts.length; ind++) {
            texts[ind] = getRss((String) urls.get(ind));
        }
        refresh = Calendar.getInstance();
        refresh.add(Calendar.MINUTE, interval);
    }
}
