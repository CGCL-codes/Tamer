package Memento;

import java.util.Stack;

public class PileDeMemento {

    private Stack<Memento> pile;

    public PileDeMemento() {
        pile = new Stack<Memento>();
    }

    public void push(Memento m) {
        pile.push(m);
    }

    public void clear() {
        pile.clear();
    }

    public boolean isEmpty() {
        return pile.isEmpty();
    }

    public Memento pop() {
        return pile.pop();
    }
}
