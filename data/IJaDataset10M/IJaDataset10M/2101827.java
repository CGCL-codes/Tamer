package knn;

/**
 * Implementação do algoritmo KNN (K-Nearest Neighbors Algorithm).
 * @author Samir Coutinho Costa <samirfor@gmail.com>
 */
public class KNN {

    private BancoDados treinamento;

    /**
     * @param treinamento
     */
    public KNN(BancoDados treinamento) {
        this.treinamento = treinamento;
    }

    /**
     * Faz a classificação de acordo com os atributos sorteados.
     * @param comprimentoSepala
     * @param larguraSepala
     * @param comprimentoPetala
     * @param larguraPetala
     * @return
     */
    public Classificacao classificar(double comprimentoSepala, double larguraSepala, double comprimentoPetala, double larguraPetala) {
        final int K = 5;
        double distancia = 0;
        int setosa = 0, versicolor = 0, virginica = 0;
        Lista lista = new Lista();
        Valor valor;
        for (int i = 0; i < treinamento.size(); i++) {
            Padrao padrao = treinamento.getPadrao(i);
            distancia = Math.pow(comprimentoSepala - padrao.getComprimentoSepala(), 2) + Math.pow(larguraSepala - padrao.getLarguraSepala(), 2) + Math.pow(comprimentoPetala - padrao.getComprimentoPetala(), 2) + Math.pow(larguraPetala - padrao.getLarguraPetala(), 2);
            distancia = Math.sqrt(distancia);
            valor = new Valor(distancia, padrao.getTipo());
            lista.add(valor);
        }
        lista.ordenar();
        for (int i = 0; i < K; i++) {
            Classificacao tipo = lista.getValor().get(i).getTipo();
            if (tipo.equals(Classificacao.SETOSA)) {
                setosa++;
            } else if (tipo.equals(Classificacao.VERSICOLOR)) {
                versicolor++;
            } else if (tipo.equals(Classificacao.VIRGINICA)) {
                virginica++;
            }
        }
        System.out.println("\tK =>");
        System.out.println("\t\tSetosas:      " + setosa);
        System.out.println("\t\tVersicolores: " + versicolor);
        System.out.println("\t\tVirginicas:   " + virginica);
        if (setosa >= versicolor && setosa >= virginica) {
            return Classificacao.SETOSA;
        } else if (versicolor >= setosa && versicolor >= virginica) {
            return Classificacao.VERSICOLOR;
        } else if (virginica >= setosa && virginica >= versicolor) {
            return Classificacao.VIRGINICA;
        }
        return null;
    }
}
