package projeto.roteador;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Classe que representa o vetor de distancia.
 * @author Camilla Falconi
 * @author Danilo Torres
 *
 */
public class VetorDistancia {

    public static final int INFINITY = 50;

    public Map<Integer, Integer> valores;

    /**
	 * Constutor da classe.
	 */
    public VetorDistancia() {
        valores = Collections.synchronizedMap(new HashMap<Integer, Integer>());
    }

    /**
	 * Recupera o custo at� um roteador.
	 * @param key O id do roteador destino.
	 * @return O custo at� um roteador.
	 */
    public int getValor(int key) {
        return valores.containsKey(key) ? valores.get(key) : INFINITY;
    }

    /**
	 * Atualiza o custo at� um roteador.
	 * @param key O id do roteador destino.
	 * @param valor O novo valor do custo.
	 */
    public void setValor(int key, int valor) {
        valores.put(key, valor);
    }

    /**
	 * Recupera o conjunto de roteadores de destino.
	 * @return O conjunto de roteadores de destino.
	 */
    public Set<Integer> getDestinos() {
        return valores.keySet();
    }

    /**
	 * Recupera informa��es formatadas do vetor de dist�ncia.
	 * @return String com informa��es formatadas dos valores do vetor de distancia.
	 */
    public String toString() {
        String result = "";
        for (Integer key : valores.keySet()) {
            result += key + ":" + valores.get(key) + "\t";
        }
        return result;
    }

    /**
	 * Remove todas as entradas do vetor dist�ncia.
	 */
    public void clear() {
        valores.clear();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof VetorDistancia)) return false;
        return valores.equals(((VetorDistancia) obj).valores);
    }

    /**
	 * Remove um destino do vetor dist�ncia.
	 * @param id O id do roteador destino a ser removido.
	 */
    public void delete(int id) {
        valores.remove(id);
    }

    public VetorDistancia clone() {
        VetorDistancia v = new VetorDistancia();
        for (Integer key : valores.keySet()) {
            v.setValor(key, getValor(key));
        }
        return v;
    }
}
