package net.innig.macker.event;

import net.innig.macker.rule.ForEach;
import java.util.*;

public class ForEachStarted extends ForEachEvent {

    public ForEachStarted(ForEach forEach) {
        super(forEach, "forEach started");
    }
}
