package org.nakedobjects.example.agent;

import org.nakedobjects.applib.DomainObjectContainer;
import java.util.ArrayList;
import java.util.List;

public class Instrument {

    private DomainObjectContainer container;

    private List players = new ArrayList();

    private String name;

    public void setContainer(final DomainObjectContainer container) {
        this.container = container;
    }

    public List getPlayers() {
        return players;
    }

    public void removeFromPlayers(final Musician player) {
        player.removeFromInstruments(this);
        container.objectChanged(this);
    }

    public void addToPlayers(final Musician player) {
        player.addToInstruments(this);
        container.objectChanged(this);
    }

    public String getName() {
        container.resolve(this, name);
        return name;
    }

    public void setName(final String name) {
        this.name = name;
        container.objectChanged(this);
    }

    public String title() {
        container.resolve(this, players);
        return name + " ~ " + players.size();
    }
}
