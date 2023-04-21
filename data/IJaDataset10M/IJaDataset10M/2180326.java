package ucm.si.Checker.modular;

import ucm.si.Checker.modular.defecto.VisitantePorDefectoProposicion;
import java.util.EnumMap;
import ucm.si.Checker.Interprete;
import ucm.si.Checker.Resultado;
import ucm.si.Checker.Visitante;
import ucm.si.Checker.modular.defecto.*;
import ucm.si.Checker.tabulacion.TabulacionFormulas;
import ucm.si.Checker.tabulacion.TabulacionMemSistema;
import ucm.si.basico.ecuaciones.*;

/**
 * Este visitante se compone de otros vmodulos que se encargan de relizar busquedas sobre una operacion en concreto segun se recorre una formula.
 * @author Niko, Jose Antonio, Ivan Antonio
 * @param <S>
 */
public class VisitanteConectorModular<S> extends Visitante<S> {

    private Interprete<S> interprete = null;

    private TabulacionFormulas<S> tabFormulas;

    public enum TFormula {

        PROP, AND, OR, NOT, AX, EX, AU, EU
    }

    ;

    private EnumMap<TFormula, VisitanteFormula<S, Formula>> visitantes = new EnumMap<TFormula, VisitanteFormula<S, Formula>>(TFormula.class);

    private S estado;

    public S getEstado() {
        return estado;
    }

    public void setEstado(S estado) {
        this.estado = estado;
    }

    public VisitanteConectorModular(S estado, Interprete<S> interprete) {
        super();
        this.estado = estado;
        this.interprete = interprete;
        this.tabFormulas = new TabulacionMemSistema<S>();
        this.visitantes.put(TFormula.PROP, new VisitantePorDefectoProposicion(interprete, this));
        this.visitantes.put(TFormula.NOT, new VisitantePorDefectoNot(interprete, this));
        this.visitantes.put(TFormula.OR, new VisitantePorDefectoOr(interprete, this));
        this.visitantes.put(TFormula.AND, new VisitantePorDefectoAnd(interprete, this));
        this.visitantes.put(TFormula.AX, new VisitantePorDefectoAX(interprete, this));
        this.visitantes.put(TFormula.EX, new VisitantePorDefectoEX(interprete, this));
        this.visitantes.put(TFormula.AU, new VisitantePorDefectoAU(interprete, this));
        this.visitantes.put(TFormula.EU, new VisitantePorDefectoEU(interprete, this));
    }

    public void setVisitante(TFormula t, VisitanteFormula<S, Formula> v) {
        this.visitantes.put(t, v);
    }

    public void visita(Proposicion<S> p) {
        if (tabFormulas.tieneEtiqueta(estado, p)) {
            Resultado r = tabFormulas.getResultado(estado, p);
            resParcial.setResultado(r.getResultado());
            resParcial.setContraejemplo(r.getContraejemplo());
            resParcial.setEjemplo(r.getEjemplo());
        } else {
            visitantes.get(TFormula.PROP).visita(p);
            this.resParcial = visitantes.get(TFormula.PROP).getResParcial();
            this.tabFormulas.aniadirEtiqueta(estado, p, resParcial);
        }
    }

    public void visita(Not n) {
        if (tabFormulas.tieneEtiqueta(estado, n)) {
            Resultado r = tabFormulas.getResultado(estado, n);
            resParcial.setResultado(r.getResultado());
            resParcial.setContraejemplo(r.getContraejemplo());
            resParcial.setEjemplo(r.getEjemplo());
        } else {
            visitantes.get(TFormula.NOT).visita(n);
            this.resParcial = visitantes.get(TFormula.NOT).getResParcial();
            this.tabFormulas.aniadirEtiqueta(estado, n, resParcial);
        }
    }

    public void visita(Or or) {
        if (tabFormulas.tieneEtiqueta(estado, or)) {
            Resultado r = tabFormulas.getResultado(estado, or);
            resParcial.setResultado(r.getResultado());
            resParcial.setContraejemplo(r.getContraejemplo());
            resParcial.setEjemplo(r.getEjemplo());
        } else {
            visitantes.get(TFormula.OR).visita(or);
            this.resParcial = visitantes.get(TFormula.OR).getResParcial();
            tabFormulas.aniadirEtiqueta(estado, or, resParcial);
        }
    }

    public void visita(And and) {
        if (tabFormulas.tieneEtiqueta(estado, and)) {
            Resultado r = tabFormulas.getResultado(estado, and);
            resParcial.setResultado(r.getResultado());
            resParcial.setContraejemplo(r.getContraejemplo());
            resParcial.setEjemplo(r.getEjemplo());
        } else {
            visitantes.get(TFormula.AND).visita(and);
            this.resParcial = visitantes.get(TFormula.AND).getResParcial();
            this.tabFormulas.aniadirEtiqueta(estado, and, resParcial);
        }
    }

    public void visita(AX allnext) {
        if (tabFormulas.tieneEtiqueta(estado, allnext)) {
            Resultado r = tabFormulas.getResultado(estado, allnext);
            resParcial.setResultado(r.getResultado());
            resParcial.setContraejemplo(r.getContraejemplo());
            resParcial.setEjemplo(r.getEjemplo());
        } else {
            visitantes.get(TFormula.AX).visita(allnext);
            this.resParcial = visitantes.get(TFormula.AX).getResParcial();
            this.tabFormulas.aniadirEtiqueta(estado, allnext, resParcial);
        }
    }

    public void visita(EX eventx) {
        if (tabFormulas.tieneEtiqueta(estado, eventx)) {
            Resultado r = tabFormulas.getResultado(estado, eventx);
            resParcial.setResultado(r.getResultado());
            resParcial.setContraejemplo(r.getContraejemplo());
            resParcial.setEjemplo(r.getEjemplo());
        } else {
            visitantes.get(TFormula.EX).visita(eventx);
            this.resParcial = visitantes.get(TFormula.EX).getResParcial();
            this.tabFormulas.aniadirEtiqueta(estado, eventx, resParcial);
        }
    }

    public void visita(AU au) {
        if (tabFormulas.tieneEtiqueta(estado, au)) {
            Resultado r = tabFormulas.getResultado(estado, au);
            resParcial.setResultado(r.getResultado());
            resParcial.setContraejemplo(r.getContraejemplo());
            resParcial.setEjemplo(r.getEjemplo());
        } else {
            visitantes.get(TFormula.AU).visita(au);
            this.resParcial = visitantes.get(TFormula.AU).getResParcial();
            this.tabFormulas.aniadirEtiqueta(estado, au, resParcial);
        }
    }

    public void visita(EU eu) {
        if (tabFormulas.tieneEtiqueta(estado, eu)) {
            Resultado r = tabFormulas.getResultado(estado, eu);
            resParcial.setResultado(r.getResultado());
            resParcial.setContraejemplo(r.getContraejemplo());
            resParcial.setEjemplo(r.getEjemplo());
        } else {
            visitantes.get(TFormula.EU).visita(eu);
            this.resParcial = visitantes.get(TFormula.EU).getResParcial();
            this.tabFormulas.aniadirEtiqueta(estado, eu, resParcial);
        }
    }
}
