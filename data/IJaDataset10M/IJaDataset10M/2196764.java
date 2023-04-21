package com.google.hackathon.reviewgetter;

import java.io.IOException;
import java.util.HashMap;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import com.google.zxing.client.android.CaptureActivity;

public class AmazonECSTask extends AsyncTask<String, Integer, HashMap<String, String>> {

    private AmazonRequester requester;

    private CaptureActivity mActivity;

    public AmazonECSTask(CaptureActivity activity, AmazonRequester _requester) {
        mActivity = activity;
        requester = _requester;
    }

    @Override
    protected HashMap<String, String> doInBackground(String... params) {
        String isbn = params[0];
        String amazonDoc = null;
        try {
            amazonDoc = requester.searchByISBN(isbn);
        } catch (IOException ex) {
        } catch (SAXException ex) {
        } catch (ParserConfigurationException ex) {
        }
        int salesRank;
        int totalReview;
        String bookDetailURL = null;
        boolean isReviewHasDot = false;
        if ((salesRank = amazonDoc.indexOf("<SalesRank>")) != -1) {
            String salesRankStr = amazonDoc.substring(amazonDoc.indexOf("<SalesRank>") + "<SalesRank>".length(), amazonDoc.indexOf("</SalesRank>"));
            salesRank = Integer.valueOf(salesRankStr);
        }
        if ((totalReview = amazonDoc.indexOf("<TotalReviews>")) != -1) {
            String reviewStr = amazonDoc.substring(amazonDoc.indexOf("<TotalReviews>") + "<TotalReviews>".length(), amazonDoc.indexOf("</TotalReviews>"));
            if (reviewStr.length() > 1) {
                isReviewHasDot = true;
            }
            totalReview = Integer.valueOf(amazonDoc.substring(amazonDoc.indexOf("<TotalReviews>") + "<TotalReviews>".length(), amazonDoc.indexOf("</TotalReviews>")));
        }
        if (amazonDoc.indexOf("<DetailPageURL>") != -1) {
            bookDetailURL = amazonDoc.substring(amazonDoc.indexOf("<DetailPageURL>") + "<DetailPageURL>".length(), amazonDoc.indexOf("</DetailPageURL>"));
        }
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("rank", String.valueOf(salesRank));
        map.put("reviews", String.valueOf(totalReview));
        map.put("url", bookDetailURL);
        map.put("isDot", String.valueOf(isReviewHasDot));
        return map;
    }

    @Override
    protected void onPostExecute(HashMap<String, String> map) {
        mActivity.stopActivityIndicator();
        mActivity.setSalesRank(Integer.valueOf(map.get("rank")));
        mActivity.setTotalReview(Integer.valueOf(map.get("reviews")));
        mActivity.setBookDetailURL(String.valueOf(map.get("url")));
        mActivity.setReviewHasDot(Boolean.valueOf(map.get("isDot")));
        mActivity.showAmazonInfo();
    }
}
