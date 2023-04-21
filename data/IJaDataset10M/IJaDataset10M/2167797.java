package edu.ucla.stat.SOCR.analyses.result;

import java.text.DecimalFormat;
import java.util.HashMap;
import org.w3c.dom.Document;

public class AnalysisResult extends Result {

    protected DecimalFormat dFormat;

    public AnalysisResult(HashMap texture) {
        super(texture);
    }

    public AnalysisResult(HashMap texture, HashMap graph) {
        super(texture, graph);
    }

    public AnalysisResult(String xmlResultString) {
        super(xmlResultString);
    }

    public AnalysisResult(Document dom) {
        super(dom);
    }

    public void setDecimalFormat(DecimalFormat format) {
        dFormat = format;
    }

    public String[] getFormattedGroup(double[] d) {
        int length = d.length;
        String[] s = new String[length];
        for (int i = 0; i < length; i++) s[i] = dFormat.format(d[i]);
        return s;
    }

    public String[][] getFormattedGroupArray(double[][] d) {
        int row = d.length;
        int col = d[0].length;
        String[][] s = new String[row][col];
        for (int i = 0; i < row; i++) for (int j = 0; j < col; j++) s[i][j] = dFormat.format(d[i][j]);
        return s;
    }

    public String getFormattedDouble(double d) {
        return dFormat.format(d);
    }
}
