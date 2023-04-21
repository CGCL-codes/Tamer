package tk.korbel.thetvdb.api.datastructures;

import java.util.ArrayList;
import java.util.List;

public class Actors {

    private List<Actor> actors = new ArrayList<Actor>();

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }
}
