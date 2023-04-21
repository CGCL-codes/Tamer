package pruebas;

import junit.framework.TestCase;
import neoAtlantis.utilidades.logger.interfaces.Logger;
import neoAtlantis.utilidades.logger.*;
import neoAtlantis.utilidades.notifier.MailNotifier;
import java.util.Properties;

/**
 *
 * @author HP
 */
public class ProbadorLogger extends TestCase {

    public static String parseaArreglo(String[] a) {
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; a != null && i < a.length; i++) {
            sb.append("<<").append(a[i]).append(">>");
        }
        return sb.toString();
    }

    public static void configuraLoger(Logger d) {
        d.setOri("127.0.0.1");
    }

    public void testLogerArchivo() throws Exception {
        Logger d = new FileLogger("Mi App", "c:/tmp/prueba.log");
        configuraLoger(d);
        d.escribeLog(ProbadorLogger.class, "Prueba log archivo.", new Exception("Excepcion generica"));
    }

    public void testLogerBD() throws Exception {
        DataBaseLogger d = new DataBaseLogger("Mi App", "c:/tmp/mysqlFenix.xml");
        Properties p = new Properties();
        p.setProperty("host", "se.economia.gob.mx");
        p.setProperty("from", "albertosl@economia.gob.mx");
        p.setProperty("to", "albertosl@economia.gob.mx");
        MailNotifier noti = new MailNotifier("Aviso del sistema 'Mi App'", p);
        noti.setDebug(true);
        configuraLoger(d);
        d.escribeLog(ProbadorLogger.class, "Prueba log BD.", new Exception("Excepcio generica"));
        String[][] dat = d.generaReporte(10, 5, null);
        for (int i = 0; dat != null && i < dat.length; i++) {
            System.out.println(this.parseaArreglo(dat[i]));
        }
        System.out.println("Pos: " + d.getPosicionActual());
    }
}
