package remote;

import entity.Principal;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Administrator
 */
@Remote
public interface PrincipalFacadeRemote {

    void create(Principal principal);

    void edit(Principal principal);

    void remove(Principal principal);

    Principal find(Object id);

    List<Principal> findAll();

    List<Principal> findRange(int[] range);

    int count();
}
