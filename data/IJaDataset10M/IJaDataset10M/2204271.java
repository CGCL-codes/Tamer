package thegamezoo;

/**
 * The Class Goldfish.
 */
public class Goldfish extends Animal implements ISwimmer, IOviparous, IAnimal {

    /**
	 * Instantiates a new goldfish.
	 */
    public Goldfish() {
        super("Goldfish");
    }

    public Goldfish LayEgg() {
        if (!isSexMale()) {
            return new Goldfish();
        }
        return null;
    }

    public void Swim() {
        System.out.println("Je nage...");
    }
}
