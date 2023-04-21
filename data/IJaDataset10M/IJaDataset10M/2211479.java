package spacewars.fisica;

import spacewars.principal.*;
import spacewars.misc.*;

public abstract class Forca extends Encaixavel {

    protected Forca(Matriz MatrizTrans) {
        super(MatrizTrans);
    }

    public abstract Vetor getDirecaoNaoTransformada();

    public Vetor getDirecaoTransformada() {
        Vetor v = new Vetor(getDirecaoNaoTransformada());
        this.getMatriz().transform(v);
        return v;
    }

    public abstract Forca transformaForca(Matriz m);
}
