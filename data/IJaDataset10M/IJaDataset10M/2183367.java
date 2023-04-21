package de.curdreinert.kelo;

public class Player {

    private String name;

    private double elo;

    public Player(String name, double elo) {
        this.name = name;
        this.elo = elo;
    }

    public String getName() {
        return name;
    }

    public double getElo() {
        return elo;
    }

    public void setElo(double elo) {
        this.elo = elo;
    }

    public String toString() {
        return name + " " + elo;
    }

    public static Player[] copy(Player[] source) {
        Player[] dest = new Player[source.length];
        for (int i = 0; i < source.length; i++) {
            dest[i] = new Player(source[i].getName(), source[i].getElo());
        }
        return dest;
    }
}
