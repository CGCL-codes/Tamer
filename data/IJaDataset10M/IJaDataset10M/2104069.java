package net.innig.macker.event;

import net.innig.macker.rule.ForEach;
import java.util.*;

public class ForEachEvent extends MackerEvent {

    public ForEachEvent(ForEach forEach, String description) {
        super(forEach, description, Collections.EMPTY_LIST);
        this.forEach = forEach;
    }

    public ForEach getForEach() {
        return forEach;
    }

    private ForEach forEach;
}
