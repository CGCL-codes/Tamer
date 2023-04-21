package hokutonorogue.level;

import hokutonorogue.game.GameWorld;
import hokutonorogue.game.MainGame;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

/**
 *
 * @author ZolfriK
 */
public class RaohPalace extends RandomCity {

    @Override
    public String getName() {
        return "RAOH PALACE";
    }

    @Override
    public int getIndex() {
        return 20;
    }

    @Override
    public void _initialize() throws MalformedURLException, FileNotFoundException, Exception {
        super._initialize();
    }
}
