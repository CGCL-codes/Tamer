package com.nightspawn.pokerstats.parsers.stars.states;

import com.nightspawn.pokerstats.parsers.ParseResult;
import com.nightspawn.pokerstats.parsers.ParseState;

public abstract class PokerStarsState extends ParseState {

    public static final String PlayerPattern = "[\\w&@]+";

    public PokerStarsState(ParseResult r) {
        super(r);
    }
}
