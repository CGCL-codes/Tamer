package subsearch.index.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class UBTree implements Serializable {

    private static final long serialVersionUID = 1;

    public static final Comparator NODE_ELEMENT_COMPARATOR = new Comparator() {

        public int compare(Object o1, Object o2) {
            Comparable element1 = o1 instanceof Node ? ((Node) o1).getElement() : (Comparable) o1;
            Comparable element2 = o2 instanceof Node ? ((Node) o2).getElement() : (Comparable) o2;
            return element1.compareTo(element2);
        }
    };

    private ArrayList<Node> roots;

    public UBTree() {
        roots = new ArrayList<Node>();
    }

    public void insert(Collection<Comparable> set, int id) {
        LinkedList<Comparable> sortedSet = new LinkedList<Comparable>(set);
        Collections.sort(sortedSet);
        insertInto(roots, sortedSet, id);
    }

    private void printSet(LinkedList<Comparable> set) {
        for (Comparable c : set) {
            System.out.print(c);
            System.out.print(" ");
        }
        System.out.println();
    }

    private void insertInto(ArrayList<Node> candidates, LinkedList<Comparable> rightSubSet, int id) {
        Comparable element = rightSubSet.pollFirst();
        int pos = Collections.binarySearch(candidates, element, NODE_ELEMENT_COMPARATOR);
        Node targetNode;
        if (pos >= 0) {
            targetNode = candidates.get(pos);
        } else {
            targetNode = new Node(element);
            candidates.add(-pos - 1, targetNode);
        }
        if (rightSubSet.isEmpty()) {
            targetNode.endSet(id);
        } else {
            insertInto(targetNode.getSons(), rightSubSet, id);
        }
    }

    private void testSorting(List<Node> c) {
        for (int i = 1; i < c.size(); i++) {
            if (c.get(i - 1).getElement().compareTo(c.get(i).getElement()) >= 0) {
                System.out.println("Wrong Sequence");
            }
        }
    }

    public LinkedList<Integer> findSuperSets(LinkedList<Comparable> set) {
        Collections.sort(set);
        LinkedList<Integer> result = new LinkedList<Integer>();
        findSuperSetsIn(roots, set, result);
        return result;
    }

    public void findSuperSetsIn(ArrayList<Node> candidates, LinkedList<Comparable> set, LinkedList<Integer> result) {
        Comparable element = set.peekFirst();
        for (Node node : candidates) {
            int compareTo = node.getElement().compareTo(element);
            if (compareTo < 0) {
                findSuperSetsIn(node.getSons(), set, result);
            } else if (compareTo == 0) {
                set.pollFirst();
                if (set.isEmpty()) {
                    result.addAll(node.getSetEndings());
                    collectSubtreeSetEndings(node.getSons(), result);
                } else {
                    findSuperSetsIn(node.getSons(), set, result);
                }
                set.offerFirst(element);
            } else {
                break;
            }
        }
    }

    private void collectSubtreeSetEndings(ArrayList<Node> candidates, LinkedList<Integer> result) {
        for (Node n : candidates) {
            result.addAll(n.getSetEndings());
            if (n.hasSons()) collectSubtreeSetEndings(n.getSons(), result);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        List<Node> level1 = roots;
        List<Node> level2 = new LinkedList<Node>();
        while (!level1.isEmpty()) {
            for (Node n : level1) level2.addAll(n.getSons());
            sb.append(level1.size() + "\n");
            level1 = level2;
            level2 = new LinkedList<Node>();
        }
        return sb.toString();
    }

    public class Node implements Serializable {

        private static final long serialVersionUID = 1;

        private Comparable element;

        private ArrayList<Node> sons;

        private ArrayList<Integer> setEndings;

        public Node(Comparable element) {
            if (element instanceof String) {
                element = ((String) element).intern();
            }
            this.element = element;
        }

        public boolean hasSons() {
            return sons != null;
        }

        public ArrayList<Node> getSons() {
            if (sons == null) this.sons = new ArrayList<Node>(0);
            return sons;
        }

        public void endSet(int id) {
            getSetEndings().add(id);
        }

        public boolean hasSetEndings() {
            return setEndings != null;
        }

        public ArrayList<Integer> getSetEndings() {
            if (setEndings == null) setEndings = new ArrayList<Integer>(0);
            return setEndings;
        }

        public Comparable getElement() {
            return element;
        }

        public String toString() {
            return element + " (" + (sons == null ? 0 : sons.size()) + ", " + (setEndings == null ? 0 : setEndings.size()) + ")";
        }
    }
}
