package it.unibo.is.communication.sharedSpace.space;

import alice.tuprolog.LibraryEvent;
import alice.tuprolog.PrologEventAdapter;
import alice.tuprolog.QueryEvent;
import alice.tuprolog.TheoryEvent;

public class TheoryListener extends PrologEventAdapter {

    public void theoryChanged(TheoryEvent ev) {
        System.out.println("THEORY CHANGED: \n old: \n" + ev.getOldTheory() + "\n new: \n" + ev.getNewTheory());
    }

    public void newQueryResultAvailable(QueryEvent ev) {
        System.out.println("NEW QUERY RESULT AVAILABLE: " + ev.getSolveInfo().getQuery().toString());
    }

    public void libraryLoaded(LibraryEvent ev) {
        System.out.println("NEW LIB loaded: " + ev.getLibraryName());
    }

    public void libraryUnloaded(LibraryEvent ev) {
        System.out.println("LIB unloaded: " + ev.getLibraryName());
    }
}
