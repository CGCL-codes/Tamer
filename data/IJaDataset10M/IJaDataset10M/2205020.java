package edu.columbia.concerns.metrics;

import org.eclipse.swt.SWT;

public class SimilarityMetricsTable extends ConcernMetricsTable {

    public SimilarityMetricsTable() {
        super(new ColumnInfo[] { new ColumnInfo("Concern", SWT.LEFT, 200), new ColumnInfo("DOSC", SWT.CENTER, 100), new ColumnInfo("DOSM", SWT.CENTER, 100), new ColumnInfo("CDC", SWT.CENTER, 50), new ColumnInfo("CDO", SWT.CENTER, 50), new ColumnInfo("SLOCC", SWT.CENTER, 50), new ColumnInfo("CAA", SWT.CENTER, 50), new ColumnInfo("Precision", SWT.CENTER, 50), new ColumnInfo("Recall", SWT.CENTER, 50), new ColumnInfo("Effectiveness", SWT.LEFT, 50), new ColumnInfo("FMeasure", SWT.LEFT, 50), new ColumnInfo("FMeasure_05", SWT.LEFT, 50) });
    }
}
