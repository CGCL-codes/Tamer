package slevnik.Daemon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class contains attributes, which represents XPaths. XPaths are used for parsing data to Item object.
 * Attribute urls is List of URLs, which contains XML feeds.
 * Other attributes of this class are used for parsing data from XML feeds
 * stored on URLs from attribute urls.
 * 
 * @author Michal Halaj
 */
public class XMLFeedProperties {

    private List<String> urls;

    private String dealXPath;

    private String titleXPath;

    private String urlXPath;

    private String cityXPath;

    private String finalPriceXPath;

    private String originalPriceXPath;

    private String dealStartXPath;

    private String dealEndXPath;

    private String discountXPath;

    private String customersXPath;

    private String categoryXPath;

    private String dateFormat;

    public String getCategoryXPath() {
        return categoryXPath;
    }

    public void setCategoryXPath(String categoryXPath) {
        this.categoryXPath = categoryXPath;
    }

    public XMLFeedProperties() {
        urls = new ArrayList<String>();
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(Collection<String> urls) {
        this.urls.clear();
        this.urls.addAll(urls);
    }

    public void addUrl(String url) {
        this.urls.add(url);
    }

    public String getCityXPath() {
        return cityXPath;
    }

    public void setCityXPath(String cityXPath) {
        this.cityXPath = cityXPath;
    }

    public String getCustomersXPath() {
        return customersXPath;
    }

    public void setCustomersXPath(String customersXPath) {
        this.customersXPath = customersXPath;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getDealEndXPath() {
        return dealEndXPath;
    }

    public void setDealEndXPath(String dealEndXPath) {
        this.dealEndXPath = dealEndXPath;
    }

    public String getDealXPath() {
        return dealXPath;
    }

    public void setDealXPath(String dealXPath) {
        this.dealXPath = dealXPath;
    }

    public String getDealStartXPath() {
        return dealStartXPath;
    }

    public void setDealStartXPath(String dealStartXPath) {
        this.dealStartXPath = dealStartXPath;
    }

    public String getDiscountXPath() {
        return discountXPath;
    }

    public void setDiscountXPath(String discountXPath) {
        this.discountXPath = discountXPath;
    }

    public String getFinalPriceXPath() {
        return finalPriceXPath;
    }

    public void setFinalPriceXPath(String finalPriceXPath) {
        this.finalPriceXPath = finalPriceXPath;
    }

    public String getOriginalPriceXPath() {
        return originalPriceXPath;
    }

    public void setOriginalPriceXPath(String originalPriceXPath) {
        this.originalPriceXPath = originalPriceXPath;
    }

    public String getTitleXPath() {
        return titleXPath;
    }

    public void setTitleXPath(String titleXPath) {
        this.titleXPath = titleXPath;
    }

    public String getUrlXPath() {
        return urlXPath;
    }

    public void setUrlXPath(String urlXPath) {
        this.urlXPath = urlXPath;
    }
}
