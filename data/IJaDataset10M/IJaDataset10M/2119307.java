package lt.bsprendimai.ddesk;

import java.io.Serializable;

/**
 * Base class for all backing bean proxy classes<br/>
 *
 * Proxy class is used to set request parameters
 * on session scoped managed beans. No other logic
 * in subclasses is to be implemented.<br/>
 * ID setting is delegated to session scoped beans.<br/>
 *
 * <code>
 *       <managed-property>
 *           <property-name>id</property-name>
 *           <value>#{param.id}</value>
 *       </managed-property>
 * </code>
 * <br/>
 * dummyOutput is for forced intialization of bean without any functional impact.<br/>
 *
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
public abstract class ProxyBase implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -5020134535594118395L;

    public String getDummyOutput() {
        return "";
    }

    public void setDummyOutput(String dummyOutput) {
    }
}
