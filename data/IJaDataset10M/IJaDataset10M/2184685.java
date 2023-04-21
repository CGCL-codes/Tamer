package jp.riken.omicspace.analysis;

import jp.riken.omicspace.osml.impl.*;
import jp.riken.omicspace.graphics.impl.*;
import jp.riken.omicspace.frame.*;
import java.io.*;
import java.awt.*;
import java.net.*;
import java.lang.*;
import java.text.*;
import java.util.*;
import java.util.zip.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.xml.sax.*;
import org.w3c.dom.*;

public class ExactDocument extends Thread {

    private OsmlContainer osmlContainer = null;

    private ExactContainer exactContainer[] = null;

    private final String sSpace = "&nbsp;";

    private String sOutputFile = "";

    private ProgressFrame progressFrame = null;

    public ExactDocument() {
    }

    public void setOsmlContainer(OsmlContainer osml) {
        osmlContainer = osml;
    }

    public void setExactContainer(ExactContainer exact[]) {
        exactContainer = exact;
    }

    public void setOutputFile(String output) {
        sOutputFile = output;
    }

    public void setProgressFrame(ProgressFrame frame) {
        progressFrame = frame;
    }

    public void run() {
        Date date0 = new Date();
        if (progressFrame != null) {
            progressFrame.setTitle("Now saving exact test results...");
            progressFrame.show();
            progressFrame.setValue(0);
        }
        saveFile(sOutputFile);
        if (progressFrame != null) {
            progressFrame.setValue(100);
            progressFrame.hide();
        }
        Date date1 = new Date();
        System.out.println((date1.getTime() - date0.getTime()) / 1000 + " sec.");
    }

    public boolean isCandidate(OmicElementContainer e1, OmicElementContainer e2) {
        if (e1 == e2) return true;
        return e1.isCandidate(e2);
    }

    public boolean saveFile(String outfile) {
        if (osmlContainer == null || exactContainer == null) return false;
        boolean bCheck = false;
        OutputStream fos = null;
        try {
            fos = new FileOutputStream(outfile);
            bCheck = outputXml(new OutputStreamWriter(fos));
            fos.close();
        } catch (IOException ioe) {
            System.err.println("IOException: " + ioe);
            return false;
        }
        return bCheck;
    }

    private void setExactTable(Hashtable table, BaseContainer target, ExactContainer exact) {
        ArrayList v = (ArrayList) table.get(target);
        if (v == null) {
            v = new ArrayList();
            table.put(target, v);
        }
        v.add(exact);
    }

    private void outTable(Hashtable table, int index, Writer fos) {
        try {
            int nCount = 1;
            for (Enumeration e = table.keys(); e.hasMoreElements(); ) {
                BaseContainer t1 = (BaseContainer) e.nextElement();
                fos.write("<tr bgcolor=#88DDDD>");
                fos.write("<a name=" + t1.getId() + ">");
                fos.write("<td colspan=3 nowrap>" + t1.getName() + "</td>");
                fos.write("<td>" + t1.getNote() + "</td>");
                fos.write("<td bgcolor=#88DDDD>" + sSpace + "</td>");
                fos.write("<td>" + "A" + "</td>");
                fos.write("<td>" + "B" + "</td>");
                fos.write("<td>" + "C" + "</td>");
                fos.write("<td>" + "D" + "</td>");
                fos.write("<td>" + "P" + "</td>");
                fos.write("<td>" + "P'" + "</td>");
                fos.write("<td>" + "N" + "</td>");
                fos.write("</tr>");
                ArrayList v1 = (ArrayList) table.get(t1);
                if (v1 == null) continue;
                for (int i = 0; i < v1.size(); i++) {
                    ExactContainer fs = (ExactContainer) v1.get(i);
                    BaseContainer t2 = fs.getFunctionalClassContainer(index);
                    fos.write("<tr bgcolor=#88DD88>");
                    fos.write("<td bgcolor=#FFFFFF>" + sSpace + sSpace + sSpace + "</td>");
                    fos.write("<td colspan=2 nowrap><a href=#" + t2.getId() + ">" + t2.getName() + "</a></td>");
                    fos.write("<td>" + t2.getNote() + "</td>");
                    fos.write("<td bgcolor=#88DD88>" + sSpace + "</td>");
                    fos.write("<td>" + fs.getA() + "</td>");
                    fos.write("<td>" + fs.getB() + "</td>");
                    fos.write("<td>" + fs.getC() + "</td>");
                    fos.write("<td>" + fs.getD() + "</td>");
                    fos.write("<td nowrap>" + (float) fs.getP() + "</td>");
                    fos.write("<td nowrap>" + (float) (fs.getP() * fs.getN()) + "</td>");
                    fos.write("<td>" + fs.getN() + "</td>");
                    fos.write("</tr>");
                    FunctionalClassContainer f1 = (FunctionalClassContainer) t1;
                    FunctionalClassContainer f2 = (FunctionalClassContainer) t2;
                    OmicElementContainer e1[] = f1.getOmicElementContainer("[@container=all]");
                    OmicElementContainer e2[] = f2.getOmicElementContainer("[@container=all]");
                    Vectortable e1Table = new Vectortable();
                    Vectortable e2Table = new Vectortable();
                    for (int k = 0; k < e1.length; k++) {
                        if (e1[k].getName().length() > 0) {
                            String s[] = e1[k].getName().split(",");
                            for (int m = 0; m < s.length; m++) e1Table.put(s[m], e1[k]);
                        }
                        if (e1[k].getAppendix().length() > 0) {
                            String s[] = e1[k].getAppendix().split(",");
                            for (int m = 0; m < s.length; m++) e1Table.put(s[m], e1[k]);
                        }
                    }
                    for (int k = 0; k < e2.length; k++) {
                        if (e2[k].getName().length() > 0) {
                            String s[] = e2[k].getName().split(",");
                            for (int m = 0; m < s.length; m++) e2Table.put(s[m], e2[k]);
                        }
                        if (e2[k].getAppendix().length() > 0) {
                            String s[] = e2[k].getAppendix().split(",");
                            for (int m = 0; m < s.length; m++) e2Table.put(s[m], e2[k]);
                        }
                    }
                    for (int j = 0; j < fs.getHitContainerSize(); j++) {
                        OmicElementContainer gene = (OmicElementContainer) fs.getHitContainer(j);
                        String sF1Name = "";
                        String sF1Note = "";
                        Hashtable hF1Name = new Hashtable();
                        Hashtable hF1Note = new Hashtable();
                        Hashtable hGeneList = new Hashtable();
                        if (gene.getName().length() > 0) {
                            String s[] = gene.getName().split(",");
                            for (int m = 0; m < s.length; m++) hGeneList.put(s[m], gene);
                        }
                        if (gene.getAppendix().length() > 0) {
                            String s[] = gene.getAppendix().split(",");
                            for (int m = 0; m < s.length; m++) hGeneList.put(s[m], gene);
                        }
                        Hashtable h1 = new Hashtable();
                        Hashtable h2 = new Hashtable();
                        for (Enumeration en = hGeneList.keys(); en.hasMoreElements(); ) {
                            String key = (String) en.nextElement();
                            ArrayList vv1 = (ArrayList) e1Table.get(key);
                            if (vv1 != null) {
                                for (int m = 0; m < vv1.size(); m++) {
                                    h1.put(vv1.get(m), vv1.get(m));
                                }
                            }
                            ArrayList vv2 = (ArrayList) e2Table.get(key);
                            if (vv2 != null) {
                                for (int m = 0; m < vv2.size(); m++) {
                                    h2.put(vv2.get(m), vv2.get(m));
                                }
                            }
                        }
                        for (Enumeration en = h1.keys(); en.hasMoreElements(); ) {
                            OmicElementContainer ee1 = (OmicElementContainer) en.nextElement();
                            String name = ee1.getName();
                            if (name.length() > 0) {
                                String s[] = name.split(",");
                                for (int m = 0; m < s.length; m++) {
                                    hF1Name.put(s[m], s[m]);
                                }
                            }
                            String note = ee1.getNote();
                            if (note.length() > 0) {
                                hF1Note.put(note, note);
                            }
                        }
                        for (Enumeration en = h2.keys(); en.hasMoreElements(); ) {
                            OmicElementContainer ee2 = (OmicElementContainer) en.nextElement();
                            String name = ee2.getName();
                            if (name.length() > 0) {
                                String s[] = name.split(",");
                                for (int m = 0; m < s.length; m++) {
                                    hF1Name.put(s[m], s[m]);
                                }
                            }
                            String note = ee2.getNote();
                            if (note.length() > 0) {
                                hF1Note.put(note, note);
                            }
                        }
                        DatasetContainer dataset = gene.getDatasetContainer();
                        int nExp = dataset.getExperimentContainerSize();
                        String sExp = "";
                        sExp += "<table border=1>\n";
                        sExp += "<tr bgcolor=#FFFFFF>\n";
                        for (int k = 0; k < gene.getAmountContainerSize(); k++) {
                            if (nExp > 0 && k > 0 && k % nExp == 0) {
                                sExp += "</tr>\n";
                                sExp += "<tr bgcolor=#FFFFFF>\n";
                            }
                            AmountContainer amount = gene.getAmountContainer(k);
                            double v = amount.getValueOfValue();
                            String color = Integer.toHexString(DrawChart.getColor(v));
                            if (color.length() == 0) color = "000000" + color;
                            if (color.length() == 1) color = "00000" + color;
                            if (color.length() == 2) color = "0000" + color;
                            if (color.length() == 3) color = "000" + color;
                            if (color.length() == 4) color = "00" + color;
                            if (color.length() == 5) color = "0" + color;
                            sExp += "<td bgcolor=" + "#" + color + ">" + sSpace + "</td>";
                        }
                        sExp += "</tr>\n";
                        sExp += "</table>\n";
                        fos.write("<tr bgcolor=#8888DD>");
                        fos.write("<td bgcolor=#FFFFFF>" + sSpace + sSpace + sSpace + "</td>");
                        fos.write("<td bgcolor=#FFFFFF>" + sSpace + sSpace + sSpace + "</td>");
                        fos.write("<td nowrap>" + gene.getName() + "</td>");
                        fos.write("<td>" + gene.getNote() + "</td>");
                        fos.write("<td bgcolor=#8888DD>" + sExp + "</td>");
                        String geneInfo = "";
                        geneInfo = gene.getName();
                        if (geneInfo.length() > 0) geneInfo += "<br>";
                        geneInfo += gene.getNote();
                        if (geneInfo.length() > 0) geneInfo += "<br>";
                        geneInfo += gene.getAppendix();
                        for (Enumeration en = hF1Name.keys(); en.hasMoreElements(); ) {
                            String sn = (String) en.nextElement();
                            if (sF1Name.length() > 0) sF1Name += ",";
                            sF1Name += sn + " ";
                        }
                        for (Enumeration en = hF1Note.keys(); en.hasMoreElements(); ) {
                            String sn = (String) en.nextElement();
                            if (sF1Note.length() > 0) sF1Note += "<br>";
                            sF1Note += sn + " ";
                        }
                        fos.write("<td colspan=7>" + sF1Name + "<br>" + sF1Note + "</td>");
                        fos.write("</tr>");
                    }
                }
                if (progressFrame != null) {
                    int nPos = (int) ((double) (nCount) / (double) (table.size()) * 100);
                    progressFrame.setValue(nPos);
                    progressFrame.setString(nCount + "/" + (table.size()));
                    nCount++;
                }
            }
        } catch (java.io.IOException ioe) {
            return;
        }
    }

    private void outInformation(Writer fos) {
        if (osmlContainer == null) return;
        if (exactContainer == null) return;
        try {
            DateFormat df = DateFormat.getDateInstance();
            String date = df.format(new Date());
            fos.write("<tr bgcolor=#DD8888>");
            fos.write("<td nowrap colspan=2>Date:</td>");
            fos.write("<td nowrap colspan=10>" + date + "</td>");
            fos.write("</tr>\n");
            if (exactContainer.length > 0) {
                String method = exactContainer[0].getMethod();
                fos.write("<tr bgcolor=#DD8888>");
                fos.write("<td nowrap colspan=2>Method:</td>");
                fos.write("<td nowrap colspan=10>" + method + "</td>");
                fos.write("</tr>\n");
                double threshold = exactContainer[0].getThreshold();
                fos.write("<tr bgcolor=#DD8888>");
                fos.write("<td nowrap colspan=2>Cut off P-value:</td>");
                fos.write("<td nowrap colspan=10>" + (float) threshold + "</td>");
                fos.write("</tr>\n");
            }
        } catch (java.io.IOException ioe) {
            return;
        }
    }

    private void outDataset(ArrayList vDataset1, ArrayList vDataset2, Writer fos) {
        if (osmlContainer == null) return;
        if (exactContainer == null) return;
        try {
            fos.write("<tr bgcolor=#DDDD88>");
            fos.write("<td nowrap colspan=2>Target dataset(s):</td>");
            String data1 = "";
            for (int i = 0; i < vDataset1.size(); i++) {
                DatasetContainer dataset = (DatasetContainer) vDataset1.get(i);
                data1 += dataset.getNote() + " " + dataset.getName() + "<br>";
            }
            fos.write("<td nowrap colspan=10>" + data1 + "</td>");
            fos.write("</tr>\n");
            fos.write("<tr bgcolor=#DDDD88>");
            fos.write("<td nowrap colspan=2>Query dataset(s):</td>");
            String data2 = "";
            for (int i = 0; i < vDataset2.size(); i++) {
                DatasetContainer dataset = (DatasetContainer) vDataset2.get(i);
                data2 += dataset.getNote() + " " + dataset.getName() + "<br>";
            }
            fos.write("<td nowrap colspan=10>" + data2 + "</td>");
            fos.write("</tr>\n");
        } catch (java.io.IOException ioe) {
            return;
        }
    }

    public boolean outputXml(Writer fos) {
        if (osmlContainer == null) return false;
        if (exactContainer == null) return false;
        Hashtable hTarget1 = new Hashtable();
        Hashtable hTarget2 = new Hashtable();
        Date date0 = new Date();
        if (progressFrame != null) {
            progressFrame.setString("Initialize parameters...");
        }
        for (int i = 0; i < exactContainer.length; i++) {
            BaseContainer target1 = exactContainer[i].getFunctionalClassContainer(1);
            BaseContainer target2 = exactContainer[i].getFunctionalClassContainer(2);
            setExactTable(hTarget1, target1, exactContainer[i]);
            setExactTable(hTarget2, target2, exactContainer[i]);
        }
        ArrayList vDataset1 = new ArrayList();
        ArrayList vDataset2 = new ArrayList();
        for (int i = 0; i < exactContainer.length; i++) {
            BaseContainer target1 = exactContainer[i].getFunctionalClassContainer(1);
            BaseContainer target2 = exactContainer[i].getFunctionalClassContainer(2);
            DatasetContainer dataset1 = target1.getDatasetContainer();
            DatasetContainer dataset2 = target2.getDatasetContainer();
            if (!vDataset1.contains(dataset1)) vDataset1.add(dataset1);
            if (!vDataset2.contains(dataset2)) vDataset2.add(dataset2);
        }
        try {
            fos.write("<html>\n");
            fos.write("<body >\n");
            fos.write("<h1>OSML Functional Assignment<h1><br>\n");
            fos.write("<table border=0 bgcolor=#FFFFFF>\n");
            outInformation(fos);
            outDataset(vDataset1, vDataset2, fos);
            fos.write("<td colspan=12>" + sSpace + "</td>");
            outTable(hTarget1, 2, fos);
            fos.write("<tr bgcolor=#FFFFFF>");
            fos.write("<td colspan=12>" + sSpace + "</td>");
            fos.write("</tr>");
            outTable(hTarget2, 1, fos);
            fos.write("</table>\n");
            fos.write("</body>\n");
            fos.write("</html>\n");
            fos.close();
        } catch (java.io.IOException ioe) {
            return false;
        }
        Date date1 = new Date();
        System.out.println("Testing finished " + (date1.getTime() - date0.getTime()) / 1000 + " sec.");
        return true;
    }
}

class Vectortable extends Hashtable {

    public Object put(Object key, Object val) {
        ArrayList v = null;
        if (containsKey(key)) {
            v = (ArrayList) get(key);
        } else {
            v = new ArrayList();
        }
        v.add(val);
        return super.put(key, v);
    }
}
