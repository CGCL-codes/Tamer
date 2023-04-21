package ru.spbu.cuckoo.pagerank.personalization;

import java.util.Map;
import ru.spbu.cuckoo.index.DataBase;
import ru.spbu.cuckoo.pagerank.personalization.impl.PageRankWithPersonalizationImpl;

public class PageRankWithPersonalizationFactory {

    public PageRankWithPersonalization getPageRank(Map<String, Topic> topics, DataBase db) {
        return new PageRankWithPersonalizationImpl(topics, db);
    }
}
