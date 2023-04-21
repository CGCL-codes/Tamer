package org.epoline.jsf.services.admin;

import java.io.Serializable;
import java.rmi.Remote;
import net.jini.admin.JoinAdmin;
import org.epoline.jsf.services.core.ServiceAttributeAdmin;
import org.epoline.jsf.services.core.ServiceRuntimeInterface;
import org.epoline.jsf.services.core.TimeableInterface;
import com.sun.jini.admin.DestroyAdmin;
import com.sun.jini.admin.StorageLocationAdmin;

/**
 * Base interface for Jini admin routines
 *
 * @author Patrick Balm
 */
public interface JiniServiceAdmin extends Remote, JoinAdmin, DestroyAdmin, StorageLocationAdmin, TimeableInterface, ServiceRuntimeInterface, ServiceAttributeAdmin, Serializable {
}
