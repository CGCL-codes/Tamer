package visugraph.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * <p>Cette classe implémente un tas binaire. L'implémentation repose
 * sur l'utilisation d'un tableau.</p>
 *
 * <p>A noter que ce type de tas n'est pas celui qui offre les meilleures performances.
 * Mais il est cependant très simple à implémenter et peut s'avérer légèrement meilleure
 * pour de petits ensembles.</p>
 *
 * <p>Ce tas offre les complexités suivantes :</p>
 * <ul>
 *  <li>Insertion : O(log(n))</li>
 *  <li>Accès à la racine : O(1)</li>
 *  <li>Suppression du minimum : O(log(n))</li>
 *  <li>Changement du poids d'un élément : O(log(n))</li>
 * </u>
 */
public class BinaryHeap<T> implements Heap<T> {

    private Map<T, HeapCell> data;

    private List<HeapCell> heap;

    /**
	 * Créer un nouveau tas.
	 */
    public BinaryHeap() {
        this.heap = new ArrayList<HeapCell>();
        this.data = new HashMap<T, HeapCell>();
        this.heap.add(null);
    }

    /**
	 * {@inheritDoc}
	 */
    public void add(T elem, double weight) {
        if (!this.data.containsKey(elem)) {
            int pos = this.heap.size();
            HeapCell cell = new HeapCell(elem, weight, pos);
            this.heap.add(pos, cell);
            this.data.put(elem, cell);
            this.siftUp(pos);
        } else {
            throw new IllegalArgumentException("Cet élément est déjà dans le tas !");
        }
    }

    /**
	 * {@inheritDoc}
	 */
    public void decrease(T elem, double newWeight) {
        HeapCell cell = this.getHeapCell(elem);
        if (cell.weight >= newWeight) {
            cell.weight = newWeight;
            this.siftUp(cell.position);
        } else {
            throw new IllegalArgumentException("Le nouveau poids doit être plus petit que l'ancien");
        }
    }

    /**
	 * {@inheritDoc}
	 */
    public double getWeight(T elem) {
        return this.getHeapCell(elem).weight;
    }

    /**
	 * {@inheritDoc}
	 */
    public double getRootWeight() {
        if (!this.data.isEmpty()) {
            return this.heap.get(1).weight;
        } else {
            throw new NoSuchElementException("Aucun élément dans le tas");
        }
    }

    /**
	 * {@inheritDoc}
	 */
    public T popRoot() {
        if (this.data.isEmpty()) {
            throw new NoSuchElementException("Aucun élément dans le tas");
        }
        T root = this.getRoot();
        int lastPos = this.heap.size() - 1;
        this.swap(1, lastPos);
        this.heap.remove(lastPos);
        this.data.remove(root);
        this.siftDown(1);
        return root;
    }

    /**
	 * {@inheritDoc}
	 */
    public boolean contains(Object elem) {
        return this.data.containsKey(elem);
    }

    /**
	 * {@inheritDoc}
	 */
    public T getRoot() {
        if (!this.data.isEmpty()) {
            return this.heap.get(1).elem;
        } else {
            throw new NoSuchElementException("Aucun élément dans le tas");
        }
    }

    /**
	 * {@inheritDoc}
	 */
    public int size() {
        return this.data.size();
    }

    /**
	 * {@inheritDoc}
	 */
    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    /**
	 * {@inheritDoc}
	 */
    public boolean remove(T elem) {
        if (this.contains(elem)) {
            this.decrease(elem, Double.NEGATIVE_INFINITY);
            this.popRoot();
            return true;
        } else {
            return false;
        }
    }

    /**
	 * {@inheritDoc}
	 */
    public void clear() {
        this.data.clear();
        this.heap.clear();
        this.heap.add(null);
    }

    /**
	 * Déplace un élément vers le haut jusqu'à ce que la structure de tas soit
	 * respectée.
	 * @param elemPos position de l'élément à déplacer.
	 */
    private void siftUp(int elemPos) {
        if (elemPos <= 1) {
            return;
        }
        int parPos = elemPos >> 1;
        double valueNode = this.heap.get(elemPos).weight;
        double valuePar = this.heap.get(parPos).weight;
        if (valuePar > valueNode) {
            this.swap(elemPos, parPos);
            this.siftUp(parPos);
        }
    }

    /**
	 * Déplace un élément vers le bas jusqu'à ce que la structure de tas soit
	 * respectée.
	 * @param elemPos position de l'élément à déplacer.
	 */
    private void siftDown(int elemPos) {
        int childLeftPos = elemPos << 1;
        int childRightPos = childLeftPos + 1;
        if (childLeftPos < this.heap.size()) {
            return;
        }
        double valueNode = this.heap.get(elemPos).weight;
        HeapCell childLeft = this.heap.get(childLeftPos);
        HeapCell childRight = childRightPos < this.size() ? this.heap.get(childRightPos) : null;
        HeapCell bestChild = childLeft.lessThan(childRight) ? childLeft : childRight;
        int bestPos = bestChild.position;
        if (bestChild.weight < valueNode) {
            this.swap(elemPos, bestPos);
            this.siftDown(bestPos);
        }
    }

    /**
	 * Echange 2 éléments dans le tas. Les positions des cellules
	 * seront mises à jour suite à l'appel de cette méthode.
	 * @param pos1 position de l'élément 1
	 * @param pos2 position de l'élément 2
	 */
    private void swap(int pos1, int pos2) {
        HeapCell cell1 = this.heap.get(pos1);
        HeapCell cell2 = this.heap.get(pos2);
        this.heap.set(pos2, cell1);
        this.heap.set(pos1, cell2);
        cell1.position = pos2;
        cell2.position = pos1;
    }

    /**
	 * Retourne la cellule associée à l'élément.
	 * @param elem élément dont on veut la cellule
	 * @return la cellule
	 * @throws IllegalArgumentException si l'élément n'est pas dans le tas"
	 */
    private HeapCell getHeapCell(T elem) {
        if (this.data.containsKey(elem)) {
            return this.data.get(elem);
        } else {
            throw new NoSuchElementException("L'élément n'est pas dans ce tas.");
        }
    }

    /**
	 * Classe qui stocke les infos d'un élément dans le tas
	 * comme sa position ou son poids.
	 */
    private class HeapCell {

        public T elem;

        public double weight;

        public int position;

        public HeapCell(T elem, double weight, int position) {
            this.elem = elem;
            this.weight = weight;
            this.position = position;
        }

        public boolean lessThan(HeapCell cell) {
            return cell == null || this.weight < cell.weight;
        }

        public String toString() {
            return "elem=" + elem + ":" + weight + ",pos=" + position;
        }
    }
}
