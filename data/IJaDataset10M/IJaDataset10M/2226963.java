package gov.lanl.translate;

import nu.xom.*;
import java.io.*;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.text.SimpleDateFormat;
import jxl.*;

/**
 * Excel Spreadsheet XML convertor built on the Apache POI package
 * It assumes the first row (and only the first row) are the column headers
 * and that each row has no more elements than the first row.
 * User: dwf
 * Date: Mar 25, 2004
 * Time: 1:24:23 PM
 */
public class XLSDOM {

    private static Document document;

    private static String dateFormat = "yyyyMMdd";

    private Hashtable set;

    private org.apache.log4j.Logger cat = org.apache.log4j.Logger.getLogger(XLSDOM.class);

    public XLSDOM() {
    }

    public XLSDOM(String input) {
        set = new Hashtable();
        try {
            SimpleDateFormat form = new SimpleDateFormat(dateFormat);
            Workbook workbook = Workbook.getWorkbook(new File(input));
            for (int k = 0; k < workbook.getNumberOfSheets(); k++) {
                try {
                    Sheet sheet = workbook.getSheet(k);
                    String nm = sheet.getName();
                    System.out.println("processing sheet: " + nm);
                    int rows = sheet.getRows();
                    if (rows > 0) {
                        ArrayList rowList = new ArrayList();
                        for (int r = 0; r < rows; r++) {
                            jxl.Cell[] row = sheet.getRow(r);
                            if (row != null) {
                                int cells = row.length;
                                ArrayList cellList = new ArrayList();
                                String value = "";
                                for (short c = 0; c < cells; c++) {
                                    Cell cell = row[c];
                                    value = "";
                                    if (cell != null) {
                                        if (cell.getType() == CellType.STRING_FORMULA) {
                                            value = "";
                                        } else if (cell.getType() == CellType.NUMBER) {
                                            NumberCell nc = (NumberCell) cell;
                                            double d = nc.getValue();
                                            value = nc.getNumberFormat().format(d);
                                        } else if (cell.getType() == CellType.DATE) {
                                            DateCell dc = (DateCell) cell;
                                            value = form.format(dc.getDate());
                                        } else value = cell.getContents();
                                    }
                                    cellList.add(value);
                                }
                                rowList.add(cellList);
                            }
                        }
                        set.put(nm, rowList);
                    }
                } catch (Exception e) {
                    cat.error(e);
                }
            }
            workbook.close();
        } catch (Exception e) {
            cat.error("Error occurred: ", e);
        }
    }

    /**
     * return the W3C Document
     *
     * @return Document
     */
    public Document getDocument() {
        document = initDOM();
        Enumeration en = set.keys();
        while (en.hasMoreElements()) {
            String key = (String) en.nextElement();
            ArrayList rowSet = (ArrayList) set.get(key);
            Element elSet = new Element("Set");
            elSet.addAttribute(new Attribute("Name", key));
            ArrayList codes = (ArrayList) rowSet.get(0);
            for (int i = 1; i < rowSet.size(); i++) {
                ArrayList cellList = (ArrayList) rowSet.get(i);
                Element item = new Element("Item");
                int codeSize = codes.size();
                for (int j = 0; j < cellList.size(); j++) {
                    String value = (String) cellList.get(j);
                    if (value != null) {
                        Element data = new Element("Data");
                        if (j < codeSize) data.addAttribute(new Attribute("Code", (String) codes.get(j)));
                        Text val = new Text(value);
                        data.appendChild(val);
                        item.appendChild(data);
                    }
                }
                elSet.appendChild(item);
            }
            document.getRootElement().appendChild(elSet);
        }
        return document;
    }

    /**
     * get the underlying Table by name (if available)
     *
     * @param name
     * @return ArrayList
     */
    public ArrayList getTable(String name) {
        if (set != null && set.size() > 0) {
            ArrayList list = (ArrayList) set.get(name);
            if (list == null) {
                Enumeration en = set.keys();
                while (en.hasMoreElements()) {
                    list = (ArrayList) en.nextElement();
                }
            }
            cat.debug("getTable: " + list.size() + "rows");
            return list;
        }
        return null;
    }

    /**
     * set the dateformat
     *
     * @param format
     */
    public static void setDateFormat(String format) {
        dateFormat = format;
    }

    /**
     * Serialize Content to String
     */
    public String toString() {
        return document.toXML();
    }

    /**
     * Initialize DOM document container
     */
    private static Document initDOM() {
        document = new Document(new Element("CSV"));
        return document;
    }

    /**
     * test driver
     *
     * @param argv
     */
    public static void main(String[] argv) {
        if (argv[0] != null && !argv[0].equals("")) {
            XLSDOM xslDoc = new XLSDOM(argv[0]);
            Document doc = xslDoc.getDocument();
            Serializer serializer = new Serializer(System.out);
            serializer.setIndent(4);
            try {
                serializer.write(doc);
            } catch (Exception e) {
                System.err.println("Serializer failed " + e);
            }
        }
    }
}
