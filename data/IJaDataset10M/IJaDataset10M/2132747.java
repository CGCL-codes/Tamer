package edu.teacmore.lenddoyg;

import java.util.*;
import edu.webteach.practice.data.*;
import edu.webteach.util.*;
import edu.math.*;

/**
 * Created by IntelliJ IDEA.
 * User: SHELEST
 * Date: 12/11/2006
 * Time: 13:48:54
 * To change this template use File | Settings | File Templates.
 */
public class CheckResultIsInteger implements ICalculator, IShow, IDialog, ITest {

    public int calc(Map in, Map results) {
        SimplexTable table = (SimplexTable) in.get("ST0");
        ArrayList<String> isInteger = (ArrayList<String>) in.get("isInteger");
        boolean result = checkResultIsInteger(table, isInteger);
        if (result) {
            results.put("m", table.getFunctionValue());
            results.put("resultMap", table.startPoint);
            return 1;
        }
        results.put("m", new Fraction(1, 0));
        results.put("resultMap", new HashMap());
        return 2;
    }

    private boolean checkResultIsInteger(SimplexTable table, List<String> isInteger) {
        Map map = table.startPoint;
        for (int i = 0; i < map.size(); i++) {
            Fraction fraction = (Fraction) map.get(table.getVariables().get(i));
            if (!fraction.isInteger() && isInInteger((String) table.getVariables().get(i), isInteger)) return false;
        }
        return true;
    }

    private boolean isInInteger(String variable, List<String> isInteger) {
        for (int i = 0; i < isInteger.size(); i++) {
            if (isInteger.get(i).equals(variable)) {
                return true;
            }
        }
        return false;
    }

    public String show(Step step) {
        StringBuilder out = new StringBuilder();
        Map in = step.getInputs();
        SimplexTable table = (SimplexTable) in.get("ST0");
        ArrayList<String> isInteger = (ArrayList<String>) in.get("isInteger");
        String stringTable = "";
        try {
            stringTable = HTMLView.getView(table);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        out.append("<b>���������� �������� �������:</b><br>");
        out.append(stringTable);
        out.append("<b>���� �� �� ��������� ����� ��������������:</b><br>");
        for (int i = 0; i < isInteger.size(); i++) {
            if (i != 0) out.append(", ");
            out.append(isInteger.get(i));
        }
        out.append("<br>");
        return out.toString();
    }

    public String dialog(Step step, Mistake e) {
        StringBuilder out = new StringBuilder();
        out.append(HtmlForm.radioGroup("action", new String[] { "1", "2" }, new String[] { "������� ������ ����'�����, ������� ������� ���������.", "����'���� �� ����������� ���� ��������������, ������� ����'������� ���" }, -1));
        return out.toString();
    }

    public String showResults(Map results, int retcode, Step step) {
        StringBuilder out = new StringBuilder();
        switch(retcode) {
            case 1:
                out.append("������ ����'�����.");
                break;
            case 2:
                out.append("����'������ ������ ������� ���������.");
                break;
        }
        System.out.println(retcode);
        return out.toString();
    }

    public Mistake check(int retcode, Map calculated, Map inputted) {
        boolean res = false;
        String answer = ((String) inputted.get("action"));
        switch(retcode) {
            case 1:
                res = answer.equals("1");
                break;
            case 2:
                res = answer.equals("2");
                break;
        }
        if (res) return Mistake.NO_MISTAKES;
        return Mistake.ANY_MISTAKE;
    }
}
