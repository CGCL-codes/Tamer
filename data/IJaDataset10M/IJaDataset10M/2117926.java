package wesodi.logic.manage.accessPoints;

import javax.ejb.Local;
import wesodi.entities.transi.ServerRequest;
import wesodi.entities.transi.ServerResponse;
import wesodi.logic.manage.interfaces.AccessPointManagement;

/**
 * This interfaces is the object oriented ejb implementation of the specified
 * methods and interfaces of {@link AccessPointManagement}
 * 
 * @author Sarah Haselbauer
 * 
 */
@Local
public interface AccessPointManagementLocal {

    public ServerResponse createAccessPoint(ServerRequest request);

    public ServerResponse editAccessPoint(ServerRequest request);

    public ServerResponse deleteAccessPoint(ServerRequest request);
}
