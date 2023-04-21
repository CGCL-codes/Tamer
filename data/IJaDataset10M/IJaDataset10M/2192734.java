package de.berlin.fu.inf.gameai.go.goban;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import de.berlin.fu.inf.gameai.utils.Copyable;

public final class Chain implements Copyable<Chain> {

    private final int color;

    private final Set<Integer> liberties;

    private final List<Integer> stones;

    private final Set<Chain> neighbours;

    public Chain(final int color, final int stone) {
        this.color = color;
        this.liberties = new HashSet<Integer>();
        this.stones = new LinkedList<Integer>();
        this.neighbours = new HashSet<Chain>();
        stones.add(stone);
    }

    public Chain(final int color, final int stone, final Collection<Integer> libs, final Collection<Chain> neighbours) {
        this.color = color;
        this.liberties = new HashSet<Integer>(libs);
        this.stones = new LinkedList<Integer>();
        this.neighbours = new HashSet<Chain>(neighbours);
        stones.add(stone);
    }

    public Chain(final Chain other) {
        this.color = other.color;
        this.liberties = new HashSet<Integer>(other.liberties);
        this.stones = new LinkedList<Integer>(other.stones);
        this.neighbours = new HashSet<Chain>(other.neighbours);
    }

    public Chain copy() {
        return new Chain(this);
    }

    public int getColor() {
        return color;
    }

    public Collection<Chain> getNeighbours() {
        return neighbours;
    }

    public Collection<Integer> getStones() {
        return stones;
    }

    public Collection<Integer> getLiberties() {
        return liberties;
    }

    public void removeLiberty(final int id) {
        liberties.remove(id);
    }

    public int getSize() {
        return stones.size();
    }

    public int getLibertyCount() {
        return liberties.size();
    }

    public void merge(final Chain m) {
        stones.addAll(m.getStones());
        liberties.addAll(m.getLiberties());
        neighbours.addAll(m.getNeighbours());
    }

    public void addField(final int id, final Collection<Integer> libs) {
        stones.add(id);
        liberties.addAll(libs);
    }

    public boolean hasLiberties() {
        return getLibertyCount() > 0;
    }

    public int getMergeCost() {
        return stones.size();
    }

    public void addLiberty(final int i) {
        liberties.add(i);
    }

    public void addNeighbour(final Chain chain) {
        neighbours.add(chain);
    }

    public void removeNeighbour(final Chain chain) {
        neighbours.remove(chain);
    }

    @Override
    public String toString() {
        return getColor() + ":" + getLibertyCount() + getStones();
    }
}
