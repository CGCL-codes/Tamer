package jogo;

import java.util.List;

public abstract class Conteiner extends Item {

    protected int capacidade;

    private List<Pegavel> pegaveis;

    public int getCapacidade() {
        return capacidade;
    }

    public boolean adiciona(Pegavel pegavel) {
        if (pegavel != null) {
            if (getCapacidade() >= getPesoTotal() + ((Item) pegavel).getPeso()) {
                return pegaveis.add(pegavel);
            }
        }
        return false;
    }

    public void remove(Pegavel pegavel) {
    }

    protected List<Pegavel> getPegaveis() {
        return pegaveis;
    }

    protected void setPegaveis(List<Pegavel> pegaveis) {
        this.pegaveis = pegaveis;
    }

    protected void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    private int getPesoTotal() {
        int total = 0;
        for (Pegavel pegavel : pegaveis) {
            total += ((Item) pegavel).getPeso();
        }
        return total;
    }
}
