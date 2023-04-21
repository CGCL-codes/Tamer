package model;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * The model of a redfox.
 * Endangered species, can't be shot.
 * They eat, move, breed, age and die.
 * 
 * @author Team X
 * @version 1
 */
public class Redfox extends Animal {

    private static final int DEF_BREEDING_AGE = 12;

    private static final int DEF_MAX_AGE = 60;

    private static final double DEF_BREEDING_PROBABILITY = 0.03;

    private static final int DEF_MAX_LITTER_SIZE = 5;

    public static int breeding_age = DEF_BREEDING_AGE;

    public static int max_age = DEF_MAX_AGE;

    public static double breeding_probability = DEF_BREEDING_PROBABILITY;

    public static int max_litter_size = DEF_MAX_LITTER_SIZE;

    private static final int RABBIT_FOOD_VALUE = 6;

    private Random generator;

    /**
     * Create a redfox. A redfox can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the redfox will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Redfox(boolean randomAge, Field field, Location location) {
        super(randomAge, field, location);
        food = RABBIT_FOOD_VALUE;
        generator = new Random();
    }

    /**
     * The acting of a redfox. It is hunting rabbits and while
     * this happens it might breed or die.
     * @param field
     * @param newRedfoxes
     */
    public void act(List<Animal> newRedfoxes) {
        incrementAge();
        incrementHunger();
        becomeSick();
        if (isAlive()) {
            giveBirth(newRedfoxes);
            Location location = getLocation();
            Location newLocation = findFood(location);
            if (newLocation == null) {
                newLocation = getField().freeAdjacentLocation(location);
            }
            if (newLocation != null) {
                setLocation(newLocation);
            } else {
                setDead();
            }
        }
    }

    /**
     * Tell the redfox to look for rabbits adjacent to its current location.
     * Only the first live rabbit is eaten.
     * @param location Where in the field it is located.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood(Location location) {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while (it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if (animal instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) animal;
                if (rabbit.isAlive()) {
                    rabbit.setDead();
                    food = RABBIT_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }

    public int getBreedingAge() {
        return breeding_age;
    }

    public int getMaxAge() {
        return max_age;
    }

    public int getMaxLitterSize() {
        return max_litter_size;
    }

    public double getBreedingprobability() {
        return breeding_probability;
    }

    public void setBreedingAge(int age) {
        breeding_age = age;
    }

    public void setMaxAge(int age) {
        max_age = age;
    }

    public void setBreedingprobability(double probability) {
        breeding_probability = probability;
    }

    public void setMaxLitterSize(int age) {
        max_litter_size = age;
    }

    /**
     * Set back the basic values
     * @see model.Animal#reset()
     */
    public static void defReset() {
        breeding_age = DEF_BREEDING_AGE;
        max_age = DEF_MAX_AGE;
        breeding_probability = DEF_BREEDING_PROBABILITY;
        max_litter_size = DEF_MAX_LITTER_SIZE;
    }

    /**
	 * Reset for when the simulator resets
	 */
    public void reset() {
        breeding_age = DEF_BREEDING_AGE;
        max_age = DEF_MAX_AGE;
        breeding_probability = DEF_BREEDING_PROBABILITY;
        max_litter_size = DEF_MAX_LITTER_SIZE;
    }

    /**
     * Check whether or not this redfox  to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newFoxes A list to add newly born redfoxes.
     */
    protected void giveBirth(List<Animal> newRedfoxes) {
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for (int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            switch(generator.nextInt(2)) {
                case 0:
                    Fox youngf = new Fox(false, field, loc);
                    if (this.sick) youngf.becomeSick();
                    newRedfoxes.add(youngf);
                    break;
                case 1:
                    Redfox youngrf = new Redfox(false, field, loc);
                    if (this.sick) youngrf.becomeSick();
                    newRedfoxes.add(youngrf);
                    break;
            }
        }
    }

    /**
     * Method to return the character info of the redfoxes actor
     * @return a arraylist with the character info
     */
    public static int[] getCharacterInfo() {
        int[] returnArray = new int[10];
        returnArray[0] = breeding_age;
        returnArray[1] = max_age;
        returnArray[2] = max_litter_size;
        return returnArray;
    }

    /**
	 * get breedingprobability
	 */
    public static double getBreedingProbability() {
        return breeding_probability;
    }

    /**
     * Change redfoxes Characteristics
     */
    public static void changeCharacteristics(String itemToChange, double value) {
        if (itemToChange.equals("breeding_age")) breeding_age = (int) value;
        if (itemToChange.equals("max_age")) max_age = (int) value;
        if (itemToChange.equals("breeding_probability")) breeding_probability = value;
        if (itemToChange.equals("max_litter_size")) max_litter_size = (int) value;
    }
}
