package websiteschema.crawler.fb;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import websiteschema.fb.core.app.Application;
import websiteschema.fb.core.RuntimeContext;

/**
 *
 * @author ray
 */
public class StandardCrawlerTest {

    @Test
    public void testCrawler() throws InterruptedException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("URL", "http://money.163.com/special/00252G50/macroNew.html");
        map.put("CRAWLER", "websiteschema.crawler.SimpleHttpCrawler");
        map.put("SITEID", "www_163_com_1");
        map.put("JOBNAME", "money_163_com_1");
        map.put("STARTURLID", "1");
        map.put("JOBID", "2");
        map.put("WRAPPERID", "5");
        map.put("XPATH", "html/body/div[@class='area clearfix']/div[@class='colLM']/ul[@class='newsList dotted']/li/span[@class='article']/a");
        Application app = new Application();
        RuntimeContext context = app.getContext();
        context.loadConfigure("fb/crawler2.app", map);
        Thread t = new Thread(app);
        t.start();
        t.join();
        System.out.println("-------------\nStatus is: " + app.getStatus().getStatus() + " " + app.getStatus().getMessage());
    }

    @Test
    public void testCrawlerContent() throws InterruptedException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("URL", "http://money.163.com/12/0305/14/7RRBTAA300253B0H.html");
        map.put("CRAWLER", "websiteschema.crawler.SimpleHttpCrawler");
        map.put("SITEID", "www_163_com_1");
        map.put("JOBNAME", "money_163_com_1");
        map.put("STARTURLID", "1");
        map.put("JOBID", "2");
        map.put("WRAPPERID", "5");
        Application app = new Application();
        RuntimeContext context = app.getContext();
        context.loadConfigure("fb/crawler2.app", map);
        Thread t = new Thread(app);
        t.start();
        t.join();
        System.out.println("-------------\nStatus is: " + app.getStatus().getStatus() + " " + app.getStatus().getMessage());
    }

    public void testXinhua() throws InterruptedException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("URL", "http://www.xinhuanet.com/house/fzh.htm");
        map.put("CRAWLER", "websiteschema.crawler.browser.BrowserWebCrawler");
        map.put("SITEID", "www_xinhuanet_com_4989");
        map.put("JOBNAME", "xinhuanet_com_34831");
        map.put("STARTURLID", "535");
        map.put("JOBID", "3");
        map.put("WRAPPERID", "5");
        map.put("XPATH", "html/body/table[@class='hei14']/tbody/tr/td/a");
        Application app = new Application();
        RuntimeContext context = app.getContext();
        context.loadConfigure("fb/crawler2.app", map);
        Thread t = new Thread(app);
        t.start();
        t.join();
        System.out.println("-------------\nStatus is: " + app.getStatus().getStatus() + " " + app.getStatus().getMessage());
    }
}
