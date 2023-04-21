package dk.kapetanovic.jaft.log.out;

import dk.kapetanovic.jaft.action.Action;

public interface Outputter {

    /** TODO Method for getting size (number of actions written)? 
	 *  Setter method for Journal -> setJournal(Journal)? */
    void writeAction(Action action);

    void writeCommit();

    void writeUndo(int actionIndex);

    boolean checkError();

    void restoreError();

    void flush();

    void close();

    void mark();

    void restore();
}
