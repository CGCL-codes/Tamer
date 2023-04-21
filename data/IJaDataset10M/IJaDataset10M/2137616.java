package org.inigma.utopia.paper.events;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.inigma.utopia.Kingdom;

public class DragonStartEvent extends AbstractEvent {

    private static final Pattern PATTERN = Pattern.compile("(\\w+ \\d+.., YR\\d+)\\s+Our kingdom has begun a dragon project targetted at (.+ \\(\\d+:\\d+\\))\\.");

    private Kingdom kingdom;

    @Override
    public AbstractEvent getEvent(String eventText) {
        Matcher matcher = PATTERN.matcher(eventText);
        if (matcher.find()) {
            DragonStartEvent event = new DragonStartEvent();
            event.setUtopiaDate(matcher.group(1));
            event.setKingdom(getKingdom(matcher.group(2)));
            return event;
        }
        return null;
    }

    public Kingdom getKingdom() {
        return kingdom;
    }

    public void setKingdom(Kingdom kingdom) {
        this.kingdom = kingdom;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("Our kingdom has begun a dragon project targetted at ");
        sb.append(kingdom);
        sb.append(".");
        return sb.toString();
    }
}
