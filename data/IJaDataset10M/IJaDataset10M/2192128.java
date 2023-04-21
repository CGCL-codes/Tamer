package games.midhedava.server.script;

import games.midhedava.server.entity.player.Player;
import games.midhedava.server.events.TurnNotifier;
import games.midhedava.server.events.TurnNotifier.TurnEvent;
import games.midhedava.server.scripting.ScriptImpl;
import games.midhedava.server.util.ObjectCounter;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * dumps debug information about turn listener events
 *
 * @author hendrik
 */
public class DumpTurnListenerEvents extends ScriptImpl {

    @Override
    public void execute(Player admin, List<String> args) {
        int outdated = 0;
        ObjectCounter<Class> counter = new ObjectCounter<Class>();
        TurnNotifier turnNotifier = TurnNotifier.get();
        int currentTurn = turnNotifier.getCurrentTurnForDebugging();
        Map<Integer, Set<TurnEvent>> events = turnNotifier.getEventListForDebugging();
        for (Integer turn : events.keySet()) {
            if (turn.intValue() < currentTurn) {
                outdated++;
            }
            for (TurnEvent event : events.get(turn)) {
                counter.add(event.turnListener.getClass());
            }
        }
        admin.sendPrivateText("Statistics: " + "\n" + counter.getMap() + "\nCounted turn events:" + events.size() + "\nOutdated turn events: " + outdated);
    }
}
