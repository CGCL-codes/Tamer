package sratworld.base.actor.parser;

import java.util.ArrayList;
import java.util.List;

public class ActorLine {

    private ActorLineType actorLineType;

    private List<Object> tokens = new ArrayList<Object>();

    private int originalLinenumber;

    private String originalLine;

    public ActorLineType getActorLineType() {
        return actorLineType;
    }

    public void setActorLineType(final ActorLineType aActorLineType) {
        actorLineType = aActorLineType;
    }

    public List<Object> getTokens() {
        return tokens;
    }

    public void setOriginalLinenumber(int originalLinenumber) {
        this.originalLinenumber = originalLinenumber;
    }

    public int getOriginalLinenumber() {
        return originalLinenumber;
    }

    public void setOriginalLine(String originalLine) {
        this.originalLine = originalLine;
    }

    public String getOriginalLine() {
        return originalLine;
    }
}
