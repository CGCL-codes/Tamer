package com.uusee.crawler.pageprocessor.baike.mtime;

import java.util.Date;
import net.sf.json.JSONObject;
import com.uusee.crawler.framework.Processor;
import com.uusee.crawler.model.CrawlStatusCodes;
import com.uusee.crawler.model.CrawlURI;
import com.uusee.shipshape.bk.model.Baike;
import com.uusee.shipshape.bk.model.BaikeDataSource;

public class RatingInterfacePageProcessor extends Processor {

    protected void innerProcess(CrawlURI crawlURI) {
        String crawlResult = crawlURI.getCrawlResult();
        if (!canProcess(crawlURI)) {
            return;
        }
        Object model = crawlURI.getModel();
        try {
            JSONObject jsonObject = JSONObject.fromObject(crawlResult);
            JSONObject item = jsonObject.getJSONObject("value");
            int favoritedCount = item.getInt("favoritedCount");
            float rating = Float.valueOf(item.getString("rating"));
            int ratingCount = item.getInt("ratingCount");
            int wantToSeeCount = item.getInt("wantToSeeCount");
            if (model instanceof Baike) {
                Baike baike = (Baike) model;
                baike.setRatingCount(ratingCount);
                baike.setRating(rating);
                baike.setFavoritedCount(favoritedCount);
                baike.setWantToSeeCount(wantToSeeCount);
                crawlURI.setCrawlStatus(CrawlStatusCodes.PAGE_PROCESS_SUCCESS);
            } else if (model instanceof BaikeDataSource) {
                BaikeDataSource baike = (BaikeDataSource) model;
                baike.setRatingCount(ratingCount);
                baike.setRating(rating);
                baike.setFavoritedCount(favoritedCount);
                baike.setWantToSeeCount(wantToSeeCount);
                crawlURI.setCrawlStatus(CrawlStatusCodes.PAGE_PROCESS_SUCCESS);
            } else {
                crawlURI.setCrawlStatus(CrawlStatusCodes.PAGE_PROCESS_INVALID);
                return;
            }
        } catch (Exception e) {
            crawlURI.setCrawlStatus(CrawlStatusCodes.PAGE_PROCESS_EXCEPTION);
            e.printStackTrace();
        }
    }

    private boolean canProcess(CrawlURI crawlURI) {
        int crawlStatus = crawlURI.getCrawlStatus();
        if (crawlStatus == CrawlStatusCodes.FETCH_SUCCESS) {
            return true;
        } else if (crawlStatus == CrawlStatusCodes.FETCH_SC_NOT_OK) {
            crawlURI.setCrawlStatus(CrawlStatusCodes.PAGE_PROCESS_INVALID);
            return false;
        } else {
            return false;
        }
    }
}
