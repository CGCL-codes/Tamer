package riaf.facade;

/**
 * The ICompositeStyle interface encapsulates a collection of IStyle objects.
 * Each property is obtained from the collection by the natural order of elements in it.
 */
public interface ICompositeStyle extends IStyle {

    public void addStyle(IStyle style);

    public void removeStyle(IStyle style);

    public void removeAll();
}
