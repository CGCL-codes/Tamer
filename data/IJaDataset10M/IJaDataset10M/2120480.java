package tgdh;

import java.io.Serializable;

/**
 * 
 * The MessageListener interface
 * 
 * @author Gilbert, Paresh, Sanket
 */
public interface MessageListener<T extends Serializable> {

    public boolean messageReceived(Object object);
}
