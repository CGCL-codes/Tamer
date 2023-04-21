package imp.cykparser;

/**AbstractProduction
 * An abstract class for production rules
 * @author Xanda
 */
public abstract class AbstractProduction {

    public abstract String getHead();

    public abstract String getBody();

    public abstract long getCost();

    public abstract String getType();

    public abstract String getMode();
}
