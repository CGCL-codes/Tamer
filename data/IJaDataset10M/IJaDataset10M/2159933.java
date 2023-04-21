package edu.teacmore.gproection.twodemensions;

import java.util.HashMap;
import java.util.Map;
import edu.math.Fraction;
import edu.math.GPHTMLView;
import edu.math.HTMLView;
import edu.math.MathVector;
import edu.math.Matrix;
import edu.math.Polynomial;
import edu.webteach.practice.data.ICalculator;
import edu.webteach.practice.data.IDialog;
import edu.webteach.practice.data.IShow;
import edu.webteach.practice.data.ITest;
import edu.webteach.practice.data.Mistake;
import edu.webteach.practice.data.Step;
import edu.webteach.practice.util.HtmlPage;
import edu.webteach.util.HtmlForm;

public class GProectionGetBeta implements ICalculator, IShow, IDialog, ITest {

    public int calc(Map in, Map results) {
        Matrix a2 = (Matrix) in.get("a2");
        Matrix b2 = (Matrix) in.get("b2");
        MathVector dk = (MathVector) in.get("dk");
        MathVector zk = (MathVector) in.get("zk");
        Fraction lastMin = new Fraction(0);
        for (int i = 0; i < a2.getRowCount(); i++) {
            Fraction bT = new Fraction(0);
            Fraction dT = new Fraction(0);
            for (int j = 0; j < a2.getColCount(); j++) {
                dT = (Fraction) dT.add(((Fraction) ((Fraction) a2.get(i, j)).mul(dk.get(j))).doubleValue());
                bT = (Fraction) bT.add(((Fraction) ((Fraction) a2.get(i, j)).mul(zk.get(j))).doubleValue());
            }
            if (dT.doubleValue() > 0) {
                bT = (Fraction) ((Fraction) b2.get(i, 0)).sub(bT);
                Fraction res = new Fraction(0);
                if (dT.doubleValue() != 0.0) {
                    res = (Fraction) bT.div(dT);
                    if (res.doubleValue() > 0) {
                        if (i == 0) lastMin = res; else {
                            if (res.doubleValue() < lastMin.doubleValue() || lastMin.doubleValue() == 0.0) lastMin = res;
                        }
                    }
                }
            }
        }
        results.put("beta", lastMin);
        return 0;
    }

    /**
	 * 
	 * @see edu.webteach.practice.data.IDialog#dialog(edu.webteach.practice.data.Step,
	 *      edu.webteach.practice.data.Mistake)
	 */
    public String dialog(Step step, Mistake e) {
        StringBuilder out = new StringBuilder();
        out.append("<table border=0><tr>");
        out.append("<td>&#946; = </td>");
        out.append("<td>" + Util.inputBoxView(e, "beta", true) + "</td>");
        out.append("</tr></table>");
        return out.toString();
    }

    /**
	 * 
	 * @see edu.webteach.practice.data.IShow#show(edu.webteach.practice.data.Step)
	 */
    public String show(Step step) {
        StringBuilder out = new StringBuilder();
        Map in = step.getInputs();
        Matrix a2 = (Matrix) in.get("a2");
        Matrix b2 = (Matrix) in.get("b2");
        MathVector dk = (MathVector) in.get("dk");
        MathVector zk = (MathVector) in.get("zk");
        out.append("<table border=\"1\" cellpadding=\"5\" cellspacing=\"0\" bordercolorlight=\"gray\" bordercolordark=\"white\">");
        out.append("<tr><td>������� ����������</td><td>" + HTMLView.getView(zk) + "</td></tr>");
        out.append("<tr><td>������ ������</td><td>" + HTMLView.getView(dk) + "</td></tr>");
        out.append("<tr><td>������� ���������� ��������</td><td>" + GPHTMLView.getView(a2) + "</td></tr>");
        out.append("<tr><td>��������-������� ��� ���������� ��������</td><td>" + GPHTMLView.getView(b2) + "</td></tr>");
        out.append("</table>");
        return out.toString();
    }

    /**
	 * 
	 * @see edu.webteach.practice.data.IDialog#showResults(java.util.Map, int,
	 *      edu.webteach.practice.data.Step)
	 */
    public String showResults(Map results, int retcode, Step step) {
        StringBuilder out = new StringBuilder();
        Fraction beta = (Fraction) results.get("beta");
        out.append("<p>&#946; = " + HTMLView.getView(beta) + "</p>");
        return out.toString();
    }

    public Mistake check(int result, Map calculated, Map inputted) {
        Mistake err = new Mistake();
        boolean isMistake = false;
        try {
            double val1 = (Fraction.parse((String) inputted.get("beta"))).doubleValue();
            double val2 = ((Fraction) calculated.get("beta")).doubleValue();
            if (Math.abs(val1 - val2) <= .01) {
                err.add("right-beta", inputted.get("beta"));
                return Mistake.NO_MISTAKES;
            }
            err.add("beta", inputted.get("beta"));
            return err;
        } catch (Exception e) {
            e.printStackTrace();
            err.add("beta", inputted.get("beta"));
            return err;
        }
    }
}
