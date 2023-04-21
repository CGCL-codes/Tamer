package spacewars.principal;

import spacewars.misc.*;

public class Explosao extends Encaixavel {

    float raio;

    float rnd = (float) Math.random();

    long instanteExplosao = System.currentTimeMillis();

    public Explosao(Matriz posicao, float raio) {
        super(posicao);
        this.raio = raio;
    }

    public Explosao(Peca pai, Matriz posicao, float raio) {
        this(posicao, raio);
        setPai(pai);
    }

    public float getRaio() {
        return this.raio;
    }

    public float getRandom() {
        return this.rnd;
    }

    public long getInstanteExplosao() {
        return instanteExplosao;
    }
}
