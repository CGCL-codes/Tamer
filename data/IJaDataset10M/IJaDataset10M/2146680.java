package superabbrevs.template.fields;

import org.gjt.sp.jedit.bsh.*;
import superabbrevs.template.TemplateInterpreter;
import superabbrevs.template.fields.visitor.TemplateFieldVisitor;
import superabbrevs.utilities.Log;

/**
 * @author Sune Simonsen
 * class TransformationField
 * a template field that can transform the field value with abitrarily code
 */
public class TransformationField implements Field {

    private VariableField field;

    private String code;

    private TemplateInterpreter interpreter;

    private String lastEvaluated;

    private String lastResult;

    public TransformationField(VariableField field, String code, TemplateInterpreter interpreter) {
        this.field = field;
        this.code = code;
        this.interpreter = interpreter;
    }

    @Override
    public String toString() {
        String s = field.toString();
        try {
            if (!s.equals(lastEvaluated)) {
                lastResult = interpreter.evaluateCodeOnSelection(code, s);
                lastEvaluated = s;
            }
        } catch (TargetError e) {
            Log.log(Log.Level.ERROR, TransformationField.class, e);
            lastResult = "<target error>";
            lastEvaluated = "<target error>";
        } catch (ParseException e) {
            Log.log(Log.Level.ERROR, TransformationField.class, e);
            lastResult = "<pasing error>";
            lastEvaluated = "<pasing error>";
        } catch (EvalError e) {
            Log.log(Log.Level.ERROR, TransformationField.class, e);
            lastResult = "<eval error>";
            lastEvaluated = "<eval error>";
        }
        return lastResult;
    }

    public int getLength() {
        String s = field.toString();
        if (!s.equals(lastEvaluated)) {
            toString();
        }
        return lastResult.length();
    }

    public String firstUp(String s) {
        StringBuffer res = new StringBuffer(s);
        if (0 < res.length()) {
            char first = res.charAt(0);
            res.setCharAt(0, Character.toUpperCase(first));
        }
        return res.toString();
    }

    public void accept(TemplateFieldVisitor visitor) {
        visitor.visit(this);
    }
}
