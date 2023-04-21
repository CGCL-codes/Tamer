package net.sourceforge.plantuml.sequencediagram;

import java.util.List;
import net.sourceforge.plantuml.Url;

public class Delay implements Event {

    private final List<String> text;

    public Delay(List<String> text) {
        this.text = text;
    }

    public final List<String> getText() {
        return text;
    }

    public boolean dealWith(Participant someone) {
        return false;
    }

    public Url getUrl() {
        return null;
    }
}
