package uiuc.oai;

import javax.xml.transform.*;
import org.w3c.dom.*;
import org.apache.xpath.*;
import org.apache.xpath.objects.*;
import org.apache.xml.utils.*;

/**
 * This class represents a list of OAI Records, as returned by the ListRecords or ListIdentifiers OAI requests. This class is 
 *  similar to a read-only, forward-only database cursor. It will automatically handle resumptionTokens and any other flow control 
 *  used by a repository, such as HTTP redirects or retries.
 *
 * This object is returned either by the OAIRepository.ListIdentifiers or the OAIRepository.ListRecords methods.
 */
public class OAIRecordList {

    /**
	 * Constructs an empty list of OAI records.
	 */
    public OAIRecordList() {
    }

    /**
	 * Returns and OAIRecord object for the current record in the list.
	 */
    public OAIRecord getCurrentItem() {
        return recCurrent;
    }

    /**
	 * Return the index of the current item in the list (0 to CompleteListSize-1)
	 */
    public int getCurrentIndex() throws OAIException {
        return oaiResume.getIndex();
    }

    /**
	 * Return the number of records in the complete set (-1 if this number is unknown)
	 */
    public int getCompleteSize() throws OAIException {
        return oaiResume.getCompleteSize();
    }

    /**
	 * Purpose: set the current record.  This will cache the current record dom node
	 */
    private void priSetCurrentItem() throws OAIException {
        Node node = oaiResume.getItem();
        OAIRecord s = new OAIRecord();
        if (node != null) {
            s.frndSetRepository(getOAIRepository());
            s.frndSetValid(isListValid());
            if (node.getNodeName().equals("identifier")) {
                s.frndSetIdOnly(true);
                s.frndSetIdentifier(node.getFirstChild().getNodeValue());
                NamedNodeMap nmap = node.getAttributes();
                if (nmap != null) {
                    if (nmap.getNamedItem("status") != null) {
                        s.frndSetStatus(nmap.getNamedItem("status").getNodeValue());
                    }
                }
            } else if (node.getNodeName().equals("header")) {
                s.frndSetIdOnly(true);
                s.frndSetMetadataPrefix(strMetadataPrefix);
                NodeList nl = node.getChildNodes();
                for (int i = 0; i < nl.getLength(); i++) {
                    if (nl.item(i).getNodeName().equals("identifier")) {
                        s.frndSetIdentifier(nl.item(i).getFirstChild().getNodeValue());
                    } else if (nl.item(i).getNodeName().equals("datestamp")) {
                        s.frndSetDatestamp(nl.item(i).getFirstChild().getNodeValue());
                    }
                }
                NamedNodeMap nmap = node.getAttributes();
                if (nmap != null) {
                    if (nmap.getNamedItem("status") != null) {
                        s.frndSetStatus(nmap.getNamedItem("status").getNodeValue());
                    }
                }
                s.frndSetRecord(node);
            } else if (node.getNodeName().equals("record")) {
                Node n = null;
                NodeList nlist = node.getChildNodes();
                for (int i = 0; i < nlist.getLength(); i++) {
                    if (nlist.item(i).getNodeName().equals("header")) {
                        n = nlist.item(i);
                        break;
                    }
                }
                if (n != null) {
                    s.frndSetIdOnly(false);
                    s.frndSetMetadataPrefix(strMetadataPrefix);
                    nlist = n.getChildNodes();
                    for (int i = 0; i < nlist.getLength(); i++) {
                        if (nlist.item(i).getNodeName().equals("identifier") && nlist.item(i).getChildNodes().getLength() > 0) {
                            s.frndSetIdentifier(nlist.item(i).getFirstChild().getNodeValue());
                        } else if (nlist.item(i).getNodeName().equals("datestamp") && nlist.item(i).getChildNodes().getLength() > 0) {
                            s.frndSetDatestamp(nlist.item(i).getFirstChild().getNodeValue());
                        }
                    }
                    NamedNodeMap nmap = node.getAttributes();
                    if (nmap != null) {
                        if (nmap.getNamedItem("status") != null) {
                            s.frndSetStatus(nmap.getNamedItem("status").getNodeValue());
                        }
                    }
                    s.frndSetRecord(node);
                } else {
                    s = null;
                }
            } else {
                throw new OAIException(OAIException.UNKNOWN_ELEMENT_ERR, "Element " + node.getNodeName() + " is unknown");
            }
            recCurrent = s;
        } else {
            recCurrent = null;
        }
    }

    /**
	 * Returns true if the record list appears to be valid (well-formed, and if the Validation if Very Strict also valid according 
	 *  to the XML Schemas); if the Validation is Loose and the record is not well-formed, false is returned. 
	 */
    public boolean isListValid() {
        return oaiResume.isResponseValid();
    }

    /**
	 * Returns true if there are more records in the record list; else false.
	 */
    public boolean moreItems() throws OAIException {
        return oaiResume.more();
    }

    /**
	 * Moves the cursor location to the next record in the list.
	 */
    public void moveNext() throws OAIException {
        oaiResume.moveNext();
        priSetCurrentItem();
    }

    /**
	 * Returns the OAIRepository object from which this list was created.
	 */
    public OAIRepository getOAIRepository() {
        return oaiResume.getRepository();
    }

    /**
	 * This will reset the entire list to the beginning, redoing the query from scratch.
	 */
    public void requery() throws OAIException {
        oaiResume.requery();
        priSetCurrentItem();
    }

    /**
	 * Purpose: set the resumption stream associated with the list
	 */
    protected void frndSetOAIResumptionStream(OAIResumptionStream rs) throws OAIException {
        oaiResume = rs;
        priSetCurrentItem();
    }

    /**
	 * Purpose: set the metadata prefix used to build this list of records
	 */
    protected void frndSetMetadataPrefix(String meta) {
        strMetadataPrefix = meta;
    }

    private OAIRecord recCurrent;

    private String strMetadataPrefix;

    private OAIResumptionStream oaiResume;
}
