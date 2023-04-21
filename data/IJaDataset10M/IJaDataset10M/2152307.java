package com.esl.dao.practice;

import java.util.List;
import com.esl.dao.IESLDao;
import com.esl.entity.practice.qa.IrregularVerb;

public interface IIrregularVerbDAO extends IESLDao<IrregularVerb> {

    /**
	 * Get some random verbs
	 */
    public List<IrregularVerb> getRandomVerbs(int count);
}
