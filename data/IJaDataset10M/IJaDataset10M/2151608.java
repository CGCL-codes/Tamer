package net.solarnetwork.node.io.comm;

import javax.comm.CommPortIdentifier;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import org.springframework.beans.factory.ObjectFactory;

/**
 * {@link GenericObjectFactory} for {@link SerialPortConversationalDataCollector}
 * objects.
 * 
 * <p>Configure the properties of this class, then calls to {@link #getObject()} will
 * return new instances of {@link SerialPortConversationalDataCollector} for each
 * invocation, configured with this object's property values.</p>
 * 
 * @author matt
 * @version $Revision: 882 $ $Date: 2010-01-26 22:54:13 -0500 (Tue, 26 Jan 2010) $
 * @param <T> the datum type
 */
public class SerialPortConversationalDataCollectorFactory<T> extends AbstractSerialPortSupportFactory implements ObjectFactory<SerialPortConversationalDataCollector<T>> {

    private String commPortAppName = getClass().getName();

    private CommPortIdentifier portId = null;

    public SerialPortConversationalDataCollector<T> getObject() {
        if (this.portId == null) {
            this.portId = getCommPortIdentifier();
        }
        try {
            SerialPort port = (SerialPort) portId.open(this.commPortAppName, 2000);
            SerialPortConversationalDataCollector<T> obj = new SerialPortConversationalDataCollector<T>(port, getMaxWait());
            setupSerialPortSupport(obj);
            return obj;
        } catch (PortInUseException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }
}
