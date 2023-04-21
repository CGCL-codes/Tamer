package antirashka.map.items;

import antirashka.ai.IBehavior;
import antirashka.engine.IController;
import antirashka.engine.IPlayerAction;
import antirashka.icons.IconManager;
import javax.swing.*;
import java.util.Random;

public final class NPC extends AbstractPerson {

    private final Team team;

    private final IBehavior<IActor> behavior;

    public NPC(Team team, IBehavior<IActor> behavior) {
        super(20);
        this.team = team;
        this.behavior = behavior;
    }

    public Icon getIcon() {
        IconManager instance = IconManager.getInstance();
        if (isAlive()) {
            switch(team) {
                case NATO:
                    return instance.getPC(0, 0);
                case CIVILIAN:
                    return instance.getPC(4, 0);
            }
            return instance.getPC(0, 6);
        } else {
            return instance.getField(2, 3);
        }
    }

    public Iterable<IPlayerAction> behave(IController controller, Random rnd) {
        return behavior.behave(controller, this, rnd);
    }

    public int getSpeed() {
        return 10;
    }

    public int getReaction() {
        return 100;
    }

    public IPC toPC() {
        return null;
    }

    public Team getTeam() {
        return team;
    }

    public String toString() {
        return "[NPC " + getId() + " of " + getTeam() + "]";
    }
}
