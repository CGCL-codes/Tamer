package org.mged.annotare.validator;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.mged.magetab.error.ErrorCode;
import org.mged.magetab.error.ErrorItem;
import org.mged.magetab.error.ErrorItemFactory;
import org.tigr.microarray.mev.file.StringSplitter;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.IDF;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.MAGETABInvestigation;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.SDRF;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.ArrayDataMatrixNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.ArrayDataNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.ArrayDesignNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.AssayNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.DerivedArrayDataMatrixNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.DerivedArrayDataNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.ExtractNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.HybridizationNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.ImageNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.LabeledExtractNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.NormalizationNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.ProtocolNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.SampleNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.ScanNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.SourceNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.attribute.CharacteristicsAttribute;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.attribute.FactorValueAttribute;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.attribute.ParameterValueAttribute;
import uk.ac.ebi.arrayexpress2.magetab.exception.ValidateException;
import uk.ac.ebi.arrayexpress2.magetab.validator.AbstractValidator;

public class SemanticValidator extends AbstractValidator<MAGETABInvestigation> {

    MAGETABInvestigation mti;

    AnnotareError annError;

    ErrorItemFactory eif;

    String tsrStr = null;

    String idfFileName;

    String sdrfFileName;

    boolean pass = false;

    boolean testDebug = false;

    boolean ontologyVal = false;

    boolean dataVal = true;

    Hashtable idfMap;

    Hashtable sdrfMap;

    ArrayList idfHeaders = new ArrayList();

    ArrayList sdrfHeaders = new ArrayList();

    /**
	 * Constructor call with MAGETABInvestigation object; normal proc
	 * Sets up validation by calling super and getting a copy of MAGETABInvestigation
	 * @param investigation
	 * @return success or failure
	 */
    public SemanticValidator(AnnotareError errorListObj) {
        this();
    }

    private SemanticValidator() {
        super();
        this.eif = ErrorItemFactory.getErrorItemFactory();
    }

    public SemanticValidator(String idfName) {
        this();
        this.idfFileName = idfName;
        this.idfMap = testReadFile(idfName, idfMap);
    }

    Hashtable testReadFile(String fileName, Hashtable map) {
        map = new Hashtable();
        readFile(fileName, map);
        Enumeration e = map.keys();
        Point key;
        while (e.hasMoreElements()) {
            key = (Point) e.nextElement();
            String value = (String) map.get(key);
            char carriageReturn = 0x0D;
            value = value.replaceAll(Character.toString(carriageReturn), " ");
        }
        return map;
    }

    /**
	 * setDataValOn / setDataValOff
	 * Activates / Deactivates Data file validation
	 * dataVal = true by default
	 */
    public void setDataValOn() {
        this.dataVal = true;
    }

    public void setDataValOff() {
        this.dataVal = false;
    }

    /**
	 * setOntologyValOn / setOntologyValOff
	 * Activates / Deactivates external ontology reference checking
	 * ontologyVal is false by default 
	 */
    public void setOntologyValOn() {
        this.ontologyVal = true;
    }

    public void setOntologyValOff() {
        this.ontologyVal = false;
    }

    /**
	 * Returns success of last validation process
	 * @return class member 'pass'
	 */
    public boolean getValidateSuccess() {
        return pass;
    }

    /**
	 * read idf/sdrf files and fill hashtable with line numbers
	 * @param: filename
	 * @param: hashtable to use
	 * @return: void
	 */
    private Hashtable readFile(String fileName, Hashtable fileMap) {
        int dataColumn = 0;
        int ctr = 0;
        BufferedReader br;
        String currentLine;
        try {
            br = new BufferedReader(new FileReader(fileName));
            StringSplitter ss = new StringSplitter((char) 0x09);
            ArrayList ch = new ArrayList();
            ArrayList rh = new ArrayList();
            while ((currentLine = br.readLine()) != null) {
                ss.init(currentLine);
                ctr++;
                int fctr = 0;
                while (ss.hasMoreTokens()) {
                    String item = ss.nextToken();
                    fctr++;
                    if (ctr == 1) ch.add(item);
                    if (fctr == 1) rh.add(item);
                    if (item != null) {
                        Point cell = new Point(ctr, fctr);
                        fileMap.put(cell, item);
                        dataColumn = fctr;
                    }
                }
            }
            if (fileName.contains("sdrf") || fileName.contains("SDRF")) {
                sdrfHeaders.addAll(ch);
            } else {
                idfHeaders.addAll(rh);
            }
            br.close();
        } catch (FileNotFoundException fnfe) {
            System.out.println("File not found: " + fileName);
        } catch (IOException ioe) {
            System.out.println("Failed to read file: " + fileName);
        }
        return fileMap;
    }

    /**
	 * Return the list of validation errors from content validation
	 * Use AnnotareError for the time being; may switch later.
	 * @return ArrayList of ErrorItem objects
	 */
    public ArrayList<ErrorItem> getErrors() {
        ArrayList<ErrorItem> errorList = annError.getErrorList();
        return errorList;
    }

    /**
	 * Validate by running a series of checks on IDF, SDRF 
	 * and Refs (ADF in future)
	 */
    public boolean validate(MAGETABInvestigation investigation) throws ValidateException {
        try {
            if (this.idfFileName == null) {
                throw new NullPointerException();
            }
        } catch (NullPointerException npe) {
            System.out.println("IDF file name is null.  Code line SemanticValidator:238.");
            createEvent("IDF file name is null.  Code line SemanticValidator:238.", 999);
            npe.printStackTrace();
            return false;
        }
        System.out.println("Semantic validation: validate method");
        this.mti = investigation;
        List<String> sdrfFileList = mti.IDF.sdrfFile;
        for (int i = 0; i < sdrfFileList.size(); i++) {
            this.sdrfFileName = sdrfFileList.get(0);
        }
        String spath = idfFileName.substring(0, this.idfFileName.lastIndexOf(File.separatorChar));
        this.sdrfMap = testReadFile(spath + File.separatorChar + this.sdrfFileName, sdrfMap);
        List<String> tsrList = mti.IDF.termSourceName;
        this.tsrStr = tsrList.toString();
        boolean success = false;
        boolean failIDF = false;
        boolean failSDRF = false;
        boolean failRefs = false;
        if (mti.IDF != null) failIDF = checkIDF(mti.IDF);
        if (mti.SDRF != null && mti.IDF.sdrfFile != null) failSDRF = checkSDRF(mti.SDRF);
        if (mti.SDRF != null && mti.IDF != null) failRefs = checkRefs(mti);
        if (failIDF == false && failSDRF == false && failRefs == false) {
            success = true;
        }
        System.out.println("Semantic validation: validate method check complete");
        return success;
    }

    /**
	 * Checks IDF groups of tags:
	 * Experiment
	 * Person
	 * Submission
	 * QC
	 * Publication
	 * Protocol
	 * Term Source
	 * 
	 * @param idf
	 * @return true if any failure occurs
	 */
    protected boolean checkIDF(IDF idf) {
        boolean fail = false;
        boolean rv = false;
        fail = checkTextTag("Investigation Title", idf.investigationTitle);
        if (fail == true) rv = true;
        fail = checkDateTag("Date Of Experiment", idf.dateOfExperiment);
        if (fail == true) rv = true;
        fail = checkDateTag("Public Release Date", idf.publicReleaseDate);
        if (fail == true) rv = true;
        fail = checkTextTag("Experiment Description", idf.experimentDescription);
        if (fail == true) rv = true;
        fail = checkArrayTag("SDRF file", idf.sdrfFile);
        if (fail == true) rv = true;
        fail = checkTermSources("Term Source", idf.termSourceName, idf.termSourceFile, idf.termSourceVersion);
        if (fail == true) rv = true;
        fail = checkTagAndTermSource("Experimental Design", idf.experimentalDesign, idf.experimentalDesignTermSourceREF);
        if (fail == true) rv = true;
        fail = checkTagAndTermSource("Experimental Factor", idf.experimentalFactorName, idf.experimentalFactorType, idf.experimentalFactorTermSourceREF);
        if (fail == true) rv = true;
        fail = checkPerson("Person", idf);
        if (fail == true) rv = true;
        if (idf.personRoles.contains(";")) {
        } else {
            checkTagAndTermSource("Person Roles", idf.personRoles, idf.personRolesTermSourceREF);
        }
        if (fail == true) rv = true;
        fail = checkTagAndTermSource("Quality Control Type", idf.qualityControlType, idf.qualityControlTermSourceREF);
        if (fail == true) rv = true;
        fail = checkTagAndTermSource("Replicate Type", idf.replicateType, idf.replicateTermSourceREF);
        if (fail == true) rv = true;
        fail = checkTagAndTermSource("Normalization Type", idf.normalizationType, idf.normalizationTermSourceREF);
        if (fail == true) rv = true;
        fail = checkPub("Publication", idf);
        if (fail == true) rv = true;
        fail = checkTagAndTermSource("Publication Status", idf.publicationStatus, idf.publicationStatusTermSourceREF);
        if (fail == true) rv = true;
        fail = checkIDFProtocol("Protocol", idf);
        if (fail == true) rv = true;
        fail = checkTagAndTermSource("Protocol Type", idf.protocolType, idf.protocolTermSourceREF);
        if (fail == true) rv = true;
        return rv;
    }

    /**
	 * checkTextTag
	 * 
	 */
    private boolean checkTextTag(String label, String tag) {
        boolean check = false;
        if (tag.equals("")) {
            Point p = findCell(label, idfMap);
            createEvent("IDF date tag " + label + " is empty", 1015, "validation error", idfFileName, p.x, p.y + 2);
        }
        return check;
    }

    /**
	 * checkDateTag
	 * 
	 */
    private boolean checkDateTag(String label, String tag) {
        boolean check = false;
        if (tag.equals("")) {
            Point p = findCell(label, idfMap);
            createEvent("IDF date tag " + label + " is empty", 1015, "validation error", idfFileName, p.x, p.y + 2);
        } else {
            String inDate = tag;
            Pattern dateFormat = Pattern.compile("\\d\\d\\d\\d-\\d\\d-\\d\\d");
            Matcher dateMatch = dateFormat.matcher(inDate);
            if (!dateMatch.find()) {
                Point p = findCell(tag, idfMap);
                createEvent("Incorrect date format for " + label + ": " + tag + "; use format: YYYY-MM-DD", 1008, "validation error", idfFileName, p.x, p.y + 2);
                check = true;
            }
        }
        return check;
    }

    /**
	 * checkPerson
	 * specialized method for Person tags
	 * 
	 */
    private boolean checkPerson(String label, IDF idf) {
        boolean check = false;
        int people = 0;
        if (idf.personLastName == null) {
            createEvent("Warning: required IDF tag 'personLastName' is missing", 24);
            check = true;
        } else {
            people = idf.personLastName.size();
            for (int i = 0; i < people; i++) {
                String lastName = idf.personLastName.get(i);
                Point p = findCell(lastName, idfMap);
                if (lastName.equals("")) createEvent("Warning: lastName in IDF column " + (i + 1) + " is missing", 24, "validation error", idfFileName, p.x, p.y);
                check = true;
            }
        }
        if (idf.personEmail == null) {
            createEvent("Error: tag 'personEmail' in IDF is missing", 24);
            check = true;
        } else {
            if (idf.personEmail.size() < 1) {
                createEvent("Error: At least one Email address must be provided in IDF", 24);
                check = true;
            } else if (idf.personEmail.size() < people) {
                for (int i = 0; i < idf.personEmail.size(); i++) {
                    if (idf.personEmail.get(i) == null) {
                        Point p = findCell(idf.personLastName.get(i), idfMap);
                        createEvent("Warning: Email address for " + idf.personLastName.get(i) + " in IDF is missing", 1015, "validation warning", idfFileName, p.x, i + 2);
                    }
                }
            }
        }
        if (idf.personRoles == null) {
            createEvent("Warning: IDF tag 'personRole' is missing", 24);
            check = true;
        } else {
            if (idf.personRoles.size() < 1) {
                Point p = findCell("Person Roles", idfMap);
                createEvent("Error: At least one Role must be provided in IDF", 24, "validation error", idfFileName, p.x, p.y);
                check = true;
            } else if (idf.personRoles.size() < people) {
                for (int i = 0; i < idf.personRoles.size(); i++) {
                    if (idf.personRoles.get(i) == null) {
                        String role = idf.personRoles.get(i);
                        Point p = findCell(role, idfMap);
                        createEvent("Warning: Role for " + idf.personLastName.get(i) + " in IDF is missing", 24, "validation error", idfFileName, p.x, p.y);
                        check = true;
                    }
                }
            }
            boolean found = false;
            for (String s : idf.personRoles) {
                if (s.equalsIgnoreCase("submitter")) {
                    found = true;
                    break;
                }
            }
            if (found == false) {
                Point p = findCell("Person Roles", idfMap);
                createEvent("Warning: IDF: at least one Person must have Role = 'submitter'", 24, "validation error", idfFileName, p.x, p.y);
                check = true;
            }
        }
        Hashtable tmp = new Hashtable();
        if (idf.personLastName != null) tmp.put("Person Last Name", idf.personLastName);
        if (idf.personFirstName != null) tmp.put("Person First Name", idf.personFirstName);
        if (idf.personMidInitials != null) tmp.put("Person Mid Initials", idf.personMidInitials);
        if (idf.personEmail != null) tmp.put("Person Email", idf.personEmail);
        if (idf.personPhone != null) tmp.put("Person Phone", idf.personPhone);
        if (idf.personFax != null) tmp.put("Person Fax", idf.personFax);
        if (idf.personAddress != null) tmp.put("Person Address", idf.personAddress);
        if (idf.personAffiliation != null) tmp.put("Person Affiliation", idf.personAffiliation);
        if (idf.personRoles != null) tmp.put("Person Roles", idf.personRoles);
        if (idf.personRolesTermSourceREF != null) tmp.put("Person Roles Term Source REF", idf.personRolesTermSourceREF);
        Enumeration e = tmp.keys();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            ArrayList<String> al = (ArrayList) tmp.get(key);
            String lastName = "";
            int j = 0;
            try {
                for (j = 0; j < people; j++) {
                    lastName = idf.personLastName.get(j);
                    String s = al.get(j);
                    if (s == null || s.equals("")) {
                        Point p = findCell(key, idfMap);
                        createEvent("Incomplete information in IDF for " + lastName + "; " + key + " is empty", 1015, "validation warning", idfFileName, p.x, j + 2);
                    }
                }
            } catch (NullPointerException npe) {
                Point p = findCell(key, idfMap);
                createEvent("Incomplete information in IDF for " + lastName + "; " + key + " is empty", 1015, "validation warning", idfFileName, p.x, j + 2);
            } catch (IndexOutOfBoundsException iob) {
                Point p = findCell(key, idfMap);
                createEvent("Incomplete information in IDF for " + lastName + "; " + key + " is empty", 1015, "validation warning", idfFileName, p.x, j + 2);
            }
        }
        return check;
    }

    /**
	 * check publication attributes for missing values
	 */
    private boolean checkPub(String label, IDF idf) {
        boolean check = false;
        int numDocs = 0;
        if (idf.pubMedId != null && idf.publicationDOI != null) {
            numDocs = (idf.pubMedId.size() >= idf.publicationDOI.size()) ? idf.pubMedId.size() : idf.publicationDOI.size();
        } else if (idf.pubMedId != null) {
            numDocs = idf.pubMedId.size();
        } else if (idf.publicationDOI != null) {
            numDocs = idf.publicationDOI.size();
        } else {
            createEvent("Information: IDF, no document identifier information was supplied", 1015, "validation warning");
        }
        Hashtable tmp = new Hashtable();
        if (idf.pubMedId != null) tmp.put("PubMed Id", idf.pubMedId);
        if (idf.publicationDOI != null) tmp.put("Publication DOI", idf.publicationDOI);
        if (idf.publicationAuthorList != null) tmp.put("Publication AuthorList", idf.publicationAuthorList);
        if (idf.publicationTitle != null) tmp.put("Publication Title", idf.publicationTitle);
        if (idf.publicationStatus != null) tmp.put("Publication Status", idf.publicationStatus);
        if (idf.publicationStatusTermSourceREF != null) tmp.put("Publication Status Term Source REF", idf.publicationStatusTermSourceREF);
        Enumeration e = tmp.keys();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            ArrayList<String> al = (ArrayList) tmp.get(key);
            int j = 0;
            try {
                for (j = 0; j < numDocs; j++) {
                    String s = al.get(j);
                    if (s == null || s.equals("")) {
                        Point p = findCell(key, idfMap);
                        createEvent("Incomplete information in IDF: " + key + " is empty", 1015, "validation warning", idfFileName, p.x, j + 2);
                    }
                }
            } catch (NullPointerException npe) {
                Point p = findCell(key, idfMap);
                createEvent("Incomplete information in IDF: " + key + " is empty", 1015, "validation warning", idfFileName, p.x, j + 2);
            } catch (IndexOutOfBoundsException iob) {
                Point p = findCell(key, idfMap);
                createEvent("Incomplete information in IDF: " + key + " is empty", 1015, "validation warning", idfFileName, p.x, j + 2);
            }
        }
        return check;
    }

    /**
	 * check Protocol objects for missing values
	 * 
	 */
    private boolean checkIDFProtocol(String label, IDF idf) {
        boolean check = false;
        int numProtocols = 0;
        if (idf.protocolName == null) {
            createEvent("Warning: required IDF tag 'protocolName' is missing", 24);
            check = true;
        } else {
            numProtocols = idf.protocolName.size();
            for (int i = 0; i < numProtocols; i++) {
                String protocolName = idf.protocolName.get(i);
                if (protocolName == null || protocolName.equals("")) {
                    Point p = findCell("Protocol Name", idfMap);
                    createEvent("Incomplete information in IDF: Protocol Name is empty", 1015, "validation warning", idfFileName, p.x, i + 2);
                }
            }
        }
        if (idf.protocolType == null) {
            createEvent("Warning: protocolType is missing", 24);
            check = true;
        } else {
            while (idf.protocolType.size() < numProtocols) {
                idf.protocolType.add("");
            }
            int i = 0;
            for (i = 0; i < numProtocols; i++) {
                String protocolType = idf.protocolType.get(i);
                String protocolName = idf.protocolName.get(i);
                if (protocolType.equals("")) {
                    Point p = findCell("Protocol Type", idfMap);
                    createEvent("Incomplete information in IDF: Protocol Type for " + protocolName + "is empty", 1015, "validation warning", idfFileName, p.x, i + 2);
                }
            }
        }
        Hashtable tmp = new Hashtable();
        if (idf.protocolName != null) tmp.put("Protocol Name", idf.protocolName);
        if (idf.protocolType != null) tmp.put("Protocol Type", idf.protocolType);
        if (idf.protocolDescription != null) tmp.put("Protocol Description", idf.protocolDescription);
        if (idf.protocolParameters != null) tmp.put("Protocol Parameters", idf.protocolParameters);
        if (idf.protocolHardware != null) tmp.put("Protocol Hardware", idf.protocolHardware);
        if (idf.protocolSoftware != null) tmp.put("Protocol Software", idf.protocolSoftware);
        if (idf.protocolContact != null) tmp.put("Protocol Contact", idf.protocolContact);
        if (idf.protocolTermSourceREF != null) tmp.put("Protocol Term Source REF", idf.protocolTermSourceREF);
        Enumeration e = tmp.keys();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            ArrayList<String> al = (ArrayList) tmp.get(key);
            while (al.size() < numProtocols) {
                al.add("");
            }
            for (int j = 0; j < numProtocols; j++) {
                String s = al.get(j);
                Point p = findCell(key, idfMap);
                if (s == null || s.equals("")) {
                    createEvent("Incomplete information in IDF: " + key + " is empty", 1015, "validation warning", idfFileName, p.x, j + 2);
                }
            }
        }
        return check;
    }

    /**
	 * checkArrayTag
	 * 
	 */
    private boolean checkArrayTag(String label, List<String> tag) {
        boolean check = false;
        if (tag == null || tag.equals("")) {
            Point p = findCell(label, idfMap);
            createEvent("IDF tag " + label + " is null", 1015, "validation warning", idfFileName, p.x, p.y + 2);
            check = true;
        }
        return check;
    }

    private boolean checkTermSources(String label, List<String> names, List<String> urls, List<String> versions) {
        boolean check = false;
        int numNames = 0;
        if (names == null) {
            createEvent("Warning: required IDF tag " + label + " is missing", 24);
            check = true;
        } else {
            numNames = names.size();
            if (numNames == 0) {
                Point p = findCell(label, idfMap);
                createEvent("Incomplete information in IDF: " + label + " is empty", 1015, "validation warning", idfFileName, p.x, p.y + 2);
            }
            while (urls.size() < numNames) {
                urls.add("");
            }
            while (versions.size() < numNames) {
                versions.add("");
            }
            for (int i = 0; i < numNames; i++) {
                String sourceName = names.get(i);
                String url = urls.get(i);
                String version = versions.get(i);
                if (sourceName == null || sourceName.equals("")) {
                    Point p = findCell("Term Source Name", idfMap);
                    createEvent("Incomplete information in IDF: " + label + " is empty", 1015, "validation warning", idfFileName, p.x, i + 2);
                }
                if (url == null || url.equals("")) {
                    Point p = findCell("Term Source File", idfMap);
                    createEvent("Incomplete information in IDF: Term Source File is empty", 1015, "validation warning", idfFileName, p.x, i + 2);
                }
                if (version == null || version.equals("")) {
                    Point p = findCell("Term Source Version", idfMap);
                    createEvent("Incomplete information in IDF: Term Source Version is empty", 1015, "validation warning", idfFileName, p.x, i + 2);
                }
            }
        }
        return check;
    }

    /**
	 * checkTagAndTermSource
	 * 
	 */
    private boolean checkTagAndTermSource(String label, List<String> types, List<String> sources) {
        boolean check = false;
        check = checkTagAndTermSource(label, null, types, sources);
        return check;
    }

    /**
	 * checkTagAndTermSource
	 * 
	 */
    private boolean checkTagAndTermSource(String label, List<String> tags, List<String> types, List<String> sources) {
        boolean check = false;
        int numTypes = 0;
        if (types == null) {
            createEvent("Warning: required IDF tag " + label + " is missing", 24);
            check = true;
        } else {
            numTypes = types.size();
            if (numTypes == 0) {
                Point p = findCell(label, idfMap);
                createEvent("Incomplete information in IDF: " + label + " is empty", 1015, "validation warning", idfFileName, p.x, p.y + 2);
            }
            while (sources.size() < numTypes) {
                sources.add("");
            }
            if (tags != null) {
                while (tags.size() < numTypes) {
                    tags.add("");
                }
            }
            for (int i = 0; i < numTypes; i++) {
                String sourceName = sources.get(i);
                int line = mti.getLocationTracker().getIDFLocations(label);
                if (sourceName == null || sourceName.equals("")) {
                    StringBuffer REFLabel = new StringBuffer(label);
                    if (REFLabel.indexOf("Type") > 0) REFLabel.replace(REFLabel.indexOf(" Type", 0), REFLabel.length(), "");
                    REFLabel.append(" Term Source REF");
                    Point p = findCell(REFLabel.toString(), idfMap);
                    if (p.x == -1) {
                        Point plabel = findCell(label, idfMap);
                        createEvent("Incomplete information in IDF: " + label + " has no Term Source REF", 1015, "validation warning", idfFileName, plabel.x, i + 2);
                    } else {
                        createEvent("Incomplete information in IDF: " + REFLabel.toString() + " has no value", 1015, "validation warning", idfFileName, p.x, i + 2);
                    }
                }
                if (tags != null) {
                    String myTag = tags.get(i);
                    if (myTag == null || myTag.equals("")) {
                        Point p = findCell(label, idfMap);
                        createEvent("Incomplete information in IDF: " + label + " is empty", 1015, "validation warning", idfFileName, p.x, i + 2);
                    }
                }
            }
        }
        return check;
    }

    /**
	 * Checks SDRF by Object and attribute (column) for:
	 * BioSource
	 * BioSample
	 * Extract
	 * LabeledExtract
	 * Assay
	 * Array
	 * Scan
	 * Normalization
	 * Data files
	 * Matrix files
	 * Image files
	 * FactorValue
	 * 
	 * @param sdrf
	 * @return true if any failure occurs
	 */
    protected boolean checkSDRF(SDRF sdrf) {
        boolean fail = false;
        boolean rv = false;
        System.out.println("Validating SDRF");
        List<SourceNode> sources = sdrf.lookupNodes(SourceNode.class);
        System.out.println("Validating Sources");
        fail = checkSources(sources);
        if (fail == true) rv = true;
        List<SampleNode> samples = sdrf.lookupNodes(SampleNode.class);
        System.out.println("Validating Samples");
        fail = checkSamples(samples);
        if (fail == true) rv = true;
        List<ExtractNode> extracts = sdrf.lookupNodes(ExtractNode.class);
        System.out.println("Validating Extracts");
        fail = checkExtracts(extracts);
        if (fail == true) rv = true;
        List<LabeledExtractNode> labeledExtracts = sdrf.lookupNodes(LabeledExtractNode.class);
        System.out.println("Validating Labeled Extracts");
        fail = checkLabeledExtracts(labeledExtracts);
        if (fail == true) rv = true;
        List<HybridizationNode> hybridizations = sdrf.lookupNodes(HybridizationNode.class);
        System.out.println("Validating Hybs");
        fail = checkHybridizations(hybridizations);
        if (fail == true) rv = true;
        List<AssayNode> assays = sdrf.lookupNodes(AssayNode.class);
        System.out.println("Validating Assays");
        fail = checkAssays(assays);
        if (fail == true) rv = true;
        List<ScanNode> scans = sdrf.lookupNodes(ScanNode.class);
        System.out.println("Validating Scans");
        fail = checkScans(scans);
        if (fail == true) rv = true;
        List<ImageNode> images = sdrf.lookupNodes(ImageNode.class);
        System.out.println("Validating Images");
        fail = checkImages(images);
        if (fail == true) rv = true;
        List<ArrayDesignNode> arrayDesigns = sdrf.lookupNodes(ArrayDesignNode.class);
        System.out.println("Validating ArrayDesign");
        fail = checkArrayDesign(arrayDesigns);
        if (fail == true) rv = true;
        List<ArrayDataNode> arrayData = sdrf.lookupNodes(ArrayDataNode.class);
        System.out.println("Validating Array Data");
        fail = checkArrayData(arrayData);
        if (fail == true) rv = true;
        List<ArrayDataMatrixNode> arrayDataMatrix = sdrf.lookupNodes(ArrayDataMatrixNode.class);
        System.out.println("Validating Array Data Matrix");
        fail = checkArrayDataMatrix(arrayDataMatrix);
        if (fail == true) rv = true;
        List<NormalizationNode> normalization = sdrf.lookupNodes(NormalizationNode.class);
        System.out.println("Validating Normalization");
        fail = checkNormalization(normalization);
        if (fail == true) rv = true;
        List<DerivedArrayDataNode> derivedArrayData = sdrf.lookupNodes(DerivedArrayDataNode.class);
        System.out.println("Validating Derived Array Data");
        fail = checkDerivedArrayData(derivedArrayData);
        if (fail == true) rv = true;
        List<DerivedArrayDataMatrixNode> derivedArrayDataMatrix = sdrf.lookupNodes(DerivedArrayDataMatrixNode.class);
        System.out.println("Validating Derived Array Data Matrix");
        fail = checkDerivedArrayDataMatrix(derivedArrayDataMatrix);
        if (fail == true) rv = true;
        List<ProtocolNode> protocols = sdrf.lookupNodes(ProtocolNode.class);
        System.out.println("Validating Protocols");
        fail = checkProtocols(protocols);
        if (fail == true) rv = true;
        return rv;
    }

    /**
	 * checkSources
	 * checks source objects and attributes associated with them
	 * Params: source list
	 * Return: boolean, true if fails
	 */
    boolean checkSources(List<SourceNode> sources) {
        boolean check = false;
        for (SourceNode source : sources) {
            String sourceName = source.getNodeName();
            List<ProtocolNode> protocols = null;
            Set<Point> locations = mti.getLocationTracker().getSDRFLocations(source);
            Object[] alist = locations.toArray();
            Point sloc = (Point) alist[0];
            Point p = findCell(sourceName, sdrfMap);
            if (sourceName.equals("")) {
                createEvent("Error: at least one Sample has no Source name", 25, "validation error", sdrfFileName, sloc.y, sloc.x);
                check = true;
            }
            if (source.materialType != null && source.materialType.getNodeName().equals("")) {
                createEvent("Incomplete information for " + sourceName + "; value for materialType is missing", 1016, "validation warning", sdrfFileName, p.x, p.y + 1);
            } else if (source.materialType == null) {
                createEvent("Incomplete information for " + sourceName + "; materialType not supplied", 1016, "validation missingData", sdrfFileName, p.x, p.y);
            } else if (source.materialType != null) {
                String materialName = source.materialType.getNodeName();
                Point m = findCell(materialName, sdrfMap);
                if (source.materialType.termSourceREF != null && source.materialType.termSourceREF.equals("")) {
                    createEvent("Incomplete information for " + sourceName + "; MaterialType " + materialName + " has no Term Source", 1005, "validation warning", sdrfFileName, sloc.y, m.y + 1);
                } else if (source.materialType.termSourceREF == null) {
                    createEvent("Incomplete information for " + sourceName + "; Material Type Term Source not supplied for " + source.materialType, 1016, "validation missingData", sdrfFileName, m.x, m.y);
                } else if (!tsrStr.contains(source.materialType.termSourceREF)) {
                    createEvent("Term Source REF, " + source.materialType.termSourceREF + ", for Material Type " + source.materialType + " is not declared in the IDF", 6, "validation warning", sdrfFileName, sloc.y, m.y + 1);
                }
            }
            if (source.provider != null && source.provider.getNodeName().equals("")) {
                Point pro = findCell("Provider", sdrfMap);
                createEvent("Incomplete information for " + sourceName + "; value for provider is missing", 1016, "validation warning", sdrfFileName, pro.x, pro.y);
            } else if (source.provider == null) {
                createEvent("Incomplete information for " + sourceName + "; provider not supplied", 1016, "validation missingData", sdrfFileName, sloc.x, sloc.y);
            }
            if (source.protocols != null) protocols = source.protocols;
            boolean fail = checkProtocols(protocols);
            List<CharacteristicsAttribute> charList = source.characteristics;
            for (CharacteristicsAttribute attr : charList) {
                String attrType = attr.type;
                Point a = findCell(attr.getNodeName(), sdrfMap);
                if (attr != null && attr.getNodeName().equals("")) {
                    createEvent("Incomplete information for " + sourceName + "; Characteristic " + attrType + " nas no value", 1016, "validation warning", sdrfFileName, sloc.y, a.y + 1);
                }
                if (attr.termSourceREF != null && attr.termSourceREF.equals("")) {
                    createEvent("Incomplete information for " + sourceName + "; Characteristic " + attrType + " has no Term Source", 1005, "validation warning", sdrfFileName, sloc.y, a.y + 1);
                } else if (attr.termSourceREF == null) {
                    createEvent("Incomplete information for " + sourceName + "; Term Source not supplied for " + attr, 1016, "validation missingData", sdrfFileName, sloc.y, a.y);
                } else if (!tsrStr.contains(attr.termSourceREF)) {
                    createEvent("Term Source REF, " + attr.termSourceREF + ", for Characteristic " + attrType + " is not declared in the IDF", 6, "validation warning", sdrfFileName, sloc.y, a.y + 1);
                    check = true;
                }
                if (attr.unit != null && attr.unit.getNodeName().equals("")) {
                    createEvent("Incomplete information for " + sourceName + "; Characteristic " + attrType + " has no Units", 1016, "validation warning", sdrfFileName, sloc.y, a.y + 1);
                } else if (attr.unit != null) {
                    Point u = findCell(attr.unit.getNodeName(), sdrfMap);
                    if (attr.unit.termSourceREF != null && attr.unit.termSourceREF.equals("")) {
                        createEvent("Incomplete information for " + sourceName + "; Characteristic " + attrType + " has no Unit Term Source", 1005, "validation warning", sdrfFileName, sloc.y, u.y + 1);
                    } else if (attr.unit.termSourceREF == null) {
                        createEvent("Incomplete information for " + sourceName + "; Unit Term Source not supplied for " + attr, 1016, "validation missingData", sdrfFileName, sloc.y, u.y);
                    } else if (!tsrStr.contains(attr.unit.termSourceREF)) {
                        createEvent("Unit Term Source REF, " + attr.unit.termSourceREF + ", for Characteristic " + attrType + " is not declared in the IDF", 6, "validation warning", sdrfFileName, sloc.y, u.y + 1);
                        check = true;
                    }
                }
            }
            if (check == true) System.out.println(sourceName);
        }
        return check;
    }

    boolean checkSamples(List<SampleNode> samples) {
        boolean check = false;
        for (SampleNode sample : samples) {
            String sampleName = sample.getNodeName();
            List<ProtocolNode> protocols = null;
            Set<Point> locations = mti.getLocationTracker().getSDRFLocations(sample);
            Object[] alist = locations.toArray();
            Point sloc = (Point) alist[0];
            Point p = findCell(sampleName, sdrfMap);
            if (sampleName.equals("")) {
                createEvent("Warning: at least one Sample has no sample name", 1016, "validation warning", sdrfFileName, sloc.y, sloc.x);
            }
            if (sample.materialType != null && sample.materialType.getNodeName().equals("")) {
                createEvent("Incomplete information for " + sampleName + "; value for materialType is missing", 1016, "validation warning", sdrfFileName, p.x, p.y + 1);
                check = true;
            } else if (sample.materialType == null) {
                createEvent("Incomplete information for " + sampleName + "; materialType not supplied", 1016, "validation missingData", sdrfFileName, p.x, p.y);
            } else if (sample.materialType != null) {
                String materialName = sample.materialType.getNodeName();
                Point m = findCell(materialName, sdrfMap);
                if (sample.materialType.termSourceREF != null && sample.materialType.termSourceREF.equals("")) {
                    createEvent("Incomplete information for " + sampleName + "; MaterialType " + sample.materialType + " has no Term Source", 1005, "validation warning", sdrfFileName, sloc.y, m.y + 1);
                } else if (sample.materialType.termSourceREF == null) {
                    createEvent("Incomplete information for " + sampleName + "; Material Type Term Source not supplied for " + sample.materialType, 1016, "validation missingData", sdrfFileName, sloc.y, m.y);
                } else if (!tsrStr.contains(sample.materialType.termSourceREF)) {
                    createEvent("Term Source REF, " + sample.materialType.termSourceREF + ", for Material Type " + sample.materialType + " is not declared in the IDF", 6, "validation warning", sdrfFileName, sloc.y, m.y + 1);
                }
            }
            if (sample.protocols != null) protocols = sample.protocols;
            boolean fail = checkProtocols(protocols);
            List<CharacteristicsAttribute> charList = sample.characteristics;
            for (CharacteristicsAttribute attr : charList) {
                String attrType = attr.type;
                Point a = findCell(attr.getNodeName(), sdrfMap);
                if (attr != null && attr.getNodeName().equals("")) {
                    createEvent("Incomplete information for " + sampleName + "; Characteristic " + attrType + " nas no value", 1016, "validation warning", sdrfFileName, sloc.y, a.y + 1);
                }
                if (attr.termSourceREF != null && attr.termSourceREF.equals("")) {
                    createEvent("Incomplete information for " + sampleName + "; Characteristic " + attrType + " has no Term Source", 1005, "validation warning", sdrfFileName, sloc.y, a.y + 1);
                } else if (attr.termSourceREF == null) {
                    createEvent("Incomplete information for " + sampleName + "; Term Source not supplied for " + attr, 1016, "validation missingData", sdrfFileName, sloc.y, a.y);
                } else if (!tsrStr.contains(attr.termSourceREF)) {
                    createEvent("Term Source REF, " + attr.termSourceREF + ", for Characteristic " + attrType + " is not declared in the IDF", 6, "validation warning", sdrfFileName, sloc.y, a.y + 1);
                    check = true;
                }
                if (attr.unit != null && attr.unit.getNodeName().equals("")) {
                    createEvent("Incomplete information for " + sampleName + "; Characteristic " + attrType + " has no Units", 1016, "validation warning", sdrfFileName, sloc.y, a.y);
                    check = true;
                } else if (attr.unit != null) {
                    Point u = findCell(attr.unit.getNodeName(), sdrfMap);
                    if (attr.unit.termSourceREF != null && attr.unit.termSourceREF.equals("")) {
                        createEvent("Incomplete information for " + sampleName + "; Characteristic " + attrType + " has no Unit Term Source", 1005, "validation warning", sdrfFileName, sloc.y, u.y + 1);
                    } else if (attr.unit.termSourceREF == null) {
                        createEvent("Incomplete information for " + sampleName + "; Unit Term Source not supplied for " + attr, 1016, "validation missingData", sdrfFileName, sloc.y, u.y);
                    } else if (!tsrStr.contains(attr.unit.termSourceREF)) {
                        createEvent("Unit Term Source REF, " + attr.unit.termSourceREF + ", for Characteristic " + attrType + " is not declared in the IDF", 6, "validation warning", sdrfFileName, sloc.y, u.y + 1);
                        check = true;
                    }
                }
            }
            if (check == true) System.out.println(sampleName);
        }
        return check;
    }

    boolean checkExtracts(List<ExtractNode> extracts) {
        boolean check = false;
        for (ExtractNode extract : extracts) {
            String extractName = extract.getNodeName();
            List<ProtocolNode> protocols = null;
            Set<Point> locations = mti.getLocationTracker().getSDRFLocations(extract);
            Object[] alist = locations.toArray();
            Point sloc = (Point) alist[0];
            Point p = findCell(extractName, sdrfMap);
            if (extractName.equals("")) {
                createEvent("Warning: at least one extract has no extract name", 25, "validation error", sdrfFileName, sloc.y, sloc.x);
                check = true;
            }
            if (extract.materialType != null && extract.materialType.getNodeName().equals("")) {
                createEvent("Incomplete information for " + extractName + "; value for materialType is missing", 1016, "validation warning", sdrfFileName, p.x, p.y + 1);
            } else if (extract.materialType == null) {
                createEvent("Incomplete information for " + extractName + "; materialType not supplied", 1016, "validation missingData", sdrfFileName, p.x, p.y);
            } else if (extract.materialType != null) {
                String materialName = extract.materialType.getNodeName();
                Point m = findCell(materialName, sdrfMap);
                if (extract.materialType.termSourceREF != null && extract.materialType.termSourceREF.equals("")) {
                    createEvent("Incomplete information for " + extractName + "; MaterialType " + extract.materialType + " has no Term Source", 1005, "validation warning", sdrfFileName, sloc.y, m.y + 1);
                } else if (extract.materialType.termSourceREF == null) {
                    createEvent("Incomplete information for " + extractName + "; Material Type Term Source not supplied for " + extract.materialType, 1016, "validation missingData", sdrfFileName, sloc.y, m.y);
                } else if (!tsrStr.contains(extract.materialType.termSourceREF)) {
                    createEvent("Term Source REF, " + extract.materialType.termSourceREF + ", for Material Type " + extract.materialType + " is not declared in the IDF", 6, "validation warning", sdrfFileName, sloc.y, m.y + 1);
                    check = true;
                }
            }
            if (extract.protocols != null) protocols = extract.protocols;
            boolean fail = checkProtocols(protocols);
            List<CharacteristicsAttribute> charList = extract.characteristics;
            for (CharacteristicsAttribute attr : charList) {
                String attrType = attr.type;
                Point a = findCell(attr.getNodeName(), sdrfMap);
                if (attr != null && attr.getNodeName().equals("")) {
                    createEvent("Incomplete information for " + extractName + "; Characteristic " + attrType + " nas no value", 1016, "validation warning", sdrfFileName, sloc.y, a.y + 1);
                }
                if (attr.termSourceREF != null && attr.termSourceREF.equals("")) {
                    createEvent("Incomplete information for " + extractName + "; Characteristic " + attrType + " has no Term Source", 1016, "validation warning", sdrfFileName, sloc.y, a.y + 1);
                } else if (attr.termSourceREF == null) {
                    createEvent("Incomplete information for " + extractName + "; Term Source not supplied for " + attr, 1016, "validation missingData", sdrfFileName, sloc.y, a.y);
                } else if (!tsrStr.contains(attr.termSourceREF)) {
                    createEvent("Term Source REF, " + attr.termSourceREF + ", for Characteristic " + attrType + " is not declared in the IDF", 6, "validation warning", sdrfFileName, sloc.y, a.y + 1);
                    check = true;
                }
                if (attr.unit != null && attr.unit.getNodeName().equals("")) {
                    createEvent("Incomplete information for " + extractName + "; Characteristic " + attrType + " has no Units", 1016, "validation warning", sdrfFileName, sloc.y, a.y + 1);
                } else if (attr.unit != null) {
                    Point u = findCell(attr.unit.getNodeName(), sdrfMap);
                    if (attr.unit.termSourceREF != null && attr.unit.termSourceREF.equals("")) {
                        createEvent("Incomplete information for " + extractName + "; Characteristic " + attrType + " has no Unit Term Source", 1005, "validation warning", sdrfFileName, sloc.y, u.y + 1);
                    } else if (attr.unit.termSourceREF == null) {
                        createEvent("Incomplete information for " + extractName + "; Unit Term Source not supplied for " + attr, 1016, "validation missingData", sdrfFileName, sloc.y, u.y);
                    } else if (!tsrStr.contains(attr.unit.termSourceREF)) {
                        createEvent("Unit Term Source REF, " + attr.unit.termSourceREF + ", for Characteristic " + attrType + " is not declared in the IDF", 6, "validation warning", sdrfFileName, sloc.y, u.y + 1);
                        check = true;
                    }
                }
            }
            if (check == true) System.out.println(extractName);
        }
        return check;
    }

    boolean checkLabeledExtracts(List<LabeledExtractNode> labeledExtracts) {
        boolean check = false;
        for (LabeledExtractNode labeledExtract : labeledExtracts) {
            String labeledExtractName = labeledExtract.getNodeName();
            List<ProtocolNode> protocols = null;
            Set<Point> locations = mti.getLocationTracker().getSDRFLocations(labeledExtract);
            Object[] alist = locations.toArray();
            Point sloc = (Point) alist[0];
            Point p = findCell(labeledExtractName, sdrfMap);
            if (labeledExtractName == null) {
                createEvent("Warning: at least one labeledExtract has no labeledExtract name", 25, "validation error", sdrfFileName, sloc.y, sloc.x);
                check = true;
            }
            if (labeledExtract.materialType != null && labeledExtract.materialType.getNodeName().equals("")) {
                createEvent("Incomplete information for " + labeledExtractName + "; value for materialType is missing", 1016, "validation warning", sdrfFileName, p.x, p.y + 1);
            } else if (labeledExtract.materialType == null) {
                createEvent("Incomplete information for " + labeledExtractName + "; materialType not supplied", 1016, "validation missingData", sdrfFileName, p.x, p.y);
            } else if (labeledExtract.materialType != null) {
                String materialName = labeledExtract.materialType.getNodeName();
                Point m = findCell(materialName, sdrfMap);
                if (labeledExtract.materialType.termSourceREF != null && labeledExtract.materialType.termSourceREF.equals("")) {
                    createEvent("Incomplete information for " + labeledExtractName + "; MaterialType " + labeledExtract.materialType + " has no Term Source", 1005, "validation warning", sdrfFileName, sloc.y, m.y + 1);
                } else if (labeledExtract.materialType.termSourceREF == null) {
                    createEvent("Incomplete information for " + labeledExtractName + "; Material Type Term Source not supplied for " + labeledExtract.materialType, 1016, "validation missingData", sdrfFileName, sloc.y, m.y);
                } else if (!tsrStr.contains(labeledExtract.materialType.termSourceREF)) {
                    createEvent("Term Source REF, " + labeledExtract.materialType.termSourceREF + ", for Material Type " + labeledExtract.materialType + " is not declared in the IDF", 6, "validation warning", sdrfFileName, sloc.y, m.y + 1);
                    check = true;
                }
            }
            if (labeledExtract.label != null && labeledExtract.label.getNodeName().equals("")) {
                createEvent("Incomplete information for " + labeledExtractName + "; value for label is missing", 1016, "validation warning", sdrfFileName, p.x, p.y + 1);
            } else if (labeledExtract.label == null) {
                createEvent("Incomplete information for " + labeledExtractName + "; label not supplied", 1016, "validation missingData", sdrfFileName, p.x, p.y);
            } else if (labeledExtract.label != null) {
                Point l = findCell(labeledExtract.label.getNodeName(), sdrfMap);
                if (labeledExtract.label.termSourceREF != null && labeledExtract.label.termSourceREF.equals("")) {
                    createEvent("Incomplete information for " + labeledExtract.label + "; MaterialType " + labeledExtract.label + " has no Term Source", 1005, "validation warning", sdrfFileName, sloc.y, l.y + 1);
                } else if (labeledExtract.label.termSourceREF == null) {
                    createEvent("Incomplete information for " + labeledExtract.label + "; Label Term Source not supplied for " + labeledExtract.label, 1016, "validation missingData", sdrfFileName, sloc.y, l.y);
                } else if (!tsrStr.contains(labeledExtract.label.termSourceREF)) {
                    createEvent("Term Source REF, " + labeledExtract.label.termSourceREF + ", for Label " + labeledExtract.label + " is not declared in the IDF", 6, "validation warning", sdrfFileName, sloc.y, l.y + 1);
                    check = true;
                }
            }
            if (labeledExtract.protocols != null) protocols = labeledExtract.protocols;
            boolean fail = checkProtocols(protocols);
            List<CharacteristicsAttribute> charList = labeledExtract.characteristics;
            for (CharacteristicsAttribute attr : charList) {
                String attrType = attr.type;
                Point a = findCell(attr.getNodeName(), sdrfMap);
                if (attr != null && attr.getNodeName().equals("")) {
                    createEvent("Incomplete information for " + labeledExtractName + "; Characteristic " + attrType + " nas no value", 1016, "validation warning", sdrfFileName, sloc.y, a.y + 1);
                }
                if (attr.termSourceREF != null && attr.termSourceREF.equals("")) {
                    createEvent("Incomplete information for " + labeledExtractName + "; Characteristic " + attrType + " has no Term Source", 1016, "validation warning", sdrfFileName, sloc.y, a.y + 1);
                } else if (attr.termSourceREF == null) {
                    createEvent("Incomplete information for " + labeledExtractName + "; Term Source not supplied for " + attr, 1016, "validation missingData", sdrfFileName, sloc.y, a.y);
                } else if (!tsrStr.contains(attr.termSourceREF)) {
                    createEvent("Term Source REF, " + attr.termSourceREF + ", for Characteristic " + attrType + " is not declared in the IDF", 6, "validation warning", sdrfFileName, sloc.y, a.y + 1);
                    check = true;
                }
                if (attr.unit != null && attr.unit.getNodeName().equals("")) {
                    createEvent("Incomplete information for " + labeledExtractName + "; Characteristic " + attrType + " has no Units", 1016, "validation warning", sdrfFileName, sloc.y, a.y + 1);
                } else if (attr.unit != null) {
                    Point u = findCell(attr.unit.getNodeName(), sdrfMap);
                    if (attr.unit.termSourceREF != null && attr.unit.termSourceREF.equals("")) {
                        createEvent("Incomplete information for " + labeledExtractName + "; Characteristic " + attrType + " has no Unit Term Source", 1005, "validation warning", sdrfFileName, sloc.y, u.y + 1);
                    } else if (attr.unit.termSourceREF == null) {
                        createEvent("Incomplete information for " + labeledExtractName + "; Unit Term Source not supplied for " + attr, 1016, "validation missingData", sdrfFileName, sloc.y, u.y);
                    } else if (!tsrStr.contains(attr.unit.termSourceREF)) {
                        createEvent("Unit Term Source REF, " + attr.unit.termSourceREF + ", for Characteristic " + attrType + " is not declared in the IDF", 6, "validation warning", sdrfFileName, sloc.y, u.y + 1);
                        check = true;
                    }
                }
            }
            if (check == true) System.out.println(labeledExtractName);
        }
        return check;
    }

    boolean checkHybridizations(List<HybridizationNode> hybridizations) {
        boolean check = false;
        for (HybridizationNode hybridization : hybridizations) {
            String hybridizationName = hybridization.getNodeName();
            List<ProtocolNode> protocols = null;
            Set<Point> locations = mti.getLocationTracker().getSDRFLocations(hybridization);
            Object[] alist = locations.toArray();
            Point sloc = new Point(-1, -1);
            if (0 < alist.length) {
                sloc = (Point) alist[0];
            }
            Point p = findCell(hybridizationName, sdrfMap);
            if (hybridizationName.equals("")) {
                createEvent("Warning: at least one hybridization has no hybridization name", 25, "validation error", sdrfFileName, sloc.y, sloc.x);
                check = true;
            }
            if (hybridization.protocols != null) protocols = hybridization.protocols;
            boolean fail = checkProtocols(protocols);
            List<FactorValueAttribute> fvList = hybridization.factorValues;
            for (FactorValueAttribute attr : fvList) {
                String attrType = attr.type;
                Point a = findCell(attr.getNodeName(), sdrfMap);
                if (attr != null && attr.getNodeName().equals("")) {
                    createEvent("Incomplete information for " + hybridizationName + "; Factor value " + attrType + " nas no value", 1016, "validation warning", sdrfFileName, sloc.y, a.y + 1);
                }
                if (attr.termSourceREF != null && attr.termSourceREF.equals("")) {
                    createEvent("Incomplete information for " + hybridizationName + "; Factor value " + attrType + " has no Term Source", 1005, "validation warning", sdrfFileName, sloc.y, a.y + 1);
                } else if (attr.termSourceREF == null) {
                    createEvent("Incomplete information for " + hybridizationName + "; Term Source not supplied for " + attr, 1016, "validation missingData", sdrfFileName, sloc.y, a.y);
                } else if (!tsrStr.contains(attr.termSourceREF)) {
                    createEvent("Term Source REF, " + attr.termSourceREF + ", for Factor value " + attrType + " is not declared in the IDF", 6, "validation warning", sdrfFileName, sloc.y, a.y + 1);
                    check = true;
                }
                if (attr.unit != null && attr.unit.getNodeName().equals("")) {
                    createEvent("Incomplete information for " + hybridizationName + "; Factor value " + attrType + " has no Units", 1016, "validation warning", sdrfFileName, sloc.y, a.y + 1);
                } else if (attr.unit != null) {
                    Point u = findCell(attr.unit.getNodeName(), sdrfMap);
                    if (attr.unit.termSourceREF != null && attr.unit.termSourceREF.equals("")) {
                        createEvent("Incomplete information for " + hybridizationName + "; Factor value " + attrType + " has no Unit Term Source", 1005, "validation warning", sdrfFileName, sloc.y, u.y + 1);
                    } else if (attr.termSourceREF == null) {
                        createEvent("Incomplete information for " + hybridizationName + "; Unit Term Source not supplied for " + attr, 1016, "validation missingData", sdrfFileName, sloc.y, u.y);
                    } else if (!tsrStr.contains(attr.termSourceREF)) {
                        createEvent("Unit Term Source REF, " + attr.unit.termSourceREF + ", for Factor value " + attrType + " is not declared in the IDF", 6, "validation warning", sdrfFileName, sloc.y, u.y + 1);
                        check = true;
                    }
                }
            }
            if (check == true) System.out.println(hybridizationName);
        }
        return check;
    }

    boolean checkAssays(List<AssayNode> assays) {
        boolean check = false;
        for (AssayNode assay : assays) {
            String assayName = assay.getNodeName();
            List<ProtocolNode> protocols = null;
            Set<Point> locations = mti.getLocationTracker().getSDRFLocations(assay);
            Object[] alist = locations.toArray();
            Point sloc = (Point) alist[0];
            Point p = findCell(assayName, sdrfMap);
            if (assayName.equals("")) {
                createEvent("Warning: at least one assay has no assay name", 25, "validation error", sdrfFileName, sloc.y, sloc.x);
                check = true;
            }
            if (assay.technologyType != null && assay.technologyType.getNodeName().equals("")) {
                createEvent("Incomplete information for " + assayName + "; value for technologyType is missing", 1026, "validation error", sdrfFileName, sloc.y, sloc.x + 1);
                check = true;
            } else if (assay.technologyType == null) {
                createEvent("Incomplete information for " + assayName + "; technologyType not supplied", 1026, "validation error", sdrfFileName, sloc.y, sloc.x);
                check = true;
            } else {
                if (assay.technologyType.termSourceREF != null && assay.technologyType.termSourceREF.equals("")) {
                    createEvent("Incomplete information for " + assayName + "; value for technologyType Term Source is missing", 1016, "validation warning", sdrfFileName, sloc.y, p.y + 1);
                } else if (assay.technologyType.termSourceREF == null) {
                    createEvent("Incomplete information for " + assayName + "; technologyType Term Source not supplied", 1016, "validation missingData", sdrfFileName, sloc.y, p.y);
                } else if (!tsrStr.contains(assay.technologyType.termSourceREF)) {
                    createEvent("Term Source REF, " + assay.technologyType.termSourceREF + ", for technologyType " + assay.technologyType + " is not declared in the IDF", 6, "validation warning", sdrfFileName, sloc.y, p.y + 1);
                    check = true;
                }
            }
            if (assay.protocols != null) protocols = assay.protocols;
            boolean fail = checkProtocols(protocols);
            if (check == true) System.out.println(assayName);
        }
        return check;
    }

    boolean checkScans(List<ScanNode> scans) {
        boolean check = false;
        for (ScanNode scan : scans) {
            String scanName = scan.getNodeName();
            List<ProtocolNode> protocols = null;
            Point p = findCell(scanName, sdrfMap);
            if (scanName.equals("")) {
                createEvent("Warning: at least one scan has no scan name", 25, "validation error", sdrfFileName, p.x, p.y);
                check = true;
            }
            if (scan.protocols != null) protocols = scan.protocols;
            boolean fail = checkProtocols(protocols);
            if (check == true) System.out.println(scanName);
        }
        return check;
    }

    boolean checkImages(List<ImageNode> images) {
        boolean check = false;
        for (ImageNode image : images) {
            String imageName = image.getNodeName();
            List<ProtocolNode> protocols = null;
            Point p = findCell(imageName, sdrfMap);
            if (imageName == null) {
                createEvent("Warning: at least one image has no image name", 25, "validation error", sdrfFileName, p.x, p.y);
                check = true;
            }
            if (image.protocols != null) protocols = image.protocols;
            boolean fail = checkProtocols(protocols);
            if (check == true) System.out.println(imageName);
        }
        return check;
    }

    boolean checkArrayDesign(List<ArrayDesignNode> arrayDesigns) {
        boolean check = false;
        for (ArrayDesignNode arrayDesign : arrayDesigns) {
            String arrayDesignName = arrayDesign.getNodeName();
            List<ProtocolNode> protocols = null;
            Point p = findCell(arrayDesignName, sdrfMap);
            if (arrayDesignName.equals("")) {
                createEvent("Warning: at least one arrayDesign has no arrayDesign name", 25, "validation error", sdrfFileName, p.x, p.y);
                check = true;
            }
            if (arrayDesign.termSourceREF != null && arrayDesign.termSourceREF.equals("")) {
                createEvent("Incomplete information for " + arrayDesignName + "; value for termSourceREF is missing", 1016, "validation warning", sdrfFileName, p.x, p.y + 1);
            } else if (arrayDesign.termSourceREF == null) {
                createEvent("Incomplete information for " + arrayDesignName + "; termSourceREF not supplied", 1016, "validation missingData", sdrfFileName, p.x, p.y);
            } else if (!tsrStr.contains(arrayDesign.termSourceREF)) {
                createEvent("Term Source REF, " + arrayDesign.termSourceREF + ", for Array Design " + arrayDesignName + " is not declared in the IDF", 6, "validation warning", sdrfFileName, p.x, p.y + 1);
                check = true;
            }
            if (arrayDesign.protocols != null) protocols = arrayDesign.protocols;
            boolean fail = checkProtocols(protocols);
            if (check == true) System.out.println(arrayDesignName);
        }
        return check;
    }

    boolean checkArrayData(List<ArrayDataNode> arrayData) {
        boolean check = false;
        for (ArrayDataNode dataSource : arrayData) {
            String dataSourceName = dataSource.getNodeName();
            String sourcePath = this.idfFileName.substring(0, this.idfFileName.lastIndexOf(File.separatorChar));
            String sourceFullName = sourcePath + File.separatorChar + dataSourceName;
            System.out.println("DataSource: " + dataSourceName + "; " + sourceFullName);
            List<ProtocolNode> protocols = null;
            Set<Point> locations = mti.getLocationTracker().getSDRFLocations(dataSource);
            Object[] alist = locations.toArray();
            Point sloc = (Point) alist[0];
            Point p = findCell(dataSourceName, sdrfMap);
            if (dataSourceName == null) {
                createEvent("Warning: at least one Array Data file entry has no name", 25, "validation error", sdrfFileName, p.x, p.y);
                check = true;
            }
            File f = new File(sourceFullName);
            if (!f.exists()) {
                createEvent("Array Data file " + dataSourceName + " is missing", 1031, "validation warning", sdrfFileName, p.x, p.y);
            }
            if (dataSource.protocols != null) protocols = dataSource.protocols;
            boolean fail = checkProtocols(protocols);
            if (check == true) System.out.println(dataSourceName);
        }
        return check;
    }

    boolean checkArrayDataMatrix(List<ArrayDataMatrixNode> arrayDataMatrix) {
        boolean check = false;
        for (ArrayDataMatrixNode dataSource : arrayDataMatrix) {
            String dataSourceName = dataSource.getNodeName();
            String sourcePath = this.idfFileName.substring(0, this.idfFileName.lastIndexOf(File.separatorChar));
            String sourceFullName = sourcePath + File.separatorChar + dataSourceName;
            System.out.println("DataSource: " + dataSourceName + "; " + sourceFullName);
            List<ProtocolNode> protocols = null;
            Set<Point> locations = mti.getLocationTracker().getSDRFLocations(dataSource);
            Object[] alist = locations.toArray();
            Point sloc = (Point) alist[0];
            Point p = findCell(dataSourceName, sdrfMap);
            if (dataSourceName == null) {
                createEvent("Warning: at least one Array Data matrix entry has no name", 25, "validation error", sdrfFileName, sloc.y, p.y);
                check = true;
            }
            if (dataSource.protocols != null) protocols = dataSource.protocols;
            boolean fail = checkProtocols(protocols);
            if (dataVal == true) {
                List nodes;
                if (this.mti.SDRF.lookupNodes(SourceNode.class) != null) {
                    nodes = (List<SourceNode>) this.mti.SDRF.lookupNodes(SourceNode.class);
                } else if (this.mti.SDRF.lookupNodes(SampleNode.class) != null) {
                    nodes = (List<SampleNode>) this.mti.SDRF.lookupNodes(SampleNode.class);
                } else if (this.mti.SDRF.lookupNodes(ExtractNode.class) != null) {
                    nodes = (List<ExtractNode>) this.mti.SDRF.lookupNodes(ExtractNode.class);
                } else if (this.mti.SDRF.lookupNodes(LabeledExtractNode.class) != null) {
                    nodes = (List<LabeledExtractNode>) this.mti.SDRF.lookupNodes(LabeledExtractNode.class);
                } else if (this.mti.SDRF.lookupNodes(HybridizationNode.class) != null) {
                    nodes = (List<HybridizationNode>) this.mti.SDRF.lookupNodes(HybridizationNode.class);
                } else if (this.mti.SDRF.lookupNodes(AssayNode.class) != null) {
                    nodes = (List<AssayNode>) this.mti.SDRF.lookupNodes(AssayNode.class);
                } else {
                    nodes = new ArrayList<LabeledExtractNode>();
                    createEvent("Unable to match Biomaterial names to Array Data matrix columns", 1024, "validation error", sdrfFileName, sloc.y, p.y);
                }
                BufferedReader br;
                try {
                    br = new BufferedReader(new FileReader(sourceFullName));
                    StringSplitter ss = new StringSplitter((char) 0x09);
                    String currentLine;
                    int ctr = 0;
                    Hashtable header1 = new Hashtable();
                    ArrayList<String> header2 = new ArrayList();
                    while ((currentLine = br.readLine()) != null) {
                        ctr++;
                        while (currentLine.endsWith("\t")) {
                            currentLine = currentLine.substring(0, currentLine.length() - 1);
                        }
                        ss.init(currentLine);
                        int fctr = 0;
                        if (ctr == 1) {
                            fctr++;
                            ss.nextIntToken();
                            while (ss.hasMoreTokens()) {
                                fctr++;
                                String value = ss.nextToken();
                                if (!header1.containsKey(value)) {
                                    header1.put(value, fctr);
                                }
                            }
                            for (Object n : nodes) {
                                String name = "";
                                if (n instanceof SourceNode) {
                                    name = ((SourceNode) n).getNodeName();
                                } else if (n instanceof SampleNode) {
                                    name = ((SampleNode) n).getNodeName();
                                } else if (n instanceof ExtractNode) {
                                    name = ((ExtractNode) n).getNodeName();
                                } else if (n instanceof LabeledExtractNode) {
                                    name = ((LabeledExtractNode) n).getNodeName();
                                } else if (n instanceof HybridizationNode) {
                                    name = ((HybridizationNode) n).getNodeName();
                                } else if (n instanceof AssayNode) {
                                    name = ((AssayNode) n).getNodeName();
                                } else {
                                    name = "[is null]";
                                }
                                if (!header1.containsKey(name)) {
                                    createEvent("BioMaterial name " + name + " not found in Array Data matrix file " + dataSourceName, 1024, "validation error", sdrfFileName, sloc.y, p.y);
                                }
                            }
                        } else if (ctr == 2) {
                            fctr++;
                            ss.nextIntToken();
                            header2.add("null");
                            while (ss.hasMoreTokens()) {
                                fctr++;
                                String value = ss.nextToken();
                                if (value.contains("value")) {
                                    header2.add("number");
                                } else if (value.contains("Value")) {
                                    header2.add("number");
                                } else if (value.contains("VALUE")) {
                                    header2.add("number");
                                } else if (value.contains("Signal")) {
                                    header2.add("number");
                                } else if (value.contains("signal")) {
                                    header2.add("number");
                                } else if (value.contains("ratio")) {
                                    header2.add("number");
                                } else {
                                    header2.add("string");
                                }
                            }
                        } else {
                            fctr++;
                            ss.nextToken();
                            while (ss.hasMoreTokens()) {
                                fctr++;
                                String value = ss.nextToken();
                                String dtype = header2.get(fctr - 1);
                                if (value != null) {
                                    try {
                                        if (dtype.equals("number")) {
                                            if (!value.contains("e")) {
                                                if (!value.contains(".") && !value.contains(",")) {
                                                    int i = Integer.parseInt(value);
                                                } else {
                                                    float f = Float.parseFloat(value);
                                                }
                                            } else {
                                            }
                                        }
                                    } catch (NumberFormatException nfe) {
                                        createEvent("Number Format Exception, value= " + value, 1038, "validation warning", dataSourceName, ctr, fctr);
                                        System.out.println("NumberFormatException in " + dataSourceName + "; line " + ctr + ", column " + fctr + "; value " + value);
                                    }
                                } else {
                                    createEvent("Empty data cell in Array Data matrix", 1038, "validation warning", dataSourceName, ctr, fctr);
                                }
                            }
                        }
                    }
                } catch (FileNotFoundException e) {
                    createEvent("Data file " + dataSourceName + " was not found", 8, "validation error", sdrfFileName, sloc.y, p.y);
                } catch (IOException ioe) {
                    createEvent("Data file " + dataSourceName + " was not readable", 8, "validation error", sdrfFileName, sloc.y, p.y);
                }
            }
            if (check == true) System.out.println(dataSourceName);
        }
        return check;
    }

    boolean checkNormalization(List<NormalizationNode> normalization) {
        boolean check = false;
        for (NormalizationNode method : normalization) {
            String normalizationName = method.getNodeName();
            List<ProtocolNode> protocols = null;
            Set<Point> locations = mti.getLocationTracker().getSDRFLocations(method);
            Object[] alist = locations.toArray();
            Point sloc = (Point) alist[0];
            Point p = findCell(normalizationName, sdrfMap);
            if (normalizationName == null) {
                createEvent("Warning: at least one normalization entry has no name", 25, "validation error", sdrfFileName, sloc.y, p.y);
                check = true;
            }
            if (method.protocols != null) protocols = method.protocols;
            boolean fail = checkProtocols(protocols);
            if (check == true) System.out.println(normalizationName);
        }
        return check;
    }

    boolean checkDerivedArrayData(List<DerivedArrayDataNode> derivedArrayData) {
        boolean check = false;
        for (DerivedArrayDataNode dataSource : derivedArrayData) {
            String dataSourceName = dataSource.getNodeName();
            String sourcePath = this.idfFileName.substring(0, this.idfFileName.lastIndexOf(File.separatorChar));
            String sourceFullName = sourcePath + File.separatorChar + dataSourceName;
            System.out.println("DataSource: " + dataSourceName + "; " + sourceFullName);
            List<ProtocolNode> protocols = null;
            Set<Point> locations = mti.getLocationTracker().getSDRFLocations(dataSource);
            Object[] alist = locations.toArray();
            Point sloc = (Point) alist[0];
            Point p = findCell(dataSourceName, sdrfMap);
            if (dataSourceName == null) {
                createEvent("Warning: at least one Derived Array Data file entry has no name", 25, "validation error", sdrfFileName, sloc.y, p.y);
                check = true;
            }
            File f = new File(sourceFullName);
            if (!f.exists()) {
                createEvent("Derived Array Data file " + dataSourceName + " is missing", 1031, "validation warning", sdrfFileName, p.x, p.y);
            }
            if (dataSource.protocols != null) protocols = dataSource.protocols;
            boolean fail = checkProtocols(protocols);
            if (check == true) System.out.println(dataSourceName);
        }
        return check;
    }

    boolean checkDerivedArrayDataMatrix(List<DerivedArrayDataMatrixNode> derivedArrayDataMatrix) {
        boolean check = false;
        for (DerivedArrayDataMatrixNode dadm : derivedArrayDataMatrix) {
            String dataSourceName = dadm.getNodeName();
            String sourcePath = this.idfFileName.substring(0, this.idfFileName.lastIndexOf(File.separatorChar));
            String sourceFullName = sourcePath + File.separatorChar + dataSourceName;
            System.out.println("DataSource: " + dataSourceName + "; " + sourceFullName);
            List<ProtocolNode> protocols = null;
            Set<Point> locations = mti.getLocationTracker().getSDRFLocations(dadm);
            Object[] alist = locations.toArray();
            Point sloc = (Point) alist[0];
            Point p = findCell(dataSourceName, sdrfMap);
            if (dataSourceName == null) {
                createEvent("Warning: at least one Derived Array Data Matrix file entry has no name", 25, "validation error", sdrfFileName, sloc.y, p.y);
                check = true;
            }
            if (dadm.protocols != null) protocols = dadm.protocols;
            boolean fail = checkProtocols(protocols);
            if (dataVal == true) {
                List nodes;
                if (this.mti.SDRF.lookupNodes(SourceNode.class) != null) {
                    nodes = (List<SourceNode>) this.mti.SDRF.lookupNodes(SourceNode.class);
                } else if (this.mti.SDRF.lookupNodes(SampleNode.class) != null) {
                    nodes = (List<SampleNode>) this.mti.SDRF.lookupNodes(SampleNode.class);
                } else if (this.mti.SDRF.lookupNodes(ExtractNode.class) != null) {
                    nodes = (List<ExtractNode>) this.mti.SDRF.lookupNodes(ExtractNode.class);
                } else if (this.mti.SDRF.lookupNodes(LabeledExtractNode.class) != null) {
                    nodes = (List<LabeledExtractNode>) this.mti.SDRF.lookupNodes(LabeledExtractNode.class);
                } else if (this.mti.SDRF.lookupNodes(HybridizationNode.class) != null) {
                    nodes = (List<HybridizationNode>) this.mti.SDRF.lookupNodes(HybridizationNode.class);
                } else if (this.mti.SDRF.lookupNodes(AssayNode.class) != null) {
                    nodes = (List<AssayNode>) this.mti.SDRF.lookupNodes(AssayNode.class);
                } else {
                    nodes = new ArrayList<LabeledExtractNode>();
                    createEvent("Unable to match Biomaterial names to Derived Array Data matrix columns", 1024, "validation error", sdrfFileName, sloc.y, p.y);
                }
                BufferedReader br;
                try {
                    br = new BufferedReader(new FileReader(sourceFullName));
                    StringSplitter ss = new StringSplitter((char) 0x09);
                    String currentLine;
                    int ctr = 0;
                    Hashtable header1 = new Hashtable();
                    ArrayList<String> header2 = new ArrayList();
                    while ((currentLine = br.readLine()) != null) {
                        ctr++;
                        while (currentLine.endsWith("\t")) {
                            currentLine = currentLine.substring(0, currentLine.length() - 1);
                        }
                        ss.init(currentLine);
                        int fctr = 0;
                        if (ctr == 1) {
                            fctr++;
                            ss.nextIntToken();
                            while (ss.hasMoreTokens()) {
                                fctr++;
                                String value = ss.nextToken();
                                if (!header1.containsKey(value)) {
                                    header1.put(value, fctr);
                                }
                            }
                            for (Object n : nodes) {
                                String name = "";
                                if (n instanceof SourceNode) {
                                    name = ((SourceNode) n).getNodeName();
                                } else if (n instanceof SampleNode) {
                                    name = ((SampleNode) n).getNodeName();
                                } else if (n instanceof ExtractNode) {
                                    name = ((ExtractNode) n).getNodeName();
                                } else if (n instanceof LabeledExtractNode) {
                                    name = ((LabeledExtractNode) n).getNodeName();
                                } else if (n instanceof HybridizationNode) {
                                    name = ((HybridizationNode) n).getNodeName();
                                } else if (n instanceof AssayNode) {
                                    name = ((AssayNode) n).getNodeName();
                                } else {
                                    name = "[is null]";
                                }
                                if (!header1.containsKey(name)) {
                                    createEvent("BioMaterial name " + name + " not found in Derived Array Data matrix file " + dataSourceName, 1024, "validation error", sdrfFileName, sloc.y, p.y);
                                }
                            }
                        } else if (ctr == 2) {
                            fctr++;
                            ss.nextIntToken();
                            header2.add("null");
                            while (ss.hasMoreTokens()) {
                                fctr++;
                                String value = ss.nextToken();
                                if (value.contains("value")) {
                                    header2.add("number");
                                } else if (value.contains("Value")) {
                                    header2.add("number");
                                } else if (value.contains("VALUE")) {
                                    header2.add("number");
                                } else if (value.contains("Signal")) {
                                    header2.add("number");
                                } else if (value.contains("signal")) {
                                    header2.add("number");
                                } else if (value.contains("ratio")) {
                                    header2.add("number");
                                } else {
                                    header2.add("string");
                                }
                            }
                        } else {
                            fctr++;
                            ss.nextToken();
                            while (ss.hasMoreTokens()) {
                                fctr++;
                                String value = ss.nextToken();
                                String dtype = header2.get(fctr - 1);
                                if (value != null) {
                                    try {
                                        if (dtype.equals("number")) {
                                            if (!value.contains("e")) {
                                                if (!value.contains(".") && !value.contains(",")) {
                                                    int i = Integer.parseInt(value);
                                                } else {
                                                    float f = Float.parseFloat(value);
                                                }
                                            } else {
                                            }
                                        }
                                    } catch (NumberFormatException nfe) {
                                        createEvent("Number Format Exception, value= " + value, 1038, "validation warning", dataSourceName, ctr, fctr);
                                        System.out.println("NumberFormatException in " + dataSourceName + "; line " + ctr + ", column " + fctr + "; value " + value);
                                    }
                                } else {
                                    createEvent("Empty data cell in data matrix", 1038, "validation warning", dataSourceName, ctr, fctr);
                                }
                            }
                        }
                    }
                } catch (FileNotFoundException e) {
                    createEvent("Data file " + dataSourceName + " was not found", 8, "validation error", sdrfFileName, sloc.y, p.y);
                } catch (IOException ioe) {
                    createEvent("Data file " + dataSourceName + " was not readable", 8, "validation error", sdrfFileName, sloc.y, p.y);
                }
            }
            if (check == true) System.out.println(dataSourceName);
        }
        return check;
    }

    boolean checkProtocols(List<ProtocolNode> protocols) {
        boolean check = false;
        for (ProtocolNode protocol : protocols) {
            String protocolName = protocol.getNodeName();
            Set<Point> locations = mti.getLocationTracker().getSDRFLocations(protocol);
            Point p = findCell(protocolName, sdrfMap);
            if (protocolName.equals("")) {
                createEvent("Warning: at least one protocol has no protocol name", 25, "validation error", sdrfFileName, p.x, p.y);
                check = true;
            }
            if (protocol.date != null && protocol.date.equals("")) {
                createEvent("Incomplete information for " + protocolName + "; value for date is missing", 1016, "validation warning", sdrfFileName, p.x, p.y);
            } else if (protocol.date == null) {
                createEvent("Incomplete information for " + protocolName + "; date not supplied", 1016, "validation missingData", sdrfFileName, p.x, p.y);
            } else {
                boolean fail = checkDateTag(protocolName, protocol.date);
                if (fail = true) check = true;
            }
            if (protocol.termSourceREF != null && protocol.termSourceREF.equals("")) {
                createEvent("Incomplete information for " + protocolName + "; value for termSourceREF is missing", 1016, "validation warning", sdrfFileName, p.x, p.y);
            } else if (protocol.termSourceREF == null) {
                createEvent("Incomplete information for " + protocolName + "; termSourceREF not supplied", 1016, "validation missingData", sdrfFileName, p.x, p.y);
            } else if (!tsrStr.contains(protocol.termSourceREF)) {
                createEvent("Term Source REF, " + protocol.termSourceREF + ", for Protocol " + protocolName + " is not declared in the IDF", 6, "validation warning", sdrfFileName, p.x, p.y);
                check = true;
            }
            if (protocol.performer != null && protocol.performer.toString().equals("")) {
                createEvent("Incomplete information for " + protocolName + "; value for performer is missing", 1016, "validation warning", sdrfFileName, p.x, p.y);
            } else if (protocol.performer == null) {
                createEvent("Incomplete information for " + protocolName + "; performer not supplied", 1016, "validation missingData", sdrfFileName, p.x, p.y);
            }
            List<ParameterValueAttribute> paramList = protocol.parameterValues;
            for (ParameterValueAttribute attr : paramList) {
                String attrType = attr.type;
                Point a = findCell(attr.getNodeName(), sdrfMap);
                if (attr != null && attr.getNodeName().equals("")) {
                    createEvent("Incomplete information for " + protocolName + "; Parameter Value " + attrType + " nas no value", 1016, "validation warning", sdrfFileName, p.x, a.y + 1);
                }
                if (attr.unit != null && attr.unit.getNodeName().equals("")) {
                    createEvent("Incomplete information for " + protocolName + "; Parameter Value " + attrType + " has no Units", 1016, "validation warning", sdrfFileName, p.x, a.y + 1);
                } else if (attr.unit != null) {
                    Point u = findCell(attr.unit.getNodeName(), sdrfMap);
                    if (attr.unit.termSourceREF != null && attr.unit.termSourceREF.equals("")) {
                        createEvent("Incomplete information for " + protocolName + "; Parameter Value " + attrType + " has no Unit Term Source", 1005, "validation warning", sdrfFileName, p.x, u.y + 1);
                    } else if (attr.unit.termSourceREF == null) {
                        createEvent("Incomplete information for " + attr.unit + "; termSourceREF not supplied", 1016, "validation missingData", sdrfFileName, p.x, u.y);
                    } else if (!tsrStr.contains(attr.unit.termSourceREF)) {
                        createEvent("Term Source REF, " + attr.unit.termSourceREF + ", for ParameterValue " + attr.unit.getNodeName() + " is not declared in the IDF", 6, "validation warning", sdrfFileName, p.x, u.y + 1);
                        check = true;
                    }
                }
            }
            if (check == true) System.out.println(protocolName);
        }
        return check;
    }

    /**
	 * Checks the References between IDF and SDRF:
	 * ExperimentalFactor <-> FactorValue
	 * Protocol <-> ProtocolREF
	 * Parameter <-> ParameterValue
	 * 
	 * @param mti
	 * @return true if there are any missing or undeclared values
	 */
    protected boolean checkRefs(MAGETABInvestigation mti) {
        boolean fail = true;
        List<String> ef = mti.IDF.experimentalFactorName;
        String efStr = ef.toString();
        List<String> pn = mti.IDF.protocolName;
        String pnStr = pn.toString();
        List<String> pv = mti.IDF.protocolParameters;
        String pvStr = pv.toString();
        List<HybridizationNode> hybridizations = mti.SDRF.lookupNodes(HybridizationNode.class);
        for (HybridizationNode hybridization : hybridizations) {
            String hybridizationName = hybridization.getNodeName();
            List<FactorValueAttribute> fvList = hybridization.factorValues;
            Set<Point> locations = mti.getLocationTracker().getSDRFLocations(hybridization);
            Object[] alist = locations.toArray();
            Point sloc = new Point(-1, -1);
            if (0 < alist.length) {
                sloc = (Point) alist[0];
            }
            for (FactorValueAttribute attr : fvList) {
                String attrName = attr.getNodeName();
                String attrType = attr.type;
                Point p = findCell(attrName, sdrfMap);
                if (!efStr.contains(attrType)) {
                    System.out.println("name: " + attrName + "; type: " + attrType);
                    createEvent("Error: Factor value " + attrType + " is not declared in the IDF", 5, "validation error", sdrfFileName, sloc.y, p.y);
                    fail = true;
                }
                if (attr != null && attr.getNodeName().equals("")) {
                    createEvent("Incomplete information for Hybridization " + hybridizationName + "; Factor value " + attrType + " nas no value", 1027, "validation warning", sdrfFileName, sloc.y, p.y);
                }
            }
        }
        List<ProtocolNode> protocols = mti.SDRF.lookupNodes(ProtocolNode.class);
        for (ProtocolNode protocol : protocols) {
            String protocolName = protocol.getNodeName();
            Set<Point> locations = mti.getLocationTracker().getSDRFLocations(protocol);
            Object[] alist = locations.toArray();
            Point sloc = (Point) alist[0];
            if (!pnStr.contains(protocolName)) {
                Point p = findCell(protocolName, sdrfMap);
                System.out.println(protocolName);
                createEvent("Error: Protocol " + protocolName + " is not declared in the IDF", 7, "validation error", sdrfFileName, sloc.y, p.y);
                fail = true;
            }
            List<ParameterValueAttribute> pvList = protocol.parameterValues;
            for (ParameterValueAttribute attr : pvList) {
                String attrName = attr.getNodeName();
                String attrType = attr.type;
                Point p = findCell(attrName, sdrfMap);
                if (!pvStr.contains(attrType)) {
                    System.out.println("name: " + attrName + "; type: " + attrType);
                    createEvent("Error: Parameter value " + attrType + " is not declared in the IDF", 13, "validation error", sdrfFileName, sloc.y, p.y);
                    fail = true;
                }
                if (attr != null && attr.getNodeName().equals("")) {
                    createEvent("Incomplete information for Protocol " + protocolName + "; Parameter value " + attrType + " nas no value", 1016, "validation warning", sdrfFileName, sloc.y, p.y);
                    fail = true;
                }
            }
        }
        if (testDebug == true) {
            ErrorItem event = eif.generateErrorItem("Check Refs test", ErrorCode.APPLICATION_TEST, this.getClass());
            fireErrorItemEvent(event);
            createEvent("test passed", 998);
        }
        return fail;
    }

    private Point findCell(String errDatum, Hashtable map) {
        Point errCell = new Point(-1, -1);
        Enumeration keys = map.keys();
        while (keys.hasMoreElements()) {
            Point p = (Point) keys.nextElement();
            if (map.get(p).equals(errDatum)) {
                errCell = p;
            }
        }
        if (testDebug == true) {
            if (errCell.x == -1) {
                System.out.println(errDatum);
            }
        }
        return errCell;
    }

    private void createEvent(String comment, int code) {
        createEvent(comment, code, "validation error");
    }

    private void createEvent(String comment, int code, String eType) {
        ErrorCode ec = ErrorCode.getErrorFromCode(code);
        String mesg = ec.getErrorMessage();
        ErrorItem event = eif.generateErrorItem(mesg, code, this.getClass());
        event.setComment(comment);
        event.setErrorType(eType);
        String fileName = null;
        if (mesg.contains("IDF") || mesg.contains("idf")) {
            fileName = idfFileName;
        } else if (mesg.contains("SDRF") || mesg.contains("sdrf")) {
            fileName = sdrfFileName;
        } else if (comment.contains("IDF") || comment.contains("idf")) {
            fileName = idfFileName;
        } else if (comment.contains("SDRF") || comment.contains("sdrf")) {
            fileName = sdrfFileName;
        } else {
            fileName = "";
        }
        event.setParsedFile(fileName);
        fireErrorItemEvent(event);
    }

    /**
	 * Create an event with row and column information
	 * @param comment
	 * @param code
	 * @param eType: error type, One-of: validation error, validation warning, validation missingData
	 * @param fileName
	 * @param line
	 * @param column
	 */
    private void createEvent(String comment, int code, String eType, String fileName, int line, int column) {
        ErrorCode ec = ErrorCode.getErrorFromCode(code);
        String mesg = ec.getErrorMessage();
        ErrorItem event = eif.generateErrorItem(mesg, code, this.getClass());
        event.setComment(comment);
        event.setErrorType(eType);
        event.setLine(line);
        event.setCol(column);
        event.setParsedFile(fileName);
        fireErrorItemEvent(event);
    }
}
