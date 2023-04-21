package tico.imageGallery.actions;

import java.awt.event.ActionEvent;
import java.util.Vector;
import java.io.File;
import javax.swing.JOptionPane;
import tico.configuration.TLanguage;
import tico.editor.TEditor;
import tico.imageGallery.dataBase.TIGDataBase;

/**
 * Action which exists from the editor application.
 * 
 * @author Patricia M. Jaray
 * @version 2.2 Apr 2, 2008
 */
public class TIGModifyImageAction extends TIGAbstractAction {

    protected TIGDataBase dataBase;

    protected Vector theConcepts;

    protected String imageName;

    protected String imagePath;

    /**
	 * Constructor for TEditorExitAction.
	 * 
	 * @param editor The boards' editor
	 */
    public TIGModifyImageAction(TEditor editor, TIGDataBase dataBase, String path, String name, Vector concepts) {
        super(editor);
        this.dataBase = dataBase;
        theConcepts = (Vector) concepts.clone();
        this.imagePath = path;
        this.imageName = name;
    }

    public void actionPerformed(ActionEvent e) {
        int error = TIGDataBase.modifyImage(imagePath, theConcepts);
        if (error != 0) JOptionPane.showConfirmDialog(null, TLanguage.getString("TIGModifyImageAction.ERROR"), TLanguage.getString("TIGModifyImageAction.NAME"), JOptionPane.CLOSED_OPTION);
    }
}
