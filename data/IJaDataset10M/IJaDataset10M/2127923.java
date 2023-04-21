package com.calefay.utils;

import java.util.ArrayList;
import java.util.Collections;

public class GameEventScript {

    private ArrayList<GameScriptEvent> script = null;

    public GameEventScript() {
        script = new ArrayList<GameScriptEvent>();
    }

    /** Adds an event to the script. 
	 *  The event will be added at the end of the script and NOT in its proper chronological position.
	 *  This may mean the script needs sorting again.
	 */
    public void addScriptEvent(GameScriptEvent event) {
        script.add(event);
    }

    public void sortScript() {
        Collections.sort(script);
    }

    /** Copies all events from the entire script to the event handler straight away, without creating a script instance.*/
    public void runEntire(GameEventHandler handler) {
        for (GameScriptEvent e : script) {
            if (handler != null) handler.addEvent(e.getEvent(), e.getTargetQueue());
        }
    }

    public GameEventScriptInstance getScriptInstance(GameEventHandler h) {
        return new GameEventScriptInstance(h);
    }

    public void cleanup() {
        script.clear();
    }

    public void printScript() {
        for (GameScriptEvent e : script) {
            System.out.println(e.getOccursTime() + "     " + e.getEvent().getEventType());
        }
    }

    /*********************************** Instance class follows ****************************************/
    public class GameEventScriptInstance implements GameRemoveable {

        private String label = null;

        private GameEventHandler handler = null;

        private float elapsedTime = 0;

        private int nextEvent = 0;

        private int repeatCount = 0;

        private boolean active = false;

        private GameEventScriptInstance(GameEventHandler h) {
            active = true;
            setHandler(h);
            elapsedTime = 0;
            nextEvent = 0;
            repeatCount = 0;
        }

        public void update(float interpolation) {
            if (script.size() <= 0) return;
            if (nextEvent >= script.size()) {
                repeatCount--;
                if (repeatCount <= 0) {
                    deactivate();
                } else {
                    elapsedTime = 0;
                    nextEvent = 0;
                }
            }
            if (!isActive()) return;
            elapsedTime += interpolation;
            GameScriptEvent scriptEvent = script.get(nextEvent);
            while (scriptEvent.getOccursTime() <= elapsedTime) {
                if (handler != null) handler.addEvent(scriptEvent.getEvent(), scriptEvent.getTargetQueue());
                nextEvent++;
                if (nextEvent < script.size()) scriptEvent = script.get(nextEvent); else break;
            }
        }

        public void setRepeat(int repetitions) {
            repeatCount = repetitions;
            if (repeatCount < 0) repeatCount = 0;
        }

        /** Sets the elapsed time in the script, ignoring any events before this. */
        public void setElapsedTime(float time) {
            printScript();
            int pos = Collections.binarySearch(script, new GameScriptEvent(time, new GameEvent("Search", null, null)));
            if (pos < 0) pos = (-pos) - 1;
            elapsedTime = time;
            nextEvent = pos;
        }

        public void initializeScript() {
            elapsedTime = 0;
            nextEvent = 0;
        }

        /** Sets the listener that will receive the events from this script.*/
        public void setHandler(GameEventHandler h) {
            handler = h;
        }

        public boolean isActive() {
            return active;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public void deactivate() {
            active = false;
            if (label != null) GameWorldInfo.getGameWorldInfo().getEventHandler().addEvent("ScriptTerminated", "scriptEvents", label, this);
        }

        public void cleanup() {
            label = null;
            active = false;
            handler = null;
            elapsedTime = 0;
            nextEvent = 0;
            repeatCount = 0;
        }
    }
}
