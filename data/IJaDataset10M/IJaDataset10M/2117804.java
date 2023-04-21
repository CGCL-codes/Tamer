package endereco.repositorio;

import endereco.entidades.Endereco;

public class IteratorEnderecosArray implements IteratorEnderecos {

    public IteratorEnderecosArray(Endereco[] colecao) {
        this.colecao = colecao;
        this.indice = 0;
    }

    private Endereco[] colecao;

    private int indice;

    public Endereco[] getColecao() {
        return colecao;
    }

    public void setColecao(Endereco[] colecao) {
        this.colecao = colecao;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

    public boolean hasNext() {
        boolean retorno = false;
        if (this.indice < this.colecao.length) {
            retorno = true;
        }
        return retorno;
    }

    public Endereco next() {
        Endereco retorno = null;
        if (this.hasNext()) {
            this.indice++;
            retorno = this.colecao[indice];
        }
        return retorno;
    }
}
