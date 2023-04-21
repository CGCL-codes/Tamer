package com.jandan.persistence.test;

import java.util.Date;
import java.util.List;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import com.jandan.persistence.iface.WordCommentDao;
import com.jandan.ui.model.WordComment;
import com.jandan.util.HealTheWordUtil;

public class WordCommentDaoTest extends AbstractDependencyInjectionSpringContextTests {

    private WordCommentDao wordCommentDao;

    private String userName = "gongyong";

    private long wordID = 1;

    @Override
    protected String[] getConfigLocations() {
        return HealTheWordUtil.generateConfiguration();
    }

    public void setWordCommentDao(WordCommentDao wordCommentDao) {
        this.wordCommentDao = wordCommentDao;
    }

    public void test() {
        WordComment wordComment = new WordComment();
        wordComment.setContent("It's so difficult to remember");
        wordComment.setUserName(userName);
        wordComment.setWordID(wordID);
        long wordCommentID = wordCommentDao.insertWordComment(wordComment);
        WordComment wc = wordCommentDao.getWordCommentByWordCommentID(wordCommentID);
        assertNotNull(wc);
        assertNull(wc.getModifiedDate());
        assertEquals(wc.getContent(), "It's so difficult to remember");
        wc.setContent("fuck,so ez");
        wc.setModifiedDate(new Date());
        wordCommentDao.updateWordComment(wc);
        WordComment wc2 = wordCommentDao.getWordCommentByWordCommentID(wordCommentID);
        assertNotNull(wc2);
        assertEquals(wc2.getContent(), "fuck,so ez");
        assertNotNull(wc.getModifiedDate());
        List<WordComment> wordCommentList = wordCommentDao.getWordCommentListByWordID(wordID);
        assertNotNull(wordCommentList);
        int n = wordCommentDao.getWordCommentCountByWordID(wordID);
        System.out.println(n);
    }
}
