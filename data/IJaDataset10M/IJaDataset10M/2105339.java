package de.funtip.vgng;

import java.util.*;

class MoveList {

    private Move first;

    public MoveList() {
        first = null;
    }

    public MoveList(Position pos) {
        this();
        initialize(pos);
    }

    public MoveList(MoveList ml) {
        this();
        Move currentSrc = ml.first();
        if (currentSrc == null) return;
        first = (Move) currentSrc.clone();
        Move currentDest = first;
        if (currentSrc != null) {
            currentSrc = currentSrc.getNext();
            while (currentSrc != null) {
                currentDest.setNext((Move) currentSrc.clone());
                currentDest = currentDest.getNext();
                currentSrc = currentSrc.getNext();
            }
        }
    }

    protected void initialize(Position pos) {
        for (int i = 1; i < 8; i++) {
            if (pos.isMovePossible(i)) {
                Move m = new Move(i);
                add(m);
            }
        }
    }

    protected void remove(int column) {
        if (first == null) return;
        if (first.getColumn() == column) {
            first = first.getNext();
            return;
        }
        Move current = first;
        while (current.getNext() != null) {
            if (current.getNext().getColumn() == column) {
                current.setNext(current.getNext().getNext());
                return;
            }
            current = current.getNext();
        }
    }

    protected void add(Move m) {
        m.setNext(null);
        if (first == null) {
            first = m;
            return;
        }
        if (first.compareTo(m) > 0) {
            m.setNext(first);
            first = m;
            return;
        }
        if (first.getNext() == null) {
            first.setNext(m);
            return;
        }
        Move current = first;
        while (current.getNext() != null) {
            if (current.getNext().compareTo(m) > 0) {
                m.setNext(current.getNext());
                current.setNext(m);
                return;
            }
            current = current.getNext();
        }
        current.setNext(m);
    }

    public void put(Move m) {
        remove(m.getColumn());
        add(m);
    }

    public Move first() {
        return first;
    }

    public Move getBestMove() {
        return first;
    }

    public Object clone() {
        return new MoveList(this);
    }

    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        Move current = first;
        while (current != null) {
            sbuf.append(current.toString() + " : ");
            current = current.getNext();
        }
        return sbuf.toString();
    }
}
