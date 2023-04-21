package org.jmlspecs.samples.dirobserver;

import org.jmlspecs.models.*;

/** An object that keeps directory observers (i.e., a subject). */
public interface DirObserverKeeper extends JMLType {

    boolean inNotifier();

    void register(DirObserver o);

    void unregister(DirObserver o);
}
