package edu.jrous.ui;

import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import javax.swing.JFileChooser;

/**
 * <p>Clase encargada de gestionar la parte </p>
 * @author Manuel Sako
 * @version 1.0
 */
public class FileMain {

    /**
	 * <p>Numero de la serie de FileMain</p>
	 */
    private static final long serialVersionUID = -1171224092623689333L;

    /**
	 * <p>El nombre del archivo</p>
	 */
    public transient String filename = null;

    /**
	 * <p>Clase principal Funcional</p>
	 */
    public transient Interface owner;

    /**
	 * <p>Browser para escoger archivo</p>
	 */
    public transient JFileChooser fChooser;

    /**
	 * <p>Filtro del browser</p>
	 */
    public transient FileFilterExtension netFilter;

    /**
	 * Constructor 
	 * @param _owner
	 */
    public FileMain(Interface _owner) {
        owner = _owner;
        filename = null;
    }

    /**
	 * Constructor 
	 * @param _owner
	 */
    public FileMain(Interface _owner, String _filename) {
        owner = _owner;
        filename = _filename;
    }

    /**
	 * Abrir Archivo de la Pc
	 * @param e
	 * @throws IOException
	 * @throws StreamCorruptedException
	 * @throws ClassNotFoundException
	 * @throws OptionalDataException
	 */
    void fileOpen(ActionEvent e) throws IOException, StreamCorruptedException, ClassNotFoundException, OptionalDataException {
        fChooser = new JFileChooser();
        netFilter = new FileFilterExtension();
        fChooser.setFileFilter(netFilter);
        fChooser.rescanCurrentDirectory();
        int returnVal = fChooser.showOpenDialog(owner);
        if (returnVal != JFileChooser.CANCEL_OPTION) filename = fChooser.getCurrentDirectory() + "/" + fChooser.getSelectedFile().getName();
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            owner.load(filename);
        }
    }

    /**
	 * Guardar el Archivo
	 * @param e
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
    void fileSave(ActionEvent e) throws FileNotFoundException, IOException {
        if (filename != null) {
            owner.save(filename);
        } else {
            fileSaveAs(e);
        }
    }

    /**
	 * Para grabar en el sistema
	 * @param e
	 * @throws IOException 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
    void fileSaveAs(ActionEvent e) throws IOException {
        fChooser = new JFileChooser();
        netFilter = new FileFilterExtension();
        fChooser.setFileFilter(netFilter);
        fChooser.rescanCurrentDirectory();
        int returnVal = fChooser.showSaveDialog(owner);
        if (returnVal != JFileChooser.CANCEL_OPTION) {
            filename = fChooser.getCurrentDirectory() + "/" + fChooser.getSelectedFile().getName();
        }
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            if (!fChooser.getSelectedFile().getName().endsWith("." + Constantes.FILE_EXTENSION)) {
                filename = filename + "." + Constantes.FILE_EXTENSION;
            }
            fChooser.setVisible(false);
            owner.save(filename);
        }
    }
}
