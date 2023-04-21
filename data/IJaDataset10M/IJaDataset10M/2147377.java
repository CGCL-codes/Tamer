package org.codegallery.javagal.concurrent.cpj;

public abstract class JoinedPair extends Box {

    protected Box fst;

    protected Box snd;

    protected JoinedPair(Box a, Box b) {
        fst = a;
        snd = b;
    }

    public synchronized void flip() {
        Box tmp = fst;
        fst = snd;
        snd = tmp;
    }
}
