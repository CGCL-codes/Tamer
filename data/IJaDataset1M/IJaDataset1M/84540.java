package org.authorsite.vocab.gui;

import java.util.*;
import org.authorsite.vocab.core.*;
import org.authorsite.vocab.exceptions.*;

/**
 * 
 * @author jejking
 * @version $Revision: 1.2 $
 */
public class VocabMediator {

    private HashMap eventMap;

    private VocabSet vocabSet;

    public VocabMediator(VocabSet vocabSet) throws VocabException {
        this.vocabSet = vocabSet;
        eventMap = new HashMap();
        eventMap.put(VocabEventType.NODE_CREATED, new LinkedList());
        eventMap.put(VocabEventType.NODE_DELETED, new LinkedList());
        eventMap.put(VocabEventType.NODE_EDITED, new LinkedList());
        eventMap.put(VocabEventType.REL_CREATED, new LinkedList());
        eventMap.put(VocabEventType.REL_DELETED, new LinkedList());
        eventMap.put(VocabEventType.SEARCH_RESULTS, new LinkedList());
    }

    public void addListener(VocabEventType eventType, VocabEventListener listener) {
        List list = (List) eventMap.get(eventType);
        list.add(listener);
    }

    public void removeListener(VocabEventType eventType, VocabEventListener listener) {
        List list = (List) eventMap.get(eventType);
        list.remove(listener);
    }

    /**
	 * @return
	 */
    public VocabSet getVocabSet() {
        return vocabSet;
    }

    /**
	 * @param command
	 * @param newNode
	 */
    public void notifyListeners(VocabCommand command, VocabNode node) {
        List listToNotify = (List) eventMap.get(command.getEventType());
        Iterator it = listToNotify.iterator();
        while (it.hasNext()) {
            VocabEventListener listener = (VocabEventListener) it.next();
            listener.nodeEventOccurred(new VocabEvent(this, command.getEventType(), node));
        }
    }

    /**
	 * @param command
	 * @param results
	 */
    public void notifyListeners(VocabCommand command, Set results) {
        List listToNotify = (List) eventMap.get(command.getEventType());
        Iterator it = listToNotify.iterator();
        while (it.hasNext()) {
            VocabEventListener listener = (VocabEventListener) it.next();
            listener.nodeEventOccurred(new VocabEvent(this, command.getEventType(), results));
        }
    }
}
