package eu.vph.predict.vre.extensions.displaytag;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.displaytag.decorator.TableDecorator;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import eu.vph.predict.vre.base.entity.user.VREUser;
import eu.vph.predict.vre.base.service.UserService;
import eu.vph.predict.vre.in_silico.entity.simulation.AbstractSimulation;

@Configurable(autowire = Autowire.BY_NAME, preConstruction = true)
public class ShowUserDecorator extends TableDecorator {

    @Autowired
    @Qualifier(UserService.COMPONENT_USER_SERVICE)
    private UserService userService;

    private Map<Long, String> userMap = new HashMap<Long, String>();

    private Log log = LogFactory.getLog(ShowUserDecorator.class);

    /**Initialising constructor - populates a map of user id and their names. */
    public ShowUserDecorator() {
        log.debug("~ShowUserDecorator() : Constructing decorator - initialising user mappings");
        for (final VREUser vreUser : userService.retrieveUsers()) {
            userMap.put(vreUser.getId(), vreUser.getName());
        }
    }

    /**
   * Retrieve the simulation creator name.
   * 
   * @return Simulation creator name.
   */
    public String getCreatorName() {
        log.debug("~getCreatorName() : Retrieving creator name");
        final AbstractSimulation simulation = (AbstractSimulation) getCurrentRowObject();
        final Long creatorId = simulation.getCreatorId();
        final String creatorName = userMap.containsKey(creatorId) ? userMap.get(creatorId) : creatorId.toString();
        return creatorName;
    }
}
