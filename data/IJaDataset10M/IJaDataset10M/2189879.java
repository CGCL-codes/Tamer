package helpfunctons.entity.beans;

import helpfunctons.entity.Players;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author alexog
 */
@Local
public interface PlayersFacadeLocal {

    void create(Players players);

    void edit(Players players);

    void destroy(Players players);

    Players find(Object pk);

    List findAll();

    int getID();

    void update();

    helpfunctons.entity.Players findByUsername(String username);
}
