package org.jfge.api.collision;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.jfge.api.fsm.StateMachine;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public final class CollisionHandlerImpl implements CollisionHandler {

    private Map<List<String>, String> collisionMap;

    @Inject
    public CollisionHandlerImpl(@Assisted Map<List<String>, String> collisionMap) {
        this.collisionMap = collisionMap;
    }

    @Override
    public synchronized boolean handleCollision(StateMachine fsm, StateMachine colFsm) {
        String fsmState = fsm.getState().getName();
        String colFsmState = colFsm.getState().getName();
        List<String> tuple = new ArrayList<String>();
        tuple.add(fsmState);
        tuple.add(colFsmState);
        String reaction = collisionMap.get(tuple);
        if (reaction != null) {
            colFsm.setState(reaction);
        }
        Collections.reverse(tuple);
        String drawBack = collisionMap.get(tuple);
        if (drawBack != null) {
            fsm.setState(drawBack);
        }
        if (reaction == null && drawBack == null) {
            return false;
        }
        return true;
    }
}
