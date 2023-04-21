package com.uusee.crawler.job.baike;

import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.uusee.crawler.dbwriter.BaikeDataSourceDbWriter;
import com.uusee.crawler.fetcher.FetchHTTP;
import com.uusee.crawler.framework.AbstractJob;
import com.uusee.crawler.model.CrawlStatusCodes;
import com.uusee.crawler.model.CrawlURI;
import com.uusee.crawler.pageprocessor.baike.BaikeDataSourcePageProcessor;
import com.uusee.crawler.pageprocessor.baike.xunlei.XunleiMoviePlotsPageProcessor;
import com.uusee.framework.bo.UniversalBo;
import com.uusee.shipshape.bk.Constants;
import com.uusee.shipshape.bk.model.Baike;
import com.uusee.shipshape.bk.model.BaikeDataSource;

public class VeryCDBaikeFastCrawlJob {

    private static Log log = LogFactory.getLog(XunLeiBaikeFastCrawlJob.class);

    private FetchHTTP fetch = new FetchHTTP();

    private BaikeDataSourcePageProcessor overviewPageProcessor;

    private BaikeDataSourceDbWriter dbWriter = new BaikeDataSourceDbWriter();

    private int start;

    private int end;

    public void doExecute() {
        try {
            for (int i = start; i > end; i--) {
                try {
                    crawl(createCrawlURI(i + ""));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
    }

    public CrawlURI createCrawlURI(String oriId) {
        CrawlURI crawlURI = new CrawlURI();
        String crawlUrl = "http://www.verycd.com/topics/" + oriId + "/";
        crawlURI.setSourceSite("www.verycd.com");
        crawlURI.setCrawlUrl(crawlUrl);
        return crawlURI;
    }

    public void crawl(CrawlURI crawlURI) {
        try {
            fetch.process(crawlURI);
            overviewPageProcessor.process(crawlURI);
            if (crawlURI.getCrawlStatus() == CrawlStatusCodes.PAGE_PROCESS_SUCCESS) {
                BaikeDataSource bds = (BaikeDataSource) crawlURI.getModel();
                String imdbNo = bds.getImdbNo();
                if (StringUtils.isNotEmpty(imdbNo)) {
                    imdbNo = imdbNo.substring(2);
                    imdbNo = "tt" + StringUtils.leftPad(imdbNo, 7, "0");
                    bds.setImdbNo(imdbNo);
                }
                String summary = bds.getSummary();
                if (summary != null && summary.length() > 1000) {
                    summary = summary.substring(0, 1000);
                    bds.setSummary(summary);
                }
                dbWriter.process(crawlURI);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
        }
    }

    public static void main(String[] args) {
        try {
            ApplicationContext acx = new ClassPathXmlApplicationContext("applicationContext.xml");
            UniversalBo universalBo = (UniversalBo) acx.getBean("universalBo");
            BaikeDataSourceDbWriter dbWriter = new BaikeDataSourceDbWriter();
            dbWriter.setUniversalBo(universalBo);
            InputStream in = ClassLoader.getSystemResourceAsStream("regex/www.verycd.com/overview.properties");
            Properties processPageRegex = new Properties();
            processPageRegex.load(in);
            BaikeDataSourcePageProcessor pageProcessor = new BaikeDataSourcePageProcessor(processPageRegex);
            VeryCDBaikeFastCrawlJob job = new VeryCDBaikeFastCrawlJob();
            job.dbWriter = dbWriter;
            job.overviewPageProcessor = pageProcessor;
            job.start = 2763413;
            job.end = 2700000;
            job.doExecute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
