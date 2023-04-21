package org.neblipedia.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.neblinux.nebliserver.NebliServerHTTPD;
import org.neblinux.nebliserver.configuracion.Carpeta;
import org.neblipedia.gui.Gui;
import org.neblipedia.gui.nogui.NoGui;
import org.neblipedia.gui.swing.ApplicationLauncher;
import org.neblipedia.gui.swing.JFrameGui;
import org.neblipedia.imagen.ImagenBasico;
import org.neblipedia.wiki.Buscador;
import org.neblipedia.wiki.config.WikiParserConfig;

/**
 * esta clase debe ser corregida
 * 
 * @author juan
 * 
 */
public class Neblipedia {

    private static Buscador instancia;

    private final Logger log;

    public static Buscador getBuscadorInstancia() {
        return instancia;
    }

    public static void main(String[] args) {
        new Neblipedia();
    }

    private Buscador buscador;

    private Gui frame = null;

    /**
	 * configuracion del nebliserver
	 */
    private NebliserverConfiguracion s_configuracion;

    private WikiParserConfig wikiconfig;

    public Neblipedia() {
        verificarHome();
        System.setProperty("neblipedia.logs", wikiconfig.getLogBase().getAbsolutePath());
        PropertyConfigurator.configure("conf/log4j.conf");
        log = Logger.getLogger(Neblipedia.class);
        log.info("inicia neblipedia");
        try {
            boolean gui = wikiconfig.isGui();
            boolean navegador = false;
            if (gui) {
                frame = new JFrameGui(this);
                navegador = wikiconfig.isGuiNavegador();
            } else {
                frame = new NoGui();
            }
            ImagenBasico.getInstancia();
            frame.barraProgreso("Cargando wikis", 3);
            frame.log("wikis: " + wikiconfig.getWikisBase());
            buscador = new Buscador(wikiconfig, frame);
            instancia = buscador;
            frame.barraProgreso("Iniciando Servidor", 4);
            Carpeta a = new Carpeta("/math2", wikiconfig.getCacheBaseMath());
            s_configuracion.addCarpeta(a);
            a = new Carpeta("/" + WikiParserConfig.DIR_CACHE_IMAGENES, wikiconfig.getCacheBaseImagenes());
            s_configuracion.addCarpeta(a);
            NebliServerHTTPD.serverFactory(s_configuracion);
            frame.barraProgreso("Abriendo Navegador web", 5);
            frame.log("Servidor Iniciado en el puerto: " + s_configuracion.getPuertoTcp());
            frame.log("Neblipedia Iniciada...");
            frame.barraProgreso("Carga Completa", 6);
            frame.log("ahora, abre tu navegador favorito " + "y digita en la barra de direcciones: http://localhost:" + s_configuracion.getPuertoTcp() + " o haz clic en el boton \"lanzar navegador\"");
            if (navegador) {
                navegador();
            }
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException ioe) {
            frame.log("Error: " + ioe);
            frame.log("Verifique sus permisos o q" + " el puerto no se encuentre ocupado.");
            frame.log("Servidor no iniciado...");
        }
    }

    /**
	 * si no existe la carpeta .neblipedia en el home del usuario entonces
	 * devuelve la carpeta con la configuracion por defecto que se encuentra en
	 * la instalacion del enciclopedia
	 * 
	 * @param archivo_conf
	 * @param base
	 * @return
	 */
    private File cargarConfiguracion(File base, String archivo_conf) {
        File configuracion_local = new File(base, archivo_conf);
        if (configuracion_local.exists() && configuracion_local.canRead()) {
            return configuracion_local;
        } else {
            return cargarConfiguracion(new File("conf"), archivo_conf);
        }
    }

    public void cerrar() {
        getBuscador().close();
    }

    public Buscador getBuscador() {
        return buscador;
    }

    public WikiParserConfig getWikiconfig() {
        return wikiconfig;
    }

    public void navegador() {
        ApplicationLauncher.launchURL("http://localhost:" + s_configuracion.getPuertoTcp());
    }

    /**
	 * crea la configuracion local para usuario
	 */
    private void verificarHome() {
        File home = WikiParserConfig.getHomeUsuario();
        if (home.exists() && home.canRead() && home.canWrite()) {
            File neblipedia = new File(home, WikiParserConfig.HOME_DIR_NEBLIPEDIA);
            File conf = cargarConfiguracion(neblipedia, "neblipedia.conf");
            try {
                Properties properties = new Properties();
                properties.load(new FileInputStream(conf));
                wikiconfig = new WikiParserConfig(properties);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            conf = cargarConfiguracion(neblipedia, "nebliserver.conf.xml");
            this.s_configuracion = new NebliserverConfiguracion(conf);
        }
    }
}
