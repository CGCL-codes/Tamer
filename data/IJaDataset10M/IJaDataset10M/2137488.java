package net.narusas.daumaccess;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;

public class RosinstrumentFetcher extends PageFetcher {

    static Pattern p = Pattern.compile("fp.pl\\?hosts=([^&]+)&amp;ports=(\\d+)");

    public RosinstrumentFetcher() throws HttpException, IOException {
        super();
    }

    public List<Proxy> fetch(int page) throws IOException {
        String html = fetch("http://tools.rosinstrument.com/cgi-bin/sps.pl?bp=&pattern=&max=100&nskip=" + (page * 50) + "&file=proxlog.csv&refresh=72", new NameValuePair[] { new NameValuePair("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; ko; rv:1.8.1.3) Gecko/20070309 Firefox/2.0.0.3"), new NameValuePair("Host", "tools.rosinstrument.com") });
        LinkedList<Proxy> res = new LinkedList<Proxy>();
        Matcher m = p.matcher(html);
        while (m.find()) {
            res.add(new Proxy(m.group(1), Integer.parseInt(m.group(2))));
        }
        return res;
    }

    @Override
    public void prepare() throws HttpException, IOException {
        fetch("http://tools.rosinstrument.com/cgi-bin/sps.pl?bp=&pattern=&max=100&nskip=0&file=proxlog.csv&refresh=72", new NameValuePair[] { new NameValuePair("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; ko; rv:1.8.1.3) Gecko/20070309 Firefox/2.0.0.3"), new NameValuePair("Host", "tools.rosinstrument.com") });
    }
}
