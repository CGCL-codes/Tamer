package org.inigma.utopia.utils.paper.events;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.inigma.utopia.Province;

public class AidEvent extends AbstractEvent {

    private static final Pattern PATTERN = Pattern.compile("(\\w+ \\d+.., YR\\d+)\\s+(.+ \\(\\d+:\\d+\\)) has sent an aid shipment to (.+ \\(\\d+:\\d+\\))\\.");

    private Province owner;

    private Province target;

    @Override
    public AbstractEvent getEvent(String eventText) {
        Matcher matcher = PATTERN.matcher(eventText);
        if (matcher.find()) {
            AidEvent event = new AidEvent();
            event.setUtopiaDate(matcher.group(1));
            event.setProvince(getProvince(matcher.group(2)));
            event.setTarget(getProvince(matcher.group(3)));
            return event;
        }
        return null;
    }

    public Province getProvince() {
        return owner;
    }

    public Province getTarget() {
        return target;
    }

    public void setProvince(Province prov) {
        this.owner = prov;
    }

    public void setTarget(Province prov) {
        this.target = prov;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(owner);
        sb.append(" has sent an aid shipment to ");
        sb.append(target);
        sb.append(".");
        return sb.toString();
    }
}
