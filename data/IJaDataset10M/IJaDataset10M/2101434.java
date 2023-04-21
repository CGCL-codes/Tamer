package thegamezoo;

/**
 * The Class Wolf.
 */
public class Wolf extends Animal implements IWanderer, IMammal, IAnimal, java.lang.Comparable<Wolf> {

    int strength;

    String shouting;

    char rank;

    public char getRank() {
        return rank;
    }

    public void setRank(char rank) {
        this.rank = rank;
    }

    public String getShouting() {
        return shouting;
    }

    public void setShouting(String shouting) {
        this.shouting = shouting;
    }

    public int getStrength() {
        return strength;
    }

    /**
	 * Instantiates a new wolf.
	 */
    public Wolf() {
        super("Wolf");
        java.util.Random r = new java.util.Random();
        strength = r.nextInt(100);
    }

    public Wolf CastDown() {
        if (!isSexMale()) {
            return new Wolf();
        }
        return null;
    }

    public void Wander() {
        System.out.println("Je marche...");
    }

    public int compareTo(Wolf wolf) {
        return this.strength - wolf.strength;
    }
}
