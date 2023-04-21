package dialogPackage;

/**
 * Questa e la classe multicaster degli eventi per il supporto dell'interfaccia dialogPackage.ScegliOnorariListenerEventMulticaster.
 */
public class ScegliOnorariListenerEventMulticaster extends java.awt.AWTEventMulticaster implements ScegliOnorariListener {

    /**
 * Constructor per il supporto di eventi multicast.
 * @param a java.util.EventListener
 * @param b java.util.EventListener
 */
    protected ScegliOnorariListenerEventMulticaster(java.util.EventListener a, java.util.EventListener b) {
        super(a, b);
    }

    /**
 * Aggiungere nuovo listener per il supporto di eventi multicast.
 * @return dialogPackage.ScegliOnorariListener
 * @param a dialogPackage.ScegliOnorariListener
 * @param b dialogPackage.ScegliOnorariListener
 */
    public static dialogPackage.ScegliOnorariListener add(dialogPackage.ScegliOnorariListener a, dialogPackage.ScegliOnorariListener b) {
        return (dialogPackage.ScegliOnorariListener) addInternal(a, b);
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
        return new ScegliOnorariListenerEventMulticaster(a, b);
    }

    /**
 *
 * @param newEvent java.util.EventObject
 */
    public void chiudiJButtonAction_actionPerformed(java.util.EventObject newEvent) {
        ((dialogPackage.ScegliOnorariListener) a).chiudiJButtonAction_actionPerformed(newEvent);
        ((dialogPackage.ScegliOnorariListener) b).chiudiJButtonAction_actionPerformed(newEvent);
    }

    /**
 *
 * @return java.util.EventListener
 * @param oldl dialogPackage.ScegliOnorariListener
 */
    protected java.util.EventListener remove(dialogPackage.ScegliOnorariListener oldl) {
        if (oldl == a) return b;
        if (oldl == b) return a;
        java.util.EventListener a2 = removeInternal(a, oldl);
        java.util.EventListener b2 = removeInternal(b, oldl);
        if (a2 == a && b2 == b) return this;
        return addInternal(a2, b2);
    }

    /**
 * Eliminare listener per il supporto di eventi multicast.
 * @return dialogPackage.ScegliOnorariListener
 * @param l dialogPackage.ScegliOnorariListener
 * @param oldl dialogPackage.ScegliOnorariListener
 */
    public static dialogPackage.ScegliOnorariListener remove(dialogPackage.ScegliOnorariListener l, dialogPackage.ScegliOnorariListener oldl) {
        if (l == oldl || l == null) return null;
        if (l instanceof ScegliOnorariListenerEventMulticaster) return (dialogPackage.ScegliOnorariListener) ((dialogPackage.ScegliOnorariListenerEventMulticaster) l).remove(oldl);
        return l;
    }

    /**
 *
 * @param newEvent java.util.EventObject
 */
    public void sceltaJButtonAction_actionPerformed(java.util.EventObject newEvent) {
        ((dialogPackage.ScegliOnorariListener) a).sceltaJButtonAction_actionPerformed(newEvent);
        ((dialogPackage.ScegliOnorariListener) b).sceltaJButtonAction_actionPerformed(newEvent);
    }
}
