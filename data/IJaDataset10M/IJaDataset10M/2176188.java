package savi.spool;

import java.io.File;
import java.util.Properties;
import savi.util.Util;

/**
 * @author dausech
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class WinPrintJob {

    public static void imprime(String arqp) {
        Properties p = Util.lerPropriedades(arqp);
        String so = System.getProperty("os.name");
        int vezes = Integer.parseInt(p.getProperty("copias"));
        String fila = p.getProperty("fila");
        String drive = arqp.substring(0, 2);
        if (drive.indexOf(":") == -1) drive = "";
        String arqRel = new String(drive + p.getProperty("arquivo"));
        if (!Util.TestaImpressora(fila)) {
            System.out.println("Impressora " + fila + " inoperante.");
            return;
        }
        StringBuffer comando1 = new StringBuffer("");
        if (so.matches("Windows 98")) {
            comando1.append("c:/command.com /C type ");
        } else {
            comando1.append("cmd /C type ");
        }
        comando1.append(arqRel + " > " + fila);
        for (int v = 0; v < vezes; v++) {
            System.out.println(comando1.toString() + "  " + (v + 1) + " de " + vezes);
            Util.executeCommand(comando1.toString(), 5000);
            if (vezes > 1) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("\n");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        File f = new File(arqp);
        f.delete();
    }
}
