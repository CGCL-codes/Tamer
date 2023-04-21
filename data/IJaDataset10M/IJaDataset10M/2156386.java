package test.endtoend;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Arrays;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import org.das2.client.DasServer;
import org.das2.datum.DatumRange;
import org.das2.datum.DatumRangeUtil;
import org.das2.stream.StreamDescriptor;
import org.das2.util.monitor.NullProgressMonitor;
import org.das2.util.monitor.ProgressMonitor;
import org.virbo.dataset.MutablePropertyDataSet;
import org.virbo.dataset.QDataSet;
import org.virbo.datasource.DataSetURI;
import org.virbo.dsops.Ops;
import static org.virbo.autoplot.ScriptContext.*;

/**
 * Plasma Wave Group at Iowa, test Das2Server entries with "exampleRange".
 * @author jbf
 */
public class Test501 {

    private static final int testid = 501;

    static void flatten(TreeModel tm, String root, Object node, List<String> result) {
        for (int i = 0; i < tm.getChildCount(node); i++) {
            Object child = tm.getChild(node, i);
            if (tm.isLeaf(child)) {
                String ss = (String) ((DefaultMutableTreeNode) child).getUserObject();
                result.add(root + "/" + ss);
            } else {
                String us = (String) ((DefaultMutableTreeNode) child).getUserObject();
                flatten(tm, root + "/" + us, child, result);
            }
        }
    }

    private static void do1(String uri, int id) throws Exception {
        long t0 = System.currentTimeMillis();
        QDataSet ds = org.virbo.jythonsupport.Util.getDataSet(uri);
        double t = (System.currentTimeMillis() - t0) / 1000.;
        MutablePropertyDataSet hist = (MutablePropertyDataSet) Ops.autoHistogram(ds);
        hist.putProperty(QDataSet.TITLE, uri);
        String label = String.format("test%03d_%03d", testid, id);
        hist.putProperty(QDataSet.LABEL, label);
        formatDataSet(hist, label + ".qds");
        QDataSet dep0 = (QDataSet) ds.property(QDataSet.DEPEND_0);
        if (dep0 != null) {
            MutablePropertyDataSet hist2 = (MutablePropertyDataSet) Ops.autoHistogram(dep0);
            formatDataSet(hist2, label + ".dep0.qds");
        } else {
            PrintWriter pw = new PrintWriter(label + ".dep0.qds");
            pw.println("no dep0");
            pw.close();
        }
        plot(ds);
        setCanvasSize(750, 300);
        int i = uri.lastIndexOf("/");
        getApplicationModel().waitUntilIdle(true);
        String fileUri = uri.substring(i + 1);
        if (!getDocumentModel().getPlotElements(0).getComponent().equals("")) {
            String dsstr = String.valueOf(getDocumentModel().getDataSourceFilters(0).getController().getDataSet());
            fileUri = fileUri + " " + dsstr + " " + getDocumentModel().getPlotElements(0).getComponent();
        }
        setTitle(fileUri);
        writeToPng(String.format("test%03d_%03d.png", testid, id));
        System.err.printf("Read in %9.3f seconds (%s): %s\n", t, label, uri);
    }

    public static void main(String[] args) throws Exception {
        TreeModel tm = DasServer.plasmaWaveGroup.getDataSetListWithDiscovery();
        List<String> ids = new ArrayList();
        flatten(tm, "", tm.getRoot(), ids);
        Map<Integer, String> failures = new LinkedHashMap();
        List<Integer> skip = new ArrayList(Arrays.asList(3, 4, 5, 6, 7, 18));
        int count = 0;
        for (String id : ids) {
            if (id.contains("/testing/")) {
                System.err.println("skipping /testing/: " + id);
                continue;
            }
            if (id.contains("juno/waves") && id.contains("housekeeping.dsdf") && !id.contains("/juno/waves/flight/housekeeping.dsdf")) skip.add(count);
            count++;
        }
        System.err.println("Skipping the tests: " + skip);
        int iid = 0;
        for (String id : ids) {
            System.err.println(String.format("==== test %d of %d ========================================================", iid, count));
            if (id.contains("/testing/")) {
                System.err.println("ids containing /testing/ are automatically skipped: " + id);
                continue;
            }
            if (skip.contains(iid)) {
                iid++;
                System.err.println("test marked for skipping in Test501.java: " + id);
                continue;
            }
            StreamDescriptor dsdf = DasServer.plasmaWaveGroup.getStreamDescriptor(DasServer.plasmaWaveGroup.getURL(id));
            String exampleRange = (String) dsdf.getProperty("exampleRange");
            DatumRange tr = DatumRangeUtil.parseTimeRangeValid(exampleRange);
            String uri = "vap+das2server:" + DasServer.plasmaWaveGroup.getURL() + "?dataset=" + id + "&start_time=" + tr.min() + "&end_time=" + tr.max();
            System.err.println("id: " + id);
            System.err.println("uri: " + uri);
            try {
                do1(uri, iid);
            } catch (Exception ex) {
                ex.printStackTrace();
                failures.put(iid, uri);
            }
            iid++;
        }
        System.err.println("DONE...");
        if (failures.size() > 0) {
            System.err.println(String.format("found %d failures:", failures.size()));
            for (int i : failures.keySet()) {
                System.err.println(String.format("%3d: %s", i, failures.get(i)));
            }
            System.exit(1);
        } else {
            System.exit(0);
        }
    }
}
