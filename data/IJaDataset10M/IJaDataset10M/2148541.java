package abstrasy;

import abstrasy.interpreter.InterpreterException;
import abstrasy.interpreter.StdErrors;
import java.util.Vector;

/**
 * Abstrasy Interpreter
 *
 * Copyright : Copyright (c) 2006-2012, Luc Bruninx.
 *
 * Concédée sous licence EUPL, version 1.1 uniquement (la «Licence»).
 *
 * Vous ne pouvez utiliser la présente oeuvre que conformément à la Licence.
 * Vous pouvez obtenir une copie de la Licence à l’adresse suivante:
 *
 *   http://www.osor.eu/eupl
 *
 * Sauf obligation légale ou contractuelle écrite, le logiciel distribué sous
 * la Licence est distribué "en l’état", SANS GARANTIES OU CONDITIONS QUELLES
 * QU’ELLES SOIENT, expresses ou implicites.
 *
 * Consultez la Licence pour les autorisations et les restrictions
 * linguistiques spécifiques relevant de la Licence.
 *
 *
 * @author Luc Bruninx
 * @version 1.0
 */
public class InterpreterSemaphore {

    public InterpreterSemaphore() {
    }

    private static final int PASSIVE_WAITING_TIME = 50;

    private static boolean mutex_lock = false;

    private static boolean signalBreak = false;

    private static boolean signalEnd = false;

    private Vector threads = new Vector();

    /**
     * Peut-on continuer l'exécution ou y a t-il un signal d'arrêt brutal ?
     *
     * @return
     */
    private final boolean canContinue() {
        return !(signalBreak || signalEnd);
    }

    /**
     * Object de synchronisation mutex global:
     * ---------------------------------------
     * Plutot que de verrouiller tout le InterpreterSemaphore, permet d'affiner la granularité du mutex.
     */
    private static Object mutex_object = new Object();

    /**
     * Verrouillage mutex...
     *
     * Utilisé par l'opérateur mutex pour introduire une section critique (mutual exclude).
     *
     * @throws Exception
     */
    public final void lockMutex() throws Exception {
        Interpreter myself = (Interpreter) Thread.currentThread();
        synchronized (mutex_object) {
            while (mutex_lock && threads.size() > 1 && canContinue() && (!myself.isThreadRaising())) {
                mutex_object.wait(PASSIVE_WAITING_TIME);
            }
            mutex_lock = true;
        }
        myself.actor_LOCKMUTEX();
    }

    /**
     * Déverrouillage mutex...
     *
     * Utilisé par l'opérateur mutex pour libérer une section critique.
     */
    public final void unlockMutex() {
        Interpreter.mySelf().actor_UNLOCKMUTEX();
        synchronized (mutex_object) {
            mutex_lock = false;
            mutex_object.notify();
        }
    }

    /**
     * Verrouillage (lock node{...})...
     *
     * Utilisé par l'opérateur lock pour introduire une section critique.
     *
     * @throws Exception
     */
    public final void lockNode(Node node) throws Exception {
        Interpreter myself = (Interpreter) Thread.currentThread();
        Exception exc = null;
        synchronized (node) {
            myself.threadlock_R_add(node);
            try {
                while (node.islock(myself) && threads.size() > 1 && canContinue() && (!myself.isThreadRaising())) {
                    myself.thowsDeadlock(node.getlock_by());
                    node.wait(PASSIVE_WAITING_TIME);
                }
            } catch (Exception e) {
                exc = e;
            }
            myself.threadlock_R_remove(node);
            if (exc == null) {
                node.lock(myself);
            }
        }
        if (exc == null) {
            myself.actor_LOCKSECTION();
        } else {
            throw exc;
        }
    }

    /**
     * Déverrouillage (lock node{...})...
     *
     * Utilisé par l'opérateur lock pour libérer une section critique.
     */
    public final void unlockNode(Node node) throws Exception {
        ((Interpreter) Thread.currentThread()).actor_UNLOCKSECTION();
        synchronized (node) {
            node.unlock((Interpreter) Thread.currentThread());
            node.notify();
        }
    }

    /**
     * Réveille l'acteur thread...
     *
     * Depuis REV6251(31/01/2011): Ne fragmente plus le verrouillage du semaphore.
     * resume n'est pas bloquant. Il peut donc être utilisé dans une section critique.
     *
     * @param thread
     * @throws Exception
     */
    public void resume(Interpreter thread) throws Exception {
        thread.actor_SIGNAL();
        Object obj = thread.thread_getSuspendLock();
        synchronized (obj) {
            obj.notify();
        }
    }

    /**
     * L'acteur en cours suspend son activité et attend qu'on le réveille
     *
     * Depuis REV6251(31/01/2011): Ne fragmente plus le verrouillage du semaphore.
     * suspend est un opérateur bloquant. Il ne peut pas être utilisé dans une section
     * atomique (section critique).
     *
     * @throws Exception
     */
    public void suspendAndWaitResume() throws Exception {
        Interpreter myself = (Interpreter) Thread.currentThread();
        Object obj = myself.thread_getSuspendLock();
        while (!myself.actor_CONSUMESIGNAL() && !myself.isThreadRaising() && canContinue()) {
            synchronized (obj) {
                myself.actor_SUSPEND();
                if (!myself.actor_HASSIGNALS()) {
                    obj.wait(PASSIVE_WAITING_TIME);
                }
            }
        }
        myself.actor_RESUME();
    }

    /**
     * L'acteur en cours suspend son activité et attend qu'on le réveille comme suspendAndWaitResume().
     * Toutefois, ici, un délai d'attente maximum est déterminé.  Si ce délai est dépassé, retourne true.
     *
     * On notera aussi que cette méthode est sûr. Si le signal n'est pas capturé avant le dépassement
     * de temps, le signal reste dans la file d'attente. Il pourra être récupéré lors d'un prochain
     * suspend.
     *
     * @throws Exception
     */
    public boolean suspendAndWaitResume(long millis) throws Exception {
        Interpreter myself = (Interpreter) Thread.currentThread();
        Object obj = myself.thread_getSuspendLock();
        long base = System.currentTimeMillis();
        long now = 0;
        long delay;
        while (((delay = millis - now) > 0) && !myself.actor_CONSUMESIGNAL() && !myself.isThreadRaising() && canContinue()) {
            synchronized (obj) {
                myself.actor_SUSPEND();
                if (!myself.actor_HASSIGNALS()) {
                    obj.wait(Math.min(delay, PASSIVE_WAITING_TIME));
                }
            }
            now = System.currentTimeMillis() - base;
        }
        myself.actor_RESUME();
        return (delay <= 0);
    }

    /**
     * Réception d'un message
     *
     * Depuis REV6251(31/01/2011): Ne fragmente plus le verrouillage du semaphore.
     * receive est un opérateur bloquant. Il ne peut donc pas être utilisé dans une section critique.
     *
     * @return
     * @throws Exception
     */
    public Node receive_MSG() throws Exception {
        Interpreter myself = (Interpreter) Thread.currentThread();
        Node msg = null;
        Object obj = myself.thread_getSuspendLock();
        while ((msg = myself.actor_GETMSG()) == null && (!myself.isThreadRaising()) && canContinue()) {
            synchronized (obj) {
                myself.actor_SUSPEND();
                if (!myself.actor_HASMSG()) {
                    obj.wait(PASSIVE_WAITING_TIME);
                }
            }
        }
        myself.actor_RESUME();
        return msg;
    }

    /**
     * Réception d'un message avec délai d'expiration de l'attente.
     *
     * Notez que cette méthode est transactionnelle. Si le délai est écoulé et qu'un message arrive juste à ce moment
     * là, celui-ci reste dans la file d'attente et pourra être récupéré ultérieurement par le prochain receive.
     *
     * @return
     * @throws Exception
     */
    public Node receive_MSG(long millis) throws Exception {
        Interpreter myself = (Interpreter) Thread.currentThread();
        Node msg = null;
        Object obj = myself.thread_getSuspendLock();
        long base = System.currentTimeMillis();
        long now = 0;
        long delay;
        while (((delay = millis - now) > 0) && (msg = myself.actor_GETMSG()) == null && (!myself.isThreadRaising()) && canContinue()) {
            synchronized (obj) {
                myself.actor_SUSPEND();
                if (!myself.actor_HASMSG()) {
                    obj.wait(Math.min(delay, PASSIVE_WAITING_TIME));
                }
            }
            now = System.currentTimeMillis() - base;
        }
        if (msg == null && delay <= 0) {
            myself.setTimeOutRaising(true);
        }
        myself.actor_RESUME();
        return msg;
    }

    /**
     * Envoyer un le message 'msg' à l'acteur 'thread'.
     *
     * Depuis REV6251(31/01/2011): Ne fragmente plus le verrouillage du semaphore.
     * send n'est pas bloquant. Il peut donc être utilisé dans une section critique.
     *
     * @param thread
     * @param msg
     * @throws Exception
     */
    public void send_MSG(Interpreter thread, Node msg) throws Exception {
        thread.actor_PUTMSG(msg);
        Object obj = thread.thread_getSuspendLock();
        synchronized (obj) {
            obj.notify();
        }
    }

    /**
     * Suspendre son activité pendant 'millis' ms.
     *
     * Depuis REV6251(31/01/2011): Ne fragmente plus le verrouillage du semaphore.
     * sleep est un opérateur bloquant. Il ne peut pas être utilisé dans une section critique.
     *
     * @param millis
     * @throws Exception
     */
    public void sleep(long millis) throws Exception {
        Interpreter myself = (Interpreter) Thread.currentThread();
        long base = System.currentTimeMillis();
        long now = 0;
        long delay;
        Object obj = myself.thread_getSuspendLock();
        while (((delay = millis - now) > 0) && (!myself.isThreadRaising()) && canContinue()) {
            synchronized (obj) {
                myself.actor_SUSPEND();
                obj.wait(Math.min(delay, PASSIVE_WAITING_TIME));
            }
            now = System.currentTimeMillis() - base;
        }
        myself.actor_RESUME();
    }

    /**
     * Tuer l'acteur 'thread'...
     *
     * @param thread
     * @throws Exception
     */
    public synchronized void kill(Interpreter thread) throws Exception {
        if (thread != null) {
            thread.actor_STOP();
            thread.actor_SIGNAL();
            Interpreter myself = (Interpreter) Thread.currentThread();
            long base = System.currentTimeMillis();
            long now = 0;
            long millis = 100;
            int i = 30;
            if (thread == myself) {
                throw new InterpreterException(StdErrors.extend(StdErrors.Thread_error, "a thread can not kill himself"));
            }
            Object obj = myself.thread_getSuspendLock();
            while (((millis - now) > 0) && (!myself.isThreadRaising()) && canContinue() && isThreadsExists(thread)) {
                synchronized (obj) {
                    notifyAll();
                    wait(1);
                }
                now = System.currentTimeMillis() - base;
            }
        }
    }

    public synchronized void reset() {
        endAllCoroutines();
        signalBreak = false;
        signalEnd = false;
        mutex_lock = false;
    }

    public synchronized void setSignalBreak(boolean signalBreak) {
        this.signalBreak = signalBreak;
    }

    boolean isSignalBreak() {
        return signalBreak;
    }

    public synchronized void setSignalEnd(boolean signalEnd) {
        this.signalEnd = signalEnd;
    }

    boolean isSignalEnd() {
        return signalEnd;
    }

    public void registerThread(Interpreter corout) {
        synchronized (threads) {
            boolean fnd = false;
            for (int i = 0; i < threads.size(); ) {
                if (fnd = (corout == (Interpreter) threads.elementAt(i++))) {
                    i = threads.size();
                }
            }
            if (!fnd) {
                threads.addElement(corout);
            }
        }
    }

    public void unregisterThread(Interpreter corout) {
        Interpreter myself = Interpreter.mySelf();
        synchronized (threads) {
            if (myself.actor_ISMUTEX()) {
                this.unlockMutex();
            }
            threads.remove(corout);
        }
    }

    private Interpreter getRegistered() {
        synchronized (threads) {
            Interpreter myself = Interpreter.mySelf();
            Interpreter inter = null;
            for (int i = 0; i < threads.size(); i++) {
                inter = (Interpreter) threads.elementAt(i);
                if (inter == myself) {
                    inter = null;
                } else {
                    i = threads.size();
                }
            }
            return inter;
        }
    }

    public synchronized String getUniqueThreadName() {
        String res = null;
        while (res == null) {
            res = "actor/" + java.util.UUID.randomUUID().toString();
            if (isThreadsExists(res)) {
                res = null;
            }
        }
        return res;
    }

    public Node getACTORS() throws Exception {
        synchronized (threads) {
            Node res = Node.createEmptyList();
            Interpreter inter = null;
            for (int i = 0; i < threads.size(); i++) {
                inter = (Interpreter) threads.elementAt(i);
                if (inter.isInterThread()) {
                    res.addElement(new Node(inter.getName()));
                }
            }
            return res;
        }
    }

    public synchronized Vector getThreadsNames() {
        Vector res = new Vector();
        Interpreter inter = null;
        for (int i = 0; i < threads.size(); i++) {
            inter = (Interpreter) threads.elementAt(i);
            if (inter.isInterThread()) {
                res.addElement(inter.getName());
            }
        }
        return res;
    }

    public synchronized Interpreter getThread(String name) {
        Interpreter res = null;
        Interpreter inter = null;
        for (int i = 0; i < threads.size(); i++) {
            inter = (Interpreter) threads.elementAt(i);
            if (inter.getName().equals(name)) {
                res = inter;
                i = threads.size();
            }
        }
        return res;
    }

    public synchronized boolean isThreadsExists(String name) {
        Interpreter inter = null;
        boolean res = false;
        for (int i = 0; i < threads.size(); i++) {
            inter = (Interpreter) threads.elementAt(i);
            if (inter.isInterThread()) {
                if (res = (inter.getName().equals(name))) {
                    i = threads.size();
                }
            }
        }
        return res;
    }

    public synchronized boolean isThreadsExists(Interpreter thread) {
        Interpreter inter = null;
        boolean res = false;
        for (int i = 0; i < threads.size(); i++) {
            inter = (Interpreter) threads.elementAt(i);
            if (inter.isInterThread()) {
                if (res = (inter == thread)) {
                    i = threads.size();
                }
            }
        }
        return res;
    }

    public static long interThreadUID = 0;

    public boolean hasOtherRegistered() {
        return threads.size() > 1;
    }

    public boolean hasRegistered() {
        return threads.size() > 0;
    }

    public void endAllCoroutines() {
        Interpreter myself = Interpreter.mySelf();
        if (!myself.isInterThread()) {
            setSignalEnd(true);
            Interpreter corout = null;
            while ((corout = getRegistered()) != null) {
                Interpreter.Log("Asking thread interruption [" + corout.getName() + " : " + corout.getId() + "] by " + myself.getName() + "<" + myself.getId() + ">...");
                corout.actor_STOP();
                corout.interrupt();
                try {
                    Thread.currentThread().sleep(100);
                } catch (Exception ex) {
                    if (Interpreter.isDebugMode()) {
                        ex.printStackTrace();
                    }
                }
                if (corout.isInterrupted()) {
                    Interpreter.Log(" ->  thread [" + corout.getName() + " : " + corout.getId() + "] Interrupted.");
                    unregisterThread(corout);
                }
                if (!corout.isAlive()) {
                    unregisterThread(corout);
                    Interpreter.Log(" ->  thread [" + corout.getName() + " : " + corout.getId() + "] Died.");
                }
            }
        } else {
            Interpreter.Log("Asking thread interruption from InterThread !!!...");
        }
    }
}
