package poweria.unite;

import poweria.partie.Terrain.Type;
import poweria.partie.cases.Case;
import poweria.utilitaire.Utilitaire;

/**
 *
 * @author Cody Stoutenburg
 */
public class Croiseur extends Unite {

    public Croiseur() {
        super();
        initValue();
    }

    public Croiseur(Case pos) {
        super(pos);
        initValue();
    }

    public void initValue() {
        this._deplacement = 1;
        this._puissance = 10;
        this._nom = "croiseur";
        this._terrain.addType(Type.eau);
        this._img = Utilitaire.path + this._nom + "_" + "_EQUIPE_" + ".png";
        this._fromWhatToCreate = new Destroyer();
        this._quantiteToCreate = 3;
    }
}
