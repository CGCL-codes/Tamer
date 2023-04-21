package com.ipolyglot.dao;

import java.util.List;
import org.springframework.dao.DataAccessException;
import com.ipolyglot.model.Lesson;
import com.ipolyglot.model.WordTranslation;

/**
 * @author mishag
 */
public class WordTranslationDAOTest extends MyLazyDAOTestCase {

    private LessonDAO ldao = null;

    private WordTranslationDAO dao = null;

    private JdbcDAO jdbcDAO = null;

    private Lesson lesson = null;

    private WordTranslation wordTranslation = null;

    protected void setUp() throws Exception {
        super.setUp();
        ldao = (LessonDAO) ctx.getBean("lessonDAO");
        dao = (WordTranslationDAO) ctx.getBean("wordTranslationDAO");
        jdbcDAO = (JdbcDAO) ctx.getBean("jdbcDAO");
        dao.setJdbcDAO(jdbcDAO);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        dao = null;
        ldao = null;
    }

    public void testGetWordTranslationInvalid() throws Exception {
        try {
            wordTranslation = dao.getWordTranslation(new Long("-1"));
            fail("lesson with id '-1' found in database, failing test...");
        } catch (DataAccessException d) {
            assertTrue(d != null);
        }
    }

    public void testGetWordTranslation() throws Exception {
        wordTranslation = dao.getWordTranslation(new Long("1"));
        assertNotNull(wordTranslation);
        assertEquals("un chat", wordTranslation.getWord());
        assertEquals("cat", wordTranslation.getTranslation());
    }

    public void testGetByLessonId() throws Exception {
        List list = dao.getByLessonId(new Long("1"));
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    public void testGetByWord() throws Exception {
        List list = dao.getByWord(new Long("1"), "un chat");
        WordTranslation wordTranslation = (WordTranslation) list.get(0);
        assertEquals("cat", wordTranslation.getTranslation());
    }

    public void testAddAndRemoveWordTranslation() throws Exception {
        lesson = ldao.getLesson(new Long("4"));
        WordTranslation wordTranslation = new WordTranslation();
        wordTranslation.setLesson(lesson);
        wordTranslation.setWord("added from testAddWordTranslation: word");
        wordTranslation.setTranslation("added from testAddWordTranslation: translation");
        dao.saveWordTranslation(wordTranslation);
        Long id = wordTranslation.getId();
        if (log.isDebugEnabled()) {
            log.debug("wordTranslation.getId()=" + id);
        }
        WordTranslation newWordTranslation = dao.getWordTranslation(id);
        assertEquals(newWordTranslation.getTranslation(), wordTranslation.getTranslation());
        dao.removeWordTranslation(id);
        try {
            wordTranslation = dao.getWordTranslation(id);
            fail("getWordTranslation didn't throw DataAccessException");
        } catch (DataAccessException d) {
            assertNotNull(d);
        }
    }

    public void testUpdateWordTranslation() throws Exception {
        wordTranslation = dao.getWordTranslation(new Long("24"));
        wordTranslation.setWord("new word");
        dao.saveWordTranslation(wordTranslation);
        assertEquals("new word", wordTranslation.getWord());
    }

    public void testGetLessonId() throws Exception {
        Long lessonId = dao.getLessonId(Long.valueOf("1"));
        assertEquals(lessonId, Long.valueOf("1"));
    }

    public void testGetRandomWordTranslationByLanguageId() {
        String languageId = "fr";
        wordTranslation = dao.getRandomWordTranslationByLanguageId(languageId);
    }
}
