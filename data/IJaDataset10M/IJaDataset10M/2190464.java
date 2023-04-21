package org.pocui.test.factorys;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.pocui.core.resources.EmptyResourceConfiguration;
import org.pocui.example.actions.kundefinden.KundenFindenAction;
import org.pocui.example.actions.kundefinden.KundenFindenActionSelectionIn;
import org.pocui.example.composites.stammdaten.VisualisiereStammdatenAC;
import org.pocui.example.composites.stammdaten.VisualisiereStammdatenComposite;
import org.pocui.example.composites.stammdaten.VisualisiereStammdatenSelectionInOut;
import org.pocui.swing.components.test.ICompositeFactory;
import org.pocui.swing.composites.AbsComposite;

/**
 * Factory to create {@link VisualisiereStammdatenComposite}
 * 
 * @author Kai Benjamin Joneleit
 * 
 */
public class StammdatenAnzeigenCompositeFactory implements ICompositeFactory<VisualisiereStammdatenAC, EmptyResourceConfiguration, VisualisiereStammdatenSelectionInOut> {

    /**
	 * {@inheritDoc}
	 */
    public AbsComposite<VisualisiereStammdatenAC, EmptyResourceConfiguration, VisualisiereStammdatenSelectionInOut> createAbsJPanel() {
        try {
            VisualisiereStammdatenComposite comp = new VisualisiereStammdatenComposite(new VisualisiereStammdatenAC());
            KundenFindenAction action = new KundenFindenAction();
            KundenFindenActionSelectionIn inSelection = new KundenFindenActionSelectionIn();
            inSelection.setKundenname("Meier");
            action.setInSelection(inSelection);
            action.performAction(null);
            VisualisiereStammdatenSelectionInOut selection = new VisualisiereStammdatenSelectionInOut();
            selection.setKunden(action.getOutSelection().getKunden());
            selection.setKundenname("Meier");
            List<String> laender = new ArrayList<String>();
            laender.add("Hamburg");
            laender.add("Bremen");
            laender.add("Berlin");
            selection.setBundeslaender(laender);
            selection.setHausnummer("12 a");
            selection.setPostleitzahl(20099);
            selection.setSelektiertesBundesland("Hamburg");
            selection.setStrasse("Ulmenstrasse");
            selection.setWohnort("Hamburg");
            List<String> produkte = new ArrayList<String>();
            produkte.add("Natur");
            produkte.add("Mix");
            produkte.add("Extra");
            selection.setProdukte(produkte);
            selection.setSelektiertesProdukt("Mix");
            selection.setVertragsbeginn(System.currentTimeMillis());
            selection.setVertragsende(System.currentTimeMillis());
            selection.setZusatzInfos("Additional Infos");
            comp.setSelection(selection);
            return comp;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * {@inheritDoc}
	 */
    public EmptyResourceConfiguration getResourceConfiguration() {
        return EmptyResourceConfiguration.getInstance();
    }
}
