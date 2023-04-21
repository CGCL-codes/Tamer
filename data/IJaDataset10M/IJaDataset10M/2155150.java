package modelz.ui;

import java.awt.Component;
import modelz.Updateable;

public interface PeerFactory {

    <C extends Peer<T, ? extends Component>, T extends Updateable<T>> C createPeer(Class<C> cls, T item) throws Exception;
}
