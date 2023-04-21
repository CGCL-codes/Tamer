package cu.edu.cujae.biowh.parser.protein.xml;

import cu.edu.cujae.biowh.dataset.WIDFactory;
import cu.edu.cujae.biowh.parser.protein.ProteinTables;
import cu.edu.cujae.biowh.parser.protein.xml.tags.CommentTags;
import cu.edu.cujae.biowh.parser.utility.ParseFiles;
import java.util.ArrayList;
import java.util.Iterator;
import org.xml.sax.Attributes;

/**
 * This Class 
 *
 * @author rvera
 * @version 0.1
 * @since Sep 30, 2010
 * @see
 */
public class Comment extends CommentTags {

    private int depth = 0;
    private boolean open = false;
    private long WID = 0;
    private long ProteinWID = 0;
    private String name = null;
    private String mass = null;
    private String error = null;
    private String method = null;
    private String type = null;
    private String locationType = null;
    private String evidence = null;
    private EvidencedStringType text = null;
    private Location location = null;
    private String molecule = null;
    private SubCellularLocation subCellularLocation = null;
    private Conflict conflict = null;
    private ArrayList<String> link = null;
    private ArrayList<String> event = null;
    private Isoform isoform = null;
    private Interactant interactant = null;
    private String organismsDiffer = null;
    private String experiments = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet manager
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public Comment() {
        open = false;

        location = new Location();
        subCellularLocation = new SubCellularLocation();
        conflict = new Conflict();
        link = new ArrayList<>();
        event = new ArrayList<>();
        isoform = new Isoform();
        interactant = new Interactant();
    }

    /**
     * This is the endElement method for the Header on GO
     * @param name XML Tag
     * @param depth XML depth
     */
    public void endElement(String qname, int depth) {
        if (open) {
            if (depth >= this.depth + 1) {
                if (qname.equals(getTEXTFLAGS())) {
                    text.setOpen(false);
                }
                if (location.isOpen()) {
                    location.endElement(qname, depth);
                }
                if (subCellularLocation.isOpen()) {
                    subCellularLocation.endElement(qname, depth);
                }
                if (conflict.isOpen()) {
                    conflict.endElement(qname, depth);
                }
                if (isoform.isOpen()) {
                    isoform.endElement(qname, depth);
                }
                if (interactant.isOpen()) {
                    interactant.endElement(qname, depth);
                }
            }
        }
        if (qname.equals(getCOMMENTFLAGS())) {
            open = false;

            ParseFiles.printOnTSVFile(ProteinTables.PROTEINCOMMENT, WID, "\t");
            ParseFiles.printOnTSVFile(ProteinTables.PROTEINCOMMENT, ProteinWID, "\t");
            ParseFiles.printOnTSVFile(ProteinTables.PROTEINCOMMENT, name, "\t");
            ParseFiles.printOnTSVFile(ProteinTables.PROTEINCOMMENT, mass, "\t");
            ParseFiles.printOnTSVFile(ProteinTables.PROTEINCOMMENT, error, "\t");
            ParseFiles.printOnTSVFile(ProteinTables.PROTEINCOMMENT, method, "\t");
            ParseFiles.printOnTSVFile(ProteinTables.PROTEINCOMMENT, type, "\t");
            ParseFiles.printOnTSVFile(ProteinTables.PROTEINCOMMENT, locationType, "\t");
            ParseFiles.printOnTSVFile(ProteinTables.PROTEINCOMMENT, evidence, "\t");
            if (text != null) {
                ParseFiles.printOnTSVFile(ProteinTables.PROTEINCOMMENT, text.getData(), "\t");
                ParseFiles.printOnTSVFile(ProteinTables.PROTEINCOMMENT, text.getEvidence(), "\t");
                ParseFiles.printOnTSVFile(ProteinTables.PROTEINCOMMENT, text.getStatus(), "\t");
            } else {
                ParseFiles.printOnTSVFile(ProteinTables.PROTEINCOMMENT, "\\N", "\t");
                ParseFiles.printOnTSVFile(ProteinTables.PROTEINCOMMENT, "\\N", "\t");
                ParseFiles.printOnTSVFile(ProteinTables.PROTEINCOMMENT, "\\N", "\t");
            }
            ParseFiles.printOnTSVFile(ProteinTables.PROTEINCOMMENT, molecule, "\t");
            ParseFiles.printOnTSVFile(ProteinTables.PROTEINCOMMENT, organismsDiffer, "\t");
            ParseFiles.printOnTSVFile(ProteinTables.PROTEINCOMMENT, experiments, "\n");

            if (!link.isEmpty()) {
                for (Iterator<String> it = link.iterator(); it.hasNext();) {
                    ParseFiles.printOnTSVFile(ProteinTables.PROTEINCOMMENTLINK, WID, "\t");
                    ParseFiles.printOnTSVFile(ProteinTables.PROTEINCOMMENTLINK, it.next(), "\n");
                }
                link.clear();
            }

            if (!event.isEmpty()) {
                for (Iterator<String> it = event.iterator(); it.hasNext();) {
                    ParseFiles.printOnTSVFile(ProteinTables.PROTEINCOMMENTEVENT, WID, "\t");
                    ParseFiles.printOnTSVFile(ProteinTables.PROTEINCOMMENTEVENT, it.next(), "\n");
                }
                event.clear();
            }
        }
    }

    /**
     * This is the method for the Header on GO
     * @param name
     * @param depth
     */
    public void startElement(String qname, int depth, Attributes attributes, long WID) {
        if (open) {
            if (depth == this.depth + 1) {
                if (qname.equals(location.getLOCATIONFLAGS())) {
                    location.setOpen(true);
                }
                if (qname.equals(subCellularLocation.getSUBCELLULARLOCATIONFLAGS())) {
                    subCellularLocation.setOpen(true);
                }
                if (qname.equals(conflict.getCONFLICTFLAGS())) {
                    conflict.setOpen(true);
                }
                if (qname.equals(getLINKFLAGS())) {
                    link.add(attributes.getValue(getURIFLAGS()));
                }
                if (qname.equals(getEVENTFLAGS())) {
                    event.add(attributes.getValue(getTYPEFLAGS()));
                }
                if (qname.equals(isoform.getISOFORMFLAGS())) {
                    isoform.setOpen(true);
                }
                if (qname.equals(interactant.getINTERACTANTFLAGS())) {
                    interactant.setOpen(true);
                }
            }

            if (depth >= this.depth + 1) {
                if (qname.equals(getTEXTFLAGS())) {
                    text = new EvidencedStringType();
                    text.setOpen(true);
                    text.setEvidence(attributes.getValue(getEVIDENCEFLAGS()));
                    text.setStatus(attributes.getValue(getSTATUSFLAGS()));
                }
                if (location.isOpen()) {
                    location.startElement(qname, depth, attributes, this.WID);
                }
                if (subCellularLocation.isOpen()) {
                    subCellularLocation.startElement(qname, depth, attributes, this.WID);
                }
                if (conflict.isOpen()) {
                    conflict.startElement(qname, depth, attributes, this.WID);
                }
                if (isoform.isOpen()) {
                    isoform.startElement(qname, depth, attributes, this.WID);
                }
                if (interactant.isOpen()) {
                    interactant.startElement(qname, depth, attributes, this.WID);
                }
            }
        }
        if (qname.equals(getCOMMENTFLAGS())) {
            this.depth = depth;
            this.ProteinWID = WID;
            open = true;

            this.WID = WIDFactory.getWid();
            WIDFactory.increaseWid();

            name = attributes.getValue(getNAMEFLAGS());
            mass = attributes.getValue(getMASSFLAGS());
            error = attributes.getValue(getERRORFLAGS());
            method = attributes.getValue(getMETHODFLAGS());
            type = attributes.getValue(getTYPEFLAGS());
            locationType = attributes.getValue(getLOCATIONTYPEFLAGS());
            evidence = attributes.getValue(getEVIDENCEFLAGS());

            molecule = null;
            text = null;
            organismsDiffer = null;
            experiments = null;

            link.clear();
            event.clear();
        }
    }

    /**
     *
     * @param tagname
     * @param name
     * @param depth
     */
    public void characters(String tagname, String qname, int depth) {
        if (open) {
            if (depth == this.depth + 1) {
                if (tagname.equals(getTEXTFLAGS())) {
                    if (text.isOpen()) {
                        text.setData(qname);
                    }
                }
                if (tagname.equals(getMOLECULEFLAGS())) {
                    molecule = qname;
                }
                if (tagname.equals(getORGANISMSDIFFERFLAGS())) {
                    organismsDiffer = qname;
                }
                if (tagname.equals(getEXPERIMENTSFLAGS())) {
                    experiments = qname;
                }
            }
            if (depth >= this.depth + 1) {
                if (subCellularLocation.isOpen()) {
                    subCellularLocation.characters(tagname, qname, depth);
                }
                if (isoform.isOpen()) {
                    isoform.characters(tagname, qname, depth);
                }
                if (interactant.isOpen()) {
                    interactant.characters(tagname, qname, depth);
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
