package com.esl.dao.dictation;

import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.esl.dao.ESLDao;
import com.esl.entity.dictation.Dictation;
import com.esl.entity.dictation.Vocab;
import com.esl.exception.IllegalParameterException;

@Transactional
@Repository("vocabDAO")
public class VocabDAO extends ESLDao<Vocab> implements IVocabDAO {

    private static Logger logger = Logger.getLogger("ESL");

    @Resource
    private IVocabHistoryDAO vocabHistoryDAO;

    public void setVocabHistoryDAO(IVocabHistoryDAO vocabHistoryDAO) {
        this.vocabHistoryDAO = vocabHistoryDAO;
    }

    @Transactional
    public void removeByDictation(Dictation dictation) {
        final String logPrefix = "removeByDictation: ";
        logger.info(logPrefix + "START");
        if (dictation == null) throw new IllegalParameterException(new String[] { "dictation" }, new Object[] { dictation });
        logger.info(logPrefix + "input dictation(" + dictation.getId() + "), with vocabs[" + dictation.getVocabsSize() + "]");
        vocabHistoryDAO.removeByVocabs(dictation.getVocabs());
        deleteAll(dictation.getVocabs());
        dictation.getVocabs().clear();
    }
}
