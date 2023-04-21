package gdg.dataGeneration;

/**
 * 
 * @author Silvio Donnini
 */
public interface ColumnDataProvider {

    public Object nextValue();

    public void update();

    public int getType();

    public String nextValueToString();
}
