package at.rc.tacos.platform.net;

import java.util.List;
import java.util.Map;
import org.apache.mina.core.service.IoHandler;
import at.rc.tacos.platform.net.handler.MessageType;

/**
 * Describes the methods that must be implemented so that the {@link IoHandler}
 * implementation can process the request.
 * <p>
 * The definition is generic to provide type save access to the implementations
 * </p>
 * 
 * @author Michael
 */
public interface Message<M> {

    /**
	 * Returns the unique id of the message.
	 * <p>
	 * The id can be used to identify a message. The server will not change the
	 * id so that the client can recognize if a message received from the server
	 * is a response to a previouse request.
	 * </p>
	 * 
	 * @return the unique id
	 */
    public String getId();

    /**
	 * Returns the type of the message.
	 * <p>
	 * The {@link MessageType} is used to select the handler method that will
	 * execute the request on the server.
	 * </p>
	 * 
	 * @return the type of the message
	 */
    public MessageType getMessageType();

    /**
	 * The message parameters to attach to a message. The parameters are simple
	 * <code>name=value</code> pairs.
	 * 
	 * @return the parameters attached to the message
	 */
    public abstract Map<String, String> getParams();

    /**
	 * Returns the list of objects that should be processed.
	 * 
	 * @return the list of objects attached to the message
	 */
    public abstract List<M> getObjects();

    /**
	 * Returns the first element in the list of objects
	 */
    public abstract M getFirstElement();
}
