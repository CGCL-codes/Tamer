package net.sf.projectgenesis;

import jade.util.leap.Serializable;

/**
 * Represents a terrain block
 */
public class Terrain implements Serializable {

    public static final int ANIMAL = 1;

    public static final int FOOD = 2;

    public static final int FREE = 0;

    public static final int OBJECT = 3;

    public static final long serialVersionUID = 123344155;

    private Animal animal;

    private Food[] food;

    private Obstacle obstacle;

    /**
	 * Constructs the terrain block
	 */
    public Terrain() {
        this.obstacle = null;
        this.animal = null;
        this.food = null;
    }

    /**
	 * Returns the terrain's content
	 * 
	 * @return Number identifying the terrain block's content
	 */
    public int content() {
        int caracter = FREE;
        if (this.animal != null) {
            switch(this.animal.getAngle()) {
                case 0:
                    caracter = ANIMAL;
                    break;
                case 90:
                    caracter = ANIMAL;
                    break;
                case 180:
                    caracter = ANIMAL;
                    break;
                case 270:
                    caracter = ANIMAL;
                    break;
            }
        } else if (this.obstacle != null) {
            if (this.obstacle.getClass().getSimpleName().compareTo("Alimento") == 0) caracter = FOOD; else caracter = OBJECT;
        } else if (this.food != null) caracter = FOOD; else caracter = FREE;
        return caracter;
    }

    /**
	 * Returns the animal that's in the terrain block
	 * 
	 * @return The animal that's in the terrain block
	 */
    public Animal getAnimal() {
        return this.animal;
    }

    /**
	 * Returns the obstacle that's in the terrain block
	 * 
	 * @return The obstacle that's in the terrain block
	 */
    public Obstacle getObstacle() {
        return this.obstacle;
    }

    /**
	 * Indicates if the terrain is occupied
	 * 
	 * @return Boolean indicating if the terrain is occupied
	 */
    public boolean occupied() {
        if (this.animal != null) return true; else if (this.obstacle != null) return true; else if (this.food != null) return true; else return false;
    }

    /**
	 * Sets an animal in the terrain block
	 * 
	 * @param p_animal
	 *            The animal to set in the terrain block
	 */
    public void setAnimal(Animal p_animal) {
        this.animal = p_animal;
    }

    /**
	 * Sets an obstacle in the terrain block
	 * 
	 * @param p_obstacle
	 *            The obstacle to set in the terrain bloc
	 */
    public void setObstacle(Obstacle p_obstacle) {
        this.obstacle = p_obstacle;
    }
}
