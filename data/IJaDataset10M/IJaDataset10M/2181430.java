package tmpa3;

import java.io.File;
import java.util.Date;

public class run {

    public static void aufgabe1() {
        for (int i = 0; i < File.listRoots().length; i++) {
            System.out.print(File.listRoots()[i] + " \tName: ");
            if (File.listRoots()[i].getName().isEmpty()) System.out.print("\tnamenlos");
            System.out.println(File.listRoots()[i].getName() + " \tFreespace: " + File.listRoots()[i].getFreeSpace() + " \tTotalspace: " + File.listRoots()[i].getTotalSpace() + " \tUseablespace: " + File.listRoots()[i].getUsableSpace());
        }
    }

    public static void aufgabe2() {
        try {
            File f1 = new File("testdir");
            if (!f1.exists()) f1.mkdir();
            File f2 = new File("testdir" + File.separator + "testfile.txt");
            if (!f2.exists()) f2.createNewFile();
            System.out.println("Es existieren:\n" + f1.getAbsolutePath() + "\n" + f2.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void analyseOrdner(File f3) {
        File fTemp;
        String[] dateien;
        dateien = f3.list();
        Date lastmodif;
        int anzinhalte = dateien.length;
        if (f3.list() != null) {
            System.out.println("\n\nStandort:  " + f3.getAbsolutePath());
            System.out.println("Ordner:");
            for (int i = 0; i < dateien.length; i++) {
                fTemp = new File(f3.getAbsolutePath() + "\\" + dateien[i]);
                System.out.println("\t\t\tfTemp=" + fTemp.getAbsolutePath());
                lastmodif = new Date(fTemp.lastModified());
                if (fTemp.isDirectory()) {
                    anzinhalte--;
                    System.out.println("\n\n\n\t" + dateien[i] + "   \t" + fTemp.length() + "\t" + lastmodif);
                    run.analyseOrdner(fTemp);
                }
            }
            System.out.println("\n\nStandort:  " + f3.getAbsolutePath());
            System.out.println("Dateien: ");
            for (int i = 0; i < dateien.length; i++) {
                fTemp = new File(f3 + "\\" + dateien[i]);
                lastmodif = new Date(fTemp.lastModified());
                if (fTemp.isFile()) System.out.println(dateien[i] + "   \t" + fTemp.length() + "\t" + lastmodif);
            }
            if (anzinhalte == 0) System.out.println("\tkeine");
        }
    }

    public static void aufgabe3() {
        ReadKlasse rc = new ReadKlasse();
        File f3;
        String p1;
        do {
            p1 = rc.ReadString("absolutenPfad");
            f3 = new File(p1);
            System.out.print("\t\t-->" + f3.getAbsolutePath());
            if (!f3.isDirectory()) System.out.println(" nicht gefunden"); else System.out.println(" akzeptiert");
        } while (!f3.isDirectory());
        System.out.println("Name\t\tGr��e\t\tUhrzeitd�nderung");
        run.analyseOrdner(f3);
    }

    public static void main(String[] args) {
        int menu;
        ReadKlasse rc = new ReadKlasse();
        do {
            System.out.println("\n\n\tAufgabe1: 1\tAufgabe2: 2\tAufgabe3: 3\tEnde: 4");
            menu = rc.ReadInt("\n\tAuswahl");
            switch(menu) {
                case 1:
                    run.aufgabe1();
                    break;
                case 2:
                    run.aufgabe2();
                    break;
                case 3:
                    run.aufgabe3();
                    break;
            }
        } while (menu != 4);
    }
}
