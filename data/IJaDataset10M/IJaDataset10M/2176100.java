package aquest.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.Vector;
import aquest.model.Question;
import aquest.model.QuestionBundle;

/**
 * @author aurelio
 */
public final class Controller {

    private static Vector<QuestionBundle> questions = new Vector<QuestionBundle>();

    private static QuestionBundle bundle = null;

    private static Random random = new Random();

    public static void loadQuestionBundle(String url) throws Exception {
        try {
            URL base = new URL(url);
            QuestionBundle bun = new QuestionBundle(base);
            questions.add(bun);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static Question getRandomQuestion() {
        if (questions.size() != 0) {
            double sum = 0.0;
            for (QuestionBundle q : questions) {
                if (q.getFactor() == Double.POSITIVE_INFINITY) {
                    bundle = q;
                    return q.getRandomQuestion();
                }
            }
            for (int i = 0; i < questions.size(); ++i) {
                sum += questions.elementAt(i).getFactor();
            }
            double rnd = random.nextDouble() * sum;
            double upToSum = 0.0;
            for (int i = 0; i < questions.size(); ++i) {
                QuestionBundle q = questions.elementAt(i);
                if (rnd >= upToSum && rnd < upToSum + q.getFactor()) {
                    bundle = q;
                    break;
                }
                upToSum += q.getFactor();
            }
            if (bundle == null) {
                bundle = questions.lastElement();
            }
            return bundle.getRandomQuestion();
        } else {
            bundle = null;
            return null;
        }
    }

    public static String getProgramName() {
        return Messages.getString("Controller.program.name");
    }

    public static void reloadAllBundles() throws IOException {
        for (QuestionBundle bundle : questions) {
            bundle.reload();
        }
    }

    public static QuestionBundle getCurrentBundle() {
        return bundle;
    }

    public static Vector<QuestionBundle> getQuestions() {
        return questions;
    }

    public static double getBundleProbability(QuestionBundle q) {
        if (q.getFactor() == Double.MAX_VALUE) {
            return Double.MAX_VALUE;
        } else {
            double sum = 0.0;
            for (QuestionBundle qb : questions) {
                sum += qb.getFactor();
            }
            return q.getFactor() / ((sum / questions.size()));
        }
    }

    public static double getQuestionProbability(Question q) {
        double sum = 0.0;
        for (Question q1 : q.getParent().questions) {
            sum += q1.getFactor();
        }
        return q.getFactor() / ((sum / q.getParent().questions.size()));
    }
}
