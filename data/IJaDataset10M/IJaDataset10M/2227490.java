package org.fudaa.dodico.crue.comparaison.tester;

import java.util.ArrayList;
import java.util.List;

public class TesterContextDigg {

    public int getMaxDeep() {
        return maxDeep;
    }

    public int getActualDeep() {
        return actualDeep;
    }

    List objectATested = new ArrayList();

    /**
   * Le nombre de beanObjet à parcourir pour déterminer l'égalite de 2 objets. Une valeur de 1 indique que l'on ne suit
   * pas les objet références par l'objet en cours.
   */
    protected final int maxDeep;

    protected int actualDeep;

    boolean continueDigg = true;

    protected TesterContextDigg(int maxDeep) {
        this.maxDeep = maxDeep;
        this.actualDeep = maxDeep;
    }

    protected TesterContextDigg(TesterContextDigg parent) {
        this.maxDeep = parent.maxDeep;
        this.actualDeep = parent.actualDeep;
        objectATested.addAll(parent.objectATested);
    }

    private boolean isAlreadTested(Object newO) {
        for (Object o : objectATested) {
            if (o == newO) {
                return true;
            }
        }
        objectATested.add(newO);
        return false;
    }

    public final boolean isObjectAlreadyTested(final Object o) {
        if (o != null) {
            if (isAlreadTested(o)) {
                return true;
            }
            objectATested.add(o);
            return false;
        }
        return false;
    }

    /**
   * @return true si le comparateur peut continuer à creuser les objets
   */
    public final boolean canDigg() {
        return continueDigg && (maxDeep < 0 || actualDeep > 0);
    }

    /**
   * Indique au contexte que l'on a creuse la
   */
    public final void diggDone() {
        actualDeep--;
    }

    /**
   * Force l'arret du parcourt de l'arbre d'objet: a utiliser si cycle dans les objets
   */
    public void stopDigg() {
        continueDigg = false;
    }
}
