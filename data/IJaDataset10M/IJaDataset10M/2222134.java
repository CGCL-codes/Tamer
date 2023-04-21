package trstudio.trdatasync.state;

import java.io.Serializable;
import java.util.Objects;
import trstudio.classlibrary.util.FileHelper;
import trstudio.trdatasync.io.FileReplicator;

/**
 * Contenu d'un message du journal.
 * Représente un message lié à un fichier ou à un dossier.
 *
 * @author Sebastien Villemain
 */
public class LoggingMessage implements Serializable {

    /**
	 * Racine du chemin source.
	 */
    private String source = null;

    /**
	 * Racine du chemin de destination.
	 */
    private String destination = null;

    /**
	 * Chemin absolu de la source.
	 */
    private String absolutePath = null;

    /**
	 * Chemin relatif.
	 */
    private String relativePath = null;

    /**
	 * Etat après synchronisation.
	 */
    private LoggingState state = null;

    /**
	 * Date de modification.
	 */
    private long timestamp = 0;

    public LoggingMessage(FileReplicator replicator) {
        absolutePath = replicator.getOriginalFile().getAbsolutePath();
        timestamp = replicator.getOriginalFile().lastModified();
        state = LoggingState.UNKNOWN;
        relativePath = replicator.getRelativeFilePath();
        source = getFormatedPath(absolutePath.replace(relativePath, ""));
        destination = getFormatedPath(replicator.getTargetFile().getAbsolutePath().replace(relativePath, ""));
    }

    public String getDestination() {
        return destination;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public String getSource() {
        return source;
    }

    public LoggingState getState() {
        return state;
    }

    public void setState(LoggingState state) {
        this.state = state;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    private static String getFormatedPath(String filePath) {
        return "(" + FileHelper.getRootName(filePath) + ") " + FileHelper.getLastDirectoryName(filePath);
    }

    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.source);
        hash = 97 * hash + Objects.hashCode(this.destination);
        hash = 97 * hash + Objects.hashCode(this.absolutePath);
        hash = 97 * hash + Objects.hashCode(this.relativePath);
        hash = 97 * hash + (this.state != null ? this.state.hashCode() : 0);
        hash = 97 * hash + (int) (this.timestamp ^ (this.timestamp >>> 32));
        return hash;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof LoggingMessage) {
            LoggingMessage message = (LoggingMessage) obj;
            if (message.relativePath.equals(relativePath)) {
                return true;
            }
        }
        return false;
    }
}
