package com.miragedev.mononara.core.service;

import com.miragedev.mononara.core.business.Basket;
import com.miragedev.mononara.core.business.Exam;
import com.miragedev.mononara.core.dao.DictionaryEntryDao;
import com.miragedev.mononara.core.dao.KanjiDao;
import junit.framework.Assert;
import org.springframework.test.jpa.AbstractJpaTests;

/**
 * Created by IntelliJ IDEA.
 * User: Nickho
 * Date: Oct 24, 2009
 * Time: 7:01:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class MononaraServiceImplTest extends AbstractJpaTests {

    private MononaraService mononaraService;

    private DictionaryEntryDao dictionaryEntryDao;

    private KanjiDao kanjiDao;

    public void setMononaraService(MononaraService mononaraService) {
        this.mononaraService = mononaraService;
    }

    public void setDictionaryEntryDao(DictionaryEntryDao dictionaryEntryDao) {
        this.dictionaryEntryDao = dictionaryEntryDao;
    }

    public void setKanjiDao(KanjiDao kanjiDao) {
        this.kanjiDao = kanjiDao;
    }

    protected String[] getConfigLocations() {
        return new String[] { "classpath:META-INF/spring-config.xml" };
    }

    protected void onSetUpInTransaction() throws Exception {
    }

    public void testEmptyExamWorkflow() {
        Basket basket = new Basket(0);
        Exam exam = mononaraService.startExam(basket);
        Assert.assertNull(exam);
    }
}
