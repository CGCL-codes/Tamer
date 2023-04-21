package org.in4ama.documentengine.evaluator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.in4ama.documentengine.exception.EvaluationException;
import org.in4ama.documentengine.generator.InforamaContext;

public class XslFoFixer {

    private List<EvaluationUnit> evaluationUnits = new ArrayList<EvaluationUnit>();

    private boolean error;

    private String errorMsg;

    /** Creates a new instance of the XslFoFixer. */
    public XslFoFixer(String text) throws EvaluationException {
        if (!error) {
            parse(text);
        }
        if (!error) {
            checkTags();
        }
    }

    /** Returns an error message, if any. */
    public String getErrorMsg() {
        return errorMsg;
    }

    /** Indicates whether an error occurred durign parsing. */
    public boolean isError() {
        return error;
    }

    /** Gets evaluation units stored in this object */
    public List<EvaluationUnit> getEvaluationUnits() {
        return evaluationUnits;
    }

    /** Checks if all tags specified in the document exist. */
    private void checkTags() {
        for (EvaluationUnit unit : evaluationUnits) {
            if (unit.isEvaluated()) continue;
            String tagName = unit.getTagName();
            int numArgs = unit.getNumArguments();
            if (!tagExists(tagName, numArgs)) {
                error = true;
                errorMsg = buildErrMsg(unit);
                return;
            }
        }
    }

    /** Indicates whether a handler for the specified tag exists. */
    private boolean tagExists(String tagName, int numArgs) {
        EvaluatorHandlersMgr evaluatorsMgr = InforamaContext.getInstance().getEvaluatorHandlersMgr();
        TagSignature tagSignature = new TagSignature(tagName, numArgs);
        CEvaluationHandler cEvaluationHandler = evaluatorsMgr.getEvaluationHandler(tagSignature);
        if (cEvaluationHandler == null) {
            tagSignature = tagSignature.varArg;
            cEvaluationHandler = evaluatorsMgr.getEvaluationHandler(tagSignature);
        }
        return cEvaluationHandler != null;
    }

    /** Adds the evaluation unit at the last position */
    public void addEvaluationUnit(EvaluationUnit unit) {
        evaluationUnits.add(unit);
    }

    /** Adds the given evaluation unit at the specified position */
    public void addEvaluationUnit(int idx, EvaluationUnit unit) {
        evaluationUnits.add(idx, unit);
    }

    /** Adds the given text at the specified position */
    public void addText(int idx, String text) {
        addEvaluationUnit(idx, new EvaluationUnitText(text));
    }

    /** Marks each evaluation unit as unevaluated */
    public void reset() {
        for (EvaluationUnit unit : evaluationUnits) unit.setEvaluated(false);
    }

    /** Merges all text pieces and evaluation units and returns the result */
    public String getAsText() {
        StringBuffer sb = new StringBuffer();
        for (EvaluationUnit unit : evaluationUnits) sb.append(unit.getAsText());
        return sb.toString();
    }

    /** Gets the number of evaluation units stored in this object */
    public int size() {
        return evaluationUnits.size();
    }

    private static final int TEXT = 1;

    private static final int TAG = 2;

    private static final int ARGUMENTS = 3;

    private int state;

    /** Indicates whether the specified character is a letter */
    private static boolean isLetter(char c) {
        return ((c >= 'A') && (c <= 'Z') || (c >= 'a') && (c <= 'z'));
    }

    /**
	 * Builds this EvaluableContent object according to the specified text.
	 * 
	 * @param text
	 *            the text to be parsed.
	 * @throws EvaluationException
	 */
    private void parse(String text) throws EvaluationException {
        state = TEXT;
        String s = "";
        int bc1 = 0;
        int bc2 = 0;
        int bc3 = 0;
        boolean isq = false, idq = false;
        EvaluationUnit evalUnit = null;
        List<String> arguments = null;
        for (int idx = 0; idx < text.length(); idx++) {
            char c = text.charAt(idx);
            switch(state) {
                case TEXT:
                    if ((c == '$') && (idx + 1 < text.length()) && isLetter(text.charAt(idx + 1))) {
                        evaluationUnits.add(new EvaluationUnitText(s));
                        s = "";
                        state = TAG;
                        evalUnit = new EvaluationUnit();
                    } else {
                        s += c;
                        if (idx == text.length() - 1) evaluationUnits.add(new EvaluationUnitText(s));
                    }
                    break;
                case TAG:
                    if ((c == '$') && (idx + 1 < text.length()) && isLetter(text.charAt(idx + 1))) {
                        evalUnit.setTagName(s);
                        evaluationUnits.add(evalUnit);
                        s = "";
                        state = TAG;
                        evalUnit = new EvaluationUnit();
                    } else if (c == '{') {
                        evalUnit.setTagName(s);
                        evaluationUnits.add(evalUnit);
                        arguments = evalUnit.getArguments();
                        state = ARGUMENTS;
                        bc1 = 1;
                        evalUnit = new EvaluationUnit();
                        evalUnit.setTagName(s);
                        s = "";
                    } else if (!isLetter(c)) {
                        evalUnit.setTagName(s);
                        evaluationUnits.add(evalUnit);
                        s = "";
                        state = TEXT;
                        evalUnit = new EvaluationUnit();
                    } else {
                        s += c;
                        if (idx == text.length() - 1) {
                            evalUnit.setTagName(s);
                            evaluationUnits.add(evalUnit);
                        }
                    }
                    break;
                case ARGUMENTS:
                    if (!isq && (c == '"')) {
                        idq ^= true;
                    } else if (!idq && (c == '\'')) {
                        isq ^= true;
                    }
                    if (isq || idq) {
                        s += c;
                        if (idx == text.length() - 1) {
                            errorMsg = "Unexpected end of tag '$" + evalUnit.getTagName() + "{";
                            for (String arg : arguments) {
                                errorMsg += arg + ", ";
                            }
                            errorMsg += s + "'";
                            error = true;
                            return;
                        }
                    } else {
                        bc1 += (c == '{' ? 1 : 0);
                        bc1 -= (c == '}' ? 1 : 0);
                        bc2 += (c == '(' ? 1 : 0);
                        bc2 -= (c == ')' ? 1 : 0);
                        bc3 += (c == '[' ? 1 : 0);
                        bc3 -= (c == ']' ? 1 : 0);
                        if ((bc1 == 1) && (bc2 == 0) && (bc3 == 0) && (c == ',')) {
                            arguments.add(s);
                            if (idx == text.length() - 1) {
                                errorMsg = "Unexpected end of tag '$" + evalUnit.getTagName() + "{";
                                for (String arg : arguments) {
                                    errorMsg += arg + ", ";
                                }
                                errorMsg += s + "'";
                                error = true;
                                return;
                            }
                            s = "";
                        } else if (bc1 == 0) {
                            arguments.add(s);
                            s = "";
                            state = TEXT;
                        } else {
                            s += c;
                            if (idx == text.length() - 1) {
                                errorMsg = "Unexpected end of tag '$" + evalUnit.getTagName() + "{";
                                for (String arg : arguments) {
                                    errorMsg += arg + ", ";
                                }
                                errorMsg += s + "'";
                                error = true;
                                return;
                            }
                        }
                    }
                    break;
            }
        }
        error = false;
    }

    /** Builds an error message in case of evaluation handler invocation error. */
    private String buildErrMsg(EvaluationUnit unit) {
        String tagName = unit.getTagName();
        StringBuffer msg1 = new StringBuffer("$" + tagName);
        List<String> args = unit.getArguments();
        Iterator<String> iter = args.iterator();
        if (iter.hasNext()) {
            msg1.append("{");
            msg1.append(iter.next());
            while (iter.hasNext()) {
                msg1.append("," + iter.next());
            }
            msg1.append("}");
        }
        StringBuffer msg2 = new StringBuffer();
        msg2.append("The tag '").append(msg1).append("' doesn't exist.");
        return msg2.toString();
    }
}
