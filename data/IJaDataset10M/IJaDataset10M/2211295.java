package com.nightspawn.pokerstats.parsers.stars.states;

import com.nightspawn.pokerstats.parsers.ParseResult;
import com.nightspawn.pokerstats.parsers.ParseState;

public class HoleCardHeaderState extends PokerStarsState {

    public HoleCardHeaderState(ParseResult r) {
        super(r);
    }

    public boolean accept(String l) {
        return l.equals("*** HOLE CARDS ***");
    }

    public static boolean matches(String l) {
        return l.equals("*** HOLE CARDS ***");
    }

    public ParseState nextState(String l) {
        if (HoleCardState.matches(l)) return new HoleCardState(Result);
        return new InvalidState(Result);
    }

    @Override
    public void onEntry(String l) {
    }

    @Override
    public void onExit(String l) {
    }
}
