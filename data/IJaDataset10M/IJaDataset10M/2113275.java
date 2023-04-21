package org.maveryx.utils.xml.diff;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class XmlDiff {

    private int idnumbers = 0;

    private Vector v1 = new Vector();

    private Vector v2 = new Vector();

    static boolean debug = false;

    public static void Usage(String msg) {
        System.out.println(msg);
        System.out.println();
        System.out.println("XmlDiff 0.1");
        System.out.println("Usage: XmlDiff {options} xmlfile1 xmlfile2");
        System.out.println("Options:");
        System.out.println("  -D         Print debugging information");
        System.out.println();
        System.exit(1);
    }

    public XmlDiff(String xml1, String xml2) throws JDOMException, IOException {
        this(new File(xml1), new File(xml2));
    }

    public XmlDiff(Document xml1, Document xml2) throws JDOMException {
        if (debug) System.out.println("getting DOM..." + xml1 + " and " + xml2);
        buildConceptVector(v1, new Concept(), xml1.getRootElement());
        buildConceptVector(v2, new Concept(), xml2.getRootElement());
    }

    public XmlDiff(File xml1, File xml2) throws JDOMException, IOException {
        this((new SAXBuilder()).build(xml1), (new SAXBuilder()).build(xml2));
    }

    public PrecisionRecall positionalSimilarity(boolean soft) {
        double AUB = 0;
        double A = v1.size();
        double B = v2.size();
        setUnsigned();
        Vector v = new Vector();
        for (int i = 0; i < v1.size(); i++) {
            VectorCouple vc;
            if (!((Concept) v1.get(i)).isSigned()) {
                Concept c = (Concept) v1.get(i);
                vc = new VectorCouple();
                c.setSign(true);
                vc.addObjectV1(c);
                boolean added = false;
                for (int j = 0; j < v2.size(); j++) {
                    if (!((Concept) v2.get(j)).isSigned()) if (c.rootPathIsEqual((Concept) v2.get(j)) && c.contentIsEqual((Concept) v2.get(j))) {
                        vc.addObjectV2((Concept) v2.get(j));
                        added = true;
                        ((Concept) v2.get(j)).setSign(true);
                    }
                }
                for (int k = i + 1; k < v1.size(); k++) {
                    if (c.rootPathIsEqual((Concept) v1.get(k)) && c.contentIsEqual((Concept) v1.get(k))) {
                        vc.addObjectV1((Concept) v1.get(k));
                        ((Concept) v1.get(k)).setSign(true);
                    }
                }
                if (vc.getV1().size() > 0 && vc.getV2().size() > 0) {
                    v.add(vc);
                }
            }
        }
        if (debug) {
            System.out.println("fase iniziale size=" + String.valueOf(v.size()));
            for (int i = 0; i < v.size(); i++) {
                VectorCouple vc = (VectorCouple) v.get(i);
                System.out.println(vc);
            }
        }
        while (true) {
            if (debug) {
                System.out.println("prima di spunta ed eliminadoppioniV1, size=" + String.valueOf(v.size()));
                for (int i = 0; i < v.size(); i++) {
                    VectorCouple vc = (VectorCouple) v.get(i);
                    System.out.println(vc);
                }
            }
            int max = 0;
            for (int i = 0; i < v.size(); i++) {
                VectorCouple vc = (VectorCouple) v.get(i);
                vc.spunta();
                vc.eliminaDoppioniV1();
                int m = vc.maxPathLength();
                if (m > max) max = m;
            }
            if (debug) {
                System.out.println("dopo spunta ed eliminadoppioniV1, size=" + String.valueOf(v.size()));
                for (int i = 0; i < v.size(); i++) {
                    VectorCouple vc = (VectorCouple) v.get(i);
                    System.out.println(vc);
                }
            }
            if (max <= 0) break;
            Vector newv = new Vector();
            for (int i = 0; i < v.size(); i++) {
                VectorCouple vci = (VectorCouple) v.get(i);
                if (!vci.isSigned()) {
                    for (int t = 0; t < vci.getV1().size(); t++) {
                        VectorCouple newvc = new VectorCouple();
                        newvc.addObjectV1(((Concept) vci.getV1().get(t)).clone());
                        for (int l = 0; l < vci.getV2().size(); l++) {
                            newvc.addObjectV2(((Concept) vci.getV2().get(l)).clone());
                            newvc.addPathNumber(vci.getPathNumber() / (vci.getV2().size() * vci.getV1().size()));
                        }
                        boolean conaggregazione = false;
                        for (int j = i + 1; j < v.size(); j++) {
                            VectorCouple vcj = (VectorCouple) v.get(j);
                            if (!vcj.isSigned()) {
                                int numc = vcj.V1removeConcept((Concept) vci.getV1().get(t));
                                if (numc > 0) {
                                    conaggregazione = false;
                                    for (int l = 0; l < vcj.getV2().size(); l++) {
                                        newvc.addObjectV2(((Concept) vcj.getV2().get(l)).clone());
                                        newvc.addPathNumber(vcj.getPathNumber() / (vcj.getV2().size() * vci.getV1().size()));
                                    }
                                }
                            }
                        }
                        newvc.eliminaDoppioniV2(conaggregazione);
                        newv.add(newvc);
                    }
                }
            }
            v = newv;
            if (debug) {
                System.out.println("dopo l'aggregazione con elimina doppioniV2 size=" + String.valueOf(v.size()));
                for (int i = 0; i < v.size(); i++) {
                    VectorCouple vc = (VectorCouple) v.get(i);
                    System.out.println(vc);
                }
            }
        }
        for (int i = 0; i < v.size(); i++) {
            VectorCouple vc = (VectorCouple) v.get(i);
            AUB += vc.getPathNumber();
        }
        return new PrecisionRecall(AUB / B, AUB / A);
    }

    public PrecisionRecall structuralSimilarity(boolean soft) {
        double AUB = 0;
        double A = v1.size();
        double B = v2.size();
        setUnsigned();
        for (int i = 0; i < v1.size(); i++) {
            for (int j = 0; j < v2.size(); j++) {
                if (((Concept) v1.get(i)).rootPathIsEqual((Concept) v2.get(j))) {
                    if (!((Concept) v2.get(j)).isSigned()) {
                        AUB++;
                        ((Concept) v2.get(j)).setSign(true);
                        ((Concept) v1.get(i)).setSign(true);
                        j = v2.size();
                    }
                }
            }
        }
        if (soft) for (int i = 0; i < v1.size(); i++) {
            if (!((Concept) v1.get(i)).isSigned()) for (int j = 0; j < v2.size(); j++) {
                if (!((Concept) v2.get(j)).isSigned()) {
                    AUB += ((Concept) v1.get(i)).matchPath((Concept) v2.get(j));
                    ((Concept) v2.get(j)).setSign(true);
                    ((Concept) v1.get(i)).setSign(true);
                    j = v2.size();
                }
            }
        }
        return new PrecisionRecall(AUB / B, AUB / A);
    }

    public PrecisionRecall contentSimilarity(boolean soft) {
        double AUB = 0;
        double A = v1.size();
        double B = v2.size();
        setUnsigned();
        for (int i = 0; i < v1.size(); i++) {
            for (int j = 0; j < v2.size(); j++) {
                if (((Concept) v1.get(i)).contentIsEqual((Concept) v2.get(j))) {
                    if (!((Concept) v2.get(j)).isSigned()) {
                        AUB++;
                        ((Concept) v2.get(j)).setSign(true);
                        ((Concept) v1.get(i)).setSign(true);
                        j = v2.size();
                    }
                }
            }
        }
        if (soft) for (int i = 0; i < v1.size(); i++) {
            if (!((Concept) v1.get(i)).isSigned()) for (int j = 0; j < v2.size(); j++) {
                if (!((Concept) v2.get(j)).isSigned()) {
                    AUB += ((Concept) v1.get(i)).matchContent((Concept) v2.get(j));
                    ((Concept) v2.get(j)).setSign(true);
                    ((Concept) v1.get(i)).setSign(true);
                    j = v2.size();
                }
            }
        }
        return new PrecisionRecall(AUB / B, AUB / A);
    }

    public void setUnsigned() {
        for (int i = 0; i < v1.size(); i++) ((Concept) v1.get(i)).setSign(false);
        for (int i = 0; i < v2.size(); i++) ((Concept) v2.get(i)).setSign(false);
    }

    public void print() {
        for (int i = 0; i < v1.size(); i++) System.out.println((Concept) v1.get(i));
        System.out.println();
        for (int i = 0; i < v2.size(); i++) System.out.println((Concept) v2.get(i));
    }

    private void buildConceptVector(Vector v, Concept c, Element e) {
        int currentid;
        List<Element> le = e.getChildren();
        if (!le.isEmpty()) {
            currentid = idnumbers++;
            for (int i = 0; i < le.size(); i++) buildConceptVector(v, c.cloneConcept(new TreeNode(currentid, e.getName())), (Element) e.getChildren().get(i));
        } else {
            c.addCategory(new TreeNode(idnumbers++, e.getName()));
            c.setContent(e.getText());
            c.setElement(e);
            v.add(c);
        }
    }

    public double getSimilarity() {
        double retVal;
        double prec;
        double rec;
        double fMstruct;
        double fMcont;
        prec = structuralSimilarity(false).getPrecision();
        rec = structuralSimilarity(false).getRecall();
        fMstruct = calculateFMeasure(prec, rec);
        System.out.println("Structural ");
        System.out.println(" p      : " + prec);
        System.out.println(" r      : " + rec);
        System.out.println(" fMesure: " + fMstruct);
        prec = contentSimilarity(false).getPrecision();
        rec = contentSimilarity(false).getRecall();
        fMcont = calculateFMeasure(prec, rec);
        System.out.println("Content ");
        System.out.println(" p      : " + prec);
        System.out.println(" r      : " + rec);
        System.out.println(" fMesure: " + fMcont);
        retVal = (fMstruct + fMcont) / 2;
        return retVal;
    }

    private double calculateFMeasure(double p, double r) {
        if ((p == 0) || (r == 0)) return 0.0;
        return (2 * (p * r)) / (p + r);
    }

    public static void main(String args[]) {
        try {
            String nameA;
            String nameB;
            nameA = "iup2_7_StateFrIFrIFr.xml";
            nameB = "repository\\iup2_3_Repository.xml";
            XmlDiff xd = new XmlDiff(nameA, nameB);
            PrecisionRecall prs = xd.structuralSimilarity(false);
            PrecisionRecall prc = xd.contentSimilarity(false);
            PrecisionRecall prp = xd.positionalSimilarity(false);
            PrecisionRecall prs2 = xd.structuralSimilarity(true);
            PrecisionRecall prc2 = xd.contentSimilarity(true);
            PrecisionRecall prp2 = xd.positionalSimilarity(true);
            System.out.println("fileA: " + nameA + "; fileB: " + nameB);
            System.out.println("Structural similarity P=" + prs.getPrecision() + ", R=" + prs.getRecall() + "; P=" + prs2.getPrecision() + ", R=" + prs2.getRecall());
            System.out.println("Content similarity P=" + prc.getPrecision() + ", R=" + prc.getRecall() + "; P=" + prc2.getPrecision() + ", R=" + prc2.getRecall());
            System.out.println("Positional similarity P=" + prp.getPrecision() + ", R=" + prp.getRecall() + "; P=" + prp2.getPrecision() + ", R=" + prp2.getRecall());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
