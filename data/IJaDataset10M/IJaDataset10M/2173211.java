package org.autoplot.csv;

import com.csvreader.CsvWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import org.das2.datum.Datum;
import org.das2.datum.Units;
import org.das2.datum.format.DatumFormatter;
import org.das2.util.monitor.ProgressMonitor;
import org.virbo.dataset.DataSetUtil;
import org.virbo.dataset.QDataSet;
import org.virbo.dataset.SemanticOps;
import org.virbo.datasource.DataSourceFormat;
import org.virbo.datasource.URISplit;

/**
 *
 * @author jbf
 */
public class CsvDataSourceFormat implements DataSourceFormat {

    public void formatData(String uri, QDataSet data, ProgressMonitor mon) throws Exception {
        URISplit split = URISplit.parse(uri);
        File f = new File(split.resourceUri);
        FileWriter fw = new FileWriter(f);
        CsvWriter writer = new CsvWriter(fw, ',');
        writer.setForceQualifier(true);
        writer.setUseTextQualifier(true);
        String[] values;
        String[] labels;
        int col = 0;
        QDataSet[] dss;
        QDataSet[] wdss;
        List<QDataSet> ldss = new ArrayList();
        List<QDataSet> lwdss = new ArrayList();
        if (data.property(QDataSet.DEPEND_0) != null) {
            ldss.add((QDataSet) data.property(QDataSet.DEPEND_0));
            lwdss.add(DataSetUtil.weightsDataSet((QDataSet) data.property(QDataSet.DEPEND_0)));
            col++;
        }
        ldss.add(data);
        lwdss.add(DataSetUtil.weightsDataSet(data));
        if (data.rank() == 1) {
            col++;
        } else if (data.rank() == 2) {
            col += data.length(0);
        } else {
            throw new IllegalArgumentException("rank limit, data must be rank 1 sequence or a rank 2 table of data");
        }
        dss = ldss.toArray(new QDataSet[ldss.size()]);
        wdss = lwdss.toArray(new QDataSet[lwdss.size()]);
        values = new String[col];
        labels = new String[col];
        {
            col = 0;
            for (int ids = 0; ids < dss.length; ids++) {
                String u = (String) dss[ids].property(QDataSet.LABEL);
                if (u == null) {
                    u = (String) dss[ids].property(QDataSet.NAME);
                }
                if (u == null) {
                    u = "data" + ids;
                }
                if (dss[ids].rank() == 1) {
                    labels[col++] = u;
                } else {
                    QDataSet dep1 = (QDataSet) dss[ids].property(QDataSet.DEPEND_1);
                    if (dep1 != null && dep1.rank() == 1) {
                        Units dep1units = SemanticOps.getUnits(dep1);
                        for (int j = 0; j < dss[ids].length(0); j++) {
                            labels[col++] = dep1units.format(Datum.create(dep1.value(j), dep1units));
                        }
                    } else {
                        for (int j = 0; j < dss[ids].length(0); j++) {
                            labels[col++] = u + " " + j;
                        }
                    }
                }
            }
        }
        writer.writeRecord(labels);
        writer.setForceQualifier(false);
        writer.setUseTextQualifier(true);
        DatumFormatter[] formats = new DatumFormatter[dss.length];
        for (int ids = 0; ids < dss.length; ids++) {
            Units u = SemanticOps.getUnits(dss[ids]);
            formats[ids] = u.getDatumFormatterFactory().defaultFormatter();
        }
        for (int i = 0; i < data.length(); i++) {
            col = 0;
            for (int ids = 0; ids < dss.length; ids++) {
                Units u = SemanticOps.getUnits(dss[ids]);
                if (dss[ids].rank() == 1) {
                    if (wdss[ids].value(i) == 0) {
                        values[col++] = "NaN";
                    } else {
                        values[col++] = formats[ids].format(u.createDatum(dss[ids].value(i)), u);
                    }
                } else {
                    for (int j = 0; j < dss[ids].length(0); j++) {
                        if (wdss[ids].value(i, j) == 0) {
                            values[col++] = "NaN";
                        } else {
                            values[col++] = formats[ids].format(u.createDatum(dss[ids].value(i, j)), u);
                        }
                    }
                }
            }
            writer.writeRecord(values);
        }
        writer.close();
        fw.close();
    }
}
