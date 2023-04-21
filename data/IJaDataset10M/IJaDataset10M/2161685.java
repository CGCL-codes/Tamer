package org.infoeng.icws.documents;

import java.io.InputStream;
import java.util.Iterator;
import java.util.ArrayList;
import java.io.ByteArrayInputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.infoeng.icws.documents.VerificationCertificate;
import org.infoeng.icws.exception.SeriesException;

public class VerificationCertificateSeries {

    private int numCerts;

    private ArrayList vcs;

    private String seriesID;

    private int numBytes;

    public VerificationCertificateSeries(String vcsXML) throws Exception {
        try {
            String srsID = null;
            String ci = null;
            String sig = null;
            vcs = new ArrayList();
            numCerts = 0;
            byte[] icuBytes = vcsXML.getBytes();
            ByteArrayInputStream bais = new ByteArrayInputStream(icuBytes);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Element rootElement = db.parse(bais).getDocumentElement();
            if (!rootElement.getTagName().equalsIgnoreCase("VerificationCertificateSeries")) {
                throw new Exception("Root element name must be VerificationCertificateSeries.");
            }
            NodeList vcNL = rootElement.getElementsByTagName("VerificationCertificate");
            for (int i = 0; i < vcNL.getLength(); i++) {
                Element vcNode = (Element) vcNL.item(i);
                VerificationCertificate vc = new VerificationCertificate(vcNode);
                vcs.add(vc);
                seriesID = vc.getSeriesID();
            }
        } catch (java.io.IOException e) {
            throw new Exception("ICSa: IOException ", e);
        } catch (javax.xml.parsers.ParserConfigurationException e) {
            throw new Exception("ICSa: PC Exception ", e);
        } catch (org.xml.sax.SAXException e) {
            throw new Exception("ICSa: SAX Exception for string:\n" + vcsXML + "\n", e);
        } catch (Exception e) {
            throw new Exception("ICSa: Exception for string:\n" + vcsXML + "\n", e);
        }
    }

    public VerificationCertificateSeries(InputStream vcsIS) throws Exception {
        try {
            String srsID = null;
            String ci = null;
            String sig = null;
            vcs = new ArrayList();
            numCerts = 0;
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Element rootElement = db.parse(vcsIS).getDocumentElement();
            if (!rootElement.getTagName().equalsIgnoreCase("VerificationCertificateSeries")) {
                throw new Exception("Root element name must be VerificationCertificateSeries.");
            }
            NodeList vcNL = rootElement.getElementsByTagName("VerificationCertificate");
            for (int i = 0; i < vcNL.getLength(); i++) {
                Element vcNode = (Element) vcNL.item(i);
                VerificationCertificate vc = new VerificationCertificate(vcNode);
                vcs.add(vc);
                seriesID = vc.getSeriesID();
            }
        } catch (java.io.IOException e) {
            throw new Exception("ICSa: IOException ", e);
        } catch (javax.xml.parsers.ParserConfigurationException e) {
            throw new Exception("ICSa: PC Exception ", e);
        } catch (org.xml.sax.SAXException e) {
            throw new Exception("ICSa: SAX Exception ", e);
        } catch (Exception e) {
            throw new Exception("ICSa: Exception: ", e);
        }
    }

    /**
     * <p> Initializes an InformationCurrencySeries holder class </p>
     * 
     * @param srsID String providing the series ID. 
     * @param numberBytes The (int) number of bytes in the individual 
     *                    information currency units contained in the series. 
     * 
     **/
    public VerificationCertificateSeries(String srsID, int numberBytes) {
        numBytes = numberBytes;
        seriesID = srsID;
        numCerts = 0;
        vcs = new ArrayList();
    }

    public VerificationCertificateSeries() {
        vcs = new ArrayList();
        numCerts = 0;
    }

    public String getSeriesID() {
        return seriesID;
    }

    public VerificationCertificate[] getVCArray() {
        VerificationCertificate[] vcArray = new VerificationCertificate[vcs.size()];
        Iterator vcIt = vcs.iterator();
        int i = 0;
        while (vcIt.hasNext()) {
            vcArray[i] = (VerificationCertificate) vcIt.next();
            i++;
        }
        return vcArray;
    }

    public int getNumber() {
        return vcs.size();
    }

    public int size() {
        return vcs.size();
    }

    public int getNumberBytes() {
        return numBytes;
    }

    public void addVC(VerificationCertificate vc) throws SeriesException {
        if (seriesID == null) {
            seriesID = vc.getSeriesID();
        }
        if (vc.getSeriesID().equals(seriesID)) {
            vcs.add(vc);
            numCerts = numCerts + 1;
        } else if (!vc.getSeriesID().equals(seriesID)) {
            throw new SeriesException("The verification certificates must be of the same series!");
        }
    }

    public void addVCS(VerificationCertificateSeries newVCSeries) throws SeriesException {
        VerificationCertificate[] newVCArray = newVCSeries.getVCArray();
        for (int i = 0; i < newVCSeries.size(); i++) {
            this.addVC(newVCArray[i]);
        }
    }

    public String toString() {
        Iterator icsIT = vcs.iterator();
        String retStr = "<VerificationCertificateSeries>\n";
        if (icsIT.hasNext()) {
            while (icsIT.hasNext()) {
                VerificationCertificate icu = (VerificationCertificate) icsIT.next();
                retStr += icu.toString();
            }
        }
        retStr += "</VerificationCertificateSeries>\n";
        return retStr;
    }
}
