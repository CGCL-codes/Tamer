package cu.edu.cujae.biowh.parser.mif25.xml;

import cu.edu.cujae.biowh.dataset.WIDFactory;
import cu.edu.cujae.biowh.parser.mif25.MIF25Tables;
import cu.edu.cujae.biowh.parser.mif25.xml.tags.FeatureRangeTags;
import cu.edu.cujae.biowh.parser.utility.ParseFiles;
import org.xml.sax.Attributes;

/**
 * This Class handled the XML FeatureRange Tags on MIF25
 *
 * @author rvera
 * @version 0.1
 * @since Oct 29, 2010
 * @see
 */
public class FeatureRange extends FeatureRangeTags {

    private int depth = 0;

    private boolean open = false;

    private long WID = 0;

    private long MIFeatureWID = 0;

    private boolean startStatus = false;

    private Names startStatusNames = null;

    private Xref startStatusXref = null;

    private boolean endStatus = false;

    private Names endStatusNames = null;

    private Xref endStatusXref = null;

    private String beginPosition = null;

    private String beginIntervalBegin = null;

    private String beginIntervalEnd = null;

    private String endPosition = null;

    private String endIntervalBegin = null;

    private String endIntervalEnd = null;

    private boolean isLink = false;

    /**
     * This constructor initialize the WH file manager and the WH DataSet manager
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public FeatureRange() {
        open = false;
        startStatusNames = new Names();
        startStatusXref = new Xref();
        endStatusNames = new Names();
        endStatusXref = new Xref();
    }

    /**
     * This is the endElement method
     *
     * @param qname the tags name
     * @param depth XML depth
     */
    public void endElement(String qname, int depth) {
        if (open) {
            if (depth >= this.depth + 2) {
                if (startStatus) {
                    startStatus = false;
                    if (startStatusNames.isOpen()) {
                        startStatusNames.endElement(qname, depth, WID, MIF25Tables.MIFOTHERALIAS);
                    }
                    if (startStatusXref.isOpen()) {
                        startStatusXref.endElement(qname, depth);
                    }
                }
                if (endStatus) {
                    endStatus = false;
                    if (endStatusNames.isOpen()) {
                        endStatusNames.endElement(qname, depth, WID, MIF25Tables.MIFOTHERALIAS);
                    }
                    if (endStatusXref.isOpen()) {
                        endStatusXref.endElement(qname, depth);
                    }
                }
            }
        }
        if (qname.equals(getFEATURERANGEFLAGS())) {
            open = false;
            ParseFiles.printOnTSVFile(MIF25Tables.MIFFEATUREFEATURERANGE, WID, "\t");
            ParseFiles.printOnTSVFile(MIF25Tables.MIFFEATUREFEATURERANGE, MIFeatureWID, "\t");
            ParseFiles.printOnTSVFile(MIF25Tables.MIFFEATUREFEATURERANGE, startStatusNames.getShortLabel(), "\t");
            ParseFiles.printOnTSVFile(MIF25Tables.MIFFEATUREFEATURERANGE, startStatusNames.getFullName(), "\t");
            ParseFiles.printOnTSVFile(MIF25Tables.MIFFEATUREFEATURERANGE, endStatusNames.getShortLabel(), "\t");
            ParseFiles.printOnTSVFile(MIF25Tables.MIFFEATUREFEATURERANGE, endStatusNames.getFullName(), "\t");
            ParseFiles.printOnTSVFile(MIF25Tables.MIFFEATUREFEATURERANGE, beginPosition, "\t");
            ParseFiles.printOnTSVFile(MIF25Tables.MIFFEATUREFEATURERANGE, beginIntervalBegin, "\t");
            ParseFiles.printOnTSVFile(MIF25Tables.MIFFEATUREFEATURERANGE, beginIntervalEnd, "\t");
            ParseFiles.printOnTSVFile(MIF25Tables.MIFFEATUREFEATURERANGE, endPosition, "\t");
            ParseFiles.printOnTSVFile(MIF25Tables.MIFFEATUREFEATURERANGE, endIntervalBegin, "\t");
            ParseFiles.printOnTSVFile(MIF25Tables.MIFFEATUREFEATURERANGE, endIntervalEnd, "\t");
            ParseFiles.printOnTSVFile(MIF25Tables.MIFFEATUREFEATURERANGE, isLink, "\n");
        }
    }

    /**
     * This is the startElement method
     *
     * @param qname the tags name
     * @param depth the depth on the XML file
     * @param attributes tag's attributes
     */
    public void startElement(String qname, int depth, Attributes attributes, long MIFParticipantWID) {
        if (open) {
            if (depth == this.depth + 1) {
                if (qname.equals(getSTARTSTATUSFLAGS())) {
                    startStatus = true;
                }
                if (qname.equals(getENDSTATUSFLAGS())) {
                    endStatus = true;
                }
                if (qname.equals(getBEGINFLAGS())) {
                    beginPosition = attributes.getValue(getPOSITIONFLAGS());
                }
                if (qname.equals(getBEGININTERVALFLAGS())) {
                    beginIntervalBegin = attributes.getValue(getBEGINFLAGS());
                    beginIntervalEnd = attributes.getValue(getENDFLAGS());
                }
                if (qname.equals(getENDFLAGS())) {
                    endPosition = attributes.getValue(getPOSITIONFLAGS());
                }
                if (qname.equals(getENDINTERVALFLAGS())) {
                    endIntervalBegin = attributes.getValue(getBEGINFLAGS());
                    endIntervalEnd = attributes.getValue(getENDFLAGS());
                }
            }
            if (depth == this.depth + 2) {
                if (startStatus) {
                    if (qname.equals(startStatusNames.getNAMESFLAGS())) {
                        startStatusNames.setOpen(true);
                    }
                    if (qname.equals(startStatusXref.getXREFFLAGS())) {
                        startStatusXref.setOpen(true);
                    }
                }
                if (endStatus) {
                    if (qname.equals(endStatusNames.getNAMESFLAGS())) {
                        endStatusNames.setOpen(true);
                    }
                    if (qname.equals(endStatusXref.getXREFFLAGS())) {
                        endStatusXref.setOpen(true);
                    }
                }
            }
            if (depth >= this.depth + 2) {
                if (startStatus) {
                    if (startStatusNames.isOpen()) {
                        startStatusNames.startElement(qname, depth, attributes);
                    }
                    if (startStatusXref.isOpen()) {
                        startStatusXref.startElement(qname, depth, attributes, WID);
                    }
                }
                if (endStatus) {
                    if (endStatusNames.isOpen()) {
                        endStatusNames.startElement(qname, depth, attributes);
                    }
                    if (endStatusXref.isOpen()) {
                        endStatusXref.startElement(qname, depth, attributes, WID);
                    }
                }
            }
        }
        if (qname.equals(getFEATURERANGEFLAGS())) {
            this.depth = depth;
            open = true;
            WID = WIDFactory.getWid();
            WIDFactory.increaseWid();
            this.MIFeatureWID = MIFParticipantWID;
            startStatus = false;
            endStatus = false;
            startStatusNames.setOpen(false);
            startStatusXref.setOpen(false);
            endStatusNames.setOpen(false);
            endStatusXref.setOpen(false);
            beginPosition = null;
            beginIntervalBegin = null;
            beginIntervalEnd = null;
            endPosition = null;
            endIntervalBegin = null;
            endIntervalEnd = null;
            isLink = false;
        }
    }

    /**
     * This is the characters method
     *
     * @param tagname the tags name
     * @param qname text value
     * @param depth the depth on the XML file
     */
    public void characters(String tagname, String qname, int depth) {
        if (open) {
            if (depth == this.depth + 1) {
                if (tagname.equals(getISLINKFLAGS())) {
                    if (qname.equals("true")) {
                        isLink = true;
                    }
                }
            }
            if (depth >= this.depth + 2) {
                if (startStatus) {
                    if (startStatusNames.isOpen()) {
                        startStatusNames.characters(tagname, qname, depth);
                    }
                    if (startStatusXref.isOpen()) {
                        startStatusXref.characters(tagname, qname, depth);
                    }
                }
                if (endStatus) {
                    if (endStatusNames.isOpen()) {
                        endStatusNames.characters(tagname, qname, depth);
                    }
                    if (endStatusXref.isOpen()) {
                        endStatusXref.characters(tagname, qname, depth);
                    }
                }
            }
        }
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
