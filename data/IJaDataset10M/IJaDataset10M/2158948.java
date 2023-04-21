package com.ipolyglot.service;

import org.jmock.Mock;
import org.springframework.orm.ObjectRetrievalFailureException;
import com.ipolyglot.dao.WordTranslationDAO;
import com.ipolyglot.model.WordTranslation;
import com.ipolyglot.service.impl.WordTranslationManagerImpl;

/**
 * @author mishag
 */
public class WordTranslationManagerTest extends BaseManagerTestCase {

    private final String wordTranslationId = "24";

    private WordTranslationManager wordTranslationManager = new WordTranslationManagerImpl();

    private Mock wordTranslationDAO = null;

    private WordTranslation wordTranslation = null;

    protected void setUp() throws Exception {
        super.setUp();
        wordTranslationDAO = new Mock(WordTranslationDAO.class);
        wordTranslationManager.setWordTranslationDAO((WordTranslationDAO) wordTranslationDAO.proxy());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        wordTranslationManager = null;
    }

    public void testGetWordTranslation() throws Exception {
        wordTranslationDAO.expects(once()).method("getWordTranslation").will(returnValue(new WordTranslation()));
        wordTranslation = wordTranslationManager.getWordTranslation(wordTranslationId);
        assertTrue(wordTranslation != null);
        wordTranslationDAO.verify();
    }

    public void testSaveWordTranslation() throws Exception {
        wordTranslationDAO.expects(once()).method("saveWordTranslation").with(same(wordTranslation)).isVoid();
        wordTranslationManager.saveWordTranslation(wordTranslation);
        wordTranslationDAO.verify();
    }

    public void testAddAndRemoveWordTranslation() throws Exception {
        wordTranslation = new WordTranslation();
        wordTranslation.setWord("I will be deleted");
        wordTranslation.setTranslation("Surely I am to be deleted");
        wordTranslationDAO.expects(once()).method("saveWordTranslation").with(same(wordTranslation)).isVoid();
        wordTranslationManager.saveWordTranslation(wordTranslation);
        wordTranslationDAO.verify();
        wordTranslationDAO.reset();
        wordTranslationDAO.expects(once()).method("removeWordTranslation").with(eq(new Long(wordTranslationId)));
        wordTranslationManager.removeWordTranslation(wordTranslationId);
        wordTranslationDAO.verify();
        wordTranslationDAO.reset();
        Exception ex = new ObjectRetrievalFailureException(WordTranslation.class, wordTranslation.getId());
        wordTranslationDAO.expects(once()).method("removeWordTranslation").isVoid();
        wordTranslationDAO.expects(once()).method("getWordTranslation").will(throwException(ex));
        wordTranslationManager.removeWordTranslation(wordTranslationId);
        try {
            wordTranslationManager.getWordTranslation(wordTranslationId);
            fail("WordTranslation with identifier '" + wordTranslationId + "' found in database");
        } catch (ObjectRetrievalFailureException e) {
            assertNotNull(e.getMessage());
        }
        wordTranslationDAO.verify();
    }
}
