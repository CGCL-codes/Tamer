package com.ggvaidya.TaxonDNA.SequenceMatrix;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.net.*;
import java.lang.reflect.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.Menu;
import java.awt.CheckboxMenuItem;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import com.ggvaidya.TaxonDNA.Common.*;
import com.ggvaidya.TaxonDNA.DNA.*;
import com.ggvaidya.TaxonDNA.DNA.formats.*;
import com.ggvaidya.TaxonDNA.UI.*;

public class DisplayDistancesMode extends DisplayMode implements ItemListener {

    public static final int FIRST_COLUMN_CONTAINING_CHARSET = 3;

    private String selected_colName = null;

    private double distances[][] = null;

    private double norm_distances[][] = null;

    private int ranks[][] = null;

    public static double DIST_ILLEGAL = -32.0;

    public static double DIST_NO_COMPARE_SEQ = -64.0;

    public static double DIST_SEQ_NA = -128.0;

    public static double DIST_NO_OVERLAP = -256.0;

    public static double DIST_SEQ_ON_TOP = -1024.0;

    public static double DIST_CANCELLED = -2048.0;

    private int currentDistanceMethod = Sequence.PDM_TRANS_ONLY;

    private int oldOverlap = 0;

    private int oldDistanceMode = Sequence.PDM_UNCORRECTED;

    private CheckboxMenuItem chmi_uncorrected = new CheckboxMenuItem("Uncorrected pairwise distances");

    private CheckboxMenuItem chmi_k2p = new CheckboxMenuItem("K2P distances");

    private CheckboxMenuItem chmi_trans = new CheckboxMenuItem("Transversions only");

    private CheckboxMenuItem chmi_last = chmi_trans;

    private List scores = null;

    private class Score implements Comparable {

        private String seqName_top = "";

        private String seqName = "";

        private double pairwise = 0.0;

        private int constant = +1;

        public Score(String seqName, double pairwise) {
            this.seqName = seqName;
            this.pairwise = pairwise;
            seqName_top = tableManager.getReferenceSequence();
            if (seqName_top == null) {
                List l = tableManager.getSequenceNames();
                if (l == null || l.size() == 0) throw new RuntimeException("Can't sort relative to a non-existant sequence!");
                seqName_top = (String) l.get(0);
            }
        }

        public Score(String seqName, double pairwise, int constant) {
            this(seqName, pairwise);
            this.constant = constant;
        }

        private String getName() {
            return seqName;
        }

        private double getPairwise() {
            return pairwise;
        }

        private String getSeqNameTop() {
            return seqName_top;
        }

        /** Compare this Score to another Score (with a common seqName_top); the smaller pairwise distance goes on top. */
        public int compareTo(Object o) {
            Score s = (Score) o;
            if (!s.getSeqNameTop().equals(getSeqNameTop())) throw new RuntimeException("Two sequences without identical reference sequences being compared in DisplayDistancesMode.Score!");
            if (getName().equals(seqName_top)) return -1;
            if (s.getName().equals(seqName_top)) return +1;
            return constant * ((int) (com.ggvaidya.TaxonDNA.DNA.Settings.makeLongFromDouble(getPairwise()) - com.ggvaidya.TaxonDNA.DNA.Settings.makeLongFromDouble(s.getPairwise())));
        }
    }

    ;

    /** 
	 * We need to know the TableManager we're serving, so that we can talk
	 * to the user. All else is vanity. Vanity, vanity, vanity.
	 */
    public DisplayDistancesMode(TableManager tm) {
        tableManager = tm;
        additionalColumns = FIRST_COLUMN_CONTAINING_CHARSET;
        chmi_uncorrected.addItemListener(this);
        chmi_k2p.addItemListener(this);
        chmi_trans.addItemListener(this);
        chmi_last.setState(true);
    }

    private TableCellRenderer static_oldRenderer = null;

    public void activateDisplay(JTable table, Object argument) {
        oldOverlap = Sequence.getMinOverlap();
        oldDistanceMode = Sequence.getPairwiseDistanceMethod();
        Sequence.setMinOverlap(1);
        Sequence.setPairwiseDistanceMethod(currentDistanceMethod);
        super.activateDisplay(table, argument);
        selected_colName = (String) argument;
        static_oldRenderer = table.getDefaultRenderer(String.class);
        table.setDefaultRenderer(String.class, new PDMColorRenderer(this));
    }

    public void deactivateDisplay() {
        Sequence.setMinOverlap(oldOverlap);
        Sequence.setPairwiseDistanceMethod(oldDistanceMode);
        table.setDefaultRenderer(String.class, static_oldRenderer);
    }

    public List getAdditionalColumns() {
        Vector v = new Vector();
        v.add(0, "Sequence name");
        v.add(1, "Total score");
        v.add(2, "No of charsets");
        return v;
    }

    public List getSortedColumns(Set colNames) {
        Vector v = new Vector(colNames);
        Collections.sort(v);
        if (selected_colName != null && tableManager.doesColumnExist(selected_colName)) {
            v.remove(selected_colName);
            v.add(0, selected_colName);
        } else {
            if (v.size() > 0) selected_colName = (String) v.get(0);
        }
        sortedColumns = v;
        return (java.util.List) v;
    }

    public void updateDisplay() {
        super.updateDisplay();
    }

    public List getSortedSequences(Set sequences) {
        if (sequences.size() == 0) {
            sortedSequences = new LinkedList();
            return sortedSequences;
        }
        String seqName_top = tableManager.getReferenceSequence();
        List sequencesList = new Vector(sequences);
        List columnList = new Vector(sortedColumns);
        if (seqName_top == null) {
            if (sequencesList.size() > 0) {
                seqName_top = (String) sequencesList.get(0);
                tableManager.setReferenceSequence(seqName_top);
            }
        }
        if (columnList == null || selected_colName == null || seqName_top == null) {
            sortedSequences = sequencesList;
            return sequencesList;
        }
        distances = new double[columnList.size()][sequencesList.size()];
        double max[] = new double[columnList.size()];
        Arrays.fill(max, -1.0);
        double min[] = new double[columnList.size()];
        Arrays.fill(min, +2.0);
        for (int x = 0; x < columnList.size(); x++) {
            String colName = (String) columnList.get(x);
            for (int y = 0; y < sequencesList.size(); y++) {
                String seqName = (String) sequencesList.get(y);
                Sequence seq = tableManager.getSequence(colName, seqName);
                Sequence seq_compare = tableManager.getSequence(colName, seqName_top);
                double dist = DIST_ILLEGAL;
                if (seq_compare == null) {
                    dist = DIST_NO_COMPARE_SEQ;
                } else if (seqName_top.equals(seqName)) {
                    dist = DIST_SEQ_ON_TOP;
                } else if (seq == null) {
                    if (tableManager.isSequenceCancelled(colName, seqName)) dist = DIST_CANCELLED; else dist = DIST_SEQ_NA;
                } else if ((dist = seq.getPairwise(seq_compare)) < 0) {
                    dist = DIST_NO_OVERLAP;
                }
                distances[x][y] = dist;
                if (dist >= 0) {
                    if (dist > max[x]) max[x] = dist;
                    if (dist < min[x]) min[x] = dist;
                }
            }
        }
        norm_distances = new double[columnList.size()][sequencesList.size()];
        for (int x = 0; x < columnList.size(); x++) {
            for (int y = 0; y < sequencesList.size(); y++) {
                if (distances[x][y] >= 0) norm_distances[x][y] = (distances[x][y] - min[x]) / (max[x] - min[x]); else norm_distances[x][y] = distances[x][y];
            }
        }
        scores = (List) new Vector();
        for (int y = 0; y < sequencesList.size(); y++) {
            String seqName = (String) sequencesList.get(y);
            double totalScore = 0.0;
            int count = 0;
            for (int x = 0; x < columnList.size(); x++) {
                String colName = (String) columnList.get(x);
                if (colName.equals(selected_colName)) continue;
                if (distances[x][y] >= 0) {
                    totalScore += norm_distances[x][y];
                    count++;
                }
            }
            totalScore = totalScore / count;
            scores.add(new Score(seqName, totalScore));
        }
        Collections.sort(scores);
        double[][] old_distances = distances;
        double[][] old_norm_distances = norm_distances;
        norm_distances = new double[columnList.size()][scores.size()];
        distances = new double[columnList.size()][scores.size()];
        Iterator i = scores.iterator();
        int seqIndex = 0;
        while (i.hasNext()) {
            String seqName = ((Score) i.next()).getName();
            int oldIndex = sequencesList.indexOf(seqName);
            for (int x = 0; x < columnList.size(); x++) {
                distances[x][seqIndex] = old_distances[x][oldIndex];
                norm_distances[x][seqIndex] = old_norm_distances[x][oldIndex];
            }
            seqIndex++;
        }
        old_distances = null;
        old_norm_distances = null;
        sequencesList = (List) new Vector();
        i = scores.iterator();
        while (i.hasNext()) {
            String seqName = ((Score) i.next()).getName();
            sequencesList.add(seqName);
        }
        ranks = new int[columnList.size()][sequencesList.size()];
        for (int x = 0; x < columnList.size(); x++) {
            LinkedList ll = new LinkedList();
            for (int y = 0; y < sequencesList.size(); y++) {
                if (distances[x][y] >= 0) ll.add(new Double(distances[x][y]));
            }
            Collections.sort(ll);
            for (int y = 0; y < sequencesList.size(); y++) {
                if (distances[x][y] >= 0) ranks[x][y] = ll.indexOf(new Double(distances[x][y])); else ranks[x][y] = (int) Math.floor(distances[x][y]);
            }
        }
        sortedSequences = sequencesList;
        return sequencesList;
    }

    /**
	 * Gets the value at a particular column. The important
	 * thing here is that two areas are 'special':
	 * 1.	Row 0 is reserved for the column names.
	 * 2.	Column 0 is reserved for the row names.
	 * 3.	(0, 0) is to be a blank box (new String("")).
	 */
    public Object getValueAt(int rowIndex, int columnIndex) {
        String colName = getColumnName(columnIndex);
        String seqName = getRowName(rowIndex);
        if (colName == null) throw new IllegalArgumentException("Either rowIndex is out of range (rowIndex=" + rowIndex + "), or sortedSequenceNames isn't primed.");
        if (seqName == null) throw new IllegalArgumentException("Either rowIndex is out of range (rowIndex=" + rowIndex + "), or sortedSequenceNames isn't primed.");
        if (columnIndex == 0) return seqName;
        if (columnIndex == 1) return percentage(((Score) scores.get(rowIndex)).getPairwise(), 1.0) + "%";
        if (columnIndex == 2) return tableManager.getCharsetsCount(seqName) + "";
        int col = columnIndex - FIRST_COLUMN_CONTAINING_CHARSET;
        int row = rowIndex;
        int ndist = (int) Math.round(norm_distances[col][row] * 100);
        int rank = ranks[col][row];
        double dist = distances[col][row];
        if (dist >= 0) {
            return ndist + "% #" + rank + " (" + percentage(dist, 1.0) + "%)";
        } else if (dist == DIST_ILLEGAL) {
            return "(N/A - bug)";
        } else if (dist == DIST_NO_COMPARE_SEQ) {
            return "(No data for reference taxon)";
        } else if (dist == DIST_SEQ_NA) {
            return "(No data)";
        } else if (dist == DIST_NO_OVERLAP) {
            return "(No overlap with reference)";
        } else if (dist == DIST_SEQ_ON_TOP) {
            return "(ON TOP)";
        } else if (dist == DIST_CANCELLED) {
            return "(CANCELLED)";
        } else {
            return "(N/A - unknown)";
        }
    }

    /**
	 * Don't accept double-clicks on 'invalid' distances.
	 */
    public void doubleClick(MouseEvent e, int colIndex, int rowIndex) {
        int col = colIndex - FIRST_COLUMN_CONTAINING_CHARSET;
        int row = rowIndex;
        if (distances.length < col) return;
        if (distances[col].length < row) return;
        double d = distances[col][row];
        if (d >= 0) {
            super.doubleClick(e, colIndex, rowIndex);
        } else ;
    }

    /** Convenience function */
    private double percentage(double x, double y) {
        return com.ggvaidya.TaxonDNA.DNA.Settings.percentage(x, y);
    }

    public void setValueAt(String colName, String rowName, Object aValue) {
    }

    public String getValueAt(String colName, String rowName, Sequence seq) {
        return null;
    }

    public void setStatusBar(StringBuffer buff) {
        double r2 = getAverageR();
        if (r2 > -1) {
            buff.append("Average intercolumn correlation coefficient = " + r2 + ".");
        }
    }

    double[][] correlations = null;

    /**
	 * Calculates and returns the correlation between two columns;
	 * in this case indicated by indices into the arrays used by
	 * us.
	 */
    public double getCorrelation(int x, int y) {
        if (x == y) return 1.0;
        if (x > y) return getCorrelation(y, x);
        int N = sortedColumns.size();
        if (correlations == null || correlations[0].length < N) {
            correlations = new double[N][N];
            for (int c = 0; c < N; c++) Arrays.fill(correlations[c], -1.0);
        }
        if (distances == null) return -1.0;
        int n = 0;
        double sum_x = 0;
        double sum_y = 0;
        double sum_x2 = 0;
        double sum_y2 = 0;
        double sum_xy = 0;
        if (distances[x][0] != DIST_SEQ_ON_TOP || distances[y][0] != DIST_SEQ_ON_TOP) return -2;
        for (int c = 1; c < sortedSequences.size(); c++) {
            double d_x = distances[x][c];
            double d_y = distances[y][c];
            if (d_x < 0 || d_y < 0) continue;
            n++;
            sum_x += d_x;
            sum_x2 += (d_x * d_x);
            sum_y += d_y;
            sum_y2 += (d_y * d_y);
            sum_xy += (d_x * d_y);
        }
        double variable_x = (n * sum_x2) - (sum_x * sum_x);
        double variable_y = (n * sum_y2) - (sum_y * sum_y);
        if (variable_x <= 0) return 0.0;
        if (variable_y <= 0) return 0.0;
        double r = (((double) n * sum_xy) - (sum_x * sum_y)) / (Math.sqrt(variable_x) * Math.sqrt(variable_y));
        correlations[x][y] = r;
        return r;
    }

    public double getAverageR() {
        int N = sortedColumns.size();
        if (N == 0 || distances == null) return -2.0;
        double dist[][] = dist = (double[][]) distances.clone();
        double R_iy[] = new double[N - 1];
        double R_ii[][] = new double[N - 1][N - 1];
        double total = 0;
        long n = 0;
        for (int y = 0; y < N; y++) {
            for (int x = 0; x < N; x++) {
                if (y < x) continue;
                double c = getCorrelation(y, x);
                if (c < -1) continue;
                total += c;
                n++;
            }
        }
        if (n == 0) return -1;
        return (total / (double) n);
    }

    /**
 	 * We, err, use our mode-specific menu to flip between pairwise distance types.
	 * That makes sense, no? No?
	 */
    public Menu getDisplayModeMenu() {
        Menu m = new Menu("Distance settings");
        m.add(chmi_uncorrected);
        m.add(chmi_k2p);
        m.add(chmi_trans);
        return m;
    }

    /**
	 * Now, somebody's going to have to LISTEN to that menu, huh?
	 */
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource().equals(chmi_last)) {
            chmi_last.setState(true);
            return;
        }
        chmi_last.setState(false);
        chmi_last = (CheckboxMenuItem) e.getSource();
        chmi_last.setState(true);
        if (chmi_uncorrected.equals(chmi_last)) currentDistanceMethod = Sequence.PDM_UNCORRECTED;
        if (chmi_k2p.equals(chmi_last)) currentDistanceMethod = Sequence.PDM_K2P;
        if (chmi_trans.equals(chmi_last)) currentDistanceMethod = Sequence.PDM_TRANS_ONLY;
        tableManager.changeDisplayMode(TableManager.DISPLAY_DISTANCES);
    }

    /** For convenience */
    public boolean identical(double x, double y) {
        return com.ggvaidya.TaxonDNA.DNA.Settings.identical(x, y);
    }
}

class PDMColorRenderer extends DefaultTableCellRenderer {

    DisplayDistancesMode dpm = null;

    public PDMColorRenderer(DisplayDistancesMode dpm) {
        this.dpm = dpm;
    }

    /**
	 * Returns the colour for 'value' (which should be in the range [0, 1] inclusive)
	 */
    public Color getXORColor(Color basicColor, Color bg) {
        Color textColor = Color.WHITE;
        if (basicColor.equals(Color.RED)) return Color.BLACK;
        if (bg.getRed() > 128) {
            return Color.BLACK;
        } else {
            return Color.WHITE;
        }
    }

    /**
	 * Returns the colour for 'value' (which should be in the range [0, 1] inclusive)
	 */
    public Color getColor(Color basicColor, double value) {
        if (basicColor.equals(Color.BLACK)) {
            return Color.getHSBColor(0.0f, 0.0f, 1.0f - (float) value);
        } else return Color.getHSBColor(0.0f, (float) value, 1.0f);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        JComponent comp = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
        comp.setOpaque(false);
        comp.setForeground(Color.BLACK);
        comp.setBackground(Color.WHITE);
        if (row < 0 || col < DisplayDistancesMode.FIRST_COLUMN_CONTAINING_CHARSET) {
            return comp;
        }
        Color basicColor = Color.BLACK;
        if (col == DisplayDistancesMode.FIRST_COLUMN_CONTAINING_CHARSET) basicColor = Color.RED;
        String val = (String) value;
        if (val.equals("(N/A)")) return comp;
        if (val.equals("(N/A - bug)")) return comp;
        if (val.indexOf(':') != -1) val = val.substring(0, val.indexOf(':'));
        if (val.indexOf('%') != -1) val = val.substring(0, val.indexOf('%'));
        val = val.replaceAll("\\%", "");
        val = val.replaceAll("\\:", "");
        try {
            double d = Double.parseDouble(val) / 100;
            comp.setOpaque(true);
            if (d < 0 || d > 1) {
                comp.setBackground(getColor(basicColor, 0.0));
                comp.setForeground(getXORColor(basicColor, comp.getBackground()));
            } else {
                comp.setBackground(getColor(basicColor, d));
                comp.setForeground(getXORColor(basicColor, comp.getBackground()));
            }
        } catch (NumberFormatException e) {
        }
        return comp;
    }
}
