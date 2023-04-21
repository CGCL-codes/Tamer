package wesodi.persistence.facade;

import java.util.List;
import javax.ejb.Local;
import wesodi.entities.persi.UpdateRule;
import wesodi.persistence.generic.facade.GenericFacadeInterface;

/**
 * The Interface defines additional methods which are used to manage the update
 * rules and are not part of the CRUD method sampling in the generic facade.
 * 
 * @author Sarah Haselbauer
 * @date 23.02.2009
 * 
 */
@Local
public interface UpdateRuleFacadeLocal extends GenericFacadeInterface<UpdateRule, Long> {

    List<UpdateRule> getAll();
}
