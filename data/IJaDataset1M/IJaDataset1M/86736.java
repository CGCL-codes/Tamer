package spacewars.piloto;

import spacewars.fisica.*;
import spacewars.misc.*;

public class ForcaControlada extends Forca {

    protected VetorControlado Direcao;

    public ForcaControlada(Matriz TransfAplicacao, VetorControlado Direcao) {
        super(TransfAplicacao);
        this.Direcao = Direcao;
    }

    public Vetor getDirecaoNaoTransformada() {
        return this.Direcao.getDirecao();
    }

    public Forca transformaForca(Matriz m) {
        Matriz m2 = new Matriz(m);
        m2.mul(MatrizTrans);
        return new ForcaControlada(m2, Direcao);
    }
}
