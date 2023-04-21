package test.endtoend;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.virbo.autoplot.ScriptContext.*;
import org.virbo.dataset.MutablePropertyDataSet;
import org.virbo.dataset.QDataSet;
import org.virbo.dsops.Ops;
import org.virbo.jythonsupport.Util;

/**
 * giant list of ASCII URIs for testing.
 * @author jbf
 */
public class Test021 {

    static long t0 = System.currentTimeMillis();

    static String[] uris = new String[] { "100 vap+dat:file:///home/jbf/ct/hudson/data.backup/dat/test021/wind_mag_plasma_94_340.txt?time=field0&column=field4&timeFormat=$Y-$m-$d+$H:$M:$S", "022 vap+dat:file:///home/jbf/ct/hudson/data.backup/dat/test021/E1_SUMM_GSE_GSM.TAB?timeFormat=ISO8601&column=field1", "023 file:///home/jbf/ct/hudson/data.backup/dat/test021/wi_swe_fc_apbimax.1995005.txt?comment=;&column=21&timeFormat=$Y+$j&time=field0", "024 file:///home/jbf/ct/hudson/data.backup/dat/test021/$Y/A105$y$m.TXT?skip=23&timeFormat=$y$m$d+$H$M&column=E1&time=YYMMDD&fill=32700&timerange=Dec+2004", "027 file:///home/jbf/ct/hudson/data.backup/dat/test021/2003.txt", "028 vap+dat:file:///home/jbf/ct/hudson/data.backup/dat/test021/omni_min200101.asc?time=field0&column=field14&timeFormat=$Y+$j+$H+$M&validMax=9999", "029 file:///home/jbf/ct/hudson/data.backup/dat/test021/wi_swe_fc_apbimax.2001017.txt?column=field2&comment=;&time=field0&timeFormat=$Y+$j", "030 file:///home/jbf/ct/hudson/data.backup/dat/test021/1998.txt?time=YY&column=GSE_X&timeFormat=$y+$b+$d+$(ignore):$H:$M:$S", "031 file:///home/jbf/ct/hudson/data.backup/dat/test021/gim-3dl2-2002-01_v02.txt?skip=68&time=field0&timeFormat=$Y+$j+$H+$M+$S&column=field8&fill=-9999.0", "035 vap+txt:file:///home/jbf/ct/hudson/data.backup/dat/pws$y$j.data?timerange=1997-094&time=field0&column=field7&timeFormat=$Y+$j+$H+$M+$S", "036 file:///home/jbf/ct/hudson/data.backup/dat/test021/$Y/A105$y$m.TXT?skip=23&timeFormat=$y$m$d+$H$M&column=E1&time=YYMMDD&fill=32700&timerange=Dec+2004", "038 file:///home/jbf/ct/hudson/data.backup/dat/test021/a7510-12.zip/av751229.dat?depend0=field0&rank2=1:", "040 file:///home/jbf/ct/hudson/data.backup/dat/test021/a$y$m-...zip/av$y$m$d.dat?rank2=1:&time=field0&timerange=1975-oct", "045 file:///home/jbf/ct/hudson/data.backup/dat/test021/A1050402.TXT", "050 file:///home/jbf/ct/hudson/data.backup/dat/vho.nasa.gov/mission/helios2/H276_021.dat?timeFormat=$Y+$j+$H&column=bn&time=year", "051 file:///home/jbf/ct/hudson/data.backup/dat/ccmc/sw1_31485.txt?time=field0&timeFormat=$Y+$m+$d+$H+$M+$S+$(milli)&bundle=field7-field9" };

    public static void xxx(String id) {
        System.err.println("-- timer -- " + id + " --: " + (System.currentTimeMillis() - t0));
        t0 = System.currentTimeMillis();
    }

    public static void main(String[] args) {
        QDataSet ds;
        int count;
        try {
            setCanvasSize(750, 300);
            getDocumentModel().getOptions().setAutolayout(false);
            getDocumentModel().getCanvases(0).getMarginColumn().setRight("100%-10em");
            xxx("start");
            ThreadPoolExecutor exec = new ThreadPoolExecutor(1, 1, 3600, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1));
            for (String s : uris) {
                count = Integer.parseInt(s.substring(0, 4).trim());
                s = s.substring(4);
                String label = String.format("test021_%03d", count);
                try {
                    if (s.startsWith("CC ")) {
                        String[] list = org.virbo.jythonsupport.Util.listDirectory(s.substring(3));
                        PrintWriter out = new PrintWriter(label + ".txt");
                        for (String l : list) {
                            out.println(l);
                        }
                        out.close();
                    } else if (s.contains("file:///") && !s.contains("file:///home/jbf/ct/hudson")) {
                        System.err.println("skipping local " + s);
                    } else {
                        doTest(s, label, exec);
                    }
                } catch (Exception ex) {
                    PrintWriter pw = new PrintWriter(label + ".error");
                    pw.println(s);
                    pw.println("");
                    ex.printStackTrace(pw);
                    pw.close();
                    ex.printStackTrace();
                }
                xxx(label + ": " + s);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        System.exit(0);
    }

    private static void doTest(final String s, final String label, ThreadPoolExecutor exec) throws IOException, InterruptedException, Exception {
        Runnable run = new Runnable() {

            public void run() {
                try {
                    QDataSet ds;
                    ds = Util.getDataSet(s);
                    MutablePropertyDataSet hist = (MutablePropertyDataSet) Ops.autoHistogram(ds);
                    hist.putProperty(QDataSet.TITLE, s);
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
                    int i = s.lastIndexOf("/");
                    setTitle(s.substring(i + 1));
                    writeToPng(label + ".png");
                } catch (Exception ex) {
                    Logger.getLogger(Test021.class.getName()).log(Level.SEVERE, ex.toString(), ex);
                    try {
                        QDataSet ds = Util.getDataSet(s);
                        System.err.println(ds);
                    } catch (Exception ex1) {
                        Logger.getLogger(Test021.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                }
            }
        };
        int timeoutSeconds = 60;
        try {
            exec.submit(run).get(timeoutSeconds, TimeUnit.SECONDS);
            System.err.println("okay!");
        } catch (Exception ex) {
            PrintWriter pw = new PrintWriter(label + ".error");
            pw.println(s);
            pw.println("\ntimeout in " + timeoutSeconds + " seconds.");
            ex.printStackTrace(pw);
            pw.close();
        }
    }
}
