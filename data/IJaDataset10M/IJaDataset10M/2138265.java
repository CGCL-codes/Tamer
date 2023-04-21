package org.lilian.models.markov;

import org.lilian.corpora.*;
import org.lilian.util.Functions;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * 
 */
public class TrieMarkovModel<T> extends AbstractMarkovModel<T> {

    private Node root = new Node(null, null);

    private List<Integer> totals = new ArrayList<Integer>();

    public TrieMarkovModel(int order) {
        super(order);
        while (totals.size() < order + 2) totals.add(0);
    }

    @Override
    protected void add(List<T> token) {
        totals.set(token.size() - 1, totals.get(token.size() - 1) + 1);
        root.add(token.iterator());
    }

    @Override
    public double distinct(int order) {
        return root.numAncestors(order);
    }

    @Override
    public double frequency(List<T> tokens) {
        checkOrder(tokens.size());
        return root.getCount(tokens.iterator());
    }

    /**
	 * Returns the total number of encountered ngrams of a given length 
	 * @param length
	 * @return
	 */
    public double total(int order) {
        checkOrder(order);
        return totals.get(order - 1);
    }

    public void writeResults(File directory, String baseName) throws IOException {
    }

    /**
	 * Returns an iterator over all n grams 
	 */
    public Iterator<List<T>> iterator() {
        return iterator(order());
    }

    /**
	 * Returns an iterator over all encountered n-grams
	 */
    public Iterator<List<T>> iterator(int n) {
        return new MMIterator(n);
    }

    public String toString() {
        return root.toString();
    }

    private class Node implements Serializable {

        private double count = 0.0;

        private int depth;

        private T token;

        private Node parent = null;

        private Map<T, Node> children = new LinkedHashMap<T, Node>();

        public Node(T token, Node parent) {
            if (parent != null) this.depth = parent.depth + 1; else this.depth = 0;
            this.token = token;
            this.parent = parent;
        }

        /**
		 * Adds all the tokens from index onwards, to children of 
		 * this node. 
		 * 
		 * If tokens contains [a, b, c], a is added as a child of this 
		 * node (if it does not already exist) and b to a child 
		 * of that node and so on. 
		 */
        public void add(Iterator<T> iterator) {
            if (!iterator.hasNext()) {
                count++;
                return;
            }
            Node child;
            T nextToken = iterator.next();
            if (children.containsKey(nextToken)) child = children.get(nextToken); else {
                child = new Node(nextToken, this);
                children.put(nextToken, child);
            }
            child.add(iterator);
        }

        /**
		 * Returns the frequency of the ngram that this node represents
		 * 
		 * (Ie. the number of times the sequence of tokens from the root 
		 * to this node has been seen so far) 
		 * 
		 */
        public double getCount() {
            return count;
        }

        public Node getParent() {
            return parent;
        }

        /**
		 * This method recursively finds the frequency of the sequence 
		 * represented by the remainder of this iterator from this node, or
		 *  one of its children.
		 */
        public double getCount(Iterator<T> iterator) {
            if (!iterator.hasNext()) return count;
            if (!hasChildren()) return 0.0;
            T token = iterator.next();
            if (!children.containsKey(token)) return 0.0;
            return children.get(token).getCount(iterator);
        }

        public boolean hasChildren() {
            return (children.size() != 0);
        }

        public Map<T, Node> getChildMap() {
            return children;
        }

        /** 
		 * Retrieves the child of this node that sits at the end of the
		 * path represented by the iterator
		 */
        public Node getChild(Iterator<T> it) {
            if (!it.hasNext()) return this;
            T token = it.next();
            if (!children.containsKey(token)) return null;
            Node n = children.get(token);
            return n.getChild(it);
        }

        public int getDepth() {
            return depth;
        }

        public T getToken() {
            return token;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < depth; i++) sb.append("-");
            sb.append(token + "\n");
            for (Map.Entry<T, Node> me : children.entrySet()) sb.append(me.getValue());
            return sb.toString();
        }

        /**
		 * Returns the number of ancestors at the exact given depth 
		 *  
		 * @param depth
		 * @return
		 */
        public double numAncestors(int depth) {
            if (depth == 0) return 1;
            double sum = 0;
            for (Node child : children.values()) sum += child.numAncestors(depth - 1);
            return sum;
        }
    }

    /**
	 * This is the iterator that MarkovModel.iterator() returns. It moves 
	 * through the tree and returns all possible ngrams of a given order 
	 */
    private class MMIterator implements Iterator<List<T>> {

        private int depth;

        private long baseModifications = state();

        private Vector<Node> nodeStack = new Vector<Node>();

        private Vector<Iterator<Node>> itStack = new Vector<Iterator<Node>>();

        private Vector<T> tokenStack = new Vector<T>();

        public boolean hasNext = true;

        public MMIterator(int depth) {
            if (depth < 1 || depth > order()) throw new IllegalArgumentException("Cannot create an iterator for depth " + depth + ".");
            this.depth = depth;
            nodeStack.add(root);
            tokenStack.add(root.getToken());
            tokenStack.add(null);
            itStack.add(root.getChildMap().values().iterator());
            iterate();
        }

        public boolean hasNext() {
            checkMod();
            return hasNext;
        }

        public List<T> next() {
            checkMod();
            if (!hasNext) throw new NoSuchElementException();
            Vector<T> result = new Vector<T>(depth + 1);
            result.addAll(tokenStack.subList(1, tokenStack.size()));
            iterate();
            return result;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        private void iterate() {
            tokenStack.remove(tokenStack.size() - 1);
            while (true) {
                if (nodeStack.size() >= depth && itStack.get(0).hasNext()) break;
                if (nodeStack.size() <= 0) break;
                if (itStack.get(0).hasNext()) {
                    Node n = itStack.get(0).next();
                    nodeStack.insertElementAt(n, 0);
                    itStack.insertElementAt(n.getChildMap().values().iterator(), 0);
                    tokenStack.add(n.getToken());
                } else {
                    nodeStack.remove(0);
                    itStack.remove(0);
                    tokenStack.remove(tokenStack.size() - 1);
                }
            }
            if (nodeStack.size() <= 0) hasNext = false; else {
                Node n = itStack.get(0).next();
                tokenStack.add(n.getToken());
            }
        }

        private void checkMod() {
            if (baseModifications != state()) throw new ConcurrentModificationException();
        }
    }
}
