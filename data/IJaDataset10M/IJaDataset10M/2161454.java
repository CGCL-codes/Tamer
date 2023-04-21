package ucm.si.Checker;

import java.util.Iterator;
import java.util.List;
import ucm.si.Checker.modular.VisitanteConectorModular;
import ucm.si.Checker.util.Roseta;
import ucm.si.basico.ecuaciones.And;
import ucm.si.basico.ecuaciones.Formula;

/**
 *
 * @author Niko, Jose Antonio, Ivan
 */
public class DefaultModelChecker<S> implements ModelChecker<S> {

    private InterpreteWrapper<S> interprete;

    public Resultado chequear(Formula formula) {
        Resultado<S> parcial = new Resultado<S>(Resultado.COD_MAYBET);
        List<S> iniciales = interprete.iniciales();
        VisitanteConectorModular<S> v;
        boolean seguir = true;
        Iterator<S> it = iniciales.iterator();
        while (seguir && it.hasNext()) {
            S e = it.next();
            v = new VisitanteConectorModular<S>(e, interprete);
            formula.accept(v);
            parcial = v.getResParcial();
            if (!parcial.equals(Resultado.COD_TRUE)) seguir = false;
        }
        return parcial;
    }

    public DefaultModelChecker() {
    }

    public Resultado chequear(Interprete<S> interprete, Formula formula, S estado) {
        S s = estado;
        this.interprete = new InterpreteWrapper<S>(interprete);
        if (estado == null) s = this.interprete.iniciales().get(0);
        VisitanteConectorModular<S> v = new VisitanteConectorModular<S>(estado, this.interprete);
        formula.accept(v);
        return v.getResParcial();
    }

    public Resultado chequear(Interprete<S> interprete, Formula formula) {
        this.interprete = new InterpreteWrapper<S>(interprete);
        Resultado<S> parcial = new Resultado<S>(Resultado.COD_MAYBET);
        List<S> iniciales = this.interprete.iniciales();
        VisitanteConectorModular<S> v;
        boolean seguir = true;
        Iterator<S> it = iniciales.iterator();
        while (seguir && it.hasNext()) {
            S e = it.next();
            v = new VisitanteConectorModular<S>(e, this.interprete);
            formula.accept(v);
            parcial = v.getResParcial();
            if (!parcial.equals(Resultado.COD_TRUE)) seguir = false;
        }
        return parcial;
    }

    public Roseta<S> getRoseta() {
        return this.interprete.getRoseta();
    }
}
