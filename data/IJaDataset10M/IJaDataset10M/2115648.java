package mx4j.tools.adaptor.rmi.iiop;

import java.lang.reflect.Constructor;
import mx4j.tools.adaptor.rmi.RMIAdaptorMBeanDescription;

/**
 * Management interface descriptions for the IIOPAdaptor MBean.
 * @author <a href="mailto:biorn_steedom@users.sourceforge.net">Simone Bordet</a>
 * @version $Revision: 1633 $
 */
public class IIOPAdaptorMBeanDescription extends RMIAdaptorMBeanDescription {

    public String getMBeanDescription() {
        return "Adaptor for the RMI/IIOP protocol";
    }

    public String getConstructorDescription(Constructor ctor) {
        if (ctor.toString().equals("public mx4j.tools.adaptor.rmi.iiop.IIOPAdaptor()")) {
            return "Creates a new IIOP adaptor";
        }
        return super.getConstructorDescription(ctor);
    }
}
