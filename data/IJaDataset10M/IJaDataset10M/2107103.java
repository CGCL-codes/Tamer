package br.com.rnavarro.padroes.comportamental.iterator;

public class IteradorDeCanais implements Iterador<Canal> {

    private Canal[] canais;

    private int posicao = 0;

    public IteradorDeCanais(Canal[] canais) {
        this.canais = canais;
    }

    public Canal proximo() {
        Canal canalAtual = canais[posicao];
        posicao++;
        return canalAtual;
    }

    public Canal itemAtual() {
        return canais[posicao];
    }

    public Canal primeiro() {
        return canais[0];
    }

    public boolean temMaisItems() {
        if (posicao >= canais.length || canais[posicao] == null) return false; else return true;
    }
}
