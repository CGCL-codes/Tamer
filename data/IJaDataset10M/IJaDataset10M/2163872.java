package manager.signals;

import manager.model.Model;
import manager.util.FileSystem;
import manager.util.MetadataController;
import org.apache.log4j.Logger;
import com.trolltech.qt.core.QObject;

/**
 * Ein globales Signal das ausgel�st wird, falls sich eine Datei �ndert.
 */
public class AfterFileChangedSignal extends QObject {

    private static Logger logger = Logger.getLogger("");

    Signal1<String> signal = new Signal1<String>();

    private static AfterFileChangedSignal instance = new AfterFileChangedSignal();

    private AfterFileChangedSignal() {
    }

    public static AfterFileChangedSignal getInstance() {
        return instance;
    }

    /**
     * L�st je nach dem, was sich bez�glich dem Pfad ge�ndert hat ein anderes Signal aus.
     *
     * @param path der Pfad wegen dem ein Signal ausgel�st werden soll
     */
    public static void emit(String path) {
        logger.debug(path);
        boolean exists = FileSystem.isExisting(path);
        boolean knownAsDataFile = (Model.getDataFiles().get(path) != null);
        boolean knownAsMetadataFile = (Model.getMetadataFiles().get(path) != null);
        boolean known = knownAsDataFile || knownAsMetadataFile;
        boolean isMetadataFile = MetadataController.checkForMetadataFile(path);
        getInstance().signal.emit(path);
        if (exists && !known) {
            Model.storeKnownFile(path, isMetadataFile);
            FoundNewFileSignal.emit(path);
            Model.getFileSystemWatcher().addPath(path);
        }
        if (knownAsMetadataFile) {
            AfterMetadataFileEditedSignal.emit(Model.getMetadataFiles().get(path));
        }
        if (!exists && known) {
            AfterFileRemovedSignal.emit(path);
            Model.removeKnownFile(path);
        }
    }

    /**
     * Liefert die Instanz dieses Signals.
     *
     * @return die Signalinstanz
     */
    public static Signal1<String> getSignal() {
        return getInstance().signal;
    }

    /**
     * Verbindet das Signal mit einer Methode.
     *
     * @param object das Objekt zu der die Methode geh�rt
     * @param method der Name der Methode als String
     */
    public static void connect(Object object, String method) {
        getInstance().signal.connect(object, method);
    }
}
