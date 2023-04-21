package core;

import java.util.Random;
import core.data.AccessGuiData;
import core.data.Pair;

public class StateManager {

    private boolean switch_on_off = false;

    private boolean switch_wait = false;

    private boolean hasNextPair = false;

    private boolean somethingChanged = false;

    private Pair nextPair = null;

    private int timerAbsolut = 100;

    private boolean firstPushed = true;

    private Random RANDOMIZER = new Random();

    public StateManager() {
        say("** StateManager erschaffen");
    }

    public synchronized void pushStart(AccessGuiData dat) {
        say("pushStart --> start ");
        if (switch_wait) switch_wait = false; else {
            switch_on_off = true;
            if (firstPushed) {
                Pair temp = dat.getPair();
                valueChangedByListener(temp);
                firstPushed = false;
            } else switch_wait = true;
        }
        say("pushStart -->  switch_on_off : " + switch_on_off);
        say("pushStart -->  switch_wait : " + switch_wait);
        say("pushStart --> end");
    }

    public synchronized void pushStopp() {
        newClear();
    }

    public synchronized void valueChangedByListener(Pair x) {
        say("valueChangeByListener besitzt :");
        say(x.toString());
        somethingChanged = true;
        nextPair = x;
        hasNextPair = true;
    }

    public synchronized void calcNextPair(Pair x) {
        somethingChanged = false;
        nextPair = x;
        hasNextPair = true;
    }

    public synchronized boolean questSthChanged() {
        return somethingChanged;
    }

    public synchronized boolean questOnOrOff() {
        return switch_on_off;
    }

    public synchronized boolean questShouldWait() {
        return switch_wait;
    }

    public synchronized boolean hasNextPair() {
        return hasNextPair;
    }

    public synchronized Pair getNextPairToDraw() {
        hasNextPair = false;
        somethingChanged = false;
        return nextPair;
    }

    public synchronized int getTimeAbsolut() {
        return timerAbsolut;
    }

    public synchronized void setTimeAbsolut(int t) {
        timerAbsolut = t;
    }

    private void newClear() {
        switch_on_off = false;
        switch_wait = false;
        hasNextPair = false;
        somethingChanged = false;
        nextPair = null;
        firstPushed = true;
    }

    private void say(String s) {
        System.out.print("\nStateManager ->" + s);
    }
}
