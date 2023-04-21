package ch.ideenarchitekten.vip.statusbar;

/**
 * Strategy zum anzeigen normaler Textmeldungen ohne spezielle Darstellung.
 *
 * @author $LastChangedBy: buehlmannstefan $
 * @version $LastChangedRevision: 232 $
 */
public class TooltipShowStrategy extends AbstractStatusShowStrategy {

    /**
	 * Konstruktor
	 * @See AbstractStatusShowStrategy
	 * @param align Ausrichtung
	 * @param width Breite
	 */
    public TooltipShowStrategy(String align, int width) {
        super(align, width);
    }

    /**
	 * Seriale Versionsid
	 */
    private static final long serialVersionUID = 1L;
}
