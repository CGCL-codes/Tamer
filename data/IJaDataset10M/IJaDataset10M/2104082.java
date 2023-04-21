package edu.teacmore.wolfmethod;

import java.util.*;
import edu.math.*;
import edu.math.compiler.*;
import edu.webteach.practice.data.*;

/**
 * Created by IntelliJ IDEA.
 * Athour: Andrey Kusmenko AK-26
 * Date: 16/7/2006
 * Time: 18:54:13
 */
public class CreateSimplexTableWolf implements ICalculator, IDialog, IShow, ITest {

    private void CreateCondition(Vector<Condition> limitations, int size, String symbol) {
        for (int i = 0; i < size; i++) {
            Polynomial p2 = (Polynomial) new PolynomialCompiler().parse(symbol + (i + 1));
            limitations.add(new Condition(p2, new Polynomial(), Condition.SIGN_NOTLESS));
        }
    }

    public int calc(Map in, Map results) {
        Vector<Condition> polinom = (Vector<Condition>) in.get("limit_taker2");
        Polynomial polynomial = (Polynomial) in.get("func");
        Vector<Condition> limit_last = new Vector<Condition>();
        for (int i = 0, j = 0, g = 0; i < polynomial.variables.size(); i++) {
            try {
                Condition lim = (Condition) polinom.get(i).clone();
                if (((String) polynomial.variables.get(i)).charAt(0) == 'x') {
                    if (polinom.get(i).CheckSign()) {
                        lim.setLeftPart((Polynomial) lim.getLeftPart().add((Calculable) new PolynomialCompiler().parse("y" + (g++ + 1))));
                    } else {
                        lim.setLeftPart((Polynomial) lim.getLeftPart().sub(new PolynomialCompiler().parse("y" + (g++ + 1))));
                    }
                } else {
                    if (polinom.get(i).CheckSign()) {
                        lim.setLeftPart((Polynomial) lim.getLeftPart().add((Calculable) new PolynomialCompiler().parse("z" + (j++ + 1))));
                    } else {
                        lim.setLeftPart((Polynomial) lim.getLeftPart().sub(new PolynomialCompiler().parse("z" + (j++ + 1))));
                    }
                }
                limit_last.add(lim);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int j = 0, g = 0;
        for (int i = 0; i < polynomial.variables.size(); i++) {
            if (((String) polynomial.variables.get(i)).charAt(0) == 'x') j++; else g++;
        }
        CreateCondition(limit_last, j, "x");
        CreateCondition(limit_last, g, "l");
        CreateCondition(limit_last, j, "p");
        CreateCondition(limit_last, g, "q");
        CreateCondition(limit_last, j, "y");
        CreateCondition(limit_last, g, "z");
        String s_func = "0*x1";
        for (int i = 1; i < j; i++) s_func += "+0*x" + (i + 1);
        for (int i = 0; i < g; i++) s_func += "+0*l" + (i + 1);
        for (int i = 0; i < j; i++) s_func += "+0*p" + (i + 1);
        for (int i = 0; i < g; i++) s_func += "+0*q" + (i + 1);
        for (int i = 0; i < j; i++) s_func += "+y" + (i + 1);
        for (int i = 0; i < g; i++) s_func += "+0*z" + (i + 1);
        s_func += "->min";
        try {
            CriterionFunctional functional = new CriterionFunctional(s_func);
            WolfSimplex wst = new WolfSimplex(limit_last, functional);
            results.put("result", wst);
            results.put("result_lim", limit_last);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String dialog(Step step, Mistake e) {
        StringBuilder out = new StringBuilder();
        Map inputs = step.getInputs();
        Vector<Condition> limitations = (Vector<Condition>) inputs.get("limit_taker2");
        out.append("<p><table>");
        out.append("<tr><td>&nbsp;F(y)=&nbsp;</td><td>");
        if (e != null && e.wasError() && e.getMistakes() != null) {
            String value = (String) e.get("resultF");
            if (value == null) {
                value = (String) e.get("right-resultF");
                if (value != null) {
                    out.append("<input type=\"text\" name=\"resultF\" value=\"");
                    out.append(value + "\" readonly>" + HTMLView.getArrowDirView(true) + HTMLView.getAnswerView(true));
                } else {
                    out.append("<input type=\"text\" name=\"resultF\" value=\"");
                    out.append("\" class=input>" + HTMLView.getArrowDirView(true) + HTMLView.getAnswerView(false));
                }
            } else {
                out.append("<input type=\"text\" name=\"resultF\" value=\"");
                out.append(value + "\" class=input>" + HTMLView.getArrowDirView(true) + HTMLView.getAnswerView(false));
            }
        } else {
            out.append("<input type=\"text\" name=\"resultF\" value=\"");
            out.append("\" class=input>" + HTMLView.getArrowDirView(true));
        }
        out.append("</td></tr></table></p>");
        out.append("<p><table>");
        for (int i = 0; i < limitations.size(); i++) {
            out.append("<tr><td><input type=\"text\" name=\"left" + Integer.toString(i) + "\" value=\"");
            out.append((limitations.get(i).getLeftPart()).toString() + "\" readonly>");
            int var = 0;
            if (e != null && e.wasError() && e.getMistakes() != null) {
                String value = (String) e.get("result" + Integer.toString(i));
                if (value == null) {
                    value = (String) e.get("right-result" + Integer.toString(i));
                    if (value != null) {
                        out.append("</td><td><input type=\"text\" name=\"result" + Integer.toString(i) + "\" value=\"");
                        out.append(value + "\" size=3 readonly>");
                        var = 1;
                    } else {
                        out.append("</td><td><select name = \"result" + Integer.toString(i) + "\">");
                        out.append("<option value = \"\"> ");
                        for (int g = 0, y = 0, z = 0; g < limitations.size(); g++) {
                            if (g < limitations.size() / 2) {
                                out.append("<option value = \"+y" + (y + 1) + "\" >" + "+y" + (y + 1));
                                out.append("<option value = \"-y" + (y + 1) + "\" >" + "-y" + (y + 1));
                                y++;
                            } else {
                                out.append("<option value = \"+z" + (z + 1) + "\" >" + "+z" + (z + 1));
                                out.append("<option value = \"-z" + (z + 1) + "\" >" + "-z" + (z + 1));
                                z++;
                            }
                            var = 2;
                        }
                        out.append("</select> ");
                    }
                } else {
                    out.append("</td><td><select name = \"result" + Integer.toString(i) + "\">");
                    out.append("<option value = \"\"> ");
                    String val = "";
                    for (int g = 0, y = 0, z = 0; g < limitations.size(); g++) {
                        if (g < limitations.size() / 2) {
                            if (value.equals("+y" + (y + 1))) val = "selected";
                            out.append("<option " + val + " value = \"+y" + (y + 1) + "\" >" + "+y" + (y + 1));
                            val = "";
                            if (value.equals("-y" + (y + 1))) val = "selected";
                            out.append("<option " + val + " value = \"-y" + (y + 1) + "\" >" + "-y" + (y + 1));
                            val = "";
                            y++;
                        } else {
                            if (value.equals("+z" + (z + 1))) val = "selected";
                            out.append("<option " + val + " value = \"+z" + (z + 1) + "\" >" + "+z" + (z + 1));
                            val = "";
                            if (value.equals("-z" + (z + 1))) val = "selected";
                            out.append("<option " + val + " value = \"-z" + (z + 1) + "\" >" + "-z" + (z + 1));
                            val = "";
                            z++;
                        }
                        var = 2;
                    }
                    out.append("</select> ");
                }
            } else {
                out.append("</td><td><select name = \"result" + Integer.toString(i) + "\">");
                out.append("<option value = \"\"> ");
                for (int g = 0, y = 0, z = 0; g < limitations.size(); g++) {
                    if (g < limitations.size() / 2) {
                        out.append("<option value = \"+y" + (y + 1) + "\" >" + "+y" + (y + 1));
                        out.append("<option value = \"-y" + (y + 1) + "\" >" + "-y" + (y + 1));
                        y++;
                    } else {
                        out.append("<option value = \"+z" + (z + 1) + "\" >" + "+z" + (z + 1));
                        out.append("<option value = \"-z" + (z + 1) + "\" >" + "-z" + (z + 1));
                        z++;
                    }
                }
                out.append("</select> ");
            }
            out.append("</td><td> = <input type=\"text\" name=\"last" + Integer.toString(i) + "\" value=\"");
            out.append((limitations.get(i).getRightPart()).toString() + "\" size=1 readonly>");
            switch(var) {
                case 1:
                    out.append(HTMLView.getAnswerView(true));
                    break;
                case 2:
                    out.append(HTMLView.getAnswerView(false));
                    break;
            }
            out.append("</td></tr>");
        }
        out.append("</table></p>");
        return out.toString();
    }

    public String showResults(Map results, int retcode, Step step) {
        StringBuilder out = new StringBuilder();
        WolfSimplex st = (WolfSimplex) results.get("result");
        CriterionFunctional function = st.getFunction();
        Vector<Condition> limitations = (Vector<Condition>) results.get("result_lim");
        out.append("<p><table>\n");
        try {
            out.append("<tr><td>F=" + HTMLView.getView(function) + "</td></tr>");
        } catch (Exception e) {
            e.printStackTrace();
        }
        out.append("</table></p>");
        out.append("<p><table>\n");
        for (int i = 0; i < limitations.size(); i++) {
            try {
                out.append("<tr><td>" + HTMLView.getView(limitations.get(i)) + "</td></tr>");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        out.append("</table></p>");
        return out.toString();
    }

    public String show(Step step) {
        StringBuilder out = new StringBuilder();
        Map in = step.getInputs();
        Vector<Condition> limitations = (Vector<Condition>) in.get("limit_taker2");
        out.append("<p><table>\n");
        for (int i = 0; i < limitations.size(); i++) {
            try {
                out.append("<tr><td>" + HTMLView.getView(limitations.get(i)) + "</td></tr>");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        out.append("</table></p>");
        return out.toString();
    }

    public Mistake check(int retcode, Map calculated, Map inputted) {
        try {
            WolfSimplex st = (WolfSimplex) calculated.get("result");
            Polynomial correct = st.getFunction().function;
            Vector<Condition> limitations = (Vector<Condition>) calculated.get("result_lim");
            Mistake err = new Mistake();
            boolean flag = true;
            boolean f = true;
            String value = (String) inputted.get("resultF");
            Polynomial input = null;
            if (!value.equals("")) {
                try {
                    input = Polynomial.parse(value);
                } catch (Exception e) {
                    f = false;
                }
                if (f == false || !correct.equals(input)) {
                    err.add("resultF", inputted.get("resultF"));
                    flag = false;
                } else err.add("right-resultF", inputted.get("resultF"));
            } else {
                err.add("resultF", inputted.get("resultF"));
                flag = false;
            }
            for (int i = 0; i < limitations.size(); i++) {
                if (limitations.get(i).getSign() == Condition.SIGN_NOTLESS && limitations.get(i).getRightPart().equals(0) && limitations.get(i).getLeftPart().variables.size() == 1) continue;
                value = (String) inputted.get("left" + Integer.toString(i));
                value += (String) inputted.get("result" + Integer.toString(i));
                value += "=";
                value += (String) inputted.get("last" + Integer.toString(i));
                if (!limitations.get(i).toString().equals(value)) {
                    err.add("result" + Integer.toString(i), inputted.get("result" + Integer.toString(i)));
                    flag = false;
                } else err.add("right-result" + Integer.toString(i), inputted.get("result" + Integer.toString(i)));
            }
            if (flag) return Mistake.NO_MISTAKES;
            return err;
        } catch (Exception e) {
            e.printStackTrace();
            return Mistake.ANY_MISTAKE;
        }
    }
}
