package other;

/**
 * Bogus class that implements a fruit, used in the EqualsInstanceOfTest.
 */
public class Fruit {

    /**
     * An orange.
     */
    public static final Fruit ORANGE = new Fruit("orange");

    /**
     * An apple.
     */
    public static final Fruit APPLE = new Fruit("apple");

    private String name;

    /**
     * Constructs a fruit with the given name.
     *
     * @param name the name of the fruit
     */
    public Fruit(String name) {
        this.name = name;
    }

    /**
     * Returns the name of this Fruit.
     *
     * @return the name of this Fruit
     */
    public String toString() {
        return name;
    }

    /**
     * Returns true if the given object is equals this Fruit.
     * That is, if they are the same object, or if the name
     * of the given fruit is the same as the name of this fruit.
     *
     * @param object the Fruit object to test
     *
     * @return true if the given object is equal to this Fruit
     */
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object != null) {
            return name.equals(object.toString());
        } else {
            return false;
        }
    }
}
