package edu.teacmore.gproection.twodemensions;

import java.util.Map;
import edu.math.*;
import edu.webteach.practice.data.ICalculator;
import edu.webteach.practice.data.IDialog;
import edu.webteach.practice.data.IShow;
import edu.webteach.practice.data.ITest;
import edu.webteach.practice.data.Mistake;
import edu.webteach.practice.data.Step;
import edu.webteach.practice.util.HtmlPage;

public class GProectionModFindDk implements ICalculator, IShow, IDialog, ITest {

    public int calc(Map in, Map results) {
        System.out.println("Find dk mod");
        MathVector antigradient = (MathVector) in.get("vector");
        Matrix a1 = (Matrix) in.get("a1");
        MathVector dk = new MathVector();
        for (int i = 0; i < antigradient.size(); i++) {
            cont: {
                int k2 = 1;
                if (((Fraction) antigradient.get(i)).doubleValue() < 0) k2 = -1;
                int j = 0;
                for (; j < a1.getRowCount(); j++) {
                    int k1 = 1;
                    if (((Fraction) a1.get(j, i)).doubleValue() < 0) {
                        k1 = -1;
                    }
                    if (k1 == k2 && ((Fraction) a1.get(j, i)).doubleValue() != 0) {
                        dk.append(new Fraction(0));
                        break cont;
                    }
                }
                dk.append((Fraction) antigradient.get(i));
            }
        }
        MathVector antiTemp = new MathVector();
        MathVector tempResult = new MathVector();
        for (int i = 0; i < antigradient.size(); i++) {
            double frValue = ((Fraction) antigradient.get(i)).doubleValue();
            if (frValue < 0) {
                antiTemp.append(Fraction.parse("-1"));
            } else if (frValue > 0) {
                antiTemp.append(Fraction.parse("1"));
            } else {
                antiTemp.append(antigradient.get(i));
            }
            tempResult.append(antigradient.get(i));
        }
        boolean[] vect = new boolean[dk.size()];
        for (int i = 0; i < dk.size(); i++) {
            if (((Fraction) dk.get(i)).doubleValue() != 0.0) vect[i] = true; else vect[i] = false;
        }
        for (int i = 0; i < dk.size(); i++) {
            if (((Fraction) dk.get(i)).doubleValue() == 0.0) {
                for (int j = 0; j < a1.getRowCount(); j++) {
                    if (((Fraction) a1.get(j, i)).doubleValue() != 0) {
                        int k;
                        int m = 0;
                        for (k = 0; k < a1.getColCount(); k++) {
                            if (((Fraction) a1.get(j, k)).doubleValue() != 0.0) {
                                m++;
                            }
                        }
                        if (k == a1.getColCount() && m == 1) vect[i] = true;
                    }
                }
            }
        }
        boolean cont = true;
        while (cont) {
            cont = false;
            for (int i = 0; i < dk.size(); i++) {
                if (!vect[i]) {
                    for (int j = 0; j < a1.getRowCount(); j++) {
                        if ((((Fraction) a1.get(j, i)).doubleValue() < 0 && ((Fraction) antiTemp.get(i)).doubleValue() < 0) || (((Fraction) a1.get(j, i)).doubleValue() > 0 && ((Fraction) antiTemp.get(i)).doubleValue() > 0)) {
                            Fraction fr = Fraction.parse("-1");
                            if (((Fraction) a1.get(j, i)).doubleValue() < 0) fr = Fraction.parse("1");
                            int k;
                            for (k = 0; k < a1.getColCount(); k++) {
                                if (a1.get(j, k).equals(fr) && antiTemp.get(i).equals(antiTemp.get(k))) {
                                    Fraction curFraction = (Fraction) tempResult.get(i);
                                    Fraction newFraction = min((Fraction) curFraction, (Fraction) dk.get(k));
                                    if (newFraction.doubleValue() != 0 && (Math.abs(((Fraction) dk.get(i)).doubleValue()) > Math.abs(newFraction.doubleValue()) || ((Fraction) dk.get(i)).doubleValue() == 0)) {
                                        tempResult.delete(i);
                                        tempResult.insert(newFraction, i);
                                        dk.delete(i);
                                        dk.insert(newFraction, i);
                                        cont = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        results.put("dk", dk);
        return 0;
    }

    private boolean canChange(boolean[] chV) {
        for (int i = 0; i < chV.length; i++) {
            if (!chV[i]) return true;
        }
        return false;
    }

    private Fraction min(Fraction a, Fraction b) {
        try {
            if (Math.abs(a.doubleValue()) > Math.abs(b.doubleValue())) return (Fraction) b.clone(); else return (Fraction) a.clone();
        } catch (Exception e) {
            return Fraction.parse("999999");
        }
    }

    public Mistake check(int retcode, Map calculated, Map inputted) {
        Mistake err = new Mistake();
        try {
            MathVector correct = (MathVector) calculated.get("dk");
            String value = (String) inputted.get("result");
            MathVector v = new MathVector();
            boolean flag = true;
            value = value.substring(1, value.length() - 1);
            for (int p = value.indexOf(';'); p != -1; p = value.indexOf(';')) {
                v.append(Fraction.parse(value.substring(0, p)));
                value = value.substring(p + 1);
            }
            v.append(Fraction.parse(value));
            if (calculated.containsKey("__PRECISION")) {
                double p = ((Fraction) calculated.get("__PRECISION")).doubleValue();
                if (correct.size() != v.size()) {
                    flag = false;
                    err.add("result", inputted.get("result"));
                    return err;
                } else for (int i = 0; i < v.size(); i++) if (Math.abs(((Fraction) correct.get(i)).doubleValue() - ((Fraction) v.get(i)).doubleValue()) > p) {
                    flag = false;
                    err.add("result", inputted.get("result"));
                    return err;
                }
            } else {
                for (int i = 0; i < correct.size(); i++) {
                    if (!correct.get(i).equals(v.get(i))) {
                        flag = false;
                        err.add("result", inputted.get("result"));
                        return err;
                    }
                }
            }
            if (flag) return Mistake.NO_MISTAKES;
            err.add("right-result", inputted.get("result"));
            return err;
        } catch (Exception e) {
            e.printStackTrace();
            err.add("result", inputted.get("result"));
            return err;
        }
    }

    public String dialog(Step step, Mistake e) {
        StringBuilder out = new StringBuilder();
        MathVector vector = (MathVector) step.getInputs().get("vector");
        Matrix a1 = (Matrix) step.getInputs().get("a1");
        out.append("<p><table>");
        out.append("<tr><td>&nbsp;d<sup>k</sup> =&nbsp;</td><td>");
        out.append(Util.inputBoxView(e, "result", true));
        out.append("</td></tr></table></p>");
        return out.toString();
    }

    public String show(Step step) {
        StringBuilder out = new StringBuilder();
        Map in = step.getInputs();
        MathVector antigradient = (MathVector) in.get("vector");
        Matrix a1 = (Matrix) in.get("a1");
        out.append("<table border=\"1\" cellpadding=\"5\" cellspacing=\"0\" bordercolorlight=\"gray\" bordercolordark=\"white\">");
        out.append("<tr><td>�������䳺��</td><td>" + HTMLView.getView(antigradient) + "</td></tr>");
        out.append("<tr><td>������� �������� ��������</td><td>" + GPHTMLView.getView(a1) + "</td></tr>");
        out.append("</table>");
        return out.toString();
    }

    public String showResults(Map results, int retcode, Step step) {
        StringBuilder out = new StringBuilder();
        MathVector dk = (MathVector) results.get("dk");
        out.append("d<sup>k</sup> =&nbsp;" + HTMLView.getView(dk));
        return out.toString();
    }
}
