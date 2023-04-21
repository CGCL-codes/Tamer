package ch.ethz.dcg.spamato.filter.ruleminator.eval;

import ch.ethz.dcg.spamato.factory.common.Mail;
import ch.ethz.dcg.spamato.filter.ruleminator.Rule;

/**
 * 
 *
 * @author simon
 *
 */
public class EvaluatorFactory {

    public static Evaluator createEvaluator(int evalType, Mail mail) {
        if (evalType == Rule.EvalType.ALL) {
            return new AllEvaluator(mail);
        } else {
            return new AnyEvaluator(mail);
        }
    }
}
