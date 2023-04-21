package edu.gsbme.wasabi.UI.editor.xml.vocab;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Stores a list of words in a tree struct
 * Allows search of suggestion given the starting few letters.
 * 
 * The search is CASE sensitive
 * 
 * @author z2274302
 *
 */
public class VocabTree {

    public static void main(String[] arg) {
        VocabTree tree = new VocabTree();
        tree.insertVocabulary("ABDG");
        tree.insertVocabulary("ABDF");
        tree.insertVocabulary("ABE");
        tree.insertVocabulary("x1");
        tree.insertVocabulary("ABD");
        tree.insertVocabulary("AC");
        tree.insertVocabulary("A");
        tree.insertVocabulary("fsfssf");
        tree.insertVocabulary("x4231");
        ArrayList<VocabNode> word = tree.getTerminatingNodes();
        for (int i = 0; i < word.size(); i++) {
            System.out.println(word.get(i).getNodePathWord());
        }
        System.out.println("");
        String[] suggestion = tree.getSuggestions("X4");
        for (int i = 0; i < suggestion.length; i++) {
            System.out.println(suggestion[i]);
        }
    }

    public HashMap<String, VocabNode> root;

    public VocabTree() {
        root = new HashMap<String, VocabNode>();
    }

    public void insertRoot(VocabNode node) {
        root.put(String.valueOf(node.getVocab()), node);
    }

    public void insertVocabulary(String word) {
        if (root.containsKey(String.valueOf(word.charAt(0)))) {
            VocabNode node = root.get(String.valueOf(word.charAt(0)));
            if (word.length() > 1) node.insertWordSuffix(word.substring(1)); else node.setTerminating(true);
        } else {
            VocabNode node = new VocabNode();
            node.setVocab(word.charAt(0));
            if (word.length() > 1) node.insertWordSuffix(word.substring(1)); else node.setTerminating(true);
            root.put(String.valueOf(node.getVocab()), node);
        }
    }

    public String[] getSuggestions(String combination) {
        ArrayList<String> suggestion = new ArrayList<String>();
        VocabNode node = traverse(combination);
        if (node == null) {
            return new String[0];
        }
        node.findSuggestions(combination, suggestion);
        return suggestion.toArray(new String[suggestion.size()]);
    }

    public String[] getAllVocab() {
        ArrayList<VocabNode> word = this.getTerminatingNodes();
        String[] wordarray = new String[word.size()];
        for (int i = 0; i < word.size(); i++) {
            wordarray[i] = word.get(i).getNodePathWord();
        }
        return wordarray;
    }

    public VocabNode traverse(String path) {
        if (root.containsKey(String.valueOf(path.charAt(0)))) {
            VocabNode node = root.get(String.valueOf(path.charAt(0)));
            if (path.length() == 1) return node;
            return node.traverse(path);
        } else {
            return null;
        }
    }

    public ArrayList<VocabNode> getTerminatingNodes() {
        ArrayList<VocabNode> result = new ArrayList<VocabNode>();
        VocabNode[] childArray = root.values().toArray(new VocabNode[root.values().size()]);
        for (int i = 0; i < childArray.length; i++) {
            childArray[i].findTerminatringNode(result);
        }
        return result;
    }
}
