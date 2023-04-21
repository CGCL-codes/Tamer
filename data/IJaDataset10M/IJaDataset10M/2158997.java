package net.slashie.serf.action;

import java.util.ArrayList;
import java.util.List;
import net.slashie.serf.level.AbstractCell;
import net.slashie.serf.ui.ActionCancelException;
import net.slashie.utils.Position;

public abstract class AwareActor extends Actor {

    public abstract int getSightRangeInCells();

    public abstract int getSightRangeInDots();

    public List<Actor> getNearbyActors() {
        int distance = getSightRangeInCells();
        List<Actor> allActors = getLevel().getActors();
        List<Actor> ret = new ArrayList<Actor>();
        for (Actor actor : allActors) {
            if (Position.distance(getPosition(), actor.getPosition()) <= distance) {
                ret.add(actor);
            }
        }
        return ret;
    }

    public int stare(Actor target) {
        if (target == null || target.isInvisible() || target.getPosition().z != getPosition().z) return -1;
        if (getLevel().getDistance(target.getPosition(), getPosition()) <= getSightRangeInDots()) {
            Position pp = getLevel().getPlayer().getPosition();
            if (pp.x == getPosition().x) {
                if (pp.y > getPosition().y) {
                    return Action.DOWN;
                } else {
                    return Action.UP;
                }
            } else if (pp.y == getPosition().y) {
                if (pp.x > getPosition().x) {
                    return Action.RIGHT;
                } else {
                    return Action.LEFT;
                }
            } else if (pp.x < getPosition().x) {
                if (pp.y > getPosition().y) return Action.DOWNLEFT; else return Action.UPLEFT;
            } else {
                if (pp.y > getPosition().y) return Action.DOWNRIGHT; else return Action.UPRIGHT;
            }
        }
        return -1;
    }

    public boolean seesActor(Actor mainTarget) {
        if (wasSeen()) {
            return isActorInLOS(mainTarget);
        } else {
            return false;
        }
    }

    public boolean isActorInLOS(Actor mainTarget) {
        return wasSeen() && mainTarget.getLevel().getDistance(mainTarget.getPosition(), getPosition()) <= getSightRangeInDots();
    }

    public void seeMapCell(AbstractCell abstractCell) {
    }

    public EnvironmentInfo getEnvironmentAround(int xrange, int yrange) {
        return getLevel().getEnvironmentAroundActor(this, getPosition().x, getPosition().y, getPosition().z, xrange, yrange);
    }

    public void landOn(Position destinationPoint) throws ActionCancelException {
        setPosition(destinationPoint);
    }
}
