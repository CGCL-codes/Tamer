package ch.jester.commonservices.api.persistency;

/**
 * Interface welches zur Steuerung von Events für die Queue benutzt wird.
 *
 */
public interface IQueueNotifier {

    /**
	 * Die IPersistencyEventQueue wird nicht mehr automatisch benachrichtigt.
	 * Die gespeicherten Objekte werden zwischengespeichert und bei einem close() oder notifyEventQueue() der EventQueue übergeben.
	 * @param pTrue
	 */
    public void manualEventQueueNotification(boolean pTrue);

    /**
	 * Manuelles notifizieren der EventQueue. <br>
	 * Hat keinen Effekt wenn der interne Cache leer ist, oder 
	 * manualEventQueueNotification(false) gesetzt wurde.
	 */
    public void notifyEventQueue();

    /**
	 * Löscht den Event Queue Cache
	 */
    public void clearEventQueueCache();
}
