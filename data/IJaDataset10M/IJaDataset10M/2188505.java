package org.inigma.utopia.utils.paper.events;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CeaseFireEvent extends RelationsEvent {

    private static final Pattern PATTERN = Pattern.compile("(\\w+ \\d+.., YR\\d+)\\s+We have proposed a ceasefire offer to (.+ \\(\\d+:\\d+\\))\\.");

    @Override
    public AbstractEvent getEvent(String eventText) {
        Matcher matcher = PATTERN.matcher(eventText);
        if (matcher.find()) {
            CeaseFireEvent event = new CeaseFireEvent();
            event.setUtopiaDate(matcher.group(1));
            event.setTarget(getKingdom(matcher.group(2)));
            return event;
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("We have proposed a ceasefire offer to ");
        sb.append(getTarget());
        sb.append(".");
        return sb.toString();
    }
}
