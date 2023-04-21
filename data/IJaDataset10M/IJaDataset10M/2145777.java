package gumbo.net.msg;

import gumbo.net.NetConnector;

/**
 * A NetConnector for transfer of "messages" via a network data connection. A
 * message is an object that can be serialized, sent over a connection's output
 * stream, and de-serialized back into that object. Message-based transfer,
 * which transfers data as a whole (i.e. an object), is distinct from
 * stream-based transfer, which transfers data incrementally and possibly
 * continuously.
 * <p>
 * Message-level transfers typically require native support (such as that
 * provided by Java serialization) or some "out-of-band" specification of
 * message format or object type.
 * @param <T> Message object type.
 * @author Jon Barrilleaux (jonb@jmbaai.com) of JMB and Associates Inc.
 */
public interface MessageConnector<T> extends NetConnector {

    /**
	 * Gets the connector's volatile message reader, which uses the connection's
	 * input stream to read data from a remote network node. The reader will be
	 * nulled automatically when the input stream is closed or the connection is
	 * broken.
	 * @return Temp exposed message reader. None if null (i.e. no stream or
	 * connection).
	 */
    public MessageIOReader<T> getMessageReader();

    /**
	 * Gets the connector's volatile message writer, which uses the connection's
	 * output stream to write data to a remote network node. The writer will be
	 * nulled automatically when the output stream is closed or the connection
	 * is broken.
	 * @return Temp exposed message writer. None if null (i.e. no stream or
	 * connection).
	 */
    public MessageIOWriter<T> getMessageWriter();
}
