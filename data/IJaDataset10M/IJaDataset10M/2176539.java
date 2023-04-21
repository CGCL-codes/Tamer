package manager.signals;

import org.apache.log4j.Logger;
import manager.model.MetadataFile;
import com.trolltech.qt.core.QObject;

/**
 * Ein globales Signal, dass ausgel�st wird, falls eine Metadatendatei entfernt wurde.
 *
 * @author Soenke Brummerloh
 */
public class AfterMetadataFileRemovedSignal extends QObject {

    private static Logger logger = Logger.getLogger("");

    Signal1<MetadataFile> signal = new Signal1<MetadataFile>();

    private static AfterMetadataFileRemovedSignal instance = new AfterMetadataFileRemovedSignal();

    private AfterMetadataFileRemovedSignal() {
    }

    private static AfterMetadataFileRemovedSignal getInstance() {
        return instance;
    }

    /**
     * L�st das Signal aus.
     *
     * @param metadata die entfernte Metadatendatei
     */
    public static void emit(MetadataFile metadata) {
        logger.debug(metadata);
        getInstance().signal.emit(metadata);
    }

    /**
     * Liefert die Instanz dieses Signals.
     *
     * @return die Signalinstanz
     */
    public static Signal1<MetadataFile> getSignal() {
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
