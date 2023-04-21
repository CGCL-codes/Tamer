package ch.ideenarchitekten.vip.statusbar;

import junit.framework.TestCase;

/**
 * Pr�ft den Verteiler von Status�nderungsevents auf Fehler
 *
 * @author $LastChangedBy: buehlmannstefan $
 * @version $LastChangedRevision: 268 $
 */
public class ZZTestStatusChangeController extends TestCase {

    /**
	 * Ein Observer der bei �nderungen informiert wird
	 */
    private Observer m_observer = null;

    /**
	 * Instanz des �nderungskontroller
	 */
    private StatusChangeController m_controller = null;

    /**
	 * Der Observer wird �ber die Methode notify informiert und speichert den
	 * Event zwischen
	 *
	 * @author $LastChangedBy: buehlmannstefan $
	 * @version $LastChangedRevision: 268 $
	 */
    class Observer implements StatusChangeListener {

        /**
		 * Zwischenspeicherung f�r den Event
		 */
        private StatusChangeEvent m_event = null;

        /**
		 * (non-Javadoc)
		 * @see ch.ideenarchitekten.vip.statusbar.StatusChangeListener#notify(ch.ideenarchitekten.vip.statusbar.StatusChangeEvent)
		 */
        public void notify(StatusChangeEvent e) {
            m_event = e;
        }

        /**
		 * Zuletzt erhaltener Event abfragen
		 * @return
		 */
        public StatusChangeEvent getEvent() {
            return m_event;
        }
    }

    /**
	 * (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
    public void setUp() throws Exception {
        super.setUp();
        m_controller = StatusChangeController.getInstance();
        m_observer = new Observer();
        m_controller.addStatusChangeListener(m_observer);
    }

    /**
	 * (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
    public void tearDown() throws Exception {
        super.tearDown();
        m_controller = null;
        m_observer = null;
    }

    /**
	 * Erhalten die Events die richtigen Informationen und werden funktioniert die Verteilung?
	 */
    public void testDistribution() {
        m_controller.sendChanges(new StatusChangeEvent(StatusChangeEvent.ADDED, 1));
        assertEquals(StatusChangeEvent.ADDED, m_observer.getEvent().getStatusChangeTyp());
        assertEquals(1, m_observer.getEvent().getStatusChangedId());
        m_controller.sendChanges(new StatusChangeEvent(StatusChangeEvent.CHANGED, 1));
        assertEquals(StatusChangeEvent.CHANGED, m_observer.getEvent().getStatusChangeTyp());
        m_controller.sendChanges(new StatusChangeEvent(StatusChangeEvent.REMOVED, 1));
        assertEquals(StatusChangeEvent.REMOVED, m_observer.getEvent().getStatusChangeTyp());
    }

    /**
	 * Observer darf nach der Deregistrierung keine Events mehr erhalten
	 */
    public void testRemove() {
        m_controller.removeStatusChangeListener(m_observer);
        m_controller.sendChanges(new StatusChangeEvent(StatusChangeEvent.ADDED, 1));
        assertEquals(null, m_observer.getEvent());
    }
}
