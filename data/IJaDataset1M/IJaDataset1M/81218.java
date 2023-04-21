package test.preprocess.util;

import java.util.Iterator;
import java.io.File;
import core.preprocess.analyzation.trie.Trie;

public class TrieTest {

    public static void main(String[] args) throws Exception {
        TrieTest test = new TrieTest();
        if (args.length != 1) {
            System.out.println("invalid parameters!");
        }
        test.serializeTest(new File(args[0]));
    }

    public void commonTest() throws Exception {
        String[] words = { "the", "they", "bahia", "cocoa", "zone", "alleviating", "the", "drought", "since", "early", "January", "and", "imporving", "prospects" };
        Trie trie = new Trie();
        for (int i = 0; i != words.length; i++) {
            trie.add(words[i]);
        }
        System.out.println(trie.size());
        trie.traverse();
        System.out.println();
        for (int i = 0; i < words.length; i++) {
            System.out.print(trie.getId(words[i]) + ",");
        }
        System.out.println();
        System.out.println();
        for (Iterator<String> it = trie.iterator(); it.hasNext(); ) {
            it.hasNext();
            System.out.println(it.next());
        }
        System.out.println();
        System.out.println("trie.getWord(5) = " + trie.getWord(5));
        System.out.println();
        String[] wordsNew = { "the", "they", "cocoa", "alleviating", "drought", "since", "and", "imporving" };
        Trie trieNew = new Trie();
        for (int i = 0; i != wordsNew.length; i++) {
            trieNew.add(wordsNew[i]);
        }
        System.out.println(trie.difference(trieNew));
    }

    public void deletionTest() throws Exception {
        String[] words = { "the", "they", "th", "t", "abc", "o", "the", "theee", "th", "opt", "theeo" };
        Trie trie = new Trie();
        for (int i = 0; i != words.length; i++) {
            trie.add(words[i]);
        }
        trie.traverse();
        System.out.println();
        for (int i = 0; i < words.length; i++) {
            System.out.print("deleting " + words[i] + ": ");
            if (trie.delete(words[i], true)) {
                System.out.print("succeed!");
            } else System.out.print("failed!!!!");
            System.out.println();
            trie.traverse();
            System.out.println();
        }
    }

    public void serializeTest(File outputDir) throws Exception {
        String[] words = { "the", "they", "bahia", "cocoa", "zone", "alleviating", "the", "drought", "since", "early", "January", "and", "imporving", "prospects" };
        Trie trie = new Trie();
        for (int i = 0; i != words.length; i++) {
            trie.add(words[i]);
        }
        trie.traverse();
        System.out.println();
        trie.serialize(new File(outputDir, "trie.txt"));
        Trie newTrie = new Trie();
        newTrie.deserializeFrom(new File(outputDir, "trie.txt"), new int[] { 3 });
        newTrie.rearrangeId();
        newTrie.traverse();
    }

    public void rearrangeIdTest() throws Exception {
        String[] words = { "the", "they", "bahia", "cocoa", "zone", "alleviating", "the", "drought", "since", "early", "January", "and", "imporving", "prospects" };
        Trie trie = new Trie();
        for (int i = 0; i != words.length; i++) {
            trie.add(words[i]);
        }
        trie.traverse();
        System.out.println();
        trie.rearrangeId();
        trie.traverse();
    }
}
