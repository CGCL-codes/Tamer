package net.sf.jawp.gf.system.transaction;

import java.util.Date;
import net.sf.jawp.gf.api.domain.GameOptions;
import net.sf.jawp.gf.api.domain.GameWorld;
import net.sf.jawp.gf.domain.GameWorldBase;
import net.sf.jawp.gf.domain.GameWorldRO;
import net.sf.jawp.gf.system.GameSystemCommand;
import net.sf.jawp.gf.system.RootSystem;

/**
 * 
 * @author jarek
 * @version $Revision: 1.5 $
 *
 * @param <GAME>
 * @param <GAMEVIEW>
 * @param <GAMESERVICE> type of gamespecific service
 */
public class CreateGame<GAME extends GameWorldBase<GAMEVIEW>, GAMEVIEW extends GameWorldRO, GAMESERVICE> extends GameSystemCommand<GameWorld, GAME, GAMEVIEW, GAMESERVICE> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 2L;

    private final String name;

    private final GameOptions options;

    public CreateGame(final String name, final GameOptions opt) {
        super();
        this.name = name;
        this.options = opt;
    }

    /**
	 * 
	 * @param system
	 * @param time
	 * @return
	 */
    public GameWorld perform(final RootSystem<GAME, GAMEVIEW, GAMESERVICE> system, final Date time) {
        return system.createGame(this.name, options);
    }
}
