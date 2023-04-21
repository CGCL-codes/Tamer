package poweria.ordre;

import java.util.ArrayList;
import java.util.Collection;
import poweria.joueur.Equipe;

/**
 *
 * @author Cody Stoutenburg
 */
public class ListOrdre {

    public static final int NB_ACTION = 5;

    private ArrayList<Ordre> _allOrdre;

    private Equipe _equipe;

    public ListOrdre(Equipe eq) {
        _allOrdre = new ArrayList<Ordre>();
        this._equipe = eq;
    }

    public void addOrdre(Ordre e) {
        if (canAddOrdre(e)) {
            this._allOrdre.add(e);
        }
    }

    public boolean canAddOrdre(Ordre nouvelleOrdre) {
        boolean result = false;
        if (this._allOrdre.size() < NB_ACTION) {
            result = nouvelleOrdre.verifieOrdre();
        }
        return result;
    }

    public void executeAllOrdre() {
        for (Ordre ordre : this._allOrdre) {
            ordre.doAction();
        }
    }

    public void undoAllOrdre() {
        for (Ordre ordre : this._allOrdre) {
            ordre.undoAction();
        }
    }

    public Collection<Ordre> getListOrdre() {
        return this._allOrdre;
    }

    public void removeOrdre(Ordre e) {
        this._allOrdre.remove(e);
        e.undoAction();
    }

    public void removeAllOrdre() {
        this._allOrdre.clear();
    }

    public Equipe getEquipe() {
        return _equipe;
    }
}
