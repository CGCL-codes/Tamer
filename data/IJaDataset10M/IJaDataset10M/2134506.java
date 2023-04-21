package it.freax.fpm.test.main;

import it.freax.fpm.core.solver.specs.TarballSpec;
import it.freax.fpm.util.TimeSpan;
import java.io.File;
import java.util.Calendar;

public class TestArchives {

    static boolean goRecursive = false;

    static int counter = 0;

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        String path = "";
        String goRec = "";
        path = "/home/kLeZ-hAcK/Scaricati/hexedit-1.2.12.src.tgz";
        goRec = "false";
        File file = new File(path);
        goRecursive = Boolean.parseBoolean(goRec);
        long start = Calendar.getInstance().getTimeInMillis();
        if (file.isDirectory()) {
            GetArchiveTypeRecursive(file.listFiles());
        } else if (file.isFile()) {
            counter++;
            TarballSpec spec = new TarballSpec(null);
            spec.Load(file);
            System.out.println(spec.toString());
        }
        long stop = Calendar.getInstance().getTimeInMillis();
        long diff = stop - start;
        System.out.print("Il programma ha impiegato ");
        System.out.print(TimeSpan.format(diff, TimeSpan.MILLISECOND));
        System.out.print(" millisecondi per rilevare ");
        System.out.print(counter);
        System.out.println(" archivi");
    }

    public static void GetArchiveTypeRecursive(File[] filesInFolder) {
        if ((filesInFolder == null) || (filesInFolder.length == 0)) {
            return;
        }
        for (int i = 0; i < filesInFolder.length; i++) {
            File child = filesInFolder[i];
            if (child.isFile()) {
                TarballSpec spec = new TarballSpec(null);
                spec.Load(child);
            } else {
                System.out.print("L'elemento è una cartella");
                if (goRecursive) {
                    System.out.println(", vado in profondità");
                    GetArchiveTypeRecursive(child.listFiles());
                } else {
                    System.out.println();
                }
                System.out.print("Cartella corrente: ");
                System.out.println(child.getName());
            }
            counter++;
        }
    }
}
