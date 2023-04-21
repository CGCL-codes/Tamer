package net.juantxu.pentaho.launcher.ejecutores;

import java.io.File;
import java.util.Properties;
import net.juantxu.pentaho.launcher.CargaProperties;
import net.juantxu.pentaho.launcher.ValidaEjecutableNix;
import net.juantxu.pentaho.launcher.ValidaEjecutableWin;
import org.apache.log4j.Logger;

public class EjecutaWorkbench implements EjecutaPentaho {

    static Logger log = Logger.getLogger(EjecutaPentaho.class);

    public int lanza() {
        int resultado = 0;
        String os = "";
        log.debug("lanzando el wokbench");
        os = System.getProperty("os.name");
        log.debug("sistema operativo :".concat(os));
        if (os.toLowerCase().contains("win")) {
            resultado = ejecutaWin();
        } else {
            log.debug("es un sistema *NIX");
            resultado = ejecutaNix();
        }
        return resultado;
    }

    public int ejecutaWin() {
        int resultado = 0;
        Properties prop = new CargaProperties().Carga();
        File miPath = new File(prop.getProperty("workbenchRelativePath"));
        log.debug("Ejecutando en :" + miPath.getAbsolutePath());
        if (new ValidaEjecutableWin().valida(miPath.getAbsolutePath() + "\\workbench.bat")) {
            Runtime r = Runtime.getRuntime();
            try {
                log.debug("ejecutando como en nix");
                r.exec(miPath.getAbsolutePath() + "\\workbench.bat", null, miPath);
                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
                resultado = 1;
            }
        } else {
            log.error("no se encuentra el ejecutable");
            resultado = 7;
        }
        return resultado;
    }

    public int ejecutaNix() {
        int resultado = 0;
        Properties prop = new CargaProperties().Carga();
        if (new ValidaEjecutableNix().valida(prop.getProperty("workbenchRelativePath") + "/workbench.sh")) {
            File miPath = new File(prop.getProperty("workbenchRelativePath"));
            log.debug("Ejecutando en :" + miPath.getAbsolutePath());
            Runtime r = Runtime.getRuntime();
            try {
                r.exec("./workbench.sh", null, miPath);
                Thread.sleep(3000);
            } catch (Exception e) {
                resultado = 1;
                e.printStackTrace();
            }
        } else {
            log.error("no se encuentra el ejecutable");
            resultado = 7;
        }
        return resultado;
    }
}
