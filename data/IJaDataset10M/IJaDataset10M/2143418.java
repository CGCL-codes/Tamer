package ch.ideenarchitekten.vip.statusbar;

/**
 * Strategy welche den Darstellungstext wackeln l�sst. Dies dient daf�r den Benutzer auf den gerade angezeigten
 * Status aufmerksam zu machen.
 *
 * @author $LastChangedBy: buehlmannstefan $
 * @version $LastChangedRevision: 359 $
 */
public class RingTooltipStrategy extends AbstractStatusShowStrategy {

    /**
	 * Seriale Versionsid
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Ursprungsposition des Anzeigetextes
	 */
    private int m_x = 0, m_y = 0;

    /**
	 * Zeitintervallkonstante zwischen den Bewegungen
	 */
    public static final int TIMEINTERVAL = 20;

    /**
	 * Anzahl der Drehungen des Darstellungstextes
	 */
    public static final int ROUNDS = 10;

    /**
	 * Abstand um welchen der Text bei der Drehung verschoben wird
	 */
    private static final int MOVEABS = 2;

    /**
	 * Thread welcher die Bewegungen ausf�hren wird
	 */
    private Thread m_t = null;

    /**
	 * Speichert die Ursprungskoordinaten des Darstellungstext ab
	 * @See AbstractStatusShowStrategy
	 * @param align Ausrichtung
	 * @param width Breite
	 */
    public RingTooltipStrategy(String align, int width) {
        super(align, width);
        m_x = getLabel().getX();
        m_y = getLabel().getY();
    }

    /**
	 * �ndert den Anzeigetext.
	 * @param literal Anzeigetext
	 */
    public void changeLiteral(String literal) {
        super.changeLiteral(literal);
        if (m_t != null && m_t.isAlive()) {
            m_t.interrupt();
        }
        m_t = new Ringer();
        m_t.start();
    }

    /**
	 * R�ttelthread verschiebt die Position des Darstellungstextes im Uhrzeigersinn jeweils um 90�
	 * @author $LastChangedBy: buehlmannstefan $
	 * @version $LastChangedRevision: 359 $
	 */
    class Ringer extends Thread {

        /**
		 * Thread Startmethode
		 */
        public void run() {
            for (int i = 0; i < ROUNDS; i++) {
                try {
                    getLabel().setLocation(m_x + MOVEABS, m_y);
                    Thread.sleep(TIMEINTERVAL);
                    getLabel().setLocation(m_x, m_y + MOVEABS);
                    Thread.sleep(TIMEINTERVAL);
                    getLabel().setLocation(m_x - MOVEABS, m_y);
                    Thread.sleep(TIMEINTERVAL);
                    getLabel().setLocation(m_x, m_y - MOVEABS);
                    Thread.sleep(TIMEINTERVAL);
                } catch (InterruptedException e) {
                }
            }
            getLabel().setLocation(m_x, m_y);
        }
    }
}
