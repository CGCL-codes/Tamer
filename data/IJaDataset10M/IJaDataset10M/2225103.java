package fenomeni.internigiustizia;

import java.util.Calendar;
import core.Fenomeno;
import core.Ministero;

/**
 * @author Ale
 *
 */
public class CrimineViolento extends Fenomeno {

    /**
	 * @param gt
	 * @param baseval
	 * @param step
	 */
    public CrimineViolento(Ministero aff, Calendar gt, double baseval, double step) {
        super(aff, "Crimini violenti", gt, baseval, step);
        super.setUnita("Denunce di crimini violenti al mese");
        super.setName_long("Tasso di crimini efferati espresso in numero di denunce mensili (stupri, omicidi, sequestri, e altri reati penali)");
        super.setSfondoStoria("img/sffenomeni/crimineviol.jpg");
    }
}
