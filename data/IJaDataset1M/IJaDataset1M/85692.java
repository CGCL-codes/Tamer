package vivarium;

import java.util.Random;

/**
 * La classe ouvrière est un l'un des rôles disponibles pour les insectes.
 * L'ouvrière se charge de récolter la nourriture pour la colonie et rapporte
 * les aliments collectés dans les entrepôts.
 * 
 * @see vivarium.Role
 * @see vivarium.Insecte
 * @author Anthony, Mathieu, Jocelyn
 * @since JDK1.5
 */
public class Ouvriere extends Role {

    StockNourriture stock;

    /**
     * Constructeur par défaut
     */
    public Ouvriere() {
        nomRole = "ouvrière";
        stock = new StockNourriture(this);
    }

    ;

    /**
     * Permet à l'ouvrière de récolter la nourriture et la mettre de coté
     * @param miam la nourriture à récolter 
     */
    public void recolter(Nourriture miam) {
        if (miam.getValeurNutritive() < (stock.getValNutMax() - stock.getValeurNutritive())) {
            stock.charger(miam.getValeurNutritive());
            miam.getPosition().viderCase();
        } else {
            miam.setValeurNutritive(miam.getValeurNutritive() - (stock.getValNutMax() - stock.getValeurNutritive()));
            stock.remplir();
        }
    }

    @Override
    public String getNomRole() {
        return nomRole;
    }

    @Override
    public Case agir() {
        if (titulaire.estVivant() && titulaire.estMature()) {
            if (stock.getValeurNutritive() == stock.getValNutMax()) {
                return titulaire.getColo().getCentre();
            } else {
                for (int i = -1; i <= 1; i++) {
                    int Xvu = titulaire.getPosition().getX() + i;
                    if (Xvu < 0 || Xvu >= titulaire.getTerrain().getLargeur()) {
                        Xvu = titulaire.getPosition().getX();
                    }
                    for (int j = -1; j <= 1; j++) {
                        int Yvu = titulaire.getPosition().getY() + j;
                        if (Yvu < 0 || Yvu >= titulaire.getTerrain().getLargeur()) {
                            Yvu = titulaire.getPosition().getY();
                        }
                        Case c = titulaire.getTerrain().getCase(Xvu, Yvu);
                        titulaire.testManger(c);
                        if (!c.estVide() && !c.estMur()) {
                            if (titulaire.estComestible(c.getOccupant())) {
                                recolter(c.getOccupant());
                            }
                        }
                    }
                }
            }
        }
        if (stock.getValeurNutritive() == stock.getValNutMax()) {
            return titulaire.getColo().getCentre();
        } else {
            if (titulaire.getPosition().getPheromone() != null) {
                if (titulaire.getPosition().getPheromone().getTypePhero().equals("nourriture")) {
                    return titulaire.getPosition().getPheromone().getCentrePhero();
                } else return null;
            } else {
                return null;
            }
        }
    }

    public StockNourriture getStock() {
        return stock;
    }

    public char getInitialRole() {
        return 'o';
    }
}
