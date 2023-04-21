package dlf.simple;

import java.util.EnumMap;
import java.util.Map;
import dlf.AnswerType;
import dlf.dal.IFilmQuestion;
import dlf.dal.IFilmQuestionBase;

public class FixedFilmQuestionBase implements IFilmQuestionBase {

    @Override
    public Map<AnswerType, Double> getAnswerDistribution(Long questionId, Long filmId) {
        Map<AnswerType, Double> result = new EnumMap<AnswerType, Double>(AnswerType.class);
        for (AnswerType type : AnswerType.values()) result.put(type, 0.0);
        if (questionId == filmId) result.put(AnswerType.Yes, 1.0); else result.put(AnswerType.No, 1.0);
        return result;
    }

    @Override
    public Map<AnswerType, Double> getWeight(Long questionId, Long filmId) {
        return getAnswerDistribution(questionId, filmId);
    }

    @Override
    public IFilmQuestion get(Long questionId, Long filmId) {
        Map<AnswerType, Long> answers = new EnumMap<AnswerType, Long>(AnswerType.class);
        for (AnswerType type : AnswerType.values()) answers.put(type, 0L);
        if (questionId == filmId) answers.put(AnswerType.Yes, 1L); else answers.put(AnswerType.No, 1L);
        Map<AnswerType, Long> totalAnswers = new EnumMap<AnswerType, Long>(AnswerType.class);
        for (AnswerType type : AnswerType.values()) totalAnswers.put(type, 0L);
        if (questionId == filmId) totalAnswers.put(AnswerType.Yes, 1L); else totalAnswers.put(AnswerType.No, 1L);
        return new SimpleFilmQuestion(1, filmId, questionId, answers, totalAnswers);
    }

    @Override
    public void add(IFilmQuestion filmQuestion) {
        throw new AssertionError("Not implemented");
    }

    @Override
    public void updateAnswers(IFilmQuestion filmQuestion) {
        throw new AssertionError("Not implemented");
    }
}
