package com.editor.view;

import java.util.Vector;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import org.kxml2.kdom.Document;
import com.editor.util.IPersistencia;
import com.editor.util.Persistencia;
import com.editor.util.Util;
import org.kxml2.kdom.Element;

/**
 * Classe main da aplica�ao.
 */
public class App extends SingleFrameApplication {

    private Vector<IPersistencia> componentes = new Vector();

    private String Nome = "teste";

    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
        View v = new View(this);
        v.getFrame().setResizable(false);
        v.liberaComponentes(false);
        show(v);
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override
    protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of App
     */
    public static App getApplication() {
        return Application.getInstance(App.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(App.class, args);
    }

    public String getXML() {
        Document document = Util.createDocument(getNome());
        for (int i = 0; i < componentes.size(); i++) {
            IPersistencia ident = componentes.elementAt(i);
            ident.toXMLElement(document.getRootElement());
        }
        return Util.toString(document);
    }

    public void gravarXML(String url) {
        Document document = Util.createDocument("ficha");
        Element root = Persistencia.createElement(document.getRootElement(), "nome_ficha", getNome());
        for (int i = 0; i < componentes.size(); i++) {
            IPersistencia ident = componentes.elementAt(i);
            ident.setId(i);
            ident.toXMLElement(document.getRootElement());
        }
        Util.toArquivo(document, url + "\\" + getNome() + ".xml");
    }

    public String toStringXML() {
        Document document = Util.createDocument("ficha");
        Element root = Persistencia.createElement(document.getRootElement(), "nome_ficha", getNome());
        for (int i = 0; i < componentes.size(); i++) {
            IPersistencia ident = componentes.elementAt(i);
            ident.setId(i);
            ident.toXMLElement(document.getRootElement());
        }
        return Util.toString(document);
    }

    /**
     * @return the componentes
     */
    public Vector<IPersistencia> getComponentes() {
        return componentes;
    }

    public void addComponentes(IPersistencia comp) {
        componentes.add(comp);
    }

    /**
     * @param componentes the componentes to set
     */
    public void setComponentes(Vector<IPersistencia> componentes) {
        this.componentes = componentes;
    }

    /**
     * @return the Nome
     */
    public String getNome() {
        return Nome;
    }

    /**
     * @param Nome the Nome to set
     */
    public void setNome(String Nome) {
        this.Nome = Nome;
    }
}
