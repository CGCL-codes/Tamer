package edu.jrous.ui;

import java.awt.Component;
import java.io.Serializable;
import javax.swing.text.Document;
import edu.jrous.core.Network;

/**
 * El objeto el cual se vba ha serializar
 * @author Administrador
 *
 */
public class FileObject implements Serializable {

    /**
	 * Numero de serie
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * La parte logica del sistema
	 */
    public Network net;

    /**
	 * La parte graphite del programa
	 */
    public Component[] grah;

    /**
	 * Documento del Post-it
	 */
    public Document doc;

    /**
	 * Si es un ejemplo
	 */
    public boolean example;

    /**
	 * Constructor Del obejto a gruardar
	 * @param _net
	 * @param _grah
	 */
    public FileObject(Network _net, Component[] _grah, Document _doc, boolean _example) {
        net = _net;
        grah = _grah;
        doc = _doc;
        example = _example;
    }

    /**
	 * @return Returns the grah.
	 */
    public Component[] getGrah() {
        return grah;
    }

    /**
	 * @return Returns the net.
	 */
    public Network getNet() {
        return net;
    }

    /**
	 * Para instanciar a un objeto a FileObject
	 * @param obj
	 * @return FileObject
	 */
    public static FileObject newInstance(Object obj) {
        return (FileObject) obj;
    }

    /**
	 * @return Returns the doc.
	 */
    public Document getDoc() {
        return doc;
    }

    /**
	 * @return Returns the example.
	 */
    public boolean isExample() {
        return example;
    }
}
