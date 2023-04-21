package dmc;

import static org.math.io.files.ASCIIFile.readDoubleArray;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

/**
 * Programa de classificação de padrões através de a implementação do algoritmo
 * DMC (Dynamic Matrix Control).
 * @author Samir Coutinho Costa
 * samirfor@gmail.com
 */
public class Main {

    /**
     * Executa o programa.
     * @param args
     */
    public static double run() {
        BancoDados treinamento = new BancoDados();
        BancoDados teste = new BancoDados();
        ArrayList<Integer> indicesSorteados = new ArrayList<Integer>();
        double[][] atributos = readDoubleArray(new File("iris.data"));
        boolean foiTreinado, foiTestado;
        Random numeroRandom = new Random();
        int indiceAleatorio;
        final int TAMTREINO = 120;
        final int TAMTESTE = 30;
        for (int i = 0; i < TAMTREINO; ) {
            foiTreinado = false;
            indiceAleatorio = numeroRandom.nextInt(150);
            for (int j = 0; j < indicesSorteados.size(); j++) {
                if (indiceAleatorio == indicesSorteados.get(j)) {
                    foiTreinado = true;
                    break;
                }
            }
            if (!foiTreinado) {
                Padrao padrao = new Padrao(atributos[indiceAleatorio][0], atributos[indiceAleatorio][1], atributos[indiceAleatorio][2], atributos[indiceAleatorio][3], Classificacao.SETOSA);
                if (atributos[indiceAleatorio][4] == 1.0) {
                    padrao.setTipo(Classificacao.SETOSA);
                    treinamento.add(padrao);
                } else if (atributos[indiceAleatorio][4] == 2.0) {
                    padrao.setTipo(Classificacao.VERSICOLOR);
                    treinamento.add(padrao);
                } else {
                    padrao.setTipo(Classificacao.VIRGINICA);
                    treinamento.add(padrao);
                }
                indicesSorteados.add(indiceAleatorio);
                i++;
            }
        }
        DMC dmc = new DMC(treinamento);
        System.out.println("\n=============================\n");
        System.out.println("TESTE:");
        for (int i = 0; i < TAMTESTE; ) {
            foiTestado = false;
            indiceAleatorio = numeroRandom.nextInt(150);
            for (int j = 0; j < indicesSorteados.size(); j++) {
                if (indiceAleatorio == indicesSorteados.get(j)) {
                    foiTestado = true;
                    break;
                }
            }
            if (!foiTestado) {
                System.out.println("\nPlanta " + (i + 1) + ":");
                Classificacao tipo = dmc.classificar(atributos[indiceAleatorio][0], atributos[indiceAleatorio][1], atributos[indiceAleatorio][2], atributos[indiceAleatorio][3]);
                Padrao padrao = new Padrao(atributos[indiceAleatorio][0], atributos[indiceAleatorio][1], atributos[indiceAleatorio][2], atributos[indiceAleatorio][3], tipo);
                System.out.println("\tCS: " + padrao.getComprimentoSepala() + " LS: " + padrao.getLarguraSepala() + " CP: " + padrao.getComprimentoPetala() + " LP: " + padrao.getLarguraPetala() + "\n\tTipo: " + padrao.getTipo());
                teste.add(padrao);
                indicesSorteados.add(indiceAleatorio);
                i++;
            }
        }
        System.out.println("\n=============================\n");
        double acertos = 0;
        double tipo = 0;
        for (int i = 0; i < teste.size(); i++) {
            Padrao padrao = teste.getPadrao(i);
            if (padrao.getTipo() == Classificacao.SETOSA) {
                tipo = 1.0;
            } else if (padrao.getTipo() == Classificacao.VERSICOLOR) {
                tipo = 2.0;
            } else if (padrao.getTipo() == Classificacao.VIRGINICA) {
                tipo = 3.0;
            }
            for (int j = 0; j < 150; j++) {
                if (padrao.getComprimentoSepala() == atributos[j][0] && padrao.getLarguraSepala() == atributos[j][1] && padrao.getComprimentoPetala() == atributos[j][2] && padrao.getLarguraPetala() == atributos[j][3] && tipo == atributos[j][4]) {
                    acertos++;
                    break;
                }
            }
        }
        double percentual = (acertos / teste.size()) * 100;
        System.out.println("Acertos: " + acertos);
        System.out.println("Tamanho do teste: " + teste.size());
        System.out.println("\nPercentual de Acertos: " + percentual + "%");
        return percentual;
    }
}
