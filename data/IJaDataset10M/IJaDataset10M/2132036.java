package evaluationRulesGenetic;

import java.util.Iterator;
import beheer.DagMenu;
import beheer.PeriodeMenu;

/**
 * klasse die een periode menu controleerd en een integer met de beoordeling als resultaat teruggeeft. 
 * Klasse maakt onderdeel uit van een ketting die word samengesteld in BTMenuEvaluaterSetup
 * 
 * @author sjoerd.verschure
 * 
 */
public class TooMuchFoodEvaluator extends MenuEvaluater {

    public TooMuchFoodEvaluator() {
        super();
    }

    public void evaluateMenu(PeriodeMenu pMenu) {
        Iterator it = pMenu.getDagMenus().iterator();
        while (it.hasNext()) {
            DagMenu dMenu = (DagMenu) it.next();
            if (dMenu.getAantalPersonen() == dMenu.getRecept().getAantalPersonen() || dMenu.getRecept().isTeBewaren() == 1) {
                pMenu.setPeriodeMenuBeoordeling(pMenu.getPeriodeMenuBeoordeling() + 1);
            }
        }
        if (nextStep != null) {
            nextStep.evaluateMenu(pMenu);
        }
    }
}
