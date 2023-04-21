package freemarker.core;

import java.util.Date;
import freemarker.template.*;

/**
 * @version 1.0
 * @author Attila Szegedi
 */
class EvaluationUtil {

    private EvaluationUtil() {
    }

    static String getString(TemplateScalarModel model, Expression expr, Environment env) throws TemplateException {
        String value = model.getAsString();
        if (value == null) {
            if (env.isClassicCompatible()) {
                return "";
            } else {
                throw new TemplateException(expr + " evaluated to null string.", env);
            }
        }
        return value;
    }

    static Number getNumber(Expression expr, Environment env) throws TemplateException {
        TemplateModel model = expr.getAsTemplateModel(env);
        return getNumber(model, expr, env);
    }

    static Number getNumber(TemplateModel model, Expression expr, Environment env) throws TemplateException {
        if (model instanceof TemplateNumberModel) {
            return getNumber((TemplateNumberModel) model, expr, env);
        } else if (model == null) {
            throw new InvalidReferenceException(expr + " is undefined.", env);
        } else {
            throw new NonNumericalException(expr + " is not a number, it is " + model.getClass().getName(), env);
        }
    }

    static Number getNumber(TemplateNumberModel model, Expression expr, Environment env) throws TemplateModelException, TemplateException {
        Number value = model.getAsNumber();
        if (value == null) {
            throw new TemplateException(expr + " evaluated to null number.", env);
        }
        return value;
    }

    static Date getDate(TemplateDateModel model, Expression expr, Environment env) throws TemplateModelException, TemplateException {
        Date value = model.getAsDate();
        if (value == null) {
            throw new TemplateException(expr + " evaluated to null date.", env);
        }
        return value;
    }
}
