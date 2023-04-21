package diamante.extension.graph.property;

public interface InteractiveProperty {

    public abstract String toString();

    public abstract void fromString(String s) throws IllegalArgumentException;

    public abstract String getName();
}
