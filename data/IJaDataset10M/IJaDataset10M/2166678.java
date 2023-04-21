package se.inera.ifv.casebox.core.service.impl;

import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.inera.ifv.casebox.core.entity.Answer;
import se.inera.ifv.casebox.core.entity.MessageType;
import se.inera.ifv.casebox.core.repository.AnswerRepository;
import se.inera.ifv.casebox.core.service.AnswerService;
import se.inera.ifv.casebox.core.service.AnswersValue;
import se.inera.ifv.casebox.core.service.StatisticService;

@Service
@Transactional(rollbackFor = IllegalStateException.class)
public class AnswerServiceImpl implements AnswerService {

    @Value("${max.fetch.results}")
    int maxResults;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private StatisticService statisticService;

    public List<Answer> getAllAnswersForCareUnit(String careUnit) {
        return answerRepository.findForCareUnit(careUnit, maxResults);
    }

    public AnswersValue getAnswersForCareUnit(String careUnit) {
        List<Answer> answers = answerRepository.findForCareUnit(careUnit, maxResults);
        Long totalNumOfAnswers = answerRepository.getNumOfQuestionsForCareUnit(careUnit);
        int questionsLeft = (int) Math.max(0, totalNumOfAnswers - answers.size());
        for (Answer a : answers) {
            a.setStatusRetrieved();
        }
        return new AnswersValue(answers, questionsLeft);
    }

    public Long saveAnswer(Answer answer) {
        Answer result = answerRepository.persist(answer);
        return result.getId();
    }

    public void deleteAnswersForCareUnit(String careUnit, Set<Long> ids) {
        int deletedAnswers = answerRepository.delete(careUnit, ids);
        if (deletedAnswers != ids.size()) {
            throw new IllegalStateException("Cannot delete answers. Illegal ids or state");
        }
        statisticService.storeStatistics(careUnit, deletedAnswers, MessageType.Answer);
    }
}
