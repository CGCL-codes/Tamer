package jmetest.monkeymahjongg.playground.model;

public class Hint {

    Tile first;

    Tile second;

    public Hint(Tile first, Tile second) {
        this.first = first;
        this.second = second;
    }

    public Tile getFirst() {
        return first;
    }

    public Tile getSecond() {
        return second;
    }
}
