package org.openXpertya.print.pdf.text.pdf;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/** Reads an FDF form and makes the fields available
 * @author Paulo Soares (psoares@consiste.pt)
 */
public class FdfReader extends PdfReader {

    HashMap fields;

    String fileSpec;

    PdfName encoding;

    /** Reads an FDF form.
     * @param filename the file name of the form
     * @throws IOException on error
     */
    public FdfReader(String filename) throws IOException {
        super(filename);
    }

    /** Reads an FDF form.
     * @param pdfIn the byte array with the form
     * @throws IOException on error
     */
    public FdfReader(byte pdfIn[]) throws IOException {
        super(pdfIn);
    }

    /** Reads an FDF form.
     * @param url the URL of the document
     * @throws IOException on error
     */
    public FdfReader(URL url) throws IOException {
        super(url);
    }

    /** Reads an FDF form.
     * @param is the <CODE>InputStream</CODE> containing the document. The stream is read to the
     * end but is not closed
     * @throws IOException on error
     */
    public FdfReader(InputStream is) throws IOException {
        super(is);
    }

    protected void readPdf() throws IOException {
        fields = new HashMap();
        try {
            tokens.checkFdfHeader();
            rebuildXref();
            readDocObj();
        } finally {
            try {
                tokens.close();
            } catch (Exception e) {
            }
        }
        readFields();
    }

    protected void kidNode(PdfDictionary merged, String name) {
        PdfArray kids = (PdfArray) getPdfObject(merged.get(PdfName.KIDS));
        if (kids == null || kids.getArrayList().size() == 0) {
            if (name.length() > 0) name = name.substring(1);
            fields.put(name, merged);
        } else {
            merged.remove(PdfName.KIDS);
            ArrayList ar = kids.getArrayList();
            for (int k = 0; k < ar.size(); ++k) {
                PdfDictionary dic = new PdfDictionary();
                dic.merge(merged);
                PdfDictionary newDic = (PdfDictionary) getPdfObject((PdfObject) ar.get(k));
                PdfString t = (PdfString) getPdfObject(newDic.get(PdfName.T));
                String newName = name;
                if (t != null) newName += "." + t.toUnicodeString();
                dic.merge(newDic);
                dic.remove(PdfName.T);
                kidNode(dic, newName);
            }
        }
    }

    protected void readFields() throws IOException {
        catalog = (PdfDictionary) getPdfObject(trailer.get(PdfName.ROOT));
        PdfDictionary fdf = (PdfDictionary) getPdfObject(catalog.get(PdfName.FDF));
        PdfString fs = (PdfString) getPdfObject(fdf.get(PdfName.F));
        if (fs != null) fileSpec = fs.toUnicodeString();
        PdfArray fld = (PdfArray) getPdfObject(fdf.get(PdfName.FIELDS));
        if (fld == null) return;
        encoding = (PdfName) getPdfObject(fdf.get(PdfName.ENCODING));
        PdfDictionary merged = new PdfDictionary();
        merged.put(PdfName.KIDS, fld);
        kidNode(merged, "");
    }

    /** Gets all the fields. The map is keyed by the fully qualified
     * field name and the value is a merged <CODE>PdfDictionary</CODE>
     * with the field content.
     * @return all the fields
     */
    public HashMap getFields() {
        return fields;
    }

    /** Gets the field dictionary.
     * @param name the fully qualified field name
     * @return the field dictionary
     */
    public PdfDictionary getField(String name) {
        return (PdfDictionary) fields.get(name);
    }

    /** Gets the field value or <CODE>null</CODE> if the field does not
     * exist or has no value defined.
     * @param name the fully qualified field name
     * @return the field value or <CODE>null</CODE>
     */
    public String getFieldValue(String name) {
        PdfDictionary field = (PdfDictionary) fields.get(name);
        if (field == null) return null;
        PdfObject v = getPdfObject(field.get(PdfName.V));
        if (v == null) return null;
        if (v.isName()) return PdfName.decodeName(((PdfName) v).toString()); else if (v.isString()) {
            PdfString vs = (PdfString) v;
            if (encoding == null || vs.getEncoding() != null) return vs.toUnicodeString();
            byte b[] = vs.getBytes();
            if (b.length >= 2 && b[0] == (byte) 254 && b[1] == (byte) 255) return vs.toUnicodeString();
            try {
                if (encoding.equals(PdfName.SHIFT_JIS)) return new String(b, "SJIS"); else if (encoding.equals(PdfName.UHC)) return new String(b, "MS949"); else if (encoding.equals(PdfName.GBK)) return new String(b, "GBK"); else if (encoding.equals(PdfName.BIGFIVE)) return new String(b, "Big5");
            } catch (Exception e) {
            }
            return vs.toUnicodeString();
        }
        return null;
    }

    /** Gets the PDF file specification contained in the FDF.
     * @return the PDF file specification contained in the FDF
     */
    public String getFileSpec() {
        return fileSpec;
    }
}
