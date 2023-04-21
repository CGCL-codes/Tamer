package edu.ipfw.nitrogo.session;

import static org.jboss.seam.ScopeType.SESSION;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.beanutils.BeanUtils;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import edu.ipfw.nitro.ws.consumer.ProjectDTO;
import edu.ipfw.nitro.ws.consumer.VehicleDTO;

@Name("vehicle")
@Scope(SESSION)
@AutoCreate
public class Vehicle extends VehicleDTO implements Serializable {

    @In
    ManageVehicle manageVehicle;

    /**
	 * 
	 */
    private static final long serialVersionUID = 4970909790753799382L;

    public Vehicle() {
    }
}
