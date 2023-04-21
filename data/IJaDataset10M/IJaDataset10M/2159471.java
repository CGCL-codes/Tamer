package com.newsbeef.site.test;

import com.newsbeef.dao.FeedInfoDao;
import com.newsbeef.site.SpringInitializer;

public class CreateFeedCommand {

    public static void main(String args[]) {
        FeedInfoDao feedDao = (FeedInfoDao) SpringInitializer.FACTORY.getBean("feedInfoDao");
        feedDao.addFeed("http://chasethedevil.blogspot.com/feeds/posts/default");
    }
}
