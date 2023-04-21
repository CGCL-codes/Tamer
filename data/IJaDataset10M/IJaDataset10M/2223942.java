package de.mnit.basis.swt.anw;

import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Shell;
import de.mnit.basis.daten.typ.Klasse;
import de.mnit.basis.fehler.Fehler;
import de.mnit.basis.fehler.system.F_Sys_InOut;
import de.mnit.basis.swt.dialog.SWT_Fehler;
import de.mnit.basis.swt.element.steuer.rollbar.gruppe.bild.deko.SWT_Fenster;
import de.mnit.basis.swt.element.steuer.rollbar.gruppe.bild.deko.S_Fenster;
import de.mnit.basis.sys.event.S_Extern;
import de.mnit.basis.sys.event.S_Zuhoerer;

/**
 * @author Michael Nitsche
 */
public abstract class A_MN_Fenster<TA extends A_MN_Fenster<?>> extends A_MN_Gruppe<TA> {

    private static final String E_SCHLIESSEN = "SCHLIESSEN";

    private static final String E_BEREIT = "BEREIT";

    private S_Extern<?, Boolean> endefenster_extern = null;

    public static void start(Class<? extends A_MN_Fenster<?>> start) {
        start(start, (Object[]) null);
    }

    public static void start(Class<? extends A_MN_Fenster<?>> start, Object... parameter) {
        if (parameter == null) parameter = new Object[0];
        try {
            final A_MN_Fenster<?> instanz = Klasse.nInstanz(start, parameter);
            ((SWT_Fenster) instanz.fenster(instanz)).start(new S_Zuhoerer<Object>() {

                public void verarbeite(Object o) {
                    instanz.zuhoererStart(E_BEREIT, null);
                }
            });
        } catch (Throwable t) {
            SWT_Fehler.zeig(t, true);
        }
    }

    public static void start(Class<? extends A_MN_Gruppe<?>> start, int dx, int dy, Object... parameter) {
        try {
            Fehler.sicherheit.da_Verboten("start(Class) verwenden!");
        } catch (Throwable t) {
            SWT_Fehler.zeig(t, true);
        }
    }

    public void oeffnen() {
        this.oeffnen(null);
    }

    public void oeffnen(Shell parent) {
        try {
            A_MN_Fenster<?> instanz = this;
            S_Fenster<?> fenster = instanz.fenster(instanz);
            fenster.oeffnen(parent, new S_Zuhoerer<Object>() {

                public void verarbeite(Object o) {
                    t.zuhoererStart(E_BEREIT, null);
                }
            });
        } catch (F_Sys_InOut e) {
            throw Fehler.zeig(e, true);
        }
    }

    public void schliessen() {
        t.zuhoererStart(E_SCHLIESSEN);
        ((Shell) t.gFenster().swt()).dispose();
    }

    public void eBereit(S_Zuhoerer<?> z) {
        this.zuhoererPlus(E_BEREIT, z);
    }

    public void eSchliessen(S_Zuhoerer<?> z) {
        this.zuhoererPlus(E_SCHLIESSEN, z);
    }

    public void eEndeFenster(final S_Extern<?, Boolean> e) {
        if (this.endefenster_extern != null) Fehler.sonstige.da_Verboten("Wurde bereits gesetzt!");
        endefenster_extern = e;
    }

    protected final void initIntern() {
        endeFenster((Shell) t.gFenster().swt(), this.endefenster_extern);
    }

    protected void endeFenster(Shell shell, final S_Extern<?, Boolean> z) {
        ((Shell) t.gFenster().swt()).addShellListener(new ShellAdapter() {

            public void shellClosed(ShellEvent event) {
                Boolean erg = null;
                if (z != null) erg = z.verarbeite(null);
                if (erg != null && erg == false) event.doit = false;
                t.zuhoererStart(E_SCHLIESSEN);
            }
        });
    }

    protected abstract S_Fenster<?> fenster(A_MN_Gruppe<?> instanz) throws F_Sys_InOut;
}
