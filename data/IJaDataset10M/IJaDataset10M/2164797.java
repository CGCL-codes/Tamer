package com.nightspawn.pokerstats.parsers.stars.states;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.nightspawn.pokerstats.parsers.ParseResult;
import com.nightspawn.pokerstats.parsers.ParseState;

public class HoleCardState extends PokerStarsState {

    private static final Pattern regex = Pattern.compile("Dealt to (\\w+) \\[([\\w ]+)\\].*");

    public HoleCardState(ParseResult r) {
        super(r);
    }

    public boolean accept(String l) {
        Matcher m = regex.matcher(l);
        if (!m.matches()) {
            System.out.println("ficken");
            return false;
        }
        System.out.println("player: " + m.group(1));
        System.out.println("cards: " + m.group(2));
        return true;
    }

    public static boolean matches(String l) {
        Matcher m = regex.matcher(l);
        return m.matches();
    }

    public ParseState nextState(String l) {
        if (ActionState.matches(l)) return new ActionState(Result);
        return new InvalidState(Result);
    }

    @Override
    public void onEntry(String l) {
    }

    @Override
    public void onExit(String l) {
    }
}
