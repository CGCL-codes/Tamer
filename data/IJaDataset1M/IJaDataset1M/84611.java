package org.jbjf.core.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * The <code>DelimitedDocumentAdapter</code> is a concrete
 * implementation of a dlmDocument adapter. The usage for this
 * implementation is a flat ascii text file that uses delimiters
 * (comma, TAB, colon, etc...) in some fashion.
 * <p>
 * @author Adym S. Lincoln<br>
 *         Copyright (C) 2007. Adym S. Lincoln All rights
 *         reserved.
 * @version 1.0.0
 * @since 1.0.0
 */
public class DelimitedDocumentAdapter implements IDocumentAdapter {

    /**
     * Stores a fully qualified class name. Used for debugging
     * and auditing.
     * 
     * @since 1.0.0
     */
    public static final String ID = DelimitedDocumentAdapter.class.getName();

    /**
     * Stores the class name, primarily used for debugging and so
     * forth. Used for debugging and auditing.
     * 
     * @since 1.0.0
     */
    private String SHORT_NAME = "DelimitedDocumentAdapter()";

    /**
     * Stores a <code>SYSTEM IDENTITY HASHCODE</code>. Used for
     * debugging and auditing.
     * 
     * @since 1.0.0
     */
    private String SYSTEM_IDENTITY = String.valueOf(System.identityHashCode(this));

    /**
     * Class property to store a delimited dlmDocument object.
     */
    private DelimitedDocument dlmDocument;

    /**
     * Simple constructor that accepts a <code>DelimitedDocument</code>
     * object and returns the adapter for it.
     * <p>
     * @param theDocument   <code>DelimitedDocument</code> object to 
     * convert.
     */
    public DelimitedDocumentAdapter(DelimitedDocument theDocument) {
        this.dlmDocument = theDocument;
    }

    /**
     * Simple constructor that accepts a <code>DelimitedDocument</code>
     * object and returns the adapter for it.
     * <p>
     * @param theDocument   <code>DelimitedDocument</code> object to 
     * convert.
     * @return 
     */
    public DelimitedDocumentAdapter(String filePath, char delimiter) throws Exception {
        this.dlmDocument = loadDelimitedFile(filePath, delimiter, false);
    }

    /**
     * Simple constructor that accepts a <code>DelimitedDocument</code>
     * object and returns the adapter for it.
     * <p>
     * @param theDocument   <code>DelimitedDocument</code> object to 
     * convert.
     * @return 
     */
    public DelimitedDocumentAdapter(String filePath, char delimiter, boolean header) throws Exception {
        this.dlmDocument = loadDelimitedFile(filePath, delimiter, header);
    }

    /**
     * Loads underlying file and returns its DocumentAdapter.  The
     * loaded DelimitedDocument is stored in the class property
     * <code>dlmDocument</code>.
     * <p>
     * @param filePath          Directory path and file name of the 
     * file to load.
     * @param delimiter         Single character delimiter used to
     * separate data elements in the file.
     * @return DocumentAdapter  The loaded <code>DelimitedDocument</code> object.
     * @throws Exception        Escalate exceptions up to the parent.
     */
    public DelimitedDocumentAdapter load(String filePath, char delimiter) throws Exception {
        return load(filePath, delimiter, false);
    }

    /**
     * Loads underlying file and returns its DocumentAdapter.  The
     * loaded DelimitedDocument is stored in the class property
     * <code>dlmDocument</code>.
     * <p>
     * @param filePath          Directory path and file name of the 
     * file to load.
     * @param delimiter         Single character delimiter used to
     * separate data elements in the file.
     * @param header            True/False that indicates whether
     * a header row is present on the file or not.
     * @return DocumentAdapter  The loaded <code>DelimitedDocument</code> object.
     * @throws Exception        Escalate exceptions up to the parent.
     */
    public DelimitedDocumentAdapter load(String filePath, char delimiter, boolean header) throws Exception {
        return new DelimitedDocumentAdapter(loadDelimitedFile(filePath, delimiter, header));
    }

    public Object getRecordContainer(long i) {
        return dlmDocument.getRow(i);
    }

    public Object createRecordContainer() {
        return dlmDocument.createRow();
    }

    public void addRecord(Object myRecord) throws Exception {
        dlmDocument.addRow((FlatRow) myRecord);
    }

    public void removeRecord(long i) {
        dlmDocument.removeRow(i);
    }

    public long getRecordCount() {
        return dlmDocument.getRowCount();
    }

    public Object getDocument() {
        return dlmDocument;
    }

    public void writeDocument(String filePath) throws Exception {
        writeDelimitedFile(dlmDocument, filePath);
    }

    /**
     * @param filename  Fully qualified directory path and filename.
     * @param delimiter Single character for the data element delimiter.
     * @param header    True/False indicator on whether a header row
     * exists on the file.
     * @return DelimitedDocument containing values specified in
     *         filename.  For a header of True, the column names 
     *         are set automatically.
     * @throws Exception    Escalate exceptions up to the parent.
     */
    public DelimitedDocument loadDelimitedFile(String filename, char delimiter, boolean header) throws Exception {
        boolean endOfFile = false;
        String lstrLine;
        ArrayList columnValues = null;
        ArrayList columnNames = null;
        DelimitedDocument docResults = null;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
        if (header) {
            lstrLine = bufferedReader.readLine();
            if (lstrLine == null) {
                endOfFile = false;
            } else {
                if (lstrLine.length() > 0) {
                    String columnName;
                    columnNames = (ArrayList) tokenizeString(lstrLine, delimiter);
                    for (int i = 0; i < columnNames.size(); i++) {
                        columnName = (String) columnNames.get(i);
                        if ((columnName != null) && columnName.startsWith("\"") && columnName.endsWith("\"")) {
                            columnName = columnName.substring(1, columnName.length() - 1).trim();
                            if (columnName.length() == 0) {
                                columnName = null;
                            }
                            columnNames.set(i, columnName);
                        }
                    }
                }
            }
        }
        while (!endOfFile) {
            lstrLine = bufferedReader.readLine();
            if (lstrLine == null) {
                endOfFile = true;
            } else {
                if (lstrLine.length() > 0) {
                    String columnValue;
                    columnValues = (ArrayList) tokenizeString(lstrLine, delimiter);
                    for (int i = 0; i < columnValues.size(); i++) {
                        columnValue = (String) columnValues.get(i);
                        if ((columnValue != null) && columnValue.startsWith("\"") && columnValue.endsWith("\"")) {
                            columnValue = columnValue.substring(1, columnValue.length() - 1).trim();
                            if (columnValue.length() == 0) {
                                columnValue = null;
                            }
                            columnValues.set(i, columnValue);
                        }
                    }
                    if (docResults == null) {
                        docResults = new DelimitedDocument(columnValues.size(), delimiter);
                    }
                    if (header) {
                        FlatRow hdrRow = docResults.createRow(columnValues, columnNames);
                        docResults.addRow(hdrRow);
                    } else {
                        FlatRow theRow = docResults.createRow(columnValues);
                        docResults.addRow(theRow);
                    }
                }
            }
        }
        if (docResults == null) {
            docResults = new DelimitedDocument(0, delimiter);
        }
        bufferedReader.close();
        return docResults;
    }

    /**
     * Write output DelimitedDocument to filesystem as file
     * 
     * @throws Exception
     */
    public void writeDelimitedFile(DelimitedDocument theDocument, String theFilename) throws Exception {
        FileWriter fwOutput = new FileWriter(theFilename);
        BufferedWriter bwOutput = new BufferedWriter(fwOutput);
        FlatRow docRow;
        StringBuffer lineBuffer;
        char chrDelimiter = theDocument.getDelimiter();
        String strDelimiter = Character.toString(chrDelimiter);
        String EMPTY_STRING = "";
        for (int j = 0; j < theDocument.getRowCount(); j++) {
            lineBuffer = new StringBuffer();
            docRow = theDocument.getRow(j);
            for (int i = 0; i < docRow.getColumnCount(); i++) {
                if (docRow.getColumnValue(i) != null) {
                    lineBuffer.append(docRow.getColumnValue(i));
                } else {
                    lineBuffer.append(EMPTY_STRING);
                }
                lineBuffer.append(chrDelimiter);
            }
            lineBuffer.deleteCharAt(lineBuffer.lastIndexOf(strDelimiter));
            bwOutput.write(lineBuffer.toString());
            bwOutput.newLine();
        }
        bwOutput.close();
    }

    /**
     * Returns a list of the tokens in the string. If there are
     * two delimiters back to back will put a null in that
     * position.
     * 
     * @param aLine
     * @param aDelimiter
     * @return
     */
    public List tokenizeString(String aLine, char aDelimiter) {
        ArrayList list = new ArrayList();
        if (aLine.length() == 0) {
            return list;
        }
        int posNow = 0;
        int newPos = 0;
        boolean done = false;
        String token = null;
        while (!done) {
            newPos = aLine.indexOf(aDelimiter, posNow);
            if (newPos != -1) {
                if (newPos == posNow) {
                    list.add(null);
                } else {
                    token = aLine.substring(posNow, newPos);
                    token = token.trim();
                    if (token.length() == 0) {
                        list.add(null);
                    } else {
                        list.add(token);
                    }
                }
                posNow = newPos + 1;
            }
            if (newPos == -1 || posNow >= aLine.length()) {
                done = true;
            }
        }
        if (aDelimiter == aLine.charAt((aLine.length() - 1)) && aLine.length() > 1) {
            list.add(null);
        } else if (newPos == -1 && posNow < aLine.length()) {
            token = aLine.substring(posNow, aLine.length());
            token = token.trim();
            if (token.length() == 0) {
                list.add(null);
            } else {
                list.add(token.trim());
            }
        }
        return list;
    }
}
