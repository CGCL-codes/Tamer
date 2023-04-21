package datcom2;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aleksey
 */
public class C3PO {

    private String for005;

    private String for006;

    private String masterPrintCmalpha;

    private String ultimateMasterPrintCmalpha;

    private String masterPrintCdalpha;

    private String ultimateMasterPrintCdalpha;

    private String masterPrintClalpha;

    private String ultimateMasterPrintClalpha;

    private String masterPrintCnalpha;

    private String ultimateMasterPrintCnalpha;

    private String masterPrintCaalpha;

    private String ultimateMasterPrintCaalpha;

    private String masterPrintXcpalpha;

    private String ultimateMasterPrintXcpalpha;

    private String masterPrintClaalpha;

    private String ultimateMasterPrintClaalpha;

    private String masterPath;

    private char[] for005array;

    private char[] for006array;

    private getFor005 getFile;

    private String path;

    private String MACH;

    private String newMACH;

    private String ALT;

    private String newALT;

    private int initialStart = 0;

    private String MASTER = null;

    ArrayList alphaTable;

    ArrayList CdTable;

    ArrayList ClTable;

    ArrayList CmTable;

    ArrayList CnTable;

    ArrayList CaTable;

    ArrayList XcpTable;

    ArrayList ClaTable;

    ArrayList Cmalpha;

    ArrayList Cdalpha;

    ArrayList Clalpha;

    ArrayList Cnalpha;

    ArrayList Caalpha;

    ArrayList Xcpalpha;

    ArrayList Claalpha;

    public C3PO(String for005) {
        this.for005 = for005;
    }

    public String alterFile(String newMACHs, String newALTs) {
        this.newMACH = null;
        this.newALT = null;
        this.newMACH = newMACHs;
        this.newALT = newALTs;
        getFile = new getFor005();
        for005array = for005.toCharArray();
        for (int i = 0; for005array.length > i; i++) {
            if (for005array[i] == 'M' && for005array[i + 1] == 'A' && for005array[i + 2] == 'C' && for005array[i + 3] == 'H' && for005array[i - 1] != 'N' && for005array[i - 1] != 'T' && for005array[i - 1] != 'S') {
                int k = 0;
                for (int j = 8; for005array[i + j] != ','; j++) {
                    if (MACH == null) {
                        MACH = Character.toString(for005array[i + j]);
                        for005array[i + j] = newMACH.charAt(k);
                        k++;
                    } else {
                        MACH = MACH + Character.toString(for005array[i + j]);
                        for005array[i + j] = newMACH.charAt(k);
                        k++;
                    }
                }
            }
        }
        for (int i = 0; for005array.length > i; i++) {
            if (for005array[i] == 'A' && for005array[i + 1] == 'L' && for005array[i + 2] == 'T' && for005array[i - 1] != 'N') {
                int k = 0;
                for (int j = 7; for005array[i + j] != ','; j++) {
                    if (ALT == null) {
                        ALT = Character.toString(for005array[i + j]);
                        for005array[i + j] = newALT.charAt(k);
                        k++;
                    } else {
                        ALT = ALT + Character.toString(for005array[i + j]);
                        for005array[i + j] = newALT.charAt(k);
                        k++;
                    }
                }
            }
        }
        System.out.println("THE MACH NUMBER IS = " + MACH);
        System.out.println("THE ALT NUMBER IS = " + ALT);
        for005 = new String(for005array);
        return for005;
    }

    public ArrayList grabParameters(String workPath) {
        path = workPath + "/for006.dat";
        ArrayList JSBtables = new ArrayList();
        alphaTable = new ArrayList();
        CdTable = new ArrayList();
        ClTable = new ArrayList();
        CmTable = new ArrayList();
        CnTable = new ArrayList();
        CaTable = new ArrayList();
        XcpTable = new ArrayList();
        ClaTable = new ArrayList();
        getFile = new getFor005();
        for006 = null;
        for006array = null;
        try {
            for006 = getFile.getText(path);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(C3PO.class.getName()).log(Level.SEVERE, null, ex);
        }
        for006array = for006.toCharArray();
        int j;
        int lineBreaks = 0;
        boolean openInput = false;
        int skip = 0;
        int position = 0;
        for (int i = 0; i < for006array.length; i++) {
            if (for006array[i] == '\n') {
                lineBreaks++;
                position = 1;
            }
            if (lineBreaks >= 150) {
                if (for006array[i] != ' ') {
                    String temp = null;
                    for (j = 0; (i + j) < for006array.length && for006array[i + j] != ' '; j++) {
                        if (temp != null) {
                            temp = temp + for006array[i + j];
                        } else {
                            temp = Character.toString(for006array[i + j]);
                        }
                    }
                    i = i + j;
                    if (temp.equals("CNB")) {
                        openInput = true;
                        System.out.println("Opened the gates");
                    }
                    if (openInput == true && temp.equals("AUTOMATED") || temp.equals("ALPHA")) {
                        openInput = false;
                    }
                    if (openInput == true) {
                        skip++;
                        if (skip >= 3) {
                            if (position == 1) {
                                alphaTable.add(temp);
                            }
                            if (position == 2) {
                                CdTable.add(temp);
                            }
                            if (position == 3) {
                                ClTable.add(temp);
                            }
                            if (position == 4) {
                                CmTable.add(temp);
                            }
                            if (position == 5) {
                                CnTable.add(temp);
                            }
                            if (position == 6) {
                                CaTable.add(temp);
                            }
                            if (position == 7) {
                                XcpTable.add(temp);
                            }
                            if (position == 8) {
                                ClaTable.add(temp);
                            }
                            position++;
                        }
                        if (temp.toCharArray()[temp.toCharArray().length - 1] == '\n') {
                            position = 1;
                            System.out.println("RESET THE POSITION .........................." + temp);
                        }
                    }
                }
            }
        }
        return JSBtables;
    }

    public void createMaster(String workingPath, String printMACH, String printALT, int key, String masterPath) {
        initialStart = key;
        MASTER = null;
        getFile = new getFor005();
        this.masterPath = masterPath;
        if (initialStart == 0) {
            Cmalpha = new ArrayList();
            Cdalpha = new ArrayList();
            Clalpha = new ArrayList();
            Cnalpha = new ArrayList();
            Caalpha = new ArrayList();
            Xcpalpha = new ArrayList();
            Claalpha = new ArrayList();
            Cmalpha.add("<tableData breakPoint=\"" + printALT + "\">");
            Cdalpha.add("<tableData breakPoint=\"" + printALT + "\">");
            Clalpha.add("<tableData breakPoint=\"" + printALT + "\">");
            Cnalpha.add("<tableData breakPoint=\"" + printALT + "\">");
            Caalpha.add("<tableData breakPoint=\"" + printALT + "\">");
            Xcpalpha.add("<tableData breakPoint=\"" + printALT + "\">");
            Claalpha.add("<tableData breakPoint=\"" + printALT + "\">");
            MASTER = "\n\t\t" + printMACH;
            Cmalpha.add(MASTER);
            Cdalpha.add(MASTER);
            Clalpha.add(MASTER);
            Cnalpha.add(MASTER);
            Caalpha.add(MASTER);
            Xcpalpha.add(MASTER);
            Claalpha.add(MASTER);
            for (int simba = 0; simba < alphaTable.size(); simba++) {
                MASTER = "\n" + alphaTable.get(simba) + "\t\t" + CmTable.get(simba);
                Cmalpha.add(MASTER);
                MASTER = "\n" + alphaTable.get(simba) + "\t\t" + CdTable.get(simba);
                Cdalpha.add(MASTER);
                MASTER = "\n" + alphaTable.get(simba) + "\t\t" + ClTable.get(simba);
                Clalpha.add(MASTER);
                MASTER = "\n" + alphaTable.get(simba) + "\t\t" + CnTable.get(simba);
                Cnalpha.add(MASTER);
                MASTER = "\n" + alphaTable.get(simba) + "\t\t" + CaTable.get(simba);
                Caalpha.add(MASTER);
                MASTER = "\n" + alphaTable.get(simba) + "\t\t" + XcpTable.get(simba);
                Xcpalpha.add(MASTER);
                MASTER = "\n" + alphaTable.get(simba) + "\t\t" + ClaTable.get(simba);
                Claalpha.add(MASTER);
            }
        }
        if (initialStart >= 1) {
            MASTER = "\t\t\t" + printMACH;
            Cmalpha.set(1, Cmalpha.get(1) + MASTER);
            Cdalpha.set(1, Cdalpha.get(1) + MASTER);
            Clalpha.set(1, Clalpha.get(1) + MASTER);
            Cnalpha.set(1, Cnalpha.get(1) + MASTER);
            Caalpha.set(1, Caalpha.get(1) + MASTER);
            Xcpalpha.set(1, Xcpalpha.get(1) + MASTER);
            Claalpha.set(1, Claalpha.get(1) + MASTER);
            for (int mufasa = 2; mufasa <= (alphaTable.size() + 1); mufasa++) {
                Cmalpha.set(mufasa, Cmalpha.get(mufasa) + "\t\t" + CmTable.get(mufasa - 2));
                Cdalpha.set(mufasa, Cdalpha.get(mufasa) + "\t\t" + CdTable.get(mufasa - 2));
                Clalpha.set(mufasa, Clalpha.get(mufasa) + "\t\t" + ClTable.get(mufasa - 2));
                Cnalpha.set(mufasa, Cnalpha.get(mufasa) + "\t\t" + CnTable.get(mufasa - 2));
                Caalpha.set(mufasa, Caalpha.get(mufasa) + "\t\t" + CaTable.get(mufasa - 2));
                Xcpalpha.set(mufasa, Xcpalpha.get(mufasa) + "\t\t" + XcpTable.get(mufasa - 2));
                Claalpha.set(mufasa, Claalpha.get(mufasa) + "\t\t" + ClaTable.get(mufasa - 2));
            }
        }
        initialStart++;
        String print = null;
        Cmalpha.add("\n\t\t\t</tableData>\n\t\t" + "</table>\n\t" + "</product>\n" + "</function>");
        for (int d = 0; d <= (alphaTable.size() + 1); d++) {
            if (print != null) {
                print = print + Cmalpha.get(d);
            } else {
                print = (String) Cmalpha.get(d);
            }
        }
        getFile.writeFile(workingPath, "Cmalpha.txt", print);
        masterPrintCmalpha = print;
        print = null;
        for (int d = 0; d <= (alphaTable.size() + 1); d++) {
            if (print != null) {
                print = print + Cdalpha.get(d);
            } else {
                print = (String) Cdalpha.get(d);
            }
        }
        getFile.writeFile(workingPath, "Cdalpha.txt", print);
        masterPrintCdalpha = print;
        print = null;
        for (int d = 0; d <= (alphaTable.size() + 1); d++) {
            if (print != null) {
                print = print + Clalpha.get(d);
            } else {
                print = (String) Clalpha.get(d);
            }
        }
        getFile.writeFile(workingPath, "Clalpha.txt", print);
        masterPrintClalpha = print;
        print = null;
        for (int d = 0; d <= (alphaTable.size() + 1); d++) {
            if (print != null) {
                print = print + Cnalpha.get(d);
            } else {
                print = (String) Cnalpha.get(d);
            }
        }
        getFile.writeFile(workingPath, "Cnalpha.txt", print);
        masterPrintCnalpha = print;
        print = null;
        for (int d = 0; d <= (alphaTable.size() + 1); d++) {
            if (print != null) {
                print = print + Caalpha.get(d);
            } else {
                print = (String) Caalpha.get(d);
            }
        }
        getFile.writeFile(workingPath, "Caalpha.txt", print);
        masterPrintCaalpha = print;
        print = null;
        for (int d = 0; d <= (alphaTable.size() + 1); d++) {
            if (print != null) {
                print = print + Xcpalpha.get(d);
            } else {
                print = (String) Xcpalpha.get(d);
            }
        }
        getFile.writeFile(workingPath, "Xcpalpha.txt", print);
        masterPrintXcpalpha = print;
        print = null;
        for (int d = 0; d <= (alphaTable.size() + 1); d++) {
            if (print != null) {
                print = print + Claalpha.get(d);
            } else {
                print = (String) Claalpha.get(d);
            }
        }
        getFile.writeFile(workingPath, "Claalpha.txt", print);
        masterPrintClaalpha = print;
    }

    public void writeMaster() {
        if (ultimateMasterPrintCmalpha != null) {
            ultimateMasterPrintCmalpha = ultimateMasterPrintCmalpha + masterPrintCmalpha + "\n</tableData>\n";
        } else {
            ultimateMasterPrintCmalpha = masterPrintCmalpha + "\n</tableData>\n";
        }
        getFile.writeFile(masterPath, "MASTER_Cmalpha.txt", ultimateMasterPrintCmalpha);
        if (ultimateMasterPrintCdalpha != null) {
            ultimateMasterPrintCdalpha = ultimateMasterPrintCdalpha + masterPrintCdalpha + "\n</tableData>\n";
        } else {
            ultimateMasterPrintCdalpha = masterPrintCdalpha + "\n</tableData>\n";
        }
        getFile.writeFile(masterPath, "MASTER_Cdalpha.txt", ultimateMasterPrintCdalpha);
        if (ultimateMasterPrintClalpha != null) {
            ultimateMasterPrintClalpha = ultimateMasterPrintClalpha + masterPrintClalpha + "\n</tableData>\n";
        } else {
            ultimateMasterPrintClalpha = masterPrintClalpha + "\n</tableData>\n";
        }
        getFile.writeFile(masterPath, "MASTER_Clalpha.txt", ultimateMasterPrintClalpha);
        if (ultimateMasterPrintCnalpha != null) {
            ultimateMasterPrintCnalpha = ultimateMasterPrintCnalpha + masterPrintCnalpha + "\n</tableData>\n";
        } else {
            ultimateMasterPrintCnalpha = masterPrintCnalpha + "\n</tableData>\n";
        }
        getFile.writeFile(masterPath, "MASTER_Cnalpha.txt", ultimateMasterPrintCnalpha);
        if (ultimateMasterPrintCaalpha != null) {
            ultimateMasterPrintCaalpha = ultimateMasterPrintCaalpha + masterPrintCaalpha + "\n</tableData>\n";
        } else {
            ultimateMasterPrintCaalpha = masterPrintCaalpha + "\n</tableData>\n";
        }
        getFile.writeFile(masterPath, "MASTER_Caalpha.txt", ultimateMasterPrintCaalpha);
        if (ultimateMasterPrintXcpalpha != null) {
            ultimateMasterPrintXcpalpha = ultimateMasterPrintXcpalpha + masterPrintXcpalpha + "\n</tableData>\n";
        } else {
            ultimateMasterPrintXcpalpha = masterPrintXcpalpha + "\n</tableData>\n";
        }
        getFile.writeFile(masterPath, "MASTER_Xcpalpha.txt", ultimateMasterPrintXcpalpha);
        if (ultimateMasterPrintClaalpha != null) {
            ultimateMasterPrintClaalpha = ultimateMasterPrintClaalpha + masterPrintClaalpha + "\n</tableData>\n";
        } else {
            ultimateMasterPrintClaalpha = masterPrintClaalpha + "\n</tableData>\n";
        }
        getFile.writeFile(masterPath, "MASTER_Claalpha.txt", ultimateMasterPrintClaalpha);
    }
}
