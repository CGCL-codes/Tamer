package org.yankun.exam4j.web.controller.answer;

import java.io.IOException;
import java.util.Date;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yankun.exam4j.core.answer.service.AnswerService;
import org.yankun.exam4j.core.question.service.QuestionService;
import org.yankun.exam4j.core.result.service.ResultService;
import org.yankun.exam4j.entity.Answer;
import org.yankun.exam4j.entity.Exam;
import org.yankun.exam4j.entity.Question;
import org.yankun.exam4j.entity.Result;
import org.yankun.exam4j.entity.User;
import org.yankun.exam4j.session.SessionManager;
import org.yankun.exam4j.session.UserSessionData;

@Controller("AnswerController")
@RequestMapping("/answer")
public class AnswerController {

    @Resource
    private QuestionService questionService;

    @Resource
    private AnswerService answerService;

    @Resource
    private ResultService resultService;

    @RequestMapping("selectChange")
    @ResponseBody
    public void changeSelect(HttpServletResponse response, HttpServletRequest request, Integer questionId, Integer[] answerArray) throws IOException {
        HttpSession session = request.getSession();
        Question question = questionService.getQuestionByQuestionId(questionId);
        Integer resultId = (Integer) session.getAttribute("resultId");
        Result result = resultService.getResultById(resultId);
        answerService.deleteAnswerByResultIdAndQuestionId(resultId, questionId);
        if (null != answerArray) {
            for (int i = 0, n = answerArray.length; i < n; i++) {
                Answer answer = new Answer();
                answer.setAnswerSelect(answerArray[i]);
                answer.setQuestion(question);
                answer.setCreateTime(new Date());
                answer.setResult(result);
                answerService.save(answer);
            }
        }
        response.getWriter().print("Sucess");
    }

    @RequestMapping("inputChange")
    @ResponseBody
    public void changeInput(HttpServletResponse response, HttpServletRequest request, Integer questionId, String[] answerArray) throws IOException {
        HttpSession session = request.getSession();
        Question question = questionService.getQuestionByQuestionId(questionId);
        Integer resultId = (Integer) session.getAttribute("resultId");
        Result result = resultService.getResultById(resultId);
        answerService.deleteAnswerByResultIdAndQuestionId(resultId, questionId);
        if (null != answerArray) {
            for (int i = 0, n = answerArray.length; i < n; i++) {
                Answer answer = new Answer();
                answer.setAnswerInput(answerArray[i]);
                answer.setQuestion(question);
                answer.setCreateTime(new Date());
                answer.setResult(result);
                answerService.save(answer);
            }
        }
        response.getWriter().print("Sucess");
    }

    @RequestMapping("doSaveResult")
    public String doAddResult(HttpServletResponse response, HttpServletRequest request, Exam exam) {
        HttpSession session = request.getSession();
        Integer resultId = (Integer) session.getAttribute("resultId");
        Result result = resultService.getResultById(resultId);
        result.setFinished(true);
        return null;
    }
}
