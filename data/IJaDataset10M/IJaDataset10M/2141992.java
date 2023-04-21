package de.ibk.ods.servantmanager;

import org.omg.CORBA.LocalObject;
import org.omg.PortableServer.ForwardRequest;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.Servant;
import org.omg.PortableServer.ServantActivator;
import de.ibk.ods.implementation.BaseElementImpl;

/**
 * @author Reinhard Kessler, Ingenieurbüro Kessler
 * @version 5.0.0
 */
public class BaseElementActivator extends LocalObject implements ServantActivator {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public BaseElementActivator() {
        super();
    }

    public Servant incarnate(byte[] oid, POA adapter) throws ForwardRequest {
        return new BaseElementImpl(oid, adapter);
    }

    public void etherealize(byte[] oid, POA adapter, Servant serv, boolean cleanup_in_progress, boolean remaining_activations) {
    }
}
