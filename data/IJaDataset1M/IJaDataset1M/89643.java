package examples.service;

import examples.common.News;

public interface NewsService {

    News getNews(String key);

    void setNews(String key, News news);
}
