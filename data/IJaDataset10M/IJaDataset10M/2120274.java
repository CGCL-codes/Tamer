package cross.event;

/**
 * Listener for IEvents of type V
 * 
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 * @param <V>
 * 
 */
public interface IListener<V extends IEvent<?>> {

    public void listen(V v);
}
