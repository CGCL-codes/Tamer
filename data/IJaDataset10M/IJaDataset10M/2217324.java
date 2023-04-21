package exemplo_10;

import java.util.List;
import java.util.Vector;
import java.util.Arrays;
import java.util.Collections;

public class Algoritmos {

    private String[] cores = { "vermelho", "branco", "amarelo", "azul" };

    private List<String> list;

    private Vector<String> vector = new Vector<String>();

    public Algoritmos() {
        list = Arrays.asList(cores);
        vector.add("preto");
        vector.add("vermelho");
        vector.add("verde");
        System.out.println("Antes de addAll, vector contém: ");
        for (String s : vector) System.out.printf("%s ", s);
        Collections.addAll(vector, cores);
        System.out.println("\n\nDepois de addAll, vector contém: ");
        for (String s : vector) System.out.printf("%s ", s);
        int frequencia = Collections.frequency(vector, "vermelho");
        System.out.printf("\n\nA frequência de vermelho é: %d\n", frequencia);
        boolean disjoint = Collections.disjoint(list, vector);
        System.out.printf("\nlist e vector %s elementos em comum\n", (disjoint ? "não têm" : "têm"));
    }

    public static void main(String args[]) {
        new Algoritmos();
    }
}
