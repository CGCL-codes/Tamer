package fenomeni.lavoro;

import java.util.Calendar;
import core.Fenomeno;
import core.Ministero;

/**
 * @author Ale
 *
 */
public class Poverta extends Fenomeno {

    public Poverta(Ministero aff, Calendar gt, double baseval, double step) {
        super(aff, "Povertà", gt, baseval, step);
        super.setUnita("%");
        super.setName_long("Tasso di povertà della popolazione; percentuale di persone con un reddito inferiore alla soglia di povertà");
        super.setSfondoStoria("img/sffenomeni/poverta.jpg");
    }
}
