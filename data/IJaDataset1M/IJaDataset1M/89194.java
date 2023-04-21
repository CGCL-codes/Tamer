package bonneville.tests.instantiation;

/**
 * Instantiated by TestInstantiation.
 */
public class GeographicEntity {

    protected String name;

    protected int population;

    protected float landArea;

    public GeographicEntity(String name, int population, float landArea) {
        this.name = name;
        this.population = population;
        this.landArea = landArea;
    }

    public String getName() {
        return name;
    }

    public int getPopulation() {
        return population;
    }

    public float getLandArea() {
        return landArea;
    }

    public String toString() {
        return name + ", " + population + ", " + Float.toString(landArea) + ", ";
    }
}
