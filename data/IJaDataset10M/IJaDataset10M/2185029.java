package org.fantasy.question.service.impl;

import java.util.Date;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.fantasy.cpp.core.dao.nutz.BaseDAO;
import org.fantasy.common.util.ParamMap;
import org.fantasy.question.pojo.Question;
import org.fantasy.question.service.QuestionService;

/** 
 * <p>Title: QuestionServiceImpl</p>  
 * <p>Description: 用于操作Question对象</p>  
 * <p>Copyright: Copyright (c) 2011-7-19</p>  
 * @author 沈飞 
 * @version 1.0 
 */
@Service("questionService")
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private BaseDAO baseDAO;

    @Override
    public Question getQuestion(Long qid) throws Exception {
        Question question = baseDAO.fetch(Question.class, qid);
        return question;
    }

    @Override
    public void saveQuestion(ParamMap<String, Object> params) throws Exception {
        if (!params.isValid("id")) {
            Question question = new Question();
            BeanUtils.copyProperties(question, params);
            question.setBeginTime(new Date());
            question.setState("A");
            baseDAO.insert(question);
        } else {
            Long id = params.getLong("id");
            Question question = baseDAO.fetch(Question.class, id);
            BeanUtils.copyProperties(question, params);
            baseDAO.update(question);
        }
    }

    @Override
    public void saveQuestion(Question question) throws Exception {
        if (question.getQid() != null) {
            baseDAO.update(question);
        } else {
            baseDAO.insert(question);
        }
    }

    @Override
    public void delQuestion(Question question) throws Exception {
        baseDAO.delete(question);
    }

    @Override
    public void delQuestion(Long id) throws Exception {
        baseDAO.delete(Question.class, id);
    }
}
