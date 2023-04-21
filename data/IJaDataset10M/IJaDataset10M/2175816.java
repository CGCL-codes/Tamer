package gov.usda.gdpc.parse.pedigree;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.hssf.usermodel.*;

/**
 *
 * @author  terryc
 */
public class PedigreeXLS {

    public static final int NUM_COLUMNS = 7;

    public static final int SOURCE_INDEX = 0;

    public static final int CULTIVAR_INDEX = 1;

    public static final int STATE_COUNTRY_INDEX = 2;

    public static final int PARENTS_OLD_INDEX = 3;

    public static final int PARENTS_INDEX = 4;

    public static final int COMMENTS_INDEX = 5;

    public static final short SOURCE_COLUMN = 0;

    public static final short CULTIVAR_COLUMN = 1;

    public static final short STATE_COUNTRY_COLUMN = 2;

    public static final short PARENTS_OLD_COLUMN = 3;

    public static final short PARENTS_COLUMN = 4;

    public static final short COMMENTS_COLUMN = 5;

    public static final int XLS_ROW_NUM_INDEX = 6;

    private List myRecords = null;

    private List myTokens = null;

    private List myHeadNodes = null;

    private List myRows = null;

    private final Map myPassportIDs = new HashMap();

    private final Map mySynonymIDs = new HashMap();

    private final Map myStockIDs = new HashMap();

    /** Creates a new instance of PedigreeXLS */
    public PedigreeXLS(String filename) {
        init(filename);
        parseParents();
        createTrees();
        Node.updateSynonymMap();
        printTrees();
    }

    private void init(String filename) {
        List result = new ArrayList();
        try {
            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(filename));
            HSSFWorkbook hssfworkbook = new HSSFWorkbook(fs);
            for (int i = 0, n = hssfworkbook.getNumberOfSheets(); i < n; i++) {
                HSSFSheet sheet = hssfworkbook.getSheetAt(i);
                int rows = sheet.getPhysicalNumberOfRows();
                for (int j = 0; j < rows; j++) {
                    HSSFRow row = sheet.getRow(j);
                    if (row != null) {
                        try {
                            int cells = row.getPhysicalNumberOfCells();
                            if (cells >= NUM_COLUMNS - 1) {
                                String[] temp = new String[NUM_COLUMNS];
                                temp[XLS_ROW_NUM_INDEX] = String.valueOf(j + 1);
                                try {
                                    temp[CULTIVAR_INDEX] = row.getCell(CULTIVAR_COLUMN).getStringCellValue();
                                } catch (Exception e) {
                                }
                                try {
                                    temp[STATE_COUNTRY_INDEX] = row.getCell(STATE_COUNTRY_COLUMN).getStringCellValue();
                                } catch (Exception e) {
                                }
                                temp[PARENTS_INDEX] = row.getCell(PARENTS_COLUMN).getStringCellValue();
                                if ((temp[CULTIVAR_INDEX] != null) && (temp[CULTIVAR_INDEX].length() != 0)) {
                                    result.add(temp);
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("problem reading row: " + j);
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        myRecords = result;
    }

    private static void printList(List list) {
        Iterator itr = list.iterator();
        while (itr.hasNext()) {
            String[] current = (String[]) itr.next();
            for (int i = 0, n = current.length; i < n; i++) {
                System.out.print(current[i] + "  ");
            }
            System.out.println("");
        }
    }

    private String getIDfromMap(Map map, String name) {
        return (String) map.get(name.toUpperCase());
    }

    private void putIDinMap(Map map, String name, String id) {
        map.put(name.toUpperCase(), id);
    }

    private boolean needToAddParents(List dbParents, List parents) {
        if (dbParents.size() == 0) {
            return true;
        }
        if (dbParents.size() != parents.size()) {
            System.out.println("PedigreeXLS: needToAddParents: number of parents in database doesn't match file.");
            return false;
        }
        Iterator itr = dbParents.iterator();
        while (itr.hasNext()) {
            String name = (String) itr.next();
            if (!parents.contains(name)) {
                System.out.println("PedigreeXLS: needToAddParents: parents do not match parents in database ");
                return false;
            }
        }
        return false;
    }

    private void parseParents() {
        myTokens = new ArrayList();
        String regExp = "(/((/)|(\\d/))?)|";
        regExp += "\\*|";
        regExp += "\\(|";
        regExp += "\\)|";
        regExp += "\\[|";
        regExp += "\\]|";
        regExp += "\\^|";
        regExp += ",";
        System.out.println("\nregular expression: " + regExp + "\n");
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regExp);
        Matcher matcher = pattern.matcher("null");
        Iterator itr = myRecords.iterator();
        while (itr.hasNext()) {
            String[] current = (String[]) itr.next();
            String parents = current[PARENTS_INDEX];
            System.out.println("parents: " + parents);
            List tokens = new ArrayList();
            if ((parents == null) || (parents.length() == 0)) {
                myTokens.add(tokens);
                break;
            }
            matcher = matcher.reset(parents);
            int position = 0;
            while (matcher.find()) {
                if (matcher.start() != position) {
                    String temp = parents.substring(position, matcher.start());
                    temp = temp.trim();
                    if (temp.length() != 0) {
                        tokens.add(temp);
                    }
                }
                tokens.add(Token.getInstance(matcher.group()));
                position = matcher.end();
            }
            if (position != parents.length()) {
                String temp = parents.substring(position, parents.length());
                temp = temp.trim();
                if (temp.length() != 0) {
                    tokens.add(temp);
                }
            }
            if (tokens.size() == 0) {
                tokens.add(parents);
            }
            myTokens.add(tokens);
        }
    }

    private void createTrees() {
        myHeadNodes = new ArrayList();
        myRows = new ArrayList();
        for (int i = 0, n = myRecords.size(); i < n; i++) {
            String[] current = (String[]) myRecords.get(i);
            try {
                Node head = Node.getInstance(current[CULTIVAR_INDEX], (List) myTokens.get(i), current[XLS_ROW_NUM_INDEX]);
                myHeadNodes.add(head.getName());
                myRows.add(head.getRow());
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void printTrees() {
        for (int i = 0, n = myHeadNodes.size(); i < n; i++) {
            System.out.println("--------------------");
            String str = (String) myHeadNodes.get(i);
            Node current = Node.getInstance(str);
            if (current != null) {
                String[] record = findRecord(current.getRow());
                System.out.println("Row: " + record[XLS_ROW_NUM_INDEX] + "  Cultivar: " + record[CULTIVAR_INDEX] + "  Parents: " + record[PARENTS_INDEX]);
                current.printTree(0, null);
            } else {
                current = Node.getSynonym(str);
                String[] record = findRecord((String) myRows.get(i));
                System.out.println("Row: " + record[XLS_ROW_NUM_INDEX] + "  Cultivar: " + record[CULTIVAR_INDEX] + "  Parents: " + record[PARENTS_INDEX]);
                System.out.println("Cultivar: " + str + " is synonym of: " + current.getName() + " (row: " + current.getRow() + ")");
            }
        }
    }

    public String[] findRecord(String row) {
        Iterator itr = myRecords.iterator();
        while (itr.hasNext()) {
            String[] current = (String[]) itr.next();
            if (current[XLS_ROW_NUM_INDEX].equals(row)) {
                return current;
            }
        }
        return null;
    }

    public static void printXLS(String filename) {
        try {
            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(filename));
            HSSFWorkbook hssfworkbook = new HSSFWorkbook(fs);
            for (int i = 0, n = hssfworkbook.getNumberOfSheets(); i < n; i++) {
                HSSFSheet sheet = hssfworkbook.getSheetAt(i);
                int rows = sheet.getPhysicalNumberOfRows();
                for (int j = 0; j < rows; j++) {
                    HSSFRow row = sheet.getRow(j);
                    if (row != null) {
                        int cells = row.getPhysicalNumberOfCells();
                        for (int k = 0; k < cells; k++) {
                            HSSFCell cell = row.getCell((short) k);
                            if (cell != null) {
                                String value = null;
                                switch(cell.getCellType()) {
                                    case HSSFCell.CELL_TYPE_FORMULA:
                                        value = "FORMULA";
                                        break;
                                    case HSSFCell.CELL_TYPE_NUMERIC:
                                        value = Double.toString(cell.getNumericCellValue());
                                        break;
                                    case HSSFCell.CELL_TYPE_STRING:
                                        value = cell.getStringCellValue();
                                        break;
                                    default:
                                }
                                System.out.print(value + "  ");
                            } else {
                                System.out.print("NULL  ");
                            }
                        }
                        System.out.println("");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("\nUsage: USCultivars <.xls filename>\n");
            System.out.println("Example: UScultivars_since_1971.xls com.mysql.jdbc.Driver ");
            System.exit(0);
        }
        String filename = args[0];
        PedigreeXLS instance = new PedigreeXLS(filename);
    }
}
