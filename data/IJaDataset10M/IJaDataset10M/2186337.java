package jaxlib.ee.socketserver.spi;

/**
 * UDP server socket resource adapter administration interface.
 * <p>
 * This interface is defined in the resource adapter's deployment descriptor. The common way to instantiate
 * it is to use application server product specific administration features, e.g. a web console or 
 * commandline.
 * </p><p>
 * E.g. on Glassfish application server 1.0 open the web administration console and go to 
 * {@code Resources -> Connectors -> Admin Object Resources}.
 * </p>
 *
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: DatagramServerChannelAdmin.java 2364 2007-06-11 11:27:13Z joerg_wassmer $
 */
public interface DatagramServerChannelAdmin extends SocketServerPortAdmin, DatagramServerChannelActivationSpec {
}
