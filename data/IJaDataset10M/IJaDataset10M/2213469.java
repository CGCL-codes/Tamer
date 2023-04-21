package dendrarium.trees.terminals;

import dendrarium.trees.TerminalNode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reprezentuje graf terminali dla danego lasu skladniowego.
 * 
 * Wierzcholkami sa liczby naturalne bedace wartosciami atrybutow
 * from i to wezlow drzewa, skierowanymi krawedziami sa terminale
 * (terminal prowadzi od swojego from do swojego to).
 * 
 * Graf terminali sluzy do znalezienia tekstowej reprezentacji
 * kawalka zdania dla danego wezla. Wtedy szuka sie najkrotszej
 * sciezki w tym grafie od from do to tego wezla i wypisuje
 * etykiety terminali z tej sciezki.
 * 
 * ZAKLADAMY, ze jest to graf bez cykli majacy wierzcholek poczatkowy
 * (taki, ze wszedzie sie da dojsc) oraz koncowy (taki, ze zewszad da
 * sie do niego dojsc) oraz ze dla kazdego node istnieje jakas sciezka
 * od jego from do jego to w tym grafie.
 *
 * @author Piotr Achinger <piotr.achinger at gmail.com>
 */
public class TerminalGraph {

    /**
     * Mapa numer wierzcholka -> wierzcholek
     */
    private Map<Integer, TerminalGraphNode> nodes = new HashMap();

    /**
     * Numer poczatkowego wierzcholka.
     *
     * Wartosc -1 oznacza brak.
     */
    private int firstNode = -1;

    /**
     * Numer koncowego wierzcholka.
     *
     * Wartosc -1 oznacza brak.
     */
    private int lastNode = -1;

    /**
     * Dodanie kolejnego wezla-terminala (UWAGA: wezly (Node) sa przejsciami,
     * a nie wierzcholkami, w grafie terminali).
     *
     * Polega to na dodaniu, jesli trzeba, nowych wierzcholkow o numerach
     * node.from i node.to i dodaniu krawedzi od jednego do drugiego
     * powiazanej z danym terminalem.
     *
     * SPOSOB TWORZENIA grafu terminali:
     *
     * 1) TerminalGraph g = new TerminalGraph();
     * 2) g.add(node_1); ... ; g.add(node_k); // dla wszystkich terminali w lesie
     * 3) g.postConstruct();
     *
     */
    public void add(TerminalNode node) {
        int from = node.getFrom();
        int to = node.getTo();
        if (!nodes.containsKey(from)) {
            addNode(from);
        }
        if (!nodes.containsKey(to)) {
            addNode(to);
        }
        TerminalGraphNode src = nodes.get(from);
        TerminalGraphNode dest = nodes.get(to);
        src.addTransition(dest, node);
        if (to == firstNode || firstNode == -1) {
            firstNode = from;
        }
        if (from == lastNode || lastNode == -1) {
            lastNode = to;
        }
    }

    /**
     * Metoda wywolywana po ostatnim add.
     *
     * Tworzy dane o najkrotszych sciezkach itp w grafie terminali.
     */
    public void postConstruct() {
        this.generateLinearExtension();
        this.updateShortestPaths();
    }

    /**
     * Sortuje topologicznie, nadajac numery zgodne z porzadkiem.
     */
    public void generateLinearExtension() {
        nodes.get(firstNode).generateLinearExtension();
    }

    /**
     * Aktualizuje dane w wierzcholkach o kierunkach najkrotszych sciezek.
     */
    public void updateShortestPaths() {
        nodes.get(firstNode).updateShortestPaths();
    }

    /**
     * Tworzy nowy wezel o danym numerze i dodaje do mapy numer->wezel.
     */
    private void addNode(int id) {
        TerminalGraphNode node = new TerminalGraphNode(id);
        nodes.put(id, node);
    }

    /**
     * Najkrotsza sciezka (lista przejsc, czyli wezlow-terminali) od
     * from do to.
     */
    public List<TerminalNode> getShortestPath(int from, int to) {
        if (!nodes.containsKey(from)) {
            from = firstNode;
        }
        if (!nodes.containsKey(to)) {
            to = lastNode;
        }
        List<TerminalNode> ret = null;
        try {
            ret = nodes.get(from).shortestPathTo(to);
        } catch (NullPointerException ex) {
            throw new RuntimeException("MAXIMAL FUCKUP AT " + from + " -> " + to + ", first=" + firstNode + ", last=" + lastNode, ex);
        }
        return ret;
    }

    /**
     * Najkrotsza sciezka (lista przejsc, czyli wezlow-terminali) od
     * from do wezla koncowego.
     */
    public List<TerminalNode> getShortestPathFrom(int from) {
        return getShortestPath(from, lastNode);
    }

    /**
     * Najkrotsza sciezka (lista przejsc, czyli wezlow-terminali) od
     * wezla poczatkowego do to.
     */
    public List<TerminalNode> getShortestPathTo(int to) {
        return getShortestPath(firstNode, to);
    }
}
