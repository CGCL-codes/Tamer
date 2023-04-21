package aipdf.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.log4j.Category;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfIndirectReference;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfNumber;
import com.lowagie.text.pdf.PdfObject;
import com.lowagie.text.pdf.PdfReader;

/**
 * provides some useful methods for the work with PDFs
 *
 * @author cengelhardt
 */
public class PdfHelper {

    private static Category cCat = Category.getInstance(PdfHelper.class.getName());

    /**
   * suppress instances
   */
    private PdfHelper() {
    }

    /**
   * This method returns the PdfObject specified by the TreePath string withhin the
   * passed PdfDictionary. 
   * Pass a string of the format <i>PdfName/PdfName/PdfName</i>.
   * EXAMPLE: MK/BC
   * returns <code>null</code> if the Object could not be found.
   * this method calls itself recursively
   * @param aszTreePath
   * @param aDict
   * @return
   */
    public static PdfObject getPdfObjectFromTree(String aszTreePath, PdfDictionary aDict) {
        if (!"".equals(aszTreePath)) {
            String[] tArrayTreePathElements = aszTreePath.split("/");
            if (tArrayTreePathElements.length > 0) {
                PdfName tPdfName = getPdfName(tArrayTreePathElements[0]);
                PdfObject tPdfObject = aDict.get(tPdfName);
                if (tPdfObject == null) return null;
                if (tArrayTreePathElements.length == 1) {
                    if (tPdfObject instanceof PdfIndirectReference) return PdfReader.getPdfObject(tPdfObject);
                    return tPdfObject;
                }
                String[] tNewTreePathElements = new String[tArrayTreePathElements.length - 1];
                System.arraycopy(tArrayTreePathElements, 1, tNewTreePathElements, 0, tArrayTreePathElements.length - 1);
                String tszNewTreePath = implode(tNewTreePathElements, "/");
                if (tPdfObject instanceof PdfDictionary) {
                    return getPdfObjectFromTree(tszNewTreePath, (PdfDictionary) tPdfObject);
                } else if (tPdfObject instanceof PdfIndirectReference) {
                    return getPdfObjectFromTree(tszNewTreePath, (PdfDictionary) PdfReader.getPdfObject(tPdfObject));
                } else return null;
            }
        }
        return null;
    }

    /**
    * Reads the FieldType out of the PdfForm and transform it to mapping pdftype
    * @author mspitzer
    * @param tFieldMap
    * @param tPdfID
    * @return
    */
    public static String getPdfType(HashMap aFieldMap, String aPdfID, PdfReader aPdfReader) {
        AcroFields.Item tItem = (AcroFields.Item) aFieldMap.get(aPdfID);
        ArrayList tList = tItem.values;
        Iterator tIter = tList.iterator();
        while (tIter.hasNext()) {
            PdfDictionary tPdfDictionary = (PdfDictionary) tIter.next();
            PdfObject tPdfTypeID = tPdfDictionary.get(PdfName.FT);
            if (tPdfTypeID == null) {
                PdfObject tPdfObject = getPdfObjectFromTree("PARENT/FT", tPdfDictionary);
                if (tPdfObject != null) tPdfTypeID = tPdfObject;
            }
            PdfObject tSubPdfTypeID = tPdfDictionary.get(PdfName.FF);
            if (tSubPdfTypeID == null) {
                PdfObject tPdfObject = getPdfObjectFromTree("PARENT/FT", tPdfDictionary);
                if (tPdfObject != null) tSubPdfTypeID = tPdfObject;
            }
            if ("/Btn".equalsIgnoreCase(tPdfTypeID.toString())) {
                if ("49152".equalsIgnoreCase("" + tSubPdfTypeID)) {
                    return "option";
                } else if ("65536".equalsIgnoreCase("" + tSubPdfTypeID)) {
                    return "button";
                } else {
                    return "checkbox";
                }
            } else if ("/Ch".equalsIgnoreCase(tPdfTypeID.toString())) {
                if ("131072".equalsIgnoreCase("" + tSubPdfTypeID)) {
                    return "combobox";
                }
                return "list";
            } else if ("/Tx".equalsIgnoreCase(tPdfTypeID.toString())) {
                return "text";
            }
        }
        return "";
    }

    /**
    * used to retrieve all component dictionaries from the given dictionary
    * @param aDict
    * @return
    */
    public static ArrayList getComponentDictionaries(PdfDictionary aDict) {
        ArrayList tList = new ArrayList();
        if (!aDict.contains(PdfName.KIDS)) {
            tList.add(aDict);
            return tList;
        } else {
            ArrayList tKidsList = ((PdfArray) aDict.get(PdfName.KIDS)).getArrayList();
            int tSize = tKidsList.size();
            for (int tIndex = 0; tIndex < tSize; tIndex++) tList.add(PdfReader.getPdfObject((PdfIndirectReference) tKidsList.get(tIndex)));
            return tList;
        }
    }

    /**
   * returns the pdf sub type ID.
   * returns -1 if ID was not found.
   * @param aDict
   * @return
   */
    public static int getFieldFlag(PdfDictionary aDict) {
        int tSubPdfTypeID = 0;
        PdfNumber tPdfSubType = (PdfNumber) PdfHelper.getPdfObjectFromTree("FF", aDict);
        if (tPdfSubType == null) tPdfSubType = (PdfNumber) PdfHelper.getPdfObjectFromTree("PARENT/FF", aDict);
        if (tPdfSubType != null) tSubPdfTypeID = tPdfSubType.intValue();
        return tSubPdfTypeID;
    }

    /**
   * returns the mapping PdfName for the given String.
   * e.g. PdfName.FF if String 'FF' is passed
   * @param aszPdfName
   * @return the mapping PdfName or null, if the PdfName doesn't exist
   */
    static PdfName getPdfName(String aszPdfName) {
        try {
            return (PdfName) PdfName.class.getDeclaredField(aszPdfName).get(null);
        } catch (Exception aException) {
            String tszMessage = aException.getMessage();
            cCat.error(tszMessage);
        }
        return null;
    }

    /**
   * checks if this PDF has been approved by Administration Intelligence AG
   * @param aFile
   * @return boolean indicating if this document has been approved
   * @throws IOException
   */
    public static boolean isAIPdf(File aFile) throws IOException {
        HashMap tInfo = new PdfReader(aFile.getAbsolutePath()).getInfo();
        String tKeywords = (String) tInfo.get("Keywords");
        cCat.info("Contained Keywords in PDF: " + tKeywords);
        if (tKeywords != null && tKeywords.matches(".*AI-PROOFED.*")) {
            return true;
        }
        return false;
    }

    /**
   * concatenates all strings in the passed array together using the
   * passed String as glue
   * @param aStringArray
   * @param aszGlue
   * @return
   */
    static String implode(String[] aStringArray, String aszGlue) {
        StringBuffer tStringBuffer = new StringBuffer();
        for (int tIndex = 0; tIndex < aStringArray.length; tIndex++) {
            tStringBuffer.append(aStringArray[tIndex]);
            if (tIndex < aStringArray.length - 1) tStringBuffer.append(aszGlue);
        }
        return tStringBuffer.toString();
    }
}
