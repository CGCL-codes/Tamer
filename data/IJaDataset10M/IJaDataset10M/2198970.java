package org.chaoticengine.cgll.entity.component.tween;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.chaoticengine.cgll.entity.component.ActiveComponent;
import org.chaoticengine.cgll.input.Command;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Component for doing simple tweening animations on a object.
 *
 * @author Matt Van Der Westhuizen <mattpwest@gmail.com>
 */
public class TweenComponent extends ActiveComponent {

    protected HashMap<String, Command> commands = new HashMap<String, Command>();

    protected List<Tween> tweens = new ArrayList<Tween>();

    protected List<Tween> add = new ArrayList<Tween>();

    protected List<Tween> remove = new ArrayList<Tween>();

    @Override
    public void onSetOwner() {
        super.onSetOwner();
        getOwner().getComponentManager().registerSpecialComponent("tween", this);
    }

    @Override
    public Map<String, Command> getCommands() {
        return (commands);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sb, int delta) {
        super.update(gc, sb, delta);
        for (Tween t : tweens) {
            if (t.isDone()) {
                removeTween(t);
            }
            t.update(gc, delta);
        }
        tweens.removeAll(remove);
        remove.clear();
        tweens.addAll(add);
        for (Tween t : add) {
            t.setOwner(this);
        }
        add.clear();
    }

    public void addTween(Tween t) {
        add.add(t);
    }

    public void removeTween(Tween t) {
        remove.add(t);
    }

    public boolean isDone() {
        return (tweens.isEmpty());
    }
}
