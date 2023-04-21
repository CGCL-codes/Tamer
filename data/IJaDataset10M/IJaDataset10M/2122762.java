package deadend.ai.dog.monteCarloAdvSingle;

import deadend.globalenum.Directions;

/**
 *
 * @author Yang JiaJian
 */
public class MCBasicDefence extends MCSimStrategy {

    /**
     *
     * @param simGame
     * @param selfPos
     * @return
     */
    public deadend.globalenum.Directions nextDir(MSimGame simGame, java.awt.Point selfPos) {
        java.awt.Point catPos = simGame.scat.position;
        int cx, cy, dx, dy;
        cx = catPos.x;
        cy = catPos.y;
        dx = selfPos.x;
        dy = selfPos.y;
        java.util.ArrayList<Directions> choices = new java.util.ArrayList<Directions>();
        choices.clear();
        if (dx < deadend.game.GameConfigClass.GridX - 1) {
            choices.add(Directions.Right);
        }
        if (dx > 0) {
            choices.add(Directions.Left);
        }
        if (cy - dy <= 5 && dy < deadend.game.GameConfigClass.GridY - 1) {
            choices.add(Directions.Down);
        }
        if (dy > 3) {
            choices.add(Directions.Up);
        }
        java.util.Random rand = new java.util.Random();
        int c = rand.nextInt(choices.size());
        return choices.get(c);
    }
}
