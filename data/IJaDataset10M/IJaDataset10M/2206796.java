package at.nullpointer.trayrss.notification;

import at.nullpointer.trayrss.model.Feed;
import at.nullpointer.trayrss.model.News;

public class Notification {

    Feed feed;

    News news;

    public Feed getFeed() {
        return feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }
}
