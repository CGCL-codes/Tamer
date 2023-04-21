package org.gs.game.gostop.action;

import java.util.ArrayList;
import java.util.List;
import org.gs.game.gostop.action.post.IGamePostAction;
import org.gs.game.gostop.action.pre.IPreExecuteAction;
import org.gs.game.gostop.event.GameEvent;
import org.gs.game.gostop.event.GameEventManager;
import org.gs.game.gostop.event.GameEventType;

public abstract class GameAction {

    protected Object target;

    protected long start;

    protected int duration;

    protected List<GameAction> nextActions;

    protected int nextStart;

    protected GameEventType completeEventType;

    protected GameEvent completeEvent;

    protected Object result;

    protected IPreExecuteAction preExecuteAction;

    protected List<IGamePostAction> gamePostActions;

    public GameAction(Object target, int duration) {
        this.target = target;
        this.duration = duration;
        nextActions = null;
        completeEventType = null;
        completeEvent = null;
        result = null;
        preExecuteAction = null;
        gamePostActions = null;
    }

    public abstract boolean execute(float progress);

    public void setStarted() {
        start = System.currentTimeMillis();
        onPreExecute();
    }

    public long getStartTime() {
        return start;
    }

    public int getDuration() {
        return duration;
    }

    public void setNextAction(GameAction nextAction) {
        setNextAction(nextAction, -1);
    }

    public void setNextAction(GameAction nextAction, int nextStart) {
        addNextAction(nextAction);
        this.nextStart = nextStart <= 0 ? duration : nextStart;
    }

    public void addNextAction(GameAction nextAction) {
        if (nextActions == null) nextActions = new ArrayList<GameAction>();
        nextActions.add(nextAction);
    }

    public void setNextActions(List<GameAction> nextActions, int nextStart) {
        this.nextActions = nextActions;
        this.nextStart = nextStart <= 0 ? duration : nextStart;
    }

    public List<GameAction> getNextActions() {
        List<GameAction> nextActions = this.nextActions;
        this.nextActions = null;
        return nextActions;
    }

    public boolean canExecuteNextAction(int elapsed) {
        return nextActions != null && elapsed >= nextStart;
    }

    protected void onPreExecute() {
        if (preExecuteAction != null) preExecuteAction.onPreExecute(this);
    }

    public void onActionComplete() {
        doPostActions();
        if (completeEventType != null) fireActionEvent(getCompleteEvent());
    }

    public void setCompleteEventType(GameEventType completeEventType) {
        this.completeEventType = completeEventType;
    }

    public GameEventType getCompleteEventType() {
        return completeEventType;
    }

    public void setCompleteEvent(GameEvent completeEvent) {
        this.completeEvent = completeEvent;
        this.completeEventType = completeEvent.getEventType();
    }

    public GameEvent getCompleteEvent() {
        if (completeEvent == null) completeEvent = new GameEvent(target, completeEventType);
        return completeEvent;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Object getResult() {
        return result;
    }

    public void setPreExecuteAction(IPreExecuteAction preExecuteAction) {
        this.preExecuteAction = preExecuteAction;
    }

    public void addGamePostAction(IGamePostAction gamePostAction) {
        if (gamePostActions == null) gamePostActions = new ArrayList<IGamePostAction>();
        gamePostActions.add(gamePostAction);
    }

    protected void fireActionEvent(GameEvent e) {
        GameEventManager.fireGameEvent(e, false);
    }

    protected void doPostActions() {
        if (gamePostActions != null) {
            for (IGamePostAction gamePostAction : gamePostActions) gamePostAction.onActionComplete(this);
        }
    }
}
