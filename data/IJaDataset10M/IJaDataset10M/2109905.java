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
public class LongEnoughAgoEvaluator extends MenuEvaluater {

    public LongEnoughAgoEvaluator() {
        super();
    }

    public void evaluateMenu(PeriodeMenu pMenu) {
        Iterator it = pMenu.getDagMenus().iterator();
        while (it.hasNext()) {
            DagMenu dMenu = (DagMenu) it.next();
            long laatstGebruikt;
            if (dMenu.getRecept().getReceptGebruikt().getLast().getTime() > dMenu.getRecept().getReceptGebruikt().getFirst().getTime()) {
                laatstGebruikt = dMenu.getRecept().getReceptGebruikt().getLast().getTime();
            } else {
                laatstGebruikt = dMenu.getRecept().getReceptGebruikt().getFirst().getTime();
            }
            if (dMenu.getDatum().getTime() > dMenu.getRecept().getReceptGebruikt().getLast().getTime()) {
                long y = dMenu.getDatum().getTime() - laatstGebruikt;
                pMenu.setPeriodeMenuBeoordeling(pMenu.getPeriodeMenuBeoordeling() + (int) (y / (7 * 24 * 60 * 60 * 1000L)));
            }
        }
        if (nextStep != null) {
            nextStep.evaluateMenu(pMenu);
        }
    }
}
