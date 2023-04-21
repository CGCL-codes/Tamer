package edu.teacmore.gproection.multidemensions;

import java.util.Map;
import java.util.Vector;
import edu.math.Condition;
import edu.math.GPHTMLView;
import edu.math.HTMLView;
import edu.math.Polynomial;
import edu.math.PolynomialItem;
import edu.webteach.practice.data.ICalculator;
import edu.webteach.practice.data.IDialog;
import edu.webteach.practice.data.IShow;
import edu.webteach.practice.data.ITest;
import edu.webteach.practice.data.Mistake;
import edu.webteach.practice.data.Step;
import edu.webteach.practice.util.HtmlPage;
import edu.teacmore.gproection.twodemensions.*;
import java.util.*;
import edu.math.Fraction;
import edu.math.MathVector;

public class MultiBuildConditions implements ICalculator, IShow, IDialog, ITest {

    public int calc(Map in, Map results) {
        MathVector zk = (MathVector) in.get("zk");
        MathVector a = (MathVector) in.get("a");
        MathVector lengths = (MathVector) in.get("length");
        int n = (int) ((Fraction) in.get("n")).doubleValue();
        ForMulti fm = new ForMulti();
        int zkSize = zk.size();
        for (int i = 0; i + n < zkSize; i++) {
            double l1 = ((Fraction) lengths.get(i)).doubleValue();
            double e1 = ((Fraction) zk.get(i)).doubleValue();
            for (int j = i + n; j < zkSize; j += n) {
                double l2 = ((Fraction) lengths.get(j)).doubleValue();
                double e2 = ((Fraction) zk.get(j)).doubleValue();
                double subLength = (l1 + l2) / 2;
                double subCentr = Math.abs(e1 - e2);
                if (subCentr >= subLength) {
                    Condition cond = new Condition();
                    cond.setSign(Condition.SIGN_NOTLESS);
                    if (e1 > e2) cond.setLeftPart(Polynomial.parse("x" + (i + 1) + "-x" + (j + 1))); else cond.setLeftPart(Polynomial.parse("x" + (j + 1) + "-x" + (i + 1)));
                    cond.setRightPart(Polynomial.parse(Double.toString(subLength)));
                    fm.addCondition(i + 1, j + 1, cond, n);
                }
            }
        }
        Vector<Condition> obl = new Vector<Condition>();
        for (int i = 0; i < zkSize; i++) {
            Condition cond = new Condition();
            cond.setSign(Condition.SIGN_NOTLESS);
            cond.setLeftPart(Polynomial.parse("x" + (i + 1)));
            cond.setRightPart(Polynomial.parse(Double.toString(((Fraction) lengths.get(i)).doubleValue() / 2)));
            obl.add(cond);
            Condition cond1 = new Condition();
            cond1.setSign(Condition.SIGN_NOTGREATER);
            cond1.setLeftPart(Polynomial.parse("x" + (i + 1)));
            cond1.setRightPart(Polynomial.parse(Double.toString(((Fraction) a.get(i % n)).doubleValue() - ((Fraction) lengths.get(i)).doubleValue() / 2)));
            obl.add(cond1);
        }
        results.put("limitations", fm);
        results.put("obllimitations", obl);
        return 0;
    }

    public Mistake check(int retcode, Map calculated, Map inputted) {
        Mistake mistake = new Mistake();
        boolean isMistake = false;
        ForMulti fm = (ForMulti) calculated.get("limitations");
        Vector<Condition> obl = (Vector<Condition>) calculated.get("obllimitations");
        int kt = 0;
        for (Enumeration en = fm.keys(); en.hasMoreElements(); kt++) {
            String objects = (String) en.nextElement();
            Vector<Condition> limits = fm.getConditions(objects);
            int k = 0;
            for (int i = 0; i < limits.size(); i++, k++) {
                Condition limitation = limits.get(i);
                Polynomial leftPart = limitation.getLeftPart();
                Vector variables = leftPart.getVariables();
                Vector values = leftPart.getItems();
                for (int j = 0; j < values.size(); j++) {
                    PolynomialItem pi = (PolynomialItem) values.elementAt(j);
                    String key = "unp" + kt + "_" + i + "_" + j;
                    if (!pi.getCoefficient().equals(Fraction.parse((String) inputted.get(key)))) {
                        isMistake = true;
                        mistake.add(key, inputted.get(key));
                    } else {
                        mistake.add("right-" + key, inputted.get(key));
                    }
                }
                Polynomial rightPart = limitation.getRightPart();
                values = rightPart.getItems();
                PolynomialItem pi = (PolynomialItem) values.elementAt(0);
                String key = "unpb" + kt + "_" + i;
                if (!pi.getCoefficient().equals(Fraction.parse((String) inputted.get(key)))) {
                    isMistake = true;
                    mistake.add(key, inputted.get(key));
                } else {
                    mistake.add("right-" + key, inputted.get(key));
                }
            }
        }
        for (int i = 0; i < obl.size(); i++) {
            Condition limitation = obl.get(i);
            Polynomial leftPart = limitation.getLeftPart();
            Vector variables = leftPart.getVariables();
            Vector values = leftPart.getItems();
            for (int j = 0; j < values.size(); j++) {
                PolynomialItem pi = (PolynomialItem) values.elementAt(j);
                String key = "obl_" + i + "_" + j;
                if (!pi.getCoefficient().equals(Fraction.parse((String) inputted.get(key)))) {
                    isMistake = true;
                    mistake.add(key, inputted.get(key));
                } else {
                    mistake.add("right-" + key, inputted.get(key));
                }
            }
            Polynomial rightPart = limitation.getRightPart();
            values = rightPart.getItems();
            PolynomialItem pi = (PolynomialItem) values.elementAt(0);
            String key = "oblb_" + i;
            if (!pi.getCoefficient().equals(Fraction.parse((String) inputted.get(key)))) {
                isMistake = true;
                mistake.add(key, inputted.get(key));
            } else {
                mistake.add("right-" + key, inputted.get(key));
            }
        }
        return mistake;
    }

    public String dialog(Step step, Mistake e) {
        StringBuilder out = new StringBuilder();
        ForMulti fm = (ForMulti) step.getOutputs().get("limitations");
        Vector<Condition> obl = (Vector<Condition>) step.getOutputs().get("obllimitations");
        int n = (int) ((Fraction) step.getInputs().get("n")).doubleValue();
        int kt = 0;
        for (Enumeration en = fm.keys(); en.hasMoreElements(); kt++) {
            String objects = (String) en.nextElement();
            Vector<Condition> limits = fm.getConditions(objects);
            out.append("<p>��'���� " + objects + ":</p>");
            out.append("<table border=0>");
            int k = 0;
            for (int i = 0; i < limits.size(); i++, k++) {
                out.append("<tr><td><b>" + ReduceGreateThan.get2num(i + 1) + ". </b></td>");
                Condition limitation = limits.get(i);
                Polynomial leftPart = limitation.getLeftPart();
                Vector variables = leftPart.getVariables();
                Vector values = leftPart.getItems();
                for (int j = 0; j < values.size(); j++) {
                    if (j != 0) out.append("<td> + </td>");
                    PolynomialItem pi = (PolynomialItem) values.elementAt(j);
                    String view = GPHTMLView.getVarView((String) variables.get(j), n);
                    String key = "unp" + kt + "_" + i + "_" + j;
                    out.append("<td>");
                    out.append(Util.inputBoxView(e, key, 1, false));
                    out.append("</td><td> � </td><td>" + view + "</td>");
                }
                out.append("<td> &ge; </td>");
                Polynomial rightPart = limitation.getRightPart();
                values = rightPart.getItems();
                PolynomialItem pi = (PolynomialItem) values.elementAt(0);
                String key = "unpb" + kt + "_" + i;
                out.append("<td>" + Util.inputBoxView(e, key, 1, false) + "</td></tr>");
            }
            out.append("</table>");
        }
        out.append("<p>����� ��������� ������:</p>");
        out.append("<table border=0>");
        int k = 0;
        for (int i = 0; i < obl.size(); i++, k++) {
            out.append("<tr><td><b>" + ReduceGreateThan.get2num(i + 1) + ". </b></td>");
            Condition limitation = obl.get(i);
            Polynomial leftPart = limitation.getLeftPart();
            Vector variables = leftPart.getVariables();
            Vector values = leftPart.getItems();
            for (int j = 0; j < values.size(); j++) {
                if (j != 0) out.append("<td> + </td>");
                PolynomialItem pi = (PolynomialItem) values.elementAt(j);
                String view = GPHTMLView.getVarView((String) variables.get(j), n);
                String key = "obl_" + i + "_" + j;
                out.append("<td>");
                out.append(Util.inputBoxView(e, key, 1, false));
                out.append("</td><td> � </td><td>" + view + "</td>");
            }
            if (limitation.getSign() == Condition.SIGN_NOTGREATER) out.append("<td> &le; </td>"); else out.append("<td> &ge; </td>");
            Polynomial rightPart = limitation.getRightPart();
            values = rightPart.getItems();
            PolynomialItem pi = (PolynomialItem) values.elementAt(0);
            String key = "oblb_" + i;
            out.append("<td>" + Util.inputBoxView(e, key, 1, false) + "</td></tr>");
        }
        out.append("</table>");
        return out.toString();
    }

    public String show(Step step) {
        StringBuilder out = new StringBuilder();
        Map inputs = step.getInputs();
        MathVector zk = (MathVector) inputs.get("zk");
        MathVector a = (MathVector) inputs.get("a");
        MathVector length = (MathVector) inputs.get("length");
        int n = (int) ((Fraction) inputs.get("n")).doubleValue();
        out.append("<table border=\"1\" cellpadding=\"5\" cellspacing=\"0\" bordercolorlight=\"gray\" bordercolordark=\"white\">");
        out.append("<tr><td>������� ����������</td><td>" + HTMLView.getView(zk) + "</td></tr>");
        out.append("<tr><td>������� ����� ������������</td><td>" + HTMLView.getView(length) + "</td></tr>");
        out.append("<tr><td>������� ���������</td><td>" + HTMLView.getView(a) + "</td></tr>");
        out.append("<tr><td>�������� ��������</td><td>" + n + "</td></tr>");
        out.append("</table>");
        return out.toString();
    }

    public String showResults(Map results, int retcode, Step step) {
        StringBuilder out = new StringBuilder();
        ForMulti fm = (ForMulti) results.get("limitations");
        Vector<Condition> obl = (Vector<Condition>) results.get("obllimitations");
        int n = (int) ((Fraction) step.getInputs().get("n")).doubleValue();
        out.append(GPHTMLView.getView(fm, n));
        out.append("<p>����� ��������� ������:</p>");
        out.append(GPHTMLView.getConditionsView(obl, n));
        return out.toString();
    }
}
