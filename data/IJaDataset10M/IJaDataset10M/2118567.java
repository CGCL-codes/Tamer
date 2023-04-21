package com.coury.dasle.helper;

import java.lang.reflect.InvocationTargetException;
import com.coury.dasle.parser.LineParser;
import com.coury.dasle.parser.states.MatchCallback;

public class ParseHelper {

    public static boolean tryToMatch(LineParser parserHolder, String nameToMatch, MatchCallback matchCallback) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        String currentName = null;
        while (parserHolder.hasMore()) {
            String part = parserHolder.next();
            if (currentName == null) {
                currentName = part.toLowerCase();
            } else {
                currentName += Character.toUpperCase(part.charAt(0));
                if (part.length() > 1) {
                    currentName += part.substring(1).toLowerCase();
                }
            }
            if (nameToMatch.equalsIgnoreCase(currentName)) {
                return matchCallback.matched(parserHolder);
            }
        }
        return false;
    }
}
