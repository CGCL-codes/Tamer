package net.sf.mpango.game.web;

import net.sf.mpango.common.directory.dto.UserDTO;
import net.sf.mpango.game.core.facade.IGameFacade;

/**
 * @author etux
 */
public class GameBackingBean {

    private IGameFacade gameFacade;

    private UserDTO user;

    private String name;

    public void join() {
        gameFacade.join(name, user);
    }

    public IGameFacade getGameFacade() {
        return gameFacade;
    }

    public void setGameFacade(IGameFacade gameFacade) {
        this.gameFacade = gameFacade;
    }
}
