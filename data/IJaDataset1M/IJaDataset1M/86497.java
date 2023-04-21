package de.mnit.basis.swt.dnd;

import de.mnit.basis.swt.element.steuer.A_SWT_Steuer;

/**
 * @author Michael Nitsche
 * 13.06.2010	Erstellt
 */
public abstract class A_DND_QuelleStd extends A_DND_Quelle {

    public A_DND_QuelleStd(S_DND... dnd) {
        super(dnd);
    }

    public boolean zielOk(A_SWT_Steuer<?, ?> e) {
        return true;
    }

    public void abschluss(DNDTYP typ, DNDART art, A_SWT_Steuer<?, ?> e) {
    }
}
