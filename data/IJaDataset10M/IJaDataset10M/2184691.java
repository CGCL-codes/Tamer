package neon.entities.creatures;

public class Animal extends neon.entities.creatures.Creature {

    public Animal(int x, int y, String id, long uid, Species species) {
        super(id, uid, species);
    }

    public boolean hasDialog() {
        return false;
    }
}
