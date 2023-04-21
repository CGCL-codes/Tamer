package org.nomicron.suber.model.factory;

import org.nomicron.suber.model.dao.KeywordDao;
import org.nomicron.suber.model.object.Keyword;
import java.util.Collections;
import java.util.List;

/**
 * Factory for Keyword objects.
 */
public class KeywordFactory {

    private KeywordDao keywordDao;

    /**
     * Set the Dao through the spring application context.
     *
     * @param keywordDao Dao
     */
    public void setKeywordDao(KeywordDao keywordDao) {
        this.keywordDao = keywordDao;
    }

    /**
     * Get the Keyword with the specified id.
     *
     * @param id id
     * @return Keyword
     */
    public Keyword getKeywordById(Integer id) {
        return keywordDao.findById(id);
    }

    /**
     * Get the list of all Keywords.
     *
     * @return list of Keywords
     */
    public List<Keyword> getKeywordList() {
        return keywordDao.findAll();
    }

    /**
     * Get the sorted list of keywords.
     *
     * @return list of keywords
     */
    public List<Keyword> getSortedKeywordList() {
        List<Keyword> keywordList = getKeywordList();
        Collections.sort(keywordList, new Keyword.NameComparator());
        return keywordList;
    }
}
