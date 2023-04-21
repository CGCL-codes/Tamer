package net.sourceforge.plantuml.activitydiagram2;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.IEntity;

public class ConditionalContext2 {

    private final Collection<IEntity> pendings = new LinkedHashSet<IEntity>();

    private final IEntity branch;

    private final Direction direction;

    private final ConditionalContext2 parent;

    private final String when;

    public ConditionalContext2(ConditionalContext2 parent, IEntity branch, Direction direction, String when) {
        if (branch.getType() != EntityType.BRANCH) {
            throw new IllegalArgumentException();
        }
        this.branch = branch;
        this.direction = direction;
        this.parent = parent;
        this.when = when;
        this.pendings.add(branch);
    }

    public Direction getDirection() {
        return direction;
    }

    public final ConditionalContext2 getParent() {
        return parent;
    }

    public final Collection<IEntity> getPendings() {
        return Collections.unmodifiableCollection(pendings);
    }

    public final IEntity getBranch() {
        return branch;
    }

    public void clearPendingsButFirst() {
        this.pendings.clear();
        pendings.add(branch);
    }

    private boolean hasElse = false;

    public void executeElse(Collection<IEntity> pendingsToAdd) {
        if (this.hasElse) {
            throw new IllegalStateException();
        }
        this.hasElse = true;
        if (pendings.size() == 0) {
            throw new IllegalStateException();
        }
        final Iterator<IEntity> it = pendings.iterator();
        final IEntity toRemove = it.next();
        if (toRemove.getType() != EntityType.BRANCH) {
            throw new IllegalStateException();
        }
        it.remove();
        this.pendings.addAll(pendingsToAdd);
    }

    public boolean isHasElse() {
        return hasElse;
    }

    public final String getWhen() {
        return when;
    }
}
