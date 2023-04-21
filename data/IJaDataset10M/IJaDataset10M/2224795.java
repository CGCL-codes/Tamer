package com.maicuole.story.search;

import java.util.List;
import com.maicuole.story.search.index.IndexClient;
import com.maicuole.story.search.index.LuceneIndexer;

public class RAMSearchEngineClient implements SearchEngineClient, IndexClient {

    static LuceneIndexer indexer = null;

    static {
        indexer = LuceneIndexer.createIndexer("story@" + System.currentTimeMillis());
    }

    public List<Story> search(SearchQuery query) throws SearchException {
        if (query.getType() == QueryType.Tag) {
            return indexer.searchByTag(query.getQuery(), query.getStartAt(), query.getReturnCount());
        } else if (query.getType() == QueryType.Story) return indexer.searchByStory(query.getQuery(), query.getStartAt(), query.getReturnCount());
        throw new SearchException("Not support search type right!");
    }

    public void index(Story story) throws SearchException {
        indexer.index(story);
    }

    public void tag(String storyid, String tag) throws SearchException {
        Story s = searchStoryById(storyid);
        if (s == null) throw new SearchException("No story id = " + storyid);
        s.addTag(tag);
        indexer.update(s);
    }

    public Story searchStoryById(String id) throws SearchException {
        return indexer.searchById(id);
    }

    public List<Story> searchByTag(String tagName) throws SearchException {
        return indexer.searchByTag(tagName);
    }

    public List<Story> searchByShop(String name) throws SearchException {
        return indexer.searchByShop(name);
    }

    public List<Story> searchByProd(String name) throws SearchException {
        return indexer.searchByProd(name);
    }
}
