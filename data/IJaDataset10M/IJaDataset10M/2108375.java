package org.oclc.da.ndiipp.packager.pvt;

import gov.loc.mets.MetsDocument;
import gov.loc.mets.MetsType;
import gov.loc.mods.v3.ModsDocument;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Vector;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.XmlCursor;
import org.oclc.da.exceptions.DAException;
import org.oclc.da.exceptions.DAExceptionCodes;
import org.oclc.da.logging.Logger;
import org.oclc.da.ndiipp.packager.data.HAndSPackagerHeaderInfo;
import org.oclc.da.ndiipp.spider.FileInfo;

/**
 * DAPackageBuilder
 *
 * This is an abstract class which has methods to build the
 * mets document which is a part of the hub and spoke package.
 *  
 * @author SR
 * @version 1.0, 
 * @created 12/11/2006
 */
public abstract class HAndSMetsBuilder {

    MetsDocument metsDoc;

    /** Logger instance. */
    private static final Logger logger = Logger.newInstance();

    /**
	 * Abstract method of creating mets documents
	 * @param objectInfo supplied object
	 * @param sipInfo supplied sip 
	 * @param fileInfoVector supplied file info collection
	 * @param modsrec supplied modsrec
	 * @param dcmetadata dcmetadata object
	 * @return Mets doc
	 * @throws DAException
	 */
    public abstract String createMETSDoc(HAndSObjectInfo objectInfo, SIPInfo sipInfo, Vector<FileInfo> fileInfoVector, String modsrec, String dcmetadata) throws DAException;

    /**
     * Creates a new <root> element for the mets document.
     * 
     * This method creates the root mets element and sets the
     * attribute values
	 * @param id id
	 * @param label label 
	 * @param objid object id
     * @return <code>MetsDocument.Mets</code> 
     * 
     */
    public MetsDocument.Mets createRootElement(String id, String label, String objid) {
        metsDoc = MetsDocument.Factory.newInstance();
        MetsDocument.Mets mets = metsDoc.addNewMets();
        mets.setID(id);
        mets.setLABEL(label);
        if (id.equals(MetsXmlConst.METS_WEBSITE)) {
            mets.setPROFILE(MetsXmlConst.WEBSITE_PROFILE);
        } else {
            mets.setPROFILE(MetsXmlConst.BASE_PROFILE);
        }
        String location = "http://www.loc.gov/METS/ http://www.loc.gov/standards/mets/mets.xsd";
        XmlCursor cursor = metsDoc.newCursor();
        if (cursor.toFirstChild()) {
            cursor.setAttributeText(new QName("http://www.w3.org/2001/XMLSchema-instance", "schemaLocation"), location);
        }
        cursor.dispose();
        return mets;
    }

    /**
     * Creates a Header element for the mets document.
     * 
     * This method creates the header element and sets the
     * attribute values
	 * @param mets mets
	 * @param headerinfo header info 
     * @return <code>MetsDocument.Mets</code> 
     * 
     */
    public MetsDocument.Mets createHeaderElement(MetsDocument.Mets mets, HAndSPackagerHeaderInfo headerinfo) {
        MetsType.MetsHdr header = mets.addNewMetsHdr();
        header.setID(headerinfo.getId());
        header.setCREATEDATE(Calendar.getInstance());
        header.setLASTMODDATE(Calendar.getInstance());
        gov.loc.mets.MetsType.MetsHdr.Agent agent = header.addNewAgent();
        agent.setROLE(MetsType.MetsHdr.Agent.ROLE.CUSTODIAN);
        agent.setTYPE(MetsType.MetsHdr.Agent.TYPE.ORGANIZATION);
        agent.setName("OCLCInc");
        gov.loc.mets.MetsType.MetsHdr.AltRecordID altRecId = header.addNewAltRecordID();
        altRecId.setTYPE(headerinfo.getAltRecType());
        altRecId.setStringValue(headerinfo.getAltRecId());
        return mets;
    }

    /**
     * Saves the mets.xml file at the tmp location
	 * @param file supplied file
     * 
     * @return <code>Absolute path of the mets file</code> 
	 * @throws DAException 
     * 
     */
    public String writeDoc(File file) throws DAException {
        try {
            metsDoc.save(file);
        } catch (IOException ioExcp) {
            DAException ex = new DAException(DAExceptionCodes.IO_ERROR, new String[] { ioExcp.getMessage() });
            logger.log(this, "writeDoc", "Unable to write mets doc", ex);
            throw ex;
        }
        return file.getAbsolutePath();
    }

    /** Closes the builder
	 * @throws DAException
	 */
    public void close() throws DAException {
        if (metsDoc != null) {
            metsDoc.setNil();
        }
    }

    /**
     * Creates a mods xml with just the title info.
	 * @param title supplied title
     * 
     * @return <code>ModsDocument</code> 
	 * @throws DAException 
     * 
     */
    public ModsDocument createMODSwithTitle(String title) throws DAException {
        ModsDocument modsDoc = ModsDocument.Factory.newInstance();
        gov.loc.mods.v3.ModsType modsType = modsDoc.addNewMods();
        String location = "http://www.loc.gov/mods/v3 http://www.loc.gov/standards/mods/v3/mods-3-2.xsd";
        XmlCursor cursor = null;
        try {
            cursor = modsDoc.newCursor();
            if (cursor.toFirstChild()) {
                cursor.setAttributeText(new QName("http://www.w3.org/2001/XMLSchema-instance", "schemaLocation"), location);
            }
            gov.loc.mods.v3.TitleInfoType infoType = modsType.addNewTitleInfo();
            org.apache.xmlbeans.XmlString titleString = infoType.addNewTitle();
            titleString.setStringValue(title);
        } finally {
            cursor.dispose();
        }
        return modsDoc;
    }
}
