package org.inigma.utopia.paper.events;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MassacreEvent extends AttackEvent {

    private static final Pattern THEM_PATTERN = Pattern.compile("(\\w+ \\d+.., YR\\d+)\\s+(.+ \\(\\d+:\\d+\\)) invaded (.+ \\(\\d+:\\d+\\)) and killed ([\\d,]+) people\\.");

    private static final Pattern US_PATTERN = Pattern.compile("(\\w+ \\d+.., YR\\d+)\\s+(.+ \\(\\d+:\\d+\\)) killed ([\\d,]+) people within (.+ \\(\\d+:\\d+\\))\\.");

    private int peasants;

    @Override
    public AbstractEvent getEvent(String eventText) {
        eventText = translate(eventText);
        Matcher matcher = THEM_PATTERN.matcher(eventText);
        if (matcher.find()) {
            MassacreEvent event = new MassacreEvent();
            event.setUtopiaDate(matcher.group(1));
            event.setAttacker(getProvince(matcher.group(2)));
            event.setVictum(getProvince(matcher.group(3)));
            event.setPeasants(getNumber(matcher.group(4)));
            event.setType(AttackType.Massacre);
            return event;
        }
        matcher = US_PATTERN.matcher(eventText);
        if (matcher.find()) {
            MassacreEvent event = new MassacreEvent();
            event.setUtopiaDate(matcher.group(1));
            event.setAttacker(getProvince(matcher.group(2)));
            event.setPeasants(getNumber(matcher.group(3)));
            event.setVictum(getProvince(matcher.group(4)));
            event.setType(AttackType.Massacre);
            return event;
        }
        return null;
    }

    public void setPeasants(int land) {
        this.peasants = land;
    }

    public int getPeasants() {
        return peasants;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(attacker);
        sb.append(" invaded ");
        sb.append(victum);
        sb.append(" and killed ");
        sb.append(peasants);
        sb.append(" people.");
        return sb.toString();
    }
}
