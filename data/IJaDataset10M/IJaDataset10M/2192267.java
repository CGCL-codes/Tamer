package com.atosorigin.nl.jspring2008.buzzword.quiz.parsing;

import com.atosorigin.nl.jspring2008.buzzword.quiz.domain.Answer;
import com.atosorigin.nl.jspring2008.buzzword.quiz.domain.ApproachQuestion;
import com.atosorigin.nl.jspring2008.buzzword.quiz.domain.MultipleChoiceQuestion;
import com.atosorigin.nl.jspring2008.buzzword.quiz.domain.Participant;
import com.atosorigin.nl.jspring2008.buzzword.quiz.domain.Question;
import com.atosorigin.nl.jspring2008.buzzword.quiz.domain.QuizRound;

/**
 * @author Jeroen Benckhuijsen (jeroen.benckhuijsen@gmail.com)
 *
 */
public class QuizMessageParser extends SmsParser {

    /**
	 * 
	 */
    public QuizMessageParser() {
        super();
    }

    @Override
    public void parseSmsMessage(QuizRound round, String message, Participant participant) throws BadMessageException {
        try {
            int startIndex = message.indexOf(":");
            participant.setName(message.substring(0, startIndex));
            startIndex++;
            String answers = message.substring(startIndex).trim();
            startIndex = 0;
            for (Question question : round.getQuestions()) {
                Answer answer = new Answer();
                answer.setQuestion(question);
                if (question instanceof MultipleChoiceQuestion) {
                    answer.setAnswer(answers.substring(startIndex, startIndex + 1));
                    startIndex++;
                } else if (question instanceof ApproachQuestion) {
                    int spaceIndex = answers.indexOf(" ", startIndex);
                    if (spaceIndex == -1) {
                        answer.setAnswer(answers.substring(startIndex));
                        startIndex = answers.length();
                    } else {
                        answer.setAnswer(answers.substring(startIndex, spaceIndex));
                        startIndex = spaceIndex + 1;
                    }
                }
                participant.getAnswers().add(answer);
            }
        } catch (IndexOutOfBoundsException e) {
            throw new BadMessageException("The message '" + message + "' is not well-formed.");
        }
    }
}
