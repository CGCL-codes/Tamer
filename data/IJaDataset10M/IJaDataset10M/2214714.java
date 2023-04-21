package axis.dynamic;

import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.namespace.QName;
import org.apache.axis.AxisEngine;
import org.apache.axis.ConfigurationException;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.Handler;
import org.apache.axis.encoding.TypeMappingRegistry;
import org.apache.axis.encoding.TypeMappingRegistryImpl;

/**
 * abstract base class for dynamic Axis {@link EngineConfiguration}s.
 */
public abstract class SoapConfiguration implements EngineConfiguration {

    /** the transport handlers. */
    protected final Map<QName, Handler> _transports = new ConcurrentHashMap<QName, Handler>();

    /** this will register the Axis default type mappings */
    protected final TypeMappingRegistry _tmr = new TypeMappingRegistryImpl();

    /** the global request handler (may be a handler chain) */
    protected Handler _globalRequest = null;

    /** the global response handler (may be a handler chain) */
    protected Handler _globalResponse = null;

    /** reference to the engine that uses this configuration, set in {@link #configureEngine} */
    protected AxisEngine _engine;

    /** synchronization shouldn't be necessary, it's a Hashtable and only modified on startup */
    protected final Hashtable<String, String> _globalOptions = new Hashtable<String, String>();

    /**
   * called from {@link AxisEngine#init()}. Note: when this method is called, the given engine already
   * has a reference to this object.
   * @param engine
   * @throws ConfigurationException
   * @see EngineConfiguration#configureEngine(AxisEngine)
   * @see AxisEngine#init()
   */
    public void configureEngine(AxisEngine engine) throws ConfigurationException {
        _engine = engine;
        _engine.refreshGlobalOptions();
    }

    /**
   * We don't write ourselves out, so this is a no-op.
   * @param engine 
   */
    public void writeEngineConfig(AxisEngine engine) {
    }

    /**
   * Returns the global configuration options.
   * @return  the global options
   */
    public Hashtable<String, String> getGlobalOptions() {
        return _globalOptions;
    }

    /**
   * Returns a global request handler.
   * @return the global request handler (may be a handler chain)
   */
    public Handler getGlobalRequest() {
        return _globalRequest;
    }

    /**
   * Set the global request Handler
   * @param globalRequest (may be a handler chain)
   */
    public void setGlobalRequest(Handler globalRequest) {
        _globalRequest = globalRequest;
    }

    /**
   * Returns a global response handler.
   * @return the global response handler (may be a handler chain)
   */
    public Handler getGlobalResponse() {
        return _globalResponse;
    }

    /**
   * Set the global response Handler
   * @param globalResponse (may be a handler chain)
   */
    public void setGlobalResponse(Handler globalResponse) {
        _globalResponse = globalResponse;
    }

    /**
   * We do not use handlers with a 'global' name, so this method always returns null.
   * @param qname
   * @return global handler with given name
   * @see EngineConfiguration#getHandler(QName)
   */
    public Handler getHandler(QName qname) {
        return null;
    }

    /**
   * Get our TypeMappingRegistry.
   * @return the TypeMappingRegistry of this configuration.
   */
    public TypeMappingRegistry getTypeMappingRegistry() {
        return _tmr;
    }

    /**
   * @param qname
   * @return the transport for the given name, may be null
   * @see EngineConfiguration#getTransport(QName)
   */
    public Handler getTransport(QName qname) {
        return _transports.get(qname);
    }

    /**
   * sets the transport for the given name
   * @param name e.g. "http"
   * @param transport
   */
    public void deployTransport(String name, Handler transport) {
        _transports.put(new QName(name), transport);
    }

    /**
   * removes the transport for the given name
   * @param name
   */
    public void undeployTransport(String name) {
        _transports.remove(new QName(name));
    }

    /**
   * currently, we don't use roles, so this method always returns null.
   * @return empty list
   */
    public List<String> getRoles() {
        return Collections.emptyList();
    }
}
