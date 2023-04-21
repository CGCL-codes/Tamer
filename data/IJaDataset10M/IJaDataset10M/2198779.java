package app;

import java.util.Vector;
import visitable.Chicken;
import visitable.ConnectionManager;
import visitable.Creature;
import visitable.Human;
import visitable.Pig;

/**
 * Représentation d'un monde cruel rempli de créatures et de visiteurs
 */
public class World {

    private Vector<Creature> creatures = new Vector<Creature>();

    private int gridSize;

    /**
	 * Constructeur
	 * @param gridSize la taille de la grille
	 */
    public World(int gridSize) {
        this.gridSize = gridSize;
        final int nbCreatures = gridSize * gridSize;
        for (int i = 0; i < nbCreatures; i++) {
            double r = Math.random();
            if (r < 0.3) creatures.add(new Human()); else if (r - 0.3 < 0.3) creatures.add(new Pig()); else creatures.add(new Chicken());
        }
        final ConnectionManager connManager = ConnectionManager.getInstance();
        for (int i = 0; i < nbCreatures; i++) {
            final int D = i + 1;
            final int E = i + gridSize;
            if (E < nbCreatures) connManager.addConnection(creatures.elementAt(i), creatures.elementAt(E));
            if (D / gridSize == i / gridSize) connManager.addConnection(creatures.elementAt(i), creatures.elementAt(D));
        }
    }

    /**
	 * @return La liste des créatures de ce monde
	 */
    public Vector<Creature> getCreatures() {
        return creatures;
    }

    /**
	 * @return Le nombre d'éléments sur une ligne de la grille du monde
	 */
    public int getGridSize() {
        return gridSize;
    }

    /**
	 * @return Le nombre de créatures au total
	 */
    public int getNbCreature() {
        return gridSize * gridSize;
    }
}
