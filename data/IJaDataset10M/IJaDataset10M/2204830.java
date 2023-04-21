package util;

import java.util.*;
import Jplay.GameImage;

/**
 * Classe auxiliar, Multimap.
 * 
 * @author Gefersom C. Lima
 * 
 */
public class MultiMap {

    ArrayList<List<GameImage>> myMultiMap;

    /**
	 * Construtor
	 */
    public MultiMap() {
        myMultiMap = new ArrayList<List<GameImage>>(Constantes.QUANT_PECAS_MULTIMAP);
        init();
    }

    /**
	 * Inicia o ArrayList.
	 */
    private void init() {
        for (int i = 0; i < Constantes.QUANT_PECAS_MULTIMAP; i++) myMultiMap.add(i, null);
    }

    /**
	 * Retorna true se o objeto passado como par�metro existe no multimap.
	 * 
	 * @param key
	 *            - objeto a ser procurado.
	 * @return true, se encontrou, false ao contr�rio.
	 */
    public boolean containsKey(Object key) {
        for (int i = 0; i < Constantes.QUANT_PECAS_MULTIMAP; i++) {
            List<GameImage> lista = myMultiMap.get(i);
            if (lista != null) for (int j = 0; j < lista.size(); j++) if (lista.get(j) == key) return true;
        }
        return false;
    }

    /**
	 * Retorna um lista de pe�as.
	 * 
	 * @param tipoPeca
	 *            - Tipo de pe�a a ser retornada.
	 * @return List - cont�m as pe�as a serem retornada, caso o tipo de pe�a n�o
	 *         exista retorna null.
	 */
    public List<GameImage> get(int tipoPeca) {
        List<GameImage> lista = null;
        try {
            lista = myMultiMap.get(calcIndice(tipoPeca));
        } catch (Exception e) {
            return null;
        }
        return lista;
    }

    /**
	 * Calcula o �ndice dentro do ArrayList do tipo de pe�a passado como
	 * par�metro.
	 * 
	 * @param tipoPeca
	 *            - Tipo de pe�a
	 * @return int - �ndice da pe�a no ArrayList.
	 */
    private int calcIndice(int tipoPeca) {
        return tipoPeca % Constantes.QUANT_PECAS_MULTIMAP;
    }

    /**
	 * Adiciona uma pe�a.
	 * 
	 * @param tipoPeca
	 *            - Tipo de pe�a a ser adicionada.
	 * @param gim
	 *            - Imagem que representa a pe�a
	 */
    public void add(int tipoPeca, GameImage gim) {
        int indice = calcIndice(tipoPeca);
        if (myMultiMap.get(indice) == null) myMultiMap.add(indice, new LinkedList<GameImage>());
        myMultiMap.get(indice).add(gim);
    }

    /**
	 * Remove uma pe�a.
	 * 
	 * @param key
	 *            - Pe�a a ser removida.
	 */
    public void remove(Object key) {
        myMultiMap.remove(key);
    }

    /**
	 * Remove uma pe�a do tabuleiro.
	 * 
	 * @param tipoPeca
	 *            - Tipo de pe�a a ser removida.
	 * @param value
	 *            - �ndice da pe�a a ser removida.
	 */
    public void remove(int tipoPeca, Object value) {
        int indice = calcIndice(tipoPeca);
        List<GameImage> values = myMultiMap.get(indice);
        if (values == null || values.contains(value) == false) return;
        values.remove(value);
    }

    /**
	 * Deleta todas as pe�as .
	 */
    public void clear() {
        myMultiMap.clear();
        init();
    }
}
