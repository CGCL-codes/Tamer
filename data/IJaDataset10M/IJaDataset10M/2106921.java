package ucm.si.adhoc;

import ucm.si.basico.ecuaciones.Proposicion;

/**
 *
 * @author Niko, Jose Antonio, Ivan
 */
public class Multiplo extends Proposicion<Integer> {

    int nbase;

    public Multiplo(int nbase) {
        this.nbase = nbase;
    }

    @Override
    public boolean esCierta(Integer s) {
        return s.intValue() % nbase == 0;
    }
}
