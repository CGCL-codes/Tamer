package net.sf.gamebay.game.moving.rule.impl.my;

import net.sf.gamebay.game.util.my.IDelegation;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import net.sf.gamebay.game.board.Field;
import net.sf.gamebay.game.figure.Figure;
import net.sf.gamebay.game.moving.rule.impl.AMovingFwdImpl;
import net.sf.gamebay.game.player.APlayerController;

/**
 * TODO
 */
public class MovingFwdMyImpl extends AMovingFwdImpl implements IDelegation {

    RuleMovingMyImpl dele = new RuleMovingMyImpl();

    /**
     * @see net.sf.gamebay.game.util.my.IDelegation#update(org.eclipse.emf.ecore.EObject)
     */
    @Override
    public void update(EObject object) {
        positionCtl = ((AMovingFwdImpl) object).getPositionCtl();
    }

    /**
     * @see game.moving.impl.AMovingImpl#getNeighbourField(net.sf.gamebay.game.figure.Figure, int, int)
     */
    @Override
    public Field getNeighbourField(Figure figure, int colOffset, int rowOffset) {
        dele.update(this);
        return dele.getNeighbourField(figure, colOffset, rowOffset);
    }

    /**
     * @see game.moving.impl.AMovingImpl#getReachableFields(net.sf.gamebay.game.figure.Figure)
     */
    @Override
    public EList<Field> getReachableFields(Figure figure) {
        dele.update(this);
        return dele.getReachableFields(figure);
    }

    /**
     * @see game.moving.rule.impl.AMovingFwdImpl#getDirection()
     */
    @Override
    public int getDirection(APlayerController player) {
        return (int) Math.signum(player.getFwdDirection().getRow().getIdx() - player.getBaseFields().get(0).getRow().getIdx());
    }
}
