package org.eucontract.agents.io;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import org.eucontract.agents.utils.MessageIDGenerator;

public class DummyInputProvider extends Thread implements InputProvider {

    private HashMap<SessionId, Set<Percept>> bdPercepts;

    private HashMap<SessionId, InputProviderListener> bdListeners;

    private Queue<SessionId> quListeners;

    public DummyInputProvider() {
        bdPercepts = new HashMap<SessionId, Set<Percept>>();
        bdListeners = new HashMap<SessionId, InputProviderListener>();
        quListeners = new LinkedList<SessionId>();
    }

    public synchronized Set<Percept> provideInputs(SessionId sid) {
        return bdPercepts.remove(sid);
    }

    public synchronized SessionId registerListener(InputProviderListener ipl) {
        SessionId sid;
        sid = new SessionId(MessageIDGenerator.getMessageID());
        bdListeners.put(sid, ipl);
        return sid;
    }

    public synchronized boolean askForInputs(SessionId sid) {
        quListeners.add(sid);
        return false;
    }

    public void run() {
        Iterator<SessionId> it;
        Set<Percept> sp;
        int n;
        SessionId sid;
        n = 0;
        while (true) {
            try {
                if (n % 5 == 0) {
                    it = bdListeners.keySet().iterator();
                    while (it.hasNext()) {
                        sid = it.next();
                        if (bdListeners.get(sid).receiveInputs(sid, bdPercepts.get(sid))) {
                            bdPercepts.remove(sid);
                        }
                    }
                }
                sleep(200);
                it = bdListeners.keySet().iterator();
                while (it.hasNext()) {
                    sid = it.next();
                    sp = bdPercepts.get(sid);
                    if (sp == null) {
                        sp = new TreeSet<Percept>();
                        bdPercepts.put(sid, sp);
                    }
                    sp.add(new DummyPercept(n));
                }
                n = n + 1;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
