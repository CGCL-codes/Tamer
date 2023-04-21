package trstudio.datasync.io.meta;

import java.io.File;
import java.io.Serializable;

/**
 * Information sur la configuration du dossier distant.
 *
 * @author Sebastien Villemain
 */
public class DirectoryConfiguration implements Serializable {

    /**
	 * Dossier de destination.
	 */
    private File directory = null;

    /**
	 * Mode du dossier.
	 */
    private DirectoryMode mode = null;

    /**
	 * Permet de savoir si nous inculons le fichier dans la synchronisation.
	 * Expression régulière à executer sur le nom du fichier.
	 */
    private String includeFilesFilter = null;

    /**
	 * Permet de savoir si nous exculons le fichier de la synchronisation.
	 * Expression régulière à executer sur le nom du fichier.
	 */
    private String excludeFilesFilter = null;

    /**
	 * Exclure les fichiers en lecture seule ?
	 */
    private boolean excludeReadyOnFiles = false;

    /**
	 * Exclure les fichiers cachés / système ?
	 */
    private boolean excludeHiddenFiles = false;

    /**
	 * Faire une sauvegarde des fichiers ?
	 */
    private boolean backupFiles = false;

    /**
	 * Information pour accèder à la destination.
	 */
    private DirectoryAccess access = null;

    /**
	 * Activer le service de syncrhonsation automatique.
	 */
    private boolean automaticSynchronization = false;

    public DirectoryConfiguration(String directoryPath) {
        if (directoryPath != null) {
            directory = new File(directoryPath);
        }
        mode = DirectoryMode.MASTER;
    }

    /**
	 * Retourne le dossier de destination.
	 * Si aucune destination définie, retourne <code>null</code>.
	 *
	 * @return File or <code>null</code>
	 */
    public File getDirectory() {
        return directory;
    }

    /**
	 * Change le dossier de destination.
	 *
	 * @param directory
	 */
    public void setDirectory(File directory) {
        this.directory = directory;
    }

    /**
	 * Retourne le mode du dossier.
	 *
	 * @return
	 */
    public DirectoryMode getMode() {
        return mode;
    }

    /**
	 * Change le mode du dossier.
	 *
	 * @param mode
	 */
    public void setMode(DirectoryMode mode) {
        this.mode = mode;
    }

    /**
	 * Retourne l'accès à la ressource.
	 *
	 * @return
	 */
    public DirectoryAccess getAcces() {
        return access;
    }

    /**
	 * Change l'accès à la ressource.
	 *
	 * @param access
	 */
    public void setAcces(DirectoryAccess access) {
        this.access = access;
    }

    /**
	 * Détermine si nous sauvegardons les fichiers.
	 *
	 * @return
	 */
    public boolean isBackupFiles() {
        return backupFiles;
    }

    /**
	 * Active ou désactive la sauvegarde des fichiers.
	 *
	 * @param backupFiles
	 */
    public void setBackupFiles(boolean backupFiles) {
        this.backupFiles = backupFiles;
    }

    /**
	 * Détermine si nous exculons les fichiers cachés / système.
	 *
	 * @return
	 */
    public boolean isExcludeHiddenFiles() {
        return excludeHiddenFiles;
    }

    /**
	 * Active ou désactive l'exculsions des fichiers cachés / système.
	 *
	 * @param excludeHiddenFiles
	 */
    public void setExcludeHiddenFiles(boolean excludeHiddenFiles) {
        this.excludeHiddenFiles = excludeHiddenFiles;
    }

    /**
	 * Détermine si nous exculons les fichiers en lecture seule.
	 *
	 * @return
	 */
    public boolean isExcludeReadyOnFiles() {
        return excludeReadyOnFiles;
    }

    /**
	 * Active ou désactive l'exculsions des fichiers en lecture seule.
	 *
	 * @param excludeReadyOnFiles
	 */
    public void setExcludeReadyOnFiles(boolean excludeReadyOnFiles) {
        this.excludeReadyOnFiles = excludeReadyOnFiles;
    }

    /**
	 * Retourne le filtre d'exclusion de fichiers.
	 * Si aucun filtre, retourne <code>null</code>.
	 *
	 * @return String or <code>null</code>.
	 */
    public String getExcludeFilesFilter() {
        return excludeFilesFilter;
    }

    /**
	 * Change le filtre d'exclusion de fichiers.
	 * Mettre à <code>null</code> pour désactiver.
	 *
	 * @param excludeFilesFilter
	 */
    public void setExcludeFilesFilter(String excludeFilesFilter) {
        this.excludeFilesFilter = excludeFilesFilter;
    }

    /**
	 * Retourne le filtre d'inclusion de fichiers.
	 * Si aucun filtre, retourne <code>null</code>.
	 *
	 * @return String or <code>null</code>.
	 */
    public String getIncludeFilesFilter() {
        return includeFilesFilter;
    }

    /**
	 * Change le filtre d'inclusion de fichiers.
	 * Mettre à <code>null</code> pour désactiver.
	 *
	 * @param includeFilesFilter
	 */
    public void setIncludeFilesFilter(String includeFilesFilter) {
        this.includeFilesFilter = includeFilesFilter;
    }

    /**
	 * Activer le service de synchronisation automatique.
	 *
	 * @return
	 */
    public boolean isAutomaticSynchronization() {
        return automaticSynchronization;
    }

    /**
	 * Change l'état du service de synchronsation automatique.
	 *
	 * @param automaticSynchronization
	 */
    public void setAutomaticSynchronization(boolean automaticSynchronization) {
        this.automaticSynchronization = automaticSynchronization;
    }
}
