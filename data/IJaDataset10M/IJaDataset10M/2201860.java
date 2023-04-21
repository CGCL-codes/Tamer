package evaluationRulesGenetic;

import java.util.Iterator;
import java.util.LinkedList;
import beheer.DagMenu;
import beheer.PeriodeMenu;

/**
 * klasse die een periode menu controleerd en een integer met de beoordeling als resultaat teruggeeft. 
 * Klasse maakt onderdeel uit van een ketting die word samengesteld in BTMenuEvaluaterSetup
 * 
 * @author sjoerd.verschure
 * 
 */
public class OncePerPeriodEvaluator extends MenuEvaluater {

    public OncePerPeriodEvaluator() {
        super();
    }

    public void evaluateMenu(PeriodeMenu pMenu) {
        boolean goedgekeurd = true;
        Iterator it = pMenu.getDagMenus().iterator();
        LinkedList<Integer> iDlist = new LinkedList<Integer>();
        while (it.hasNext()) {
            DagMenu dMenu = (DagMenu) it.next();
            if (iDlist.contains(dMenu.getRecept().getReceptID())) {
                goedgekeurd = false;
            } else {
                iDlist.add(dMenu.getRecept().getReceptID());
                pMenu.setPeriodeMenuBeoordeling(pMenu.getPeriodeMenuBeoordeling() + 1);
            }
        }
        if (nextStep != null && goedgekeurd) {
            nextStep.evaluateMenu(pMenu);
        }
    }
}
