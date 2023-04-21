package tad;

public class OperacoesListaApontadores implements InterfaceLista {

    private static final int NIL = -1;

    public void flVazia(Lista l) {
        l.ultimoApontador = l.primeiroApontador;
        l.primeiroApontador.prox = NIL;
    }

    public void imprime(Lista l) {
    }

    public void insere(Item x, Lista l) {
        l.ultimoApontador = x;
        l.ultimoApontador.prox = NIL;
    }

    public Item retira(int pos, Lista l) {
        return null;
    }

    public boolean vazia(Lista l) {
        return l.primeiroApontador.equals(l.ultimoApontador);
    }
}
