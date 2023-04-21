package org.makados.web.beans;

import org.makados.web.domain.News;
import java.util.List;
import java.util.Vector;

/**
 * @author makados
 */
public class NewsWrapper {

    private List<News> newsList = new Vector<News>();

    private String sinceDate;

    private String tillDate;

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }

    public String getSinceDate() {
        return sinceDate;
    }

    public void setSinceDate(String sinceDate) {
        this.sinceDate = sinceDate;
    }

    public String getTillDate() {
        return tillDate;
    }

    public void setTillDate(String tillDate) {
        this.tillDate = tillDate;
    }
}
