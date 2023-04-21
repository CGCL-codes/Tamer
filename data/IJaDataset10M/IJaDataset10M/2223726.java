package hokutonorogue.object;

/**
 *
 * @author ZolfriK
 */
public interface Stackable {

    public void increaseQuantity(int quantity);

    public void decreaseQuantity(int quantity);

    public int getQuantity();

    public boolean isConsumed();
}
