package lamao.soh.core;

import java.util.List;
import lamao.soh.core.entities.SHBrick;

/**
 * Game context for Stones of History
 * @author lamao
 *
 */
public class SHBreakoutGameContext implements ISHGameContext {

    private int _numDeletableBricks = 0;

    public int getNumDeletableBricks() {
        return _numDeletableBricks;
    }

    public void updateNumDeletableBricks() {
        _numDeletableBricks = 0;
        List<SHEntity> bricks = SHGamePack.scene.getEntities("brick");
        if (bricks != null) {
            SHBrick brick = null;
            for (SHEntity e : bricks) {
                brick = (SHBrick) e;
                if (brick.getStrength() != Integer.MAX_VALUE) {
                    _numDeletableBricks++;
                }
            }
        }
    }
}
