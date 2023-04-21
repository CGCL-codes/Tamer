package net.sf.gamebay.chess.moving.rule.impl.my;

import net.sf.gamebay.game.board.Field;
import net.sf.gamebay.game.figure.Figure;
import net.sf.gamebay.game.moving.impl.my.MovingMyImpl;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import net.sf.gamebay.chess.moving.rule.impl.JumpImpl;

/**
 * TODO
 */
public class JumpMyImpl extends JumpImpl {

    MovingMyImpl dele = new MovingMyImpl();

    /**
     * @see game.moving.impl.AMovingImpl#getReachableFields(net.sf.gamebay.game.figure.Figure)
     */
    @Override
    public EList<Field> getReachableFields(Figure figure) {
        EList<Field> list = new BasicEList<Field>();
        Field target;
        dele.update(this);
        target = dele.getNeighbourField(figure, -1, 2);
        if (target != null) {
            Figure targetFig = positionCtl.getFigure(target);
            if (targetFig == null || !targetFig.eContainer().equals(figure.eContainer())) {
                list.add(target);
            }
        }
        target = dele.getNeighbourField(figure, 1, 2);
        if (target != null) {
            Figure targetFig = positionCtl.getFigure(target);
            if (targetFig == null || !targetFig.eContainer().equals(figure.eContainer())) {
                list.add(target);
            }
        }
        target = dele.getNeighbourField(figure, -2, 1);
        if (target != null) {
            Figure targetFig = positionCtl.getFigure(target);
            if (targetFig == null || !targetFig.eContainer().equals(figure.eContainer())) {
                list.add(target);
            }
        }
        target = dele.getNeighbourField(figure, -2, -1);
        if (target != null) {
            Figure targetFig = positionCtl.getFigure(target);
            if (targetFig == null || !targetFig.eContainer().equals(figure.eContainer())) {
                list.add(target);
            }
        }
        target = dele.getNeighbourField(figure, 2, 1);
        if (target != null) {
            Figure targetFig = positionCtl.getFigure(target);
            if (targetFig == null || !targetFig.eContainer().equals(figure.eContainer())) {
                list.add(target);
            }
        }
        target = dele.getNeighbourField(figure, 2, -1);
        if (target != null) {
            Figure targetFig = positionCtl.getFigure(target);
            if (targetFig == null || !targetFig.eContainer().equals(figure.eContainer())) {
                list.add(target);
            }
        }
        target = dele.getNeighbourField(figure, -1, -2);
        if (target != null) {
            Figure targetFig = positionCtl.getFigure(target);
            if (targetFig == null || !targetFig.eContainer().equals(figure.eContainer())) {
                list.add(target);
            }
        }
        target = dele.getNeighbourField(figure, 1, -2);
        if (target != null) {
            Figure targetFig = positionCtl.getFigure(target);
            if (targetFig == null || !targetFig.eContainer().equals(figure.eContainer())) {
                list.add(target);
            }
        }
        return list;
    }

    /**
     * @see game.moving.rule.impl.ARuleMovingImpl#getThreatenedFields(net.sf.gamebay.game.figure.Figure)
     */
    @Override
    public EList<Field> getThreatenedFields(Figure figure) {
        EList<Field> list = new BasicEList<Field>();
        Field target;
        dele.update(this);
        target = dele.getNeighbourField(figure, -1, 2);
        if (target != null) {
            list.add(target);
        }
        target = dele.getNeighbourField(figure, 1, 2);
        if (target != null) {
            list.add(target);
        }
        target = dele.getNeighbourField(figure, -2, 1);
        if (target != null) {
            list.add(target);
        }
        target = dele.getNeighbourField(figure, -2, -1);
        if (target != null) {
            list.add(target);
        }
        target = dele.getNeighbourField(figure, 2, 1);
        if (target != null) {
            list.add(target);
        }
        target = dele.getNeighbourField(figure, 2, -1);
        if (target != null) {
            list.add(target);
        }
        target = dele.getNeighbourField(figure, -1, -2);
        if (target != null) {
            list.add(target);
        }
        target = dele.getNeighbourField(figure, 1, -2);
        if (target != null) {
            list.add(target);
        }
        return list;
    }
}
