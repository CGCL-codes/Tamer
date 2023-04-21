package dialogPackage;

/**
 * Questa e la classe multicaster degli eventi per il supporto dell'interfaccia dialogPackage.ScegliStatoListenerEventMulticaster.
 */
public class ScegliStatoListenerEventMulticaster extends java.awt.AWTEventMulticaster implements dialogPackage.ScegliStatoListener {

    /**
 * Constructor per il supporto di eventi multicast.
 * @param a java.util.EventListener
 * @param b java.util.EventListener
 */
    protected ScegliStatoListenerEventMulticaster(java.util.EventListener a, java.util.EventListener b) {
        super(a, b);
    }

    /**
 * Aggiungere nuovo listener per il supporto di eventi multicast.
 * @return dialogPackage.ScegliStatoListener
 * @param a dialogPackage.ScegliStatoListener
 * @param b dialogPackage.ScegliStatoListener
 */
    public static dialogPackage.ScegliStatoListener add(dialogPackage.ScegliStatoListener a, dialogPackage.ScegliStatoListener b) {
        return (dialogPackage.ScegliStatoListener) addInternal(a, b);
    }

    /**
 * Aggiungere nuovo listener per il supporto di eventi multicast.
 * @return java.util.EventListener
 * @param a java.util.EventListener
 * @param b java.util.EventListener
 */
    protected static java.util.EventListener addInternal(java.util.EventListener a, java.util.EventListener b) {
        if (a == null) return b;
        if (b == null) return a;
        return new ScegliStatoListenerEventMulticaster(a, b);
    }

    /**
 *
 * @return java.util.EventListener
 * @param oldl dialogPackage.ScegliStatoListener
 */
    protected java.util.EventListener remove(dialogPackage.ScegliStatoListener oldl) {
        if (oldl == a) return b;
        if (oldl == b) return a;
        java.util.EventListener a2 = removeInternal(a, oldl);
        java.util.EventListener b2 = removeInternal(b, oldl);
        if (a2 == a && b2 == b) return this;
        return addInternal(a2, b2);
    }

    /**
 * Eliminare listener per il supporto di eventi multicast.
 * @return dialogPackage.ScegliStatoListener
 * @param l dialogPackage.ScegliStatoListener
 * @param oldl dialogPackage.ScegliStatoListener
 */
    public static dialogPackage.ScegliStatoListener remove(dialogPackage.ScegliStatoListener l, dialogPackage.ScegliStatoListener oldl) {
        if (l == oldl || l == null) return null;
        if (l instanceof ScegliStatoListenerEventMulticaster) return (dialogPackage.ScegliStatoListener) ((dialogPackage.ScegliStatoListenerEventMulticaster) l).remove(oldl);
        return l;
    }

    /**
 *
 * @param newEvent java.util.EventObject
 */
    public void sceltaJButtonAction_actionPerformed(java.util.EventObject newEvent) {
        ((dialogPackage.ScegliStatoListener) a).sceltaJButtonAction_actionPerformed(newEvent);
        ((dialogPackage.ScegliStatoListener) b).sceltaJButtonAction_actionPerformed(newEvent);
    }
}
