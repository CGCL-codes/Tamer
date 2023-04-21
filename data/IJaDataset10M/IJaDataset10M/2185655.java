package com.rapidminer.gui.viewer;

import javax.swing.table.AbstractTableModel;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.tools.math.similarity.DistanceMeasure;

/**
 * The table model for the similarity visualization.
 *
 * @author Ingo Mierswa
 */
public class SimilarityTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 7451178433975831387L;

    private static final int COLUMN_FIRST = 0;

    private static final int COLUMN_SECOND = 1;

    private static final int COLUMN_SIMILARITY = 2;

    private DistanceMeasure similarity;

    private ExampleSet exampleSet;

    private Attribute idAttribute;

    public SimilarityTableModel(DistanceMeasure similarity, ExampleSet exampleSet) {
        this.similarity = similarity;
        this.exampleSet = exampleSet;
        this.idAttribute = exampleSet.getAttributes().getId();
    }

    @Override
    public String getColumnName(int column) {
        switch(column) {
            case 0:
                return "First";
            case 1:
                return "Second";
            case 2:
                if (similarity.isDistance()) {
                    return "Distance";
                } else {
                    return "Similarity";
                }
        }
        return "";
    }

    @Override
    public Class<?> getColumnClass(int column) {
        if (column == COLUMN_SIMILARITY) {
            return Double.class;
        } else {
            return String.class;
        }
    }

    public int getColumnCount() {
        return 3;
    }

    public int getRowCount() {
        int n = exampleSet.size();
        return ((n - 1) * n) / 2;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        int[] actualRows = getActualRows(rowIndex);
        Example first = exampleSet.getExample(actualRows[0]);
        Example second = exampleSet.getExample(actualRows[1]);
        switch(columnIndex) {
            case COLUMN_FIRST:
                return first.getValueAsString(idAttribute);
            case COLUMN_SECOND:
                return second.getValueAsString(idAttribute);
            case COLUMN_SIMILARITY:
                if (similarity.isDistance()) return Double.valueOf(this.similarity.calculateDistance(first, second)); else return Double.valueOf(this.similarity.calculateSimilarity(first, second));
            default:
                return "?";
        }
    }

    private int[] getActualRows(int rowIndex) {
        int sum = 0;
        int currentLength = exampleSet.size() - 1;
        int result = 0;
        while ((sum + currentLength) <= rowIndex) {
            sum += currentLength;
            currentLength--;
            result++;
        }
        return new int[] { result, exampleSet.size() - (sum + currentLength - rowIndex) };
    }
}
