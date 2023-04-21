package com.nightspawn.pokerstats.parsers.stars.states;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.nightspawn.pokerstats.parsers.ParseResult;
import com.nightspawn.pokerstats.parsers.ParseState;

public class TurnHeaderState extends PokerStarsState {

    private static final Pattern regex = Pattern.compile("\\*\\*\\* TURN \\*\\*\\* \\[([\\w\\s]+)\\] \\[(\\w+)\\].*");

    public TurnHeaderState(ParseResult r) {
        super(r);
    }

    public boolean accept(String l) {
        Matcher m = regex.matcher(l);
        if (!m.matches()) {
            System.out.println("ficken");
            return false;
        }
        System.out.println("turn card: " + m.group(2));
        return true;
    }

    public ParseState nextState(String l) {
        if (ActionState.matches(l)) return new ActionState(Result);
        return new InvalidState(Result);
    }

    public static boolean matches(String l) {
        Matcher m = regex.matcher(l);
        return m.matches();
    }

    @Override
    public void onEntry(String l) {
    }

    @Override
    public void onExit(String l) {
    }
}
