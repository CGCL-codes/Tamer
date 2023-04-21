package galronnlp.pcfg.parser;

import galronnlp.util.SortedLinkedList;
import galronnlp.util.Symbol;

/**
 * The Agenda class for the CKY parser
 *
 * This is based on the Agenda as implemented in Chris Brew's python
 * Statistical CKY parser
 *
 * @author Daniel A. Galron
 */
public class Agenda {

    private SortedLinkedList<AgendaEntry> agenda = new SortedLinkedList<AgendaEntry>();

    /** Creates a new instance of Agenda */
    public Agenda() {
    }

    /**
     * Push a new edge on the agenda
     */
    public void push(int i, int j, Symbol LHS, EntryTriple RHS) {
        agenda.insort(new AgendaEntry(i, j, LHS, RHS));
    }

    /**
     * Return the next edge in the agenda
     */
    public AgendaEntry pop() {
        AgendaEntry ret = agenda.removeHighest();
        return ret;
    }

    /**
     * @return the number of items left on the agenda
     */
    public int size() {
        return agenda.size();
    }
}
