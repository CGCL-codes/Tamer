package net.juantxu.pentaho.launcher.ejecutores;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import net.juantxu.pentaho.launcher.CargaProperties;
import net.juantxu.pentaho.launcher.CreaBat;
import net.juantxu.pentaho.launcher.RealizaComprobaciones;
import org.apache.log4j.Logger;

public class EjecutaBIServer implements EjecutaPentaho {

    static Logger log = Logger.getLogger(EjecutaBIServer.class);

    public int lanza() {
        String os = "";
        int resultado = 0;
        log.debug("lanzando bi server");
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
        String ejecutable = prop.getProperty("biserverRelativePath") + "/start-pentaho.bat";
        resultado = new RealizaComprobaciones().compruebaWin(ejecutable);
        new CreaBat(prop.getProperty("biserverRelativePath"), "start-pentaho.bat", "tmprunbi.bat");
        Runtime r = Runtime.getRuntime();
        try {
            r.exec("cmd.exe /K  start tmprunbi.bat ");
            Thread.sleep(8000);
            java.awt.Desktop.getDesktop().browse(new URI("http://localhost:8080"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
            resultado = 1;
        } catch (IOException e) {
            e.printStackTrace();
            resultado = 1;
        } catch (InterruptedException e) {
            e.printStackTrace();
            resultado = 1;
        }
        return resultado;
    }

    public int ejecutaNix() {
        int resultado = 0;
        String ejecutable = "";
        Properties prop = new CargaProperties().Carga();
        RealizaComprobaciones comp = new RealizaComprobaciones();
        ejecutable = prop.getProperty("biserverRelativePath") + "/tomcat/bin/catalina.sh";
        resultado = comp.compruebaNix(ejecutable);
        ejecutable = prop.getProperty("biserverRelativePath") + "/start-pentaho.sh";
        resultado = comp.compruebaNix(ejecutable);
        log.info("prompt to the user disabled in linux to avoid problems in first execution");
        File f = new File(prop.getProperty("biserverRelativePath") + "/promptuser.sh");
        if (f.exists()) {
            f.delete();
        }
        if (resultado == 0) {
            File miPath = new File(prop.getProperty("biserverRelativePath"));
            log.debug("Ejecutando en :" + miPath.getAbsolutePath());
            Runtime r = Runtime.getRuntime();
            try {
                r.exec("./start-pentaho.sh", null, miPath);
                Thread.sleep(8000);
                java.awt.Desktop.getDesktop().browse(new URI("http://localhost:8080"));
            } catch (URISyntaxException e) {
                e.printStackTrace();
                resultado = 1;
            } catch (IOException e) {
                e.printStackTrace();
                resultado = 1;
            } catch (InterruptedException e) {
                e.printStackTrace();
                resultado = 1;
            }
        }
        return resultado;
    }

    public int Para() {
        String os = "";
        int resultado = 0;
        log.debug("Parando Bi Server");
        os = System.getProperty("os.name");
        log.debug("sistema operativo :".concat(os));
        if (os.toLowerCase().contains("win")) {
            resultado = paraWin();
        } else {
            log.debug("es un sistema *NIX");
            resultado = paraNix();
        }
        return resultado;
    }

    public int paraWin() {
        int resultado = 0;
        RealizaComprobaciones comp = new RealizaComprobaciones();
        Properties prop = new CargaProperties().Carga();
        resultado = comp.compruebaWin(prop.getProperty("biserverRelativePath") + "/stop-pentaho.bat");
        if (resultado == 0) {
            new CreaBat(prop.getProperty("biserverRelativePath"), "stop-pentaho.bat", "tmprunstopbi.bat");
            Runtime r = Runtime.getRuntime();
            try {
                r.exec("cmd.exe /K start tmprunstopbi.bat  ");
            } catch (IOException e) {
                e.printStackTrace();
                resultado = 1;
            }
        }
        return resultado;
    }

    public int paraNix() {
        int resultado = 0;
        String ejecutable = "";
        Properties prop = new CargaProperties().Carga();
        RealizaComprobaciones comp = new RealizaComprobaciones();
        ejecutable = prop.getProperty("biserverRelativePath") + "/tomcat/bin/catalina.sh";
        resultado = comp.compruebaNix(ejecutable);
        ejecutable = prop.getProperty("biserverRelativePath") + "/stop-pentaho.sh";
        resultado = comp.compruebaNix(ejecutable);
        if (resultado == 0) {
            File miPath = new File(prop.getProperty("biserverRelativePath"));
            log.debug("Ejecutando en :" + miPath.getAbsolutePath());
            Runtime r = Runtime.getRuntime();
            try {
                r.exec("./stop-pentaho.sh", null, miPath);
            } catch (IOException e) {
                e.printStackTrace();
                resultado = 1;
            }
        }
        return resultado;
    }
}
