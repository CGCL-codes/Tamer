package websiteschema.crawler.fb;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.w3c.dom.Document;
import websiteschema.crawler.Crawler;
import websiteschema.model.domain.Websiteschema;
import websiteschema.model.domain.cluster.ClusterModel;
import websiteschema.persistence.Mapper;

/**
 *
 * @author ray
 */
public class ClusterTest {

    ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-beans.xml");

    Mapper<ClusterModel> clusterModelMapper = ctx.getBean("clusterModelMapper", Mapper.class);

    Mapper<Websiteschema> websiteschemaMapper = ctx.getBean("websiteschemaMapper", Mapper.class);

    @Test
    public void clustering() {
        String siteId = "www_163_com_1";
        String url = "http://money.163.com/12/0121/13/7OA1HC8J00253B0H.html";
        Document source = null;
        Crawler crawler = new websiteschema.crawler.SimpleHttpCrawler();
        long t1 = System.currentTimeMillis();
        Document docs[] = crawler.crawl(url);
        long t2 = System.currentTimeMillis();
        source = null != docs ? docs[0] : null;
        FBClustering clusterer = new FBClustering();
        clusterer.cm = clusterModelMapper.get(siteId);
        clusterer.websiteschema = websiteschemaMapper.get(siteId);
        clusterer.doc = source;
        clusterer.clustering();
        System.out.println(clusterer.cluster);
    }
}
