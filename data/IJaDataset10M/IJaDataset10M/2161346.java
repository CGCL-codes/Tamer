package projectplanner;

/**
 * @author Jacob Kjaer
 *
 */
public abstract class Deprecateable {

    private boolean deprecated;

    public void deprecate() {
        this.deprecated = true;
    }

    public void undeprecate() {
        this.deprecated = false;
    }

    public boolean isDeprecated() {
        return deprecated;
    }
}
