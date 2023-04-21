package org.hironico.dbtool2.querymanager;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import org.apache.log4j.Logger;
import org.hironico.dbtool2.sqleditor.SQLDocumentEditorPanel;

/**
 * Cette classe permet de charger un fichier texte et de le mettre dans
 * le SQL editor associé lors de la construction de cet objet.
 * On va utiliser cette action pour le SQLEditor ainsi que dans le 
 * Query manager.
 * @author hironico
 * @since 2.0.0
 */
public class LoadSQLFileAction extends AbstractAction {

    private static final long serialVersionUID = -6922365601307862601L;

    private static final Logger logger = Logger.getLogger("org.hironico.dbtool2.querymanager");

    private StringBuffer fileNameToLoad = null;

    private SQLDocumentEditorPanel sqlEditor = null;

    public LoadSQLFileAction(StringBuffer fileNameToLoad, SQLDocumentEditorPanel sqlEditor) {
        this.fileNameToLoad = fileNameToLoad;
        this.sqlEditor = sqlEditor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (sqlEditor == null) {
            logger.error("Cannot put the sql text in a null sql editor...");
            return;
        }
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Open SQL text file...");
        chooser.setFileFilter(new SQLFileFilter());
        chooser.setAcceptAllFileFilterUsed(true);
        int ret = chooser.showOpenDialog(sqlEditor);
        if (ret != JFileChooser.APPROVE_OPTION) return;
        File fileToLoad = chooser.getSelectedFile();
        if (!fileToLoad.exists()) {
            logger.error("File not found: " + fileToLoad.getName());
            return;
        }
        fileNameToLoad.delete(0, fileNameToLoad.length());
        fileNameToLoad.append(fileToLoad.getAbsolutePath());
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileToLoad));
            String ligne = null;
            String fileContent = "";
            while ((ligne = in.readLine()) != null) fileContent += ligne + "\n";
            in.close();
            sqlEditor.setSqlQuery(fileContent);
        } catch (FileNotFoundException fnfe) {
            logger.error("Cannot open file for reading.", fnfe);
        } catch (IOException ioe) {
            logger.error("Cannot read file.", ioe);
        }
    }
}
