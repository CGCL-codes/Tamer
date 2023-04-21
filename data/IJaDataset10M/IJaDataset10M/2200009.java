package fr.pingtimeout.jtail.io;

import java.io.File;
import java.util.TimerTask;

/**
 * Watcher générique permettant de déclencher un traitement sur modification d'un fichier.
 *
 * @author Pierre Laporte
 *         Date: 11 mai 2010
 */
public abstract class AbstractFileWatcher extends TimerTask {

    /**
     * Le fichier monitoré.
     */
    protected final File file;

    /**
     * La date de dernière modification du fichier.
     */
    private long lastModifiedTime;

    /**
     * Constructeur de AbstractFileWatcher.
     *
     * @param file le fichier à monitorer
     */
    public AbstractFileWatcher(File file) {
        this.file = file;
        this.lastModifiedTime = 0;
    }

    @Override
    public void run() {
        long timeStamp = file.lastModified();
        if (this.lastModifiedTime != timeStamp) {
            this.lastModifiedTime = timeStamp;
            fileChanged();
        }
    }

    /**
     * Méthode exécutée lorsque le fichier monitoré est modifié.
     */
    protected abstract void fileChanged();
}
