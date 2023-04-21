package deduction;

import java.util.HashMap;
import java.util.Map;
import table.Table;

/**
 * Cache pour les faits.
 */
public class FaitCache {

    /**
	 * Liste des faits existant.
	 */
    private Map<Fait, Table> faits;

    /**
	 * Nombre total de faits.
	 */
    private int nbFaits;

    /**
	 * Constructeur par defaut.
	 */
    public FaitCache() {
        faits = new HashMap<Fait, Table>();
        nbFaits = 0;
    }

    /**
	 * Retourne la table correspondant au fait.
	 * @param fait fait a chercher.
	 * @return la table trouvee.
	 */
    public Table table(final Fait fait) {
        Table table = faits.get(fait);
        if (table != null) {
            return table;
        }
        String[] points = fait.getPoints();
        Table[] tables = new Table[points.length];
        for (int i = 0; i < points.length; ++i) {
            tables[i] = Point.get(points[i]);
        }
        table = Table.cree(tables);
        faits.put(fait, table);
        ++nbFaits;
        return table;
    }

    /**
	 * Enleve un fait du cache.
	 * @param fait fait a retirer.
	 */
    public void enlever(final Fait fait) {
        if (faits.remove(fait) != null) {
            --nbFaits;
        }
    }

    /**
	 * Vide le cache.
	 */
    public void vider() {
        faits.clear();
        nbFaits = 0;
    }
}
