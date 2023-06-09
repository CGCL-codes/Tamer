package fr.crnan.videso3d;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import javax.swing.SwingWorker;
import javax.swing.event.EventListenerList;
import fr.crnan.videso3d.DatabaseManager.Type;

/**
 * Importe les données de fichiers et les stocke en base de données
 * L'import des données doit être fait dans la méthode doInBackground() afin d'être fait dans un thread secondaire.
 * doInBackground() renvoit le nombre de fichiers traités et publie le nom des fichiers traités
 * @author Bruno Spyckerelle
 * @version 0.3.3
 */
public abstract class FileParser extends SwingWorker<Integer, String> {

    /**
	 * Liste des listeners
	 */
    protected final EventListenerList listeners = new EventListenerList();

    /**
	 * Chemin vers les données
	 */
    protected String path;

    /**
	 * Nom du fichier en cours de traitement
	 */
    private String file;

    public FileParser() {
        super();
    }

    public FileParser(String path) {
        this.path = path;
    }

    /**
	 * Récupère les données des différents fichiers
	 * Envoit les évènements FileParserEvent
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws ParseException 
	 */
    protected abstract void getFromFiles() throws IOException, SQLException, ParseException;

    /**
	 * Donne le nombre de fichiers que le parser gère
	 * @return int Nombre de fichiers gérés
	 */
    public abstract int numberFiles();

    public abstract Integer doInBackground();

    /**
	 * Doit au moins contenir le code suivant pour prévenir les autres composants que l'import est terminé :</br>
	 * <code>firePropertyChange("done", boolean oldValue, boolean newValue);</code><br />
	 * <code>newValue</code> est vrai si le parsing s'est correctement déroulé.
	 */
    public abstract void done();

    /**
	 * @return the file
	 */
    public String getFile() {
        return file;
    }

    /**
	 * @param file the file to set
	 */
    public void setFile(String file) {
        if (file != this.file) {
            String oldFile = this.file;
            this.file = file;
            this.firePropertyChange("file", oldFile, file);
        }
    }

    public String getPath() {
        return this.path;
    }

    /**
	 * Name of the created database
	 * @return
	 */
    public abstract String getName();

    public abstract Type getType();
}
