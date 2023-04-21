package manager.signals;

import org.apache.log4j.Logger;
import com.trolltech.qt.core.QObject;

/**
 * Ein globales Signal, dass ausgel�st wird, falls ein Verzeichnis entfernt wurde.
 *
 * @author Soenke Brummerloh
 */
public class AfterDirectoryRemovedSignal extends QObject {

    private static Logger logger = Logger.getLogger("");

    Signal1<String> signal = new Signal1<String>();

    private static AfterDirectoryRemovedSignal instance = new AfterDirectoryRemovedSignal();

    private AfterDirectoryRemovedSignal() {
    }

    public static AfterDirectoryRemovedSignal getInstance() {
        return instance;
    }

    /**
     * L�st das Signal aus.
     *
     * @param path der Pfad des entfernten Verzeichnisses
     */
    public static void emit(String path) {
        logger.debug(path);
        getInstance().signal.emit(path);
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
