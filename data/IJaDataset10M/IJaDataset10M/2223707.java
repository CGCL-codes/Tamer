package com.rooster.action.boarding.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.textmining.text.extraction.WordExtractor;
import com.rooster.constants.PropertyFileConst;
import com.rooster.form.boarding.DocumentDetails;
import com.lowagie.text.Document;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.rtf.style.RtfFont;

public class BoardingUtil {

    public static String getDocumentAsString(String sActualFilePath) {
        String sStr = new String();
        try {
            FileInputStream fisDoc = new FileInputStream(sActualFilePath);
            java.io.InputStream inputStream = fisDoc;
            WordExtractor extractor = new WordExtractor();
            sStr = extractor.extractText(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sStr;
    }

    public static String getDocumentPath(DataSource dbSrc, String sDocumentId) {
        Connection dbCon = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sDocumentPath = new String();
        try {
            String sSql = "select email_id, virtual_path, actual_path from rooster_candidate_documents where id = ?";
            dbCon = dbSrc.getConnection();
            pstmt = dbCon.prepareStatement(sSql);
            pstmt.setString(1, sDocumentId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                sDocumentPath = rs.getString(3);
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (dbCon != null) {
                    dbCon.close();
                }
            } catch (SQLException e) {
            }
        }
        return sDocumentPath;
    }

    public static String getStringWithTextBoxes(String sInput) {
        StringBuffer sbCorrectedStr = new StringBuffer();
        int iCountUnserscore = 0;
        int iCountTextBox = 0;
        boolean bFound = false;
        for (int i = 0; i < sInput.length(); i++) {
            char cFirstChar = sInput.charAt(i);
            int iFirstNum = (int) cFirstChar;
            if (iFirstNum == 7) {
                sbCorrectedStr.append("\t");
            } else if (iFirstNum == 95) {
                bFound = true;
                iCountUnserscore++;
            } else if (iFirstNum != 95 && bFound) {
                if (iCountUnserscore > 3) {
                    iCountTextBox++;
                    sbCorrectedStr.append("<input type=\"text\" name=\"fill_up" + iCountTextBox + "\" size=\"" + iCountUnserscore + "\">");
                }
                iCountUnserscore = 0;
                bFound = false;
            } else if (iFirstNum == 13) {
                sbCorrectedStr.append("<br>");
            } else {
                sbCorrectedStr.append(String.valueOf(cFirstChar));
            }
        }
        sbCorrectedStr.append(String.valueOf(iCountTextBox));
        return sbCorrectedStr.toString();
    }

    public static Vector<String> uploadDocumentAsPDF(HttpServletRequest req, String sUser, String sDocumentName, String sDocumentContent) {
        HttpSession session = req.getSession(false);
        String sWebSite = String.valueOf(session.getAttribute("WEBSITE"));
        String sFolder = String.valueOf(session.getAttribute("CANDIDATE_DOCUMENTS_FOLDER"));
        String folderPath = String.valueOf(session.getAttribute(PropertyFileConst.APPLICATION_ROOT_PATH));
        Vector<String> vFilePathPDF = new Vector<String>();
        folderPath = folderPath + sFolder + sUser + "/";
        String sActualPath = folderPath + sDocumentName + ".pdf";
        String sVirtualPath = sWebSite + sFolder + sUser + "/" + sDocumentName + ".pdf";
        java.io.File file = new java.io.File(folderPath);
        try {
            if (!(file.exists())) {
                file.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(sActualPath));
            document.addTitle("Candidate");
            document.addAuthor(com.rooster.utils.RoosterDefaults.getCOMAPANY_NAME());
            document.addSubject("Candidate Document");
            document.open();
            RtfFont rtfFont = new RtfFont("Times New Roman", 12, RtfFont.BOLD);
            RtfFont bodyFont = new RtfFont("Times New Roman", 10, RtfFont.NORMAL);
            RtfFont bodyBldFont = new RtfFont("Times New Roman", 10, RtfFont.BOLD);
            document.add(new Paragraph("", rtfFont));
            document.add(new Paragraph(sDocumentContent, bodyFont));
            document.add(new Paragraph("\n"));
            HeaderFooter footer = new HeaderFooter(new Phrase(com.rooster.utils.RoosterDefaults.getCOMAPANY_NAME()), true);
            document.setFooter(footer);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        vFilePathPDF.add(sVirtualPath);
        vFilePathPDF.add(sActualPath);
        return vFilePathPDF;
    }

    public static String getDocName(String sFilePath) {
        String sDocName = new String();
        sDocName = sFilePath.substring(sFilePath.lastIndexOf('/') + 1, sFilePath.lastIndexOf('.'));
        return sDocName;
    }

    public static void saveDocumentDetails(DataSource dbSrc, DocumentDetails alDocDetails, HttpServletRequest req) {
        Connection dbCon = null;
        PreparedStatement pstmt = null;
        try {
            String sSql = "insert into rooster_candidate_documents (email_id, virtual_path, actual_path, saved) values (?, ?, ?, ?)";
            dbCon = dbSrc.getConnection();
            pstmt = dbCon.prepareStatement(sSql);
            pstmt.setString(1, alDocDetails.getEmail_id());
            pstmt.setString(2, alDocDetails.getVitual_path());
            pstmt.setString(3, alDocDetails.getActual_path());
            if (alDocDetails.getSaved() == null || alDocDetails.getSaved().equals(new String())) {
                pstmt.setString(4, "N");
            } else {
                pstmt.setString(4, alDocDetails.getSaved());
            }
            pstmt.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (dbCon != null) {
                    dbCon.close();
                }
            } catch (SQLException e) {
            }
        }
    }

    public static void changeSavedType(DataSource dbSrc, String sSaved, String sDocumentId) {
        Connection dbCon = null;
        PreparedStatement pstmt = null;
        try {
            String sSql = "update rooster_candidate_documents set saved = ? where id = ?";
            dbCon = dbSrc.getConnection();
            pstmt = dbCon.prepareStatement(sSql);
            pstmt.setString(1, sSaved);
            pstmt.setString(2, sDocumentId);
            pstmt.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (dbCon != null) {
                    dbCon.close();
                }
            } catch (SQLException e) {
            }
        }
    }

    public static String replaceString(String sInput, String sReplace, String sRegex) {
        Pattern p = Pattern.compile(sRegex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(sInput);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, sReplace);
        }
        m.appendTail(sb);
        return sb.toString();
    }
}
