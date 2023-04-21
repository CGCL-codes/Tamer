package org.verus.ngl.sl.bprocess.technicalprocessing;

import compression.IssuesImprovedCompressor;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.verus.ngl.sl.objectmodel.technicalprocessing.CATALOGUERECORD;
import org.verus.ngl.sl.objectmodel.technicalprocessing.SEARCHABLE_CATALOGUERECORD;
import org.verus.ngl.sl.utilities.Connections;
import org.verus.ngl.sl.utilities.NGLBeanFactory;
import org.verus.ngl.utilities.NGLUtility;
import org.verus.ngl.utilities.marc.NGLConverter;
import org.verus.ngl.utilities.marc.NGLMARCRecord;
import org.verus.ngl.utilities.marc.NGLSubfield;
import org.verus.ngl.utilities.marc.NGLTag;
import pa.CaptionsAndPatternsParser;
import util.InBetweenNumbers;

/**
 *
 * @author santosh
 */
public class ViewHoldingsImpl implements ViewHoldings {

    private boolean isSerial = false, isOnlyItemized = false;

    private String callNo;

    @Override
    public String[] generateHoldingsStatement2_2(String catalogueRecordId, String ownerLibraryId, String preferredLibraryId, String dataBaseId) {
        String[] returnValue = null;
        try {
            Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
            Session session = connections.getSession(dataBaseId);
            Connection con = connections.getConnection(dataBaseId);
            Integer catalogId = Integer.parseInt(catalogueRecordId);
            Integer ownerLibId = Integer.parseInt(ownerLibraryId);
            Query query = session.getNamedQuery("CATALOGUERECORD.findByBibiliographic_level_id");
            query.setParameter("cataloguerecordid", catalogId);
            query.setParameter("ownerLibraryId", ownerLibId);
            CATALOGUERECORD catalogueRec = (CATALOGUERECORD) query.uniqueResult();
            String part1 = getLevel1_2_2(session, catalogueRecordId, ownerLibraryId);
            String bibliolevel = NGLUtility.getInstance().getTestedString(catalogueRec.getHoldingsTypeOfRecord());
            System.out.println("part1: " + part1);
            if (bibliolevel.equals("V") || bibliolevel.equals("Y")) {
                returnValue = new String[3];
                returnValue[0] = "SERIAL";
                String[] returnValues = generateSerialHoldings2_2(catalogueRecordId, ownerLibraryId, preferredLibraryId, con);
                returnValue[1] = part1 + "<br>" + returnValues[0];
                returnValue[2] = part1 + "<br>" + returnValues[1];
            } else {
                returnValue = new String[2];
                returnValue[0] = "NONSERIAL";
                returnValue[1] = part1 + "<br>" + generateNonSerialHoldings2_2(catalogueRecordId, ownerLibraryId, preferredLibraryId, con);
            }
        } catch (Exception e) {
        }
        return returnValue;
    }

    public String getLevel1_2_2(Session session, String catRecId, String ownLibId) {
        System.out.println("+++++++++++++getLevel1+++++++++++++");
        String htmPart = "";
        if (catRecId != null && ownLibId != null) {
            try {
                callNo = "";
                Query query = session.getNamedQuery("SEARCHABLE_CATALOGUERECORD.findByCataloguerecordidOwnerLibraryId");
                query.setParameter("cataloguerecordid", Integer.parseInt(catRecId));
                query.setParameter("ownerLibraryId", Integer.parseInt(ownLibId));
                SEARCHABLE_CATALOGUERECORD catRec = (SEARCHABLE_CATALOGUERECORD) query.uniqueResult();
                String xmlWholeRecord = catRec.getXmlWholerecord();
                System.out.println(".....................xmlWholeRecord: " + xmlWholeRecord);
                if (xmlWholeRecord != null) {
                    NGLMARCRecord[] marcRec = NGLConverter.getInstance().getMarcModel(xmlWholeRecord);
                    NGLMARCRecord mARCRecord = marcRec[0];
                    NGLTag[] nglTag = mARCRecord.getTags();
                    for (int i = 0; i < nglTag.length; i++) {
                        NGLSubfield[] nGLSubfield = nglTag[i].getSubfields();
                        for (int j = 0; j < nGLSubfield.length; j++) {
                            String localTag = nglTag[i].getTag() + "_" + nGLSubfield[j].getIdentifier();
                            if (nglTag[i].getTag().equalsIgnoreCase("080") || nglTag[i].getTag().equalsIgnoreCase("082") || nglTag[i].getTag().equalsIgnoreCase("083") || nglTag[i].getTag().equalsIgnoreCase("084") || nglTag[i].getTag().equalsIgnoreCase("085") || nglTag[i].getTag().equalsIgnoreCase("086")) {
                                System.out.println("..................localTag: " + localTag);
                                System.out.println("................subfield Data: " + nGLSubfield[j].getData());
                                callNo += nGLSubfield[j].getData();
                            }
                            if (nglTag[i].getTag().equalsIgnoreCase("020")) {
                                htmPart += "<P><B>ISBN: </B>" + nGLSubfield[j].getData() + "</P>";
                            }
                            if (nglTag[i].getTag().equalsIgnoreCase("022")) {
                                htmPart += "<P><B>ISSN: </B>" + nGLSubfield[j].getData() + "</P>";
                            }
                            if (nglTag[i].getTag().equalsIgnoreCase("030")) {
                                htmPart += "<P><B>CODEN: </B>" + nGLSubfield[j].getData() + "</P>";
                            }
                        }
                    }
                    System.out.println(".........................callNo: " + callNo);
                }
                htmPart += "<P><B>(LocalCATID): </B>" + catRecId.toString() + "_" + ownLibId.toString() + "</P>";
            } catch (Exception e) {
                e.printStackTrace();
            }
            return htmPart;
        }
        return htmPart;
    }

    public String[] generateSerialHoldings2_2(String catalogueRecordId, String ownerLibraryId, String preferredLibraryId, Connection con) {
        String[] retvals = new String[2];
        retvals[0] = generateSerialHoldingsCompressed2_2(catalogueRecordId, ownerLibraryId, preferredLibraryId, con);
        System.out.println("Compressed holdings: " + retvals[0]);
        retvals[1] = generateSerialHoldingsItemized2_2(catalogueRecordId, ownerLibraryId, preferredLibraryId, con);
        System.out.println("Itemozed holdings: " + retvals[1]);
        return retvals;
    }

    public String generateSerialHoldingsCompressed2_2(String catalogueRecordId, String ownerLibraryId, String preferredLibraryId, Connection con) {
        System.out.println("********************* generateSerialHoldingsCompressed2_2 ********************************");
        String compressedSerialHoldings = "";
        try {
            IssuesImprovedCompressor issuesCompressor = IssuesImprovedCompressor.getInstance();
            System.out.println(".........................issuesCompressor: " + issuesCompressor);
            Vector olderBoundReceiptsVec = new Vector();
            Vector compOlderBoundReceiptsVec = new Vector();
            Vector currentReceiptsVec = new Vector();
            Vector compCurrentReceiptsVec = new Vector();
            Vector olderReceiptsVec = new Vector();
            Vector compOlderReceiptsVec = new Vector();
            Statement stat = con.createStatement();
            Hashtable htHoldAll = new Hashtable();
            String sql = "select cap_pat_id,cap_pat,cap_pat_name,cap_pat_type,wef from captions_patterns where cataloguerecordid=" + catalogueRecordId + " and owner_library_id=" + ownerLibraryId + " and cap_pat_type='853' and UPPER(cap_pat_name)='BASICBIBLIOGRAPHICUNIT' order by wef desc";
            System.out.println(sql);
            ResultSet rs = stat.executeQuery(sql);
            System.out.println("########################### # RS  1st Level ###########################");
            while (rs.next()) {
                String capPatId = rs.getString(1);
                String capPat = rs.getString(2);
                String capPatname = rs.getString(3);
                String capPattype = rs.getString(4);
                Timestamp wef = rs.getTimestamp(5);
                String sql2 = "select a.volume_id,a.enum_chrono,b.library_id,a.status,b.archive from cat_volume a, enum_chrono_holdings b where a.volume_id=b.volume_id and a.cap_pat_id=" + capPatId;
                System.out.println(sql2);
                Statement stat2 = con.createStatement();
                ResultSet rs2 = stat2.executeQuery(sql2);
                pa.CaptionsAndPatternsParser parser = new pa.CaptionsAndPatternsParser(capPat);
                String frequency = parser.getFrequency();
                String frequencyName = NGLUtility.getInstance().getFrequencyNameFromCode(frequency);
                if (!frequencyName.equals("")) {
                    compressedSerialHoldings += "<p><b>Frequency: </b>" + frequencyName + "</p>";
                }
                while (rs2.next()) {
                    System.out.println("@@@@@@@@@@@@@@@@@@@@ while (rs2.next())@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                    String volumeId = rs2.getString(1);
                    String enumChrono = rs2.getString(2);
                    String libraryId = rs2.getString(3);
                    String compressStatus = NGLUtility.getInstance().getTestedString(rs2.getString(4));
                    String archiveStatus = NGLUtility.getInstance().getTestedString(rs2.getString(5));
                    System.out.println("........................compressStatus: " + compressStatus);
                    System.out.println("........................archiveStatus: " + archiveStatus);
                    if (htHoldAll.get(libraryId) == null) {
                        Hashtable htLibrary = new Hashtable();
                        htLibrary.put("CurrentReceiptsVec", new Vector(10, 10));
                        htLibrary.put("OlderReceiptsVec", new Vector(10, 10));
                        htLibrary.put("OlderBoundReceiptsVec", new Vector(10, 10));
                        htLibrary.put("CompCurrentReceiptsVec", new Vector(10, 10));
                        htLibrary.put("CompOlderReceiptsVec", new Vector(10, 10));
                        htLibrary.put("CompOlderBoundReceiptsVec", new Vector(10, 10));
                        htHoldAll.put(libraryId, htLibrary);
                    }
                    String sql3 = "select count(*) from complex_volumes where volume_id=" + volumeId;
                    System.out.println(sql3);
                    Statement stat3 = con.createStatement();
                    ResultSet rs3 = stat3.executeQuery(sql3);
                    int count = 0;
                    while (rs3.next()) {
                        count = rs3.getInt(1);
                    }
                    rs3.close();
                    stat3.close();
                    System.out.println("............................count: " + count);
                    if (count > 0) {
                        System.out.println("........... if (count > 0).................");
                        if (compressStatus.equals("") || compressStatus.equals("B")) {
                            Hashtable htLib = (Hashtable) htHoldAll.get(libraryId);
                            ((Vector) htLib.get("OlderBoundReceiptsVec")).addElement(enumChrono);
                            olderBoundReceiptsVec.addElement(enumChrono);
                        } else {
                            Hashtable htLib = (Hashtable) htHoldAll.get(libraryId);
                            ((Vector) htLib.get("CompOlderBoundReceiptsVec")).addElement(enumChrono);
                            compOlderBoundReceiptsVec.addElement(enumChrono);
                        }
                    } else {
                        System.out.println("........... if (count > 0).else ................");
                        if (archiveStatus.equals("") || archiveStatus.equals("B")) {
                            if (compressStatus.equals("") || compressStatus.equals("B")) {
                                Hashtable htLib = (Hashtable) htHoldAll.get(libraryId);
                                ((Vector) htLib.get("CurrentReceiptsVec")).addElement(enumChrono);
                                currentReceiptsVec.addElement(enumChrono);
                            } else {
                                Hashtable htLib = (Hashtable) htHoldAll.get(libraryId);
                                ((Vector) htLib.get("CompCurrentReceiptsVec")).addElement(enumChrono);
                                compCurrentReceiptsVec.addElement(enumChrono);
                            }
                        } else {
                            if (compressStatus.equals("") || compressStatus.equals("B")) {
                                Hashtable htLib = (Hashtable) htHoldAll.get(libraryId);
                                ((Vector) htLib.get("OlderReceiptsVec")).addElement(enumChrono);
                                olderReceiptsVec.addElement(enumChrono);
                            } else {
                                Hashtable htLib = (Hashtable) htHoldAll.get(libraryId);
                                ((Vector) htLib.get("CompOlderReceiptsVec")).addElement(enumChrono);
                                compOlderReceiptsVec.addElement(enumChrono);
                            }
                        }
                    }
                }
                rs2.close();
                stat2.close();
                if (htHoldAll.get(preferredLibraryId) != null) {
                    compressedSerialHoldings += "<table border='1'>";
                    stat2 = con.createStatement();
                    rs2 = stat2.executeQuery("select library_name from library where library_id=" + preferredLibraryId);
                    String libraryName = "";
                    while (rs2.next()) {
                        libraryName = rs2.getString(1);
                    }
                    rs2.close();
                    stat2.close();
                    compressedSerialHoldings += "<tr>";
                    compressedSerialHoldings += "<TD WIDTH=16%><P ALIGN=RIGHT><B>Location:</B></P></TD>";
                    compressedSerialHoldings += "<TD WIDTH=16%><P>" + libraryName + "</P></TD>";
                    compressedSerialHoldings += "</tr>";
                    Hashtable htLibrary = (Hashtable) htHoldAll.get(preferredLibraryId);
                    inflateCompressedHoldingsAndAddToItemizedHoldings(capPat, (Vector) htLibrary.get("CurrentReceiptsVec"), (Vector) htLibrary.get("CompCurrentReceiptsVec"));
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ htLibrary.get(CurrentReceiptsVec): " + htLibrary.get("CurrentReceiptsVec"));
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ capPat: " + capPat);
                    issuesCompressor.setCompressedIssuesOrAndItemizedIssues((Vector) htLibrary.get("CurrentReceiptsVec"), capPat, true, false);
                    String issues = issuesCompressor.getCompressedIssues();
                    System.out.println("New 2_2 current issues: " + issues);
                    compressedSerialHoldings += "<tr>";
                    compressedSerialHoldings += "<TD WIDTH=16%  VALIGN=TOP><P ALIGN=RIGHT><B>Current receipts:</B></P></TD>";
                    compressedSerialHoldings += "<TD WIDTH=16%><P>" + issues + "</P></TD>";
                    compressedSerialHoldings += "</tr>";
                    inflateCompressedHoldingsAndAddToItemizedHoldings(capPat, (Vector) htLibrary.get("OlderReceiptsVec"), (Vector) htLibrary.get("CompOlderReceiptsVec"));
                    issuesCompressor.setCompressedIssuesOrAndItemizedIssues((Vector) htLibrary.get("OlderReceiptsVec"), capPat, true, false);
                    issues = issuesCompressor.getCompressedIssues();
                    System.out.println("New 2_2 older issues: " + issues);
                    compressedSerialHoldings += "<tr>";
                    compressedSerialHoldings += "<TD WIDTH=16%  VALIGN=TOP><P ALIGN=RIGHT><B>Older receipts:</B></P></TD>";
                    compressedSerialHoldings += "<TD WIDTH=16%><P>" + issues + "</P></TD>";
                    compressedSerialHoldings += "</tr>";
                    inflateCompressedHoldingsAndAddToItemizedHoldings(capPat, (Vector) htLibrary.get("OlderBoundReceiptsVec"), (Vector) htLibrary.get("CompOlderBoundReceiptsVec"));
                    issuesCompressor.setCompressedIssuesOrAndItemizedIssues((Vector) htLibrary.get("OlderBoundReceiptsVec"), capPat, true, false);
                    issues = issuesCompressor.getCompressedIssues();
                    System.out.println("New 2_2 older bound issues: " + issues);
                    compressedSerialHoldings += "<tr>";
                    compressedSerialHoldings += "<TD WIDTH=16% VALIGN=TOP><P ALIGN=RIGHT><B>Older receipts(Bound volumes):</B></P></TD>";
                    compressedSerialHoldings += "<TD WIDTH=16%><P>" + issues + "</P></TD>";
                    compressedSerialHoldings += "</tr>";
                    compressedSerialHoldings += "</table>";
                }
                compressedSerialHoldings += "<br>";
                Enumeration enumLibs = htHoldAll.keys();
                while (enumLibs.hasMoreElements()) {
                    String key = enumLibs.nextElement().toString();
                    if (!key.equals(preferredLibraryId)) {
                        compressedSerialHoldings += "<table border='1'>";
                        stat2 = con.createStatement();
                        rs2 = stat2.executeQuery("select library_name from library where library_id=" + key);
                        String libraryName = "";
                        while (rs2.next()) {
                            libraryName = rs2.getString(1);
                        }
                        rs2.close();
                        stat2.close();
                        compressedSerialHoldings += "<tr>";
                        compressedSerialHoldings += "<TD WIDTH=16%><P ALIGN=RIGHT><B>Location:</B></P></TD>";
                        compressedSerialHoldings += "<TD WIDTH=16%><P>" + libraryName + "</P></TD>";
                        compressedSerialHoldings += "</tr>";
                        Hashtable htLibrary = (Hashtable) htHoldAll.get(key);
                        inflateCompressedHoldingsAndAddToItemizedHoldings(capPat, (Vector) htLibrary.get("CurrentReceiptsVec"), (Vector) htLibrary.get("CompCurrentReceiptsVec"));
                        issuesCompressor.setCompressedIssuesOrAndItemizedIssues((Vector) htLibrary.get("CurrentReceiptsVec"), capPat, true, false);
                        String issues = issuesCompressor.getCompressedIssues();
                        System.out.println("New 2_2 current issues: " + issues);
                        compressedSerialHoldings += "<tr>";
                        compressedSerialHoldings += "<TD WIDTH=16% VALIGN=TOP><P ALIGN=RIGHT><B>Current receipts:</B></P></TD>";
                        compressedSerialHoldings += "<TD WIDTH=16%><P>" + issues + "</P></TD>";
                        compressedSerialHoldings += "</tr>";
                        inflateCompressedHoldingsAndAddToItemizedHoldings(capPat, (Vector) htLibrary.get("OlderReceiptsVec"), (Vector) htLibrary.get("CompOlderReceiptsVec"));
                        issuesCompressor.setCompressedIssuesOrAndItemizedIssues((Vector) htLibrary.get("OlderReceiptsVec"), capPat, true, false);
                        issues = issuesCompressor.getCompressedIssues();
                        System.out.println("New 2_2 older issues: " + issues);
                        compressedSerialHoldings += "<tr>";
                        compressedSerialHoldings += "<TD WIDTH=16% VALIGN=TOP><P ALIGN=RIGHT><B>Older receipts:</B></P></TD>";
                        compressedSerialHoldings += "<TD WIDTH=16%><P>" + issues + "</P></TD>";
                        compressedSerialHoldings += "</tr>";
                        inflateCompressedHoldingsAndAddToItemizedHoldings(capPat, (Vector) htLibrary.get("OlderBoundReceiptsVec"), (Vector) htLibrary.get("CompOlderBoundReceiptsVec"));
                        issuesCompressor.setCompressedIssuesOrAndItemizedIssues((Vector) htLibrary.get("OlderBoundReceiptsVec"), capPat, true, false);
                        issues = issuesCompressor.getCompressedIssues();
                        System.out.println("New 2_2 older bound issues: " + issues);
                        compressedSerialHoldings += "<tr>";
                        compressedSerialHoldings += "<TD WIDTH=16% VALIGN=TOP><P ALIGN=RIGHT><B>Older receipts(Bound volumes):</B></P></TD>";
                        compressedSerialHoldings += "<TD WIDTH=16%><P>" + issues + "</P></TD>";
                        compressedSerialHoldings += "</tr>";
                        compressedSerialHoldings += "</table>";
                    }
                }
            }
            stat.close();
            rs.close();
            System.out.println("########################### # RS  2nd Level ###########################");
            String textholding = "select textual_holdings from textual_holdings where cataloguerecordid=" + catalogueRecordId + " and owner_library_id=" + ownerLibraryId + " and textual_holdings_type='866'";
            Statement stmhold = con.createStatement();
            ResultSet rshold = stmhold.executeQuery(textholding);
            String texthold = "";
            while (rshold.next()) {
                String textholdid = rshold.getString(1);
                texthold = texthold + textholdid;
                texthold += "<br>";
            }
            rshold.close();
            stmhold.close();
            if (!texthold.equals("")) {
                compressedSerialHoldings += "<table border='1'>";
                compressedSerialHoldings += "<tr>";
                compressedSerialHoldings += "<TD WIDTH=16% VALIGN=TOP><P ALIGN=RIGHT><B>Older receipts(Textual):</B></P></TD>";
                compressedSerialHoldings += "<TD WIDTH=16%><P>" + texthold + "</P></TD>";
                compressedSerialHoldings += "</tr>";
                compressedSerialHoldings += "</table>";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return compressedSerialHoldings;
    }

    public String generateSerialHoldingsItemized2_2(String catalogueRecordId, String ownerLibraryId, String preferredLibraryId, Connection con) {
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@ generateSerialHoldingsItemized2_2 @@@@@@@@@@@@@@@@@@@@@@@@@@@");
        String compressedSerialHoldings = "";
        try {
            IssuesImprovedCompressor issuesCompressor = compression.IssuesImprovedCompressor.getInstance();
            Vector olderBoundReceiptsVec = new Vector();
            Vector compOlderBoundReceiptsVec = new Vector();
            Vector currentReceiptsVec = new Vector();
            Vector compCurrentReceiptsVec = new Vector();
            Vector olderReceiptsVec = new Vector();
            Vector compOlderReceiptsVec = new Vector();
            java.sql.Statement stat = con.createStatement();
            String sqlq = "select cap_pat_id,cap_pat,cap_pat_name,cap_pat_type,wef from captions_patterns where cataloguerecordid=" + catalogueRecordId + " and owner_library_id=" + ownerLibraryId;
            System.out.println(sqlq);
            String textholding = "select textualholdingsid from textual_holdings where cataloguerecordid=" + catalogueRecordId + " and owner_library_id=" + ownerLibraryId + " and textual_holdings_type='866'";
            java.sql.ResultSet rs = stat.executeQuery(sqlq);
            Hashtable htHoldAll = new Hashtable();
            while (rs != null && rs.next()) {
                String capPatId = rs.getString(1);
                String capPat = rs.getString(2);
                String capPatname = rs.getString(3);
                String capPattype = rs.getString(4);
                Timestamp wef = rs.getTimestamp(5);
                String sql2 = "select a.volume_id,a.enum_chrono,b.library_id,a.status,b.archive from cat_volume a, enum_chrono_holdings b where a.volume_id=b.volume_id and a.cap_pat_id=" + capPatId;
                Statement stat2 = con.createStatement();
                ResultSet rs2 = stat2.executeQuery(sql2);
                while (rs2.next()) {
                    String volumeId = rs2.getString(1);
                    String enumChrono = rs2.getString(2);
                    String libraryId = rs2.getString(3);
                    String compressStatus = NGLUtility.getInstance().getTestedString(rs2.getString(4));
                    String archiveStatus = NGLUtility.getInstance().getTestedString(rs2.getString(5));
                    if (htHoldAll.get(libraryId) == null) {
                        Hashtable htCaptionMeta = new Hashtable();
                        Hashtable htCaption = new Hashtable();
                        htCaption.put("CaptionName", capPatname);
                        htCaption.put("CaptionType", capPattype);
                        htCaption.put("Caption", capPat);
                        htCaption.put("CurrentReceiptsVec", new Vector(10, 10));
                        htCaption.put("OlderReceiptsVec", new Vector(10, 10));
                        htCaption.put("OlderBoundReceiptsVec", new Vector(10, 10));
                        htCaption.put("CompCurrentReceiptsVec", new Vector(10, 10));
                        htCaption.put("CompOlderReceiptsVec", new Vector(10, 10));
                        htCaption.put("CompOlderBoundReceiptsVec", new Vector(10, 10));
                        htCaptionMeta.put(capPatId, htCaption);
                        htHoldAll.put(libraryId, htCaptionMeta);
                    } else {
                        Hashtable htCaptionMeta = (Hashtable) htHoldAll.get(libraryId);
                        if (htCaptionMeta.get(capPatId) == null) {
                            Hashtable htCaption = new Hashtable();
                            htCaption.put("CaptionName", capPatname);
                            htCaption.put("CaptionType", capPattype);
                            htCaption.put("Caption", capPat);
                            htCaption.put("CurrentReceiptsVec", new Vector(10, 10));
                            htCaption.put("OlderReceiptsVec", new Vector(10, 10));
                            htCaption.put("OlderBoundReceiptsVec", new Vector(10, 10));
                            htCaption.put("CompCurrentReceiptsVec", new Vector(10, 10));
                            htCaption.put("CompOlderReceiptsVec", new Vector(10, 10));
                            htCaption.put("CompOlderBoundReceiptsVec", new Vector(10, 10));
                            htCaptionMeta.put(capPatId, htCaption);
                        }
                    }
                    String sql3 = "select count(*) from complex_volumes where volume_id=" + volumeId;
                    Statement stat3 = con.createStatement();
                    System.out.println(sql3);
                    ResultSet rs3 = stat3.executeQuery(sql3);
                    int count = 0;
                    while (rs3.next()) {
                        count = rs3.getInt(1);
                    }
                    rs3.close();
                    stat3.close();
                    if (count > 0) {
                        if (compressStatus.equals("") || compressStatus.equals("B")) {
                            Hashtable htCaptionMeta = (Hashtable) htHoldAll.get(libraryId);
                            Hashtable htLib = (Hashtable) htCaptionMeta.get(capPatId);
                            ((Vector) htLib.get("OlderBoundReceiptsVec")).addElement(enumChrono);
                            olderBoundReceiptsVec.addElement(enumChrono);
                        } else {
                            Hashtable htCaptionMeta = (Hashtable) htHoldAll.get(libraryId);
                            Hashtable htLib = (Hashtable) htCaptionMeta.get(capPatId);
                            ((Vector) htLib.get("CompOlderBoundReceiptsVec")).addElement(enumChrono);
                            compOlderBoundReceiptsVec.addElement(enumChrono);
                        }
                    } else {
                        if (archiveStatus.equals("") || archiveStatus.equals("B")) {
                            if (compressStatus.equals("") || compressStatus.equals("B")) {
                                Hashtable htCaptionMeta = (Hashtable) htHoldAll.get(libraryId);
                                Hashtable htLib = (Hashtable) htCaptionMeta.get(capPatId);
                                ((Vector) htLib.get("CurrentReceiptsVec")).addElement(enumChrono);
                                currentReceiptsVec.addElement(enumChrono);
                            } else {
                                Hashtable htCaptionMeta = (Hashtable) htHoldAll.get(libraryId);
                                Hashtable htLib = (Hashtable) htCaptionMeta.get(capPatId);
                                ((Vector) htLib.get("CompCurrentReceiptsVec")).addElement(enumChrono);
                                compCurrentReceiptsVec.addElement(enumChrono);
                            }
                        } else {
                            if (compressStatus.equals("") || compressStatus.equals("B")) {
                                Hashtable htCaptionMeta = (Hashtable) htHoldAll.get(libraryId);
                                Hashtable htLib = (Hashtable) htCaptionMeta.get(capPatId);
                                ((Vector) htLib.get("OlderReceiptsVec")).addElement(enumChrono);
                                olderReceiptsVec.addElement(enumChrono);
                            } else {
                                Hashtable htCaptionMeta = (Hashtable) htHoldAll.get(libraryId);
                                Hashtable htLib = (Hashtable) htCaptionMeta.get(capPatId);
                                ((Vector) htLib.get("CompOlderReceiptsVec")).addElement(enumChrono);
                                compOlderReceiptsVec.addElement(enumChrono);
                            }
                        }
                    }
                }
                rs2.close();
                stat2.close();
            }
            stat.close();
            String textholding1 = "select textual_holdings from textual_holdings where cataloguerecordid=" + catalogueRecordId + " and owner_library_id=" + ownerLibraryId;
            Statement stmhold = con.createStatement();
            ResultSet rshold = stmhold.executeQuery(textholding1);
            String texthold = "";
            while (rshold.next()) {
                String textholdid = rshold.getString(1);
                texthold = texthold + textholdid;
                texthold += "<br>";
            }
            rshold.close();
            stmhold.close();
            if (htHoldAll.get(preferredLibraryId) != null) {
                compressedSerialHoldings += "<table border='1'>";
                Statement stat2 = con.createStatement();
                ResultSet rs2 = stat2.executeQuery("select library_name from library where library_id=" + preferredLibraryId);
                String libraryName = "";
                while (rs2.next()) {
                    libraryName = rs2.getString(1);
                }
                rs2.close();
                stat2.close();
                compressedSerialHoldings += "<tr>";
                compressedSerialHoldings += "<TD WIDTH=16%><P ALIGN=RIGHT><B>Location:</B></P></TD>";
                compressedSerialHoldings += "<TD WIDTH=16%><P>" + libraryName + "</P></TD>";
                compressedSerialHoldings += "</tr>";
                Hashtable htCaptionMeta = (Hashtable) htHoldAll.get(preferredLibraryId);
                Enumeration enumCaptions = htCaptionMeta.keys();
                compressedSerialHoldings += "<tr>";
                compressedSerialHoldings += "<TD WIDTH=16% VALIGN=TOP><P ALIGN=RIGHT><B>Current receipts:</B></P></TD>";
                compressedSerialHoldings += "<TD WIDTH=16%>";
                while (enumCaptions.hasMoreElements()) {
                    String key = enumCaptions.nextElement().toString();
                    Hashtable htLibrary = (Hashtable) htCaptionMeta.get(key);
                    String capPat = htLibrary.get("Caption").toString();
                    CaptionsAndPatternsParser cpParser = new CaptionsAndPatternsParser(capPat);
                    String capPatname = htLibrary.get("CaptionName").toString();
                    String capPatType = htLibrary.get("CaptionType").toString();
                    inflateCompressedHoldingsAndAddToItemizedHoldings(capPat, (Vector) htLibrary.get("CurrentReceiptsVec"), (Vector) htLibrary.get("CompCurrentReceiptsVec"));
                    issuesCompressor.setCompressedIssuesOrAndItemizedIssues((Vector) htLibrary.get("CurrentReceiptsVec"), capPat, false, true);
                    String issues = issuesCompressor.getItemizedIssues();
                    issues = paintAlternateIssuesBackgroundBlue(issues);
                    System.out.println("New 2_2 current issues: " + issues);
                    String frequency = cpParser.getFrequency();
                    String frequencyName = NGLUtility.getInstance().getFrequencyNameFromCode(frequency);
                    System.out.println("######### $$$$$$$$$ cpParser.getCaptionName(): " + cpParser.getCaptionName() + " ((Vector) htLibrary.get(\"CurrentReceiptsVec\")).size(): " + ((Vector) htLibrary.get("CurrentReceiptsVec")).size() + " capPatType: " + capPatType);
                    compressedSerialHoldings += "<p>";
                    compressedSerialHoldings += "<b>";
                    if (cpParser.getCaptionName() != null && !cpParser.getCaptionName().equals("") && ((Vector) htLibrary.get("CurrentReceiptsVec")).size() > 0) {
                        if (capPatType.equals("853")) {
                            compressedSerialHoldings += "Regular issues [" + cpParser.getCaptionName() + "]<br>";
                        } else if (capPatType.equals("854")) {
                            compressedSerialHoldings += "Supplementary material [" + cpParser.getCaptionName() + "]<br>";
                        } else {
                            compressedSerialHoldings += "Index [" + cpParser.getCaptionName() + "]<br>";
                        }
                    }
                    if (!frequencyName.equals("") && ((Vector) htLibrary.get("CurrentReceiptsVec")).size() > 0) {
                        compressedSerialHoldings += "Frequency: " + frequencyName;
                    }
                    compressedSerialHoldings += "</b>";
                    compressedSerialHoldings += "</p>";
                    compressedSerialHoldings += issues;
                }
                compressedSerialHoldings += "</TD>";
                compressedSerialHoldings += "</tr>";
                enumCaptions = htCaptionMeta.keys();
                compressedSerialHoldings += "<tr>";
                compressedSerialHoldings += "<TD WIDTH=16% VALIGN=TOP><P ALIGN=RIGHT><B>Older receipts:</B></P></TD>";
                compressedSerialHoldings += "<TD WIDTH=16%>";
                while (enumCaptions.hasMoreElements()) {
                    String key = enumCaptions.nextElement().toString();
                    Hashtable htLibrary = (Hashtable) htCaptionMeta.get(key);
                    String capPat = htLibrary.get("Caption").toString();
                    CaptionsAndPatternsParser cpParser = new CaptionsAndPatternsParser(capPat);
                    String capPatname = htLibrary.get("CaptionName").toString();
                    String capPatType = htLibrary.get("CaptionType").toString();
                    inflateCompressedHoldingsAndAddToItemizedHoldings(capPat, (Vector) htLibrary.get("OlderReceiptsVec"), (Vector) htLibrary.get("CompOlderReceiptsVec"));
                    issuesCompressor.setCompressedIssuesOrAndItemizedIssues((Vector) htLibrary.get("OlderReceiptsVec"), capPat, false, true);
                    String issues = issuesCompressor.getItemizedIssues();
                    issues = paintAlternateIssuesBackgroundBlue(issues);
                    System.out.println("New 2_2 older issues: " + issues);
                    String frequency = cpParser.getFrequency();
                    String frequencyName = NGLUtility.getInstance().getFrequencyNameFromCode(frequency);
                    compressedSerialHoldings += "<p>";
                    compressedSerialHoldings += "<b>";
                    if (cpParser.getCaptionName() != null && !cpParser.getCaptionName().equals("") && ((Vector) htLibrary.get("OlderReceiptsVec")).size() > 0) {
                        if (capPatType.equals("853")) {
                            compressedSerialHoldings += "Regular issues [" + cpParser.getCaptionName() + "]<br>";
                        } else if (capPatType.equals("854")) {
                            compressedSerialHoldings += "Supplementary material [" + cpParser.getCaptionName() + "]<br>";
                        } else {
                            compressedSerialHoldings += "Index [" + cpParser.getCaptionName() + "]<br>";
                        }
                    }
                    if (!frequencyName.equals("") && ((Vector) htLibrary.get("OlderReceiptsVec")).size() > 0) {
                        compressedSerialHoldings += "Frequency: " + frequencyName;
                    }
                    compressedSerialHoldings += "</b>";
                    compressedSerialHoldings += "</p>";
                    compressedSerialHoldings += issues;
                }
                compressedSerialHoldings += "</TD>";
                compressedSerialHoldings += "</tr>";
                enumCaptions = htCaptionMeta.keys();
                compressedSerialHoldings += "<tr>";
                compressedSerialHoldings += "<TD WIDTH=16% VALIGN=TOP><P ALIGN=RIGHT><B>Older receipts (Bound volumes):</B></P></TD>";
                compressedSerialHoldings += "<TD WIDTH=16%>";
                while (enumCaptions.hasMoreElements()) {
                    String key = enumCaptions.nextElement().toString();
                    Hashtable htLibrary = (Hashtable) htCaptionMeta.get(key);
                    String capPat = htLibrary.get("Caption").toString();
                    CaptionsAndPatternsParser cpParser = new CaptionsAndPatternsParser(capPat);
                    String capPatname = htLibrary.get("CaptionName").toString();
                    String capPatType = htLibrary.get("CaptionType").toString();
                    inflateCompressedHoldingsAndAddToItemizedHoldings(capPat, (Vector) htLibrary.get("OlderBoundReceiptsVec"), (Vector) htLibrary.get("CompOlderBoundReceiptsVec"));
                    issuesCompressor.setCompressedIssuesOrAndItemizedIssues((Vector) htLibrary.get("OlderBoundReceiptsVec"), capPat, false, true);
                    String issues = issuesCompressor.getItemizedIssues();
                    issues = paintAlternateIssuesBackgroundBlue(issues);
                    System.out.println("New 2_2 older bound issues: " + issues);
                    String frequency = cpParser.getFrequency();
                    String frequencyName = NGLUtility.getInstance().getFrequencyNameFromCode(frequency);
                    compressedSerialHoldings += "<p>";
                    compressedSerialHoldings += "<b>";
                    if (cpParser.getCaptionName() != null && !cpParser.getCaptionName().equals("") && ((Vector) htLibrary.get("OlderBoundReceiptsVec")).size() > 0) {
                        if (capPatType.equals("853")) {
                            compressedSerialHoldings += "Regular issues [" + cpParser.getCaptionName() + "]<br>";
                        } else if (capPatType.equals("854")) {
                            compressedSerialHoldings += "Supplementary material [" + cpParser.getCaptionName() + "]<br>";
                        } else {
                            compressedSerialHoldings += "Index [" + cpParser.getCaptionName() + "]<br>";
                        }
                    }
                    if (!frequencyName.equals("") && ((Vector) htLibrary.get("OlderBoundReceiptsVec")).size() > 0) {
                        compressedSerialHoldings += "Frequency: " + frequencyName;
                    }
                    compressedSerialHoldings += "</b>";
                    compressedSerialHoldings += "</p>";
                    compressedSerialHoldings += issues;
                }
                compressedSerialHoldings += "</TD>";
                compressedSerialHoldings += "</tr>";
                compressedSerialHoldings += "</table>";
            }
            compressedSerialHoldings += "<br>";
            Enumeration enumLibs = htHoldAll.keys();
            while (enumLibs.hasMoreElements()) {
                String keyLibId = enumLibs.nextElement().toString();
                if (!keyLibId.equals(preferredLibraryId)) {
                    compressedSerialHoldings += "<table border='1'>";
                    Statement stat2 = con.createStatement();
                    ResultSet rs2 = stat2.executeQuery("select library_name from library where library_id=" + keyLibId);
                    String libraryName = "";
                    while (rs2.next()) {
                        libraryName = rs2.getString(1);
                    }
                    rs2.close();
                    stat2.close();
                    compressedSerialHoldings += "<tr>";
                    compressedSerialHoldings += "<TD WIDTH=16%><P ALIGN=RIGHT><B>Location:</B></P></TD>";
                    compressedSerialHoldings += "<TD WIDTH=16%><P>" + libraryName + "</P></TD>";
                    compressedSerialHoldings += "</tr>";
                    Hashtable htCaptionMeta = (Hashtable) htHoldAll.get(keyLibId);
                    Enumeration enumCaptions = htCaptionMeta.keys();
                    compressedSerialHoldings += "<tr>";
                    compressedSerialHoldings += "<TD WIDTH=16% VALIGN=TOP><P ALIGN=RIGHT><B>Current receipts:</B></P></TD>";
                    compressedSerialHoldings += "<TD WIDTH=16%>";
                    while (enumCaptions.hasMoreElements()) {
                        String key = enumCaptions.nextElement().toString();
                        Hashtable htLibrary = (Hashtable) htCaptionMeta.get(key);
                        String capPat = htLibrary.get("Caption").toString();
                        CaptionsAndPatternsParser cpParser = new CaptionsAndPatternsParser(capPat);
                        String capPatname = htLibrary.get("CaptionName").toString();
                        String capPatType = htLibrary.get("CaptionType").toString();
                        inflateCompressedHoldingsAndAddToItemizedHoldings(capPat, (Vector) htLibrary.get("CurrentReceiptsVec"), (Vector) htLibrary.get("CompCurrentReceiptsVec"));
                        issuesCompressor.setCompressedIssuesOrAndItemizedIssues((Vector) htLibrary.get("CurrentReceiptsVec"), capPat, false, true);
                        String issues = issuesCompressor.getItemizedIssues();
                        issues = paintAlternateIssuesBackgroundBlue(issues);
                        System.out.println("New 2_2 current issues: " + issues);
                        String frequency = cpParser.getFrequency();
                        String frequencyName = NGLUtility.getInstance().getFrequencyNameFromCode(frequency);
                        compressedSerialHoldings += "<p>";
                        compressedSerialHoldings += "<b>";
                        if (cpParser.getCaptionName() != null && !cpParser.getCaptionName().equals("") && ((Vector) htLibrary.get("CurrentReceiptsVec")).size() > 0) {
                            if (capPatType.equals("853")) {
                                compressedSerialHoldings += "Regular issues [" + cpParser.getCaptionName() + "]<br>";
                            } else if (capPatType.equals("854")) {
                                compressedSerialHoldings += "Supplementary material [" + cpParser.getCaptionName() + "]<br>";
                            } else {
                                compressedSerialHoldings += "Index [" + cpParser.getCaptionName() + "]<br>";
                            }
                        }
                        if (!frequencyName.equals("") && ((Vector) htLibrary.get("CurrentReceiptsVec")).size() > 0) {
                            compressedSerialHoldings += "Frequency: " + frequencyName;
                        }
                        compressedSerialHoldings += "</b>";
                        compressedSerialHoldings += "</p>";
                        compressedSerialHoldings += issues;
                    }
                    compressedSerialHoldings += "</TD>";
                    compressedSerialHoldings += "</tr>";
                    enumCaptions = htCaptionMeta.keys();
                    compressedSerialHoldings += "<tr>";
                    compressedSerialHoldings += "<TD WIDTH=16% VALIGN=TOP><P ALIGN=RIGHT><B>Older receipts:</B></P></TD>";
                    compressedSerialHoldings += "<TD WIDTH=16%>";
                    while (enumCaptions.hasMoreElements()) {
                        String key = enumCaptions.nextElement().toString();
                        Hashtable htLibrary = (Hashtable) htCaptionMeta.get(key);
                        String capPat = htLibrary.get("Caption").toString();
                        CaptionsAndPatternsParser cpParser = new CaptionsAndPatternsParser(capPat);
                        String capPatname = htLibrary.get("CaptionName").toString();
                        String capPatType = htLibrary.get("CaptionType").toString();
                        inflateCompressedHoldingsAndAddToItemizedHoldings(capPat, (Vector) htLibrary.get("OlderReceiptsVec"), (Vector) htLibrary.get("CompOlderReceiptsVec"));
                        issuesCompressor.setCompressedIssuesOrAndItemizedIssues((Vector) htLibrary.get("OlderReceiptsVec"), capPat, false, true);
                        String issues = issuesCompressor.getItemizedIssues();
                        issues = paintAlternateIssuesBackgroundBlue(issues);
                        System.out.println("New 2_2 older issues: " + issues);
                        String frequency = cpParser.getFrequency();
                        String frequencyName = NGLUtility.getInstance().getFrequencyNameFromCode(frequency);
                        compressedSerialHoldings += "<p>";
                        compressedSerialHoldings += "<b>";
                        if (cpParser.getCaptionName() != null && !cpParser.getCaptionName().equals("") && ((Vector) htLibrary.get("OlderReceiptsVec")).size() > 0) {
                            if (capPatType.equals("853")) {
                                compressedSerialHoldings += "Regular issues [" + cpParser.getCaptionName() + "]<br>";
                            } else if (capPatType.equals("854")) {
                                compressedSerialHoldings += "Supplementary material [" + cpParser.getCaptionName() + "]<br>";
                            } else {
                                compressedSerialHoldings += "Index [" + cpParser.getCaptionName() + "]<br>";
                            }
                        }
                        if (!frequencyName.equals("") && ((Vector) htLibrary.get("OlderReceiptsVec")).size() > 0) {
                            compressedSerialHoldings += "Frequency: " + frequencyName;
                        }
                        compressedSerialHoldings += "</b>";
                        compressedSerialHoldings += "</p>";
                        compressedSerialHoldings += issues;
                    }
                    compressedSerialHoldings += "</TD>";
                    compressedSerialHoldings += "</tr>";
                    enumCaptions = htCaptionMeta.keys();
                    compressedSerialHoldings += "<tr>";
                    compressedSerialHoldings += "<TD WIDTH=16% VALIGN=TOP><P ALIGN=RIGHT><B>Older receipts (Bound volumes):</B></P></TD>";
                    compressedSerialHoldings += "<TD WIDTH=16%>";
                    while (enumCaptions.hasMoreElements()) {
                        String key = enumCaptions.nextElement().toString();
                        Hashtable htLibrary = (Hashtable) htCaptionMeta.get(key);
                        String capPat = htLibrary.get("Caption").toString();
                        CaptionsAndPatternsParser cpParser = new CaptionsAndPatternsParser(capPat);
                        String capPatname = htLibrary.get("CaptionName").toString();
                        String capPatType = htLibrary.get("CaptionType").toString();
                        inflateCompressedHoldingsAndAddToItemizedHoldings(capPat, (Vector) htLibrary.get("OlderBoundReceiptsVec"), (Vector) htLibrary.get("CompOlderBoundReceiptsVec"));
                        issuesCompressor.setCompressedIssuesOrAndItemizedIssues((Vector) htLibrary.get("OlderBoundReceiptsVec"), capPat, false, true);
                        String issues = issuesCompressor.getItemizedIssues();
                        issues = paintAlternateIssuesBackgroundBlue(issues);
                        System.out.println("New 2_2 older issues: " + issues);
                        String frequency = cpParser.getFrequency();
                        String frequencyName = NGLUtility.getInstance().getFrequencyNameFromCode(frequency);
                        compressedSerialHoldings += "<p>";
                        compressedSerialHoldings += "<b>";
                        if (cpParser.getCaptionName() != null && !cpParser.getCaptionName().equals("") && ((Vector) htLibrary.get("OlderBoundReceiptsVec")).size() > 0) {
                            if (capPatType.equals("853")) {
                                compressedSerialHoldings += "Regular issues [" + cpParser.getCaptionName() + "]<br>";
                            } else if (capPatType.equals("854")) {
                                compressedSerialHoldings += "Supplementary material [" + cpParser.getCaptionName() + "]<br>";
                            } else {
                                compressedSerialHoldings += "Index [" + cpParser.getCaptionName() + "]<br>";
                            }
                        }
                        if (!frequencyName.equals("") && ((Vector) htLibrary.get("OlderBoundReceiptsVec")).size() > 0) {
                            compressedSerialHoldings += "Frequency: " + frequencyName;
                        }
                        compressedSerialHoldings += "</b>";
                        compressedSerialHoldings += "</p>";
                        compressedSerialHoldings += issues;
                    }
                    compressedSerialHoldings += "</TD>";
                    compressedSerialHoldings += "</tr>";
                    compressedSerialHoldings += "</table>";
                }
            }
            if (!texthold.equals("")) {
                compressedSerialHoldings += "<table border='1'>";
                compressedSerialHoldings += "<tr>";
                compressedSerialHoldings += "<TD WIDTH=16% VALIGN=TOP><P ALIGN=RIGHT><B>Older receipts(Textual):</B></P></TD>";
                compressedSerialHoldings += "<TD WIDTH=16%><P>" + texthold + "</P></TD>";
                compressedSerialHoldings += "</tr>";
                compressedSerialHoldings += "</table>";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return compressedSerialHoldings;
    }

    public String generateNonSerialHoldings2_2(String catalogueRecordId, String ownerLibraryId, String preferredLibraryId, Connection con) {
        System.out.println("...................generateNonSerialHoldings2_2............................");
        String holdingsText = "";
        try {
            Statement stat = con.createStatement();
            String sql = "select a.volume_id, a.volume_no,a.part_sub_division,a.enum_chrono,a.cap_pat_id,b.accession_number,b.library_id,b.material_type_id,b.location_id,b.barcode,b.book_number,b.classification_number,b.call_number,b.status,c.location,c.coordinates,d.library_name,e.material_type from cat_volume a, document b, location c, library d, adm_co_material_type e where a.cataloguerecordid=" + catalogueRecordId + " and a.owner_library_id=" + ownerLibraryId + " and a.volume_id=b.volume_id and b.library_id=c.library_id and b.location_id=c.location_id and b.library_id=d.library_id and b.material_type_id=e.material_type_id";
            System.out.println(sql);
            ResultSet rs = stat.executeQuery(sql);
            Hashtable htHoldAll = new Hashtable();
            while (rs.next()) {
                String volumeid = rs.getString(1);
                String volumeno = NGLUtility.getInstance().getTestedString(rs.getString(2));
                String part = NGLUtility.getInstance().getTestedString(rs.getString(3));
                String enumchrono = NGLUtility.getInstance().getTestedString(rs.getString(4));
                String cappat = NGLUtility.getInstance().getTestedString(rs.getString(5));
                String accessionnumber = NGLUtility.getInstance().getTestedString(rs.getString(6));
                String libraryid = NGLUtility.getInstance().getTestedString(rs.getString(7));
                String materialTypeId = NGLUtility.getInstance().getTestedString(rs.getString(8));
                String locationId = NGLUtility.getInstance().getTestedString(rs.getString(9));
                String barcode = NGLUtility.getInstance().getTestedString(rs.getString(10));
                String bookNumber = NGLUtility.getInstance().getTestedString(rs.getString(11));
                String classificationNo = NGLUtility.getInstance().getTestedString(rs.getString(12));
                String callNo = NGLUtility.getInstance().getTestedString(rs.getString(13));
                String status = NGLUtility.getInstance().getTestedString(rs.getString(14));
                String statusName = NGLUtility.getInstance().getStatusName(status);
                String location = NGLUtility.getInstance().getTestedString(rs.getString(15));
                String coordinates = NGLUtility.getInstance().getTestedString(rs.getString(16));
                String libraryName = NGLUtility.getInstance().getTestedString(rs.getString(17));
                String physicalName = NGLUtility.getInstance().getTestedString(rs.getString(18));
                String cappatxml = "";
                if (!cappat.equals("")) {
                    Statement stat2 = con.createStatement();
                    String sql2 = "select cap_pat from captions_patterns where cap_pat_id=" + cappat + " and cataloguerecordid=" + catalogueRecordId + " and owner_library_id=" + ownerLibraryId;
                    System.out.println(sql2);
                    ResultSet rs2 = stat2.executeQuery(sql2);
                    while (rs2.next()) {
                        cappatxml = rs2.getString(1);
                    }
                    rs2.close();
                    stat2.close();
                }
                if (htHoldAll.get(libraryid) == null) {
                    Hashtable htOneVolume = new Hashtable();
                    if (!cappatxml.equals("")) {
                        htOneVolume.put("VolumeDisplay", util.Utility.getInstance().getEnumChronoDisplayString(cappatxml, enumchrono));
                    }
                    Vector vecData = new Vector(1, 1);
                    String[] str = new String[8];
                    str[0] = accessionnumber;
                    str[1] = barcode;
                    str[2] = physicalName;
                    str[3] = statusName;
                    str[4] = location;
                    str[5] = coordinates;
                    str[6] = classificationNo;
                    str[7] = callNo;
                    vecData.addElement(str);
                    htOneVolume.put("CallNumber", callNo);
                    htOneVolume.put("Classification", classificationNo);
                    htOneVolume.put("Copies", vecData);
                    Hashtable htVolume = new Hashtable();
                    htVolume.put(volumeid, htOneVolume);
                    htHoldAll.put(libraryid, htVolume);
                } else {
                    Hashtable htVolume = (Hashtable) htHoldAll.get(libraryid);
                    if (htVolume.get(volumeid) == null) {
                        Hashtable htOneVolume = new Hashtable();
                        if (!cappatxml.equals("")) {
                            htOneVolume.put("VolumeDisplay", util.Utility.getInstance().getEnumChronoDisplayString(cappatxml, enumchrono));
                        }
                        Vector vecData = new Vector(1, 1);
                        String[] str = new String[8];
                        str[0] = accessionnumber;
                        str[1] = barcode;
                        str[2] = physicalName;
                        str[3] = statusName;
                        str[4] = location;
                        str[5] = coordinates;
                        str[6] = classificationNo;
                        str[7] = callNo;
                        vecData.addElement(str);
                        htOneVolume.put("CallNumber", callNo);
                        htOneVolume.put("Classification", classificationNo);
                        htOneVolume.put("Copies", vecData);
                        htVolume.put(volumeid, htOneVolume);
                    } else {
                        Hashtable htOneVolume = (Hashtable) htVolume.get(volumeid);
                        if (!cappatxml.equals("")) {
                            htOneVolume.put("VolumeDisplay", util.Utility.getInstance().getEnumChronoDisplayString(cappatxml, enumchrono));
                        }
                        Vector vecData = (Vector) htOneVolume.get("Copies");
                        String[] str = new String[8];
                        str[0] = accessionnumber;
                        str[1] = barcode;
                        str[2] = physicalName;
                        str[3] = statusName;
                        str[4] = location;
                        str[5] = coordinates;
                        str[6] = classificationNo;
                        str[7] = callNo;
                        vecData.addElement(str);
                        htOneVolume.put("CallNumber", callNo);
                        htOneVolume.put("Classification", classificationNo);
                        htOneVolume.put("Copies", vecData);
                        htVolume.put(volumeid, htOneVolume);
                    }
                }
            }
            rs.close();
            stat.close();
            System.out.println("######################### htHoldAll: " + htHoldAll);
            Enumeration enumHold = htHoldAll.keys();
            while (enumHold.hasMoreElements()) {
                holdingsText += "<table  border='1'>";
                String libId = enumHold.nextElement().toString();
                Statement stat2 = con.createStatement();
                ResultSet rs2 = stat2.executeQuery("select library_name from library where library_id=" + libId);
                String libraryName = "";
                while (rs2.next()) {
                    libraryName = rs2.getString(1);
                }
                rs2.close();
                stat2.close();
                holdingsText += "<tr>";
                holdingsText += "<td><b>Library: </b>" + libraryName + "</td>";
                holdingsText += "<td></td>";
                holdingsText += "</tr>";
                Hashtable htVolume = (Hashtable) htHoldAll.get(libId);
                Enumeration enumVols = htVolume.keys();
                while (enumVols.hasMoreElements()) {
                    holdingsText += "<tr>";
                    String volumeId = enumVols.nextElement().toString();
                    Hashtable htOneVolume = (Hashtable) htVolume.get(volumeId);
                    System.out.println(".........................Call number for htOneVolume: " + htOneVolume);
                    holdingsText += "<td>";
                    if (htOneVolume.get("VolumeDisplay") != null) {
                        holdingsText += "<p><b>Volume: </b>" + htOneVolume.get("VolumeDisplay").toString() + "</p>";
                    }
                    holdingsText += "<p><b>Call number: </b><a href='/newgenlibctxt/SearchViewAction.do?ClassNo=" + htOneVolume.get("Classification") + "'>" + getCallNo() + "</a></p>";
                    holdingsText += "</td>";
                    holdingsText += "<td>";
                    holdingsText += "<table border='1'>";
                    holdingsText += "<tr>";
                    holdingsText += "<td><P ALIGN=CENTER><b>Accession number</b></P></td>";
                    holdingsText += "<td><P ALIGN=CENTER><b>Barcode</b></P></td>";
                    holdingsText += "<td><P ALIGN=CENTER><b>Physical form</b></P></td>";
                    holdingsText += "<td><P ALIGN=CENTER><b>Status</b></P></td>";
                    holdingsText += "<td><P ALIGN=CENTER><b>Location</b></P></td>";
                    holdingsText += "</tr>";
                    Vector veccopies = (Vector) htOneVolume.get("Copies");
                    for (int i = 0; i < veccopies.size(); i++) {
                        holdingsText += "<tr>";
                        String[] data = (String[]) veccopies.elementAt(i);
                        holdingsText += "<td>" + data[0] + "</td>";
                        holdingsText += "<td>" + data[1] + "</td>";
                        holdingsText += "<td>" + data[2] + "</td>";
                        holdingsText += "<td>" + data[3] + "</td>";
                        holdingsText += "<td><a href='/NGLv3Web/displayLocationImage?ownlibid=" + libId + "&coordinates=" + data[5] + "' target='_new'>" + data[4] + "</a></td>";
                        holdingsText += "</tr>";
                    }
                    holdingsText += "</table>";
                    holdingsText += "</td>";
                    holdingsText += "</tr>";
                }
                holdingsText += "</table>";
            }
        } catch (Exception e) {
        }
        System.out.println("Non serial: " + holdingsText);
        return holdingsText;
    }

    public void inflateCompressedHoldingsAndAddToItemizedHoldings(String capPat, Vector itemVec, Vector compVec) {
        XMLOutputter xmlout = new XMLOutputter();
        for (int i = 0; i < compVec.size(); i++) {
            String enumChronoComp = compVec.elementAt(i).toString();
            Document doc = null;
            try {
                doc = (new SAXBuilder()).build(new StringReader(enumChronoComp));
            } catch (Exception e) {
            }
            if (doc != null) {
                List liRange = doc.getRootElement().getChildren("Range");
                for (int j = 0; j < liRange.size(); j++) {
                    Element eleRange = (Element) liRange.get(j);
                    Element eleFrom = eleRange.getChild("From");
                    Element eleTo = eleRange.getChild("To");
                    if (eleTo == null) {
                        String str = xmlout.outputString(eleFrom);
                        itemVec.addElement(str);
                    } else {
                        InBetweenNumbers inb = new InBetweenNumbers();
                        System.out.println("..........inflateCompressedHoldingsAndAddToItemizedHoldings &&&&&&&&&7 InBetweenNumbers");
                        inb.getInBetweenNumbers(capPat, xmlout.outputString(eleFrom), xmlout.outputString(eleTo), true);
                        String xml = inb.getInBetweenNumbersXML();
                        System.out.println("Between issues: " + xml);
                        SAXBuilder sb = new SAXBuilder();
                        Document docx = null;
                        try {
                            docx = sb.build(new StringReader(xml));
                        } catch (Exception exception) {
                        }
                        if (docx != null) {
                            List liitems = docx.getRootElement().getChildren();
                            for (int k = 0; k < liitems.size(); k++) {
                                Element eleitem = (Element) liitems.get(k);
                                String str = xmlout.outputString(eleitem);
                                itemVec.addElement(str);
                            }
                        }
                    }
                }
            }
        }
    }

    public String paintAlternateIssuesBackgroundBlue(String issues) {
        StringTokenizer stk = new StringTokenizer(issues, "#");
        String returnStr = "";
        boolean flag = true;
        while (stk.hasMoreTokens()) {
            if (flag) {
                returnStr += stk.nextToken() + " ";
            } else {
                returnStr += "<SPAN STYLE=\"background: #ccffff\">" + stk.nextToken() + "</SPAN> ";
            }
            flag = !flag;
        }
        return returnStr;
    }

    public boolean isSerial() {
        return isSerial;
    }

    public boolean isOnlyItemized() {
        return isOnlyItemized;
    }

    public String getCallNo() {
        return callNo;
    }

    public void setCallNo(String callNo) {
        this.callNo = callNo;
    }
}
