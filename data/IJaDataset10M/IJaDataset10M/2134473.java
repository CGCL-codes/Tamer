package edu.webteach.test.check;

import java.util.*;
import javax.servlet.*;
import edu.webteach.data.*;

class RequestUtil {

    public static String extractOneParameter(ServletRequest request, Question question) {
        return request.getParameter("__QST:" + new Integer(question.getId()).toString());
    }

    public static String[] extractManyParameters(ServletRequest request, Question question) {
        return request.getParameterValues("__QST:" + new Integer(question.getId()).toString());
    }

    public static Map<Integer, Integer> extractPairs(ServletRequest request, Question question) {
        Map<Integer, Integer> pairs = new HashMap<Integer, Integer>();
        for (Object o : question.getAnswers()) {
            QuestionAnswer qa = (QuestionAnswer) o;
            if (qa.getValid() > 0) {
                String userId = request.getParameter("__QST:" + question.getId() + "_" + qa.getAnswer().getId());
                pairs.put(qa.getAnswer().getId(), new Integer(userId));
            }
        }
        return pairs;
    }

    public static List<Integer> extractSequence(ServletRequest request, Question question) {
        List<Integer> sequence = new ArrayList<Integer>(4);
        int number = 1;
        while (true) {
            String id = request.getParameter("__QST:" + question.getId() + "_" + number);
            if (id == null) break;
            sequence.add(new Integer(id));
            number++;
        }
        return sequence;
    }
}
