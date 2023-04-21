package FileSE;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import org.junit.Assert;
import org.junit.Test;
import edu.princeton.cs.stdlib.StdRandom;
import edu.princeton.cs.stdlib.Stopwatch;

public class FileSE3Test {

    public void testSeashells() throws IOException {
        FileSE3 e = new FileSE3();
        e.indexFile(".\\bin\\shells.txt");
        System.out.println(e.toString());
        Object[] expecteds = new Object[] { 2 };
        e.search("so");
        Assert.assertArrayEquals(expecteds, e.getResult());
        expecteds = new Object[] { 0 };
        e.search("by the");
        Assert.assertArrayEquals(expecteds, e.getResult());
        expecteds = new Object[] { 3 };
        e.search("i'm shells she sells");
        Assert.assertArrayEquals(expecteds, e.getResult());
    }

    public void testBible() throws IOException {
        FileSE3 e = new FileSE3();
        System.out.println("Indexing the bible...");
        Stopwatch w = new Stopwatch();
        e.indexFile(".\\bin\\bible.txt");
        System.out.print("it took: " + w.elapsedTime() + "\n");
        Object[] expecteds = new Object[] { 30205, 30206, 30228 };
        w = new Stopwatch();
        System.out.println(e.search("number beast"));
        System.out.println("Search time: " + w.elapsedTime());
        System.out.println(Arrays.toString(e.getResult()));
        Assert.assertArrayEquals(expecteds, e.getResult());
        expecteds = new Object[] { 19117, 19276, 23943 };
        w = new Stopwatch();
        System.out.println(e.search("i you he and but for to"));
        System.out.println("Search time: " + w.elapsedTime());
        Assert.assertArrayEquals(expecteds, e.getResult());
        w = new Stopwatch();
        System.out.println(e.search("i am you he will"));
        System.out.println("Search time: " + w.elapsedTime());
        w = new Stopwatch();
        System.out.println(e.search("and if"));
        System.out.println("Search time: " + w.elapsedTime());
        w = new Stopwatch();
        System.out.println(e.search("hell heaven"));
        System.out.println("Search time: " + w.elapsedTime());
    }

    @Test
    public void Test3() throws IOException {
        FileSE3 e = new FileSE3();
        System.out.println("Indexing the 1 mil...");
        Stopwatch w = new Stopwatch();
        e.indexFile(".\\trunk\\FileSE\\one_meelyun_sentences.txt");
        HashMap<String, ArrayList<Integer>> words = (HashMap<String, ArrayList<Integer>>) e.getData();
        FileWriter writer = new FileWriter("output_5.txt");
        for (String s : words.keySet()) if (s.length() == 5) writer.write(s + System.getProperty("line.separator"));
        writer.close();
        System.out.print("it took: " + w.elapsedTime() + "\n");
    }

    /***
	 * Tests queries for three random words   
	 * @throws IOException
	 */
    public void testBibleRandom() throws IOException {
        FileSE3 e = new FileSE3();
        System.out.println("Indexing the bible...");
        Stopwatch w = new Stopwatch();
        e.indexFile(".\\bin\\bible.txt");
        System.out.print("it took: " + w.elapsedTime() + "\n");
        int tests = 1000;
        int wordcount = e.getIndexFileCount();
        ArrayList<Integer> idxs;
        for (int i = 0; i < tests; i++) {
            idxs = new ArrayList<Integer>();
            for (int j = 0; j < 2; j++) {
                idxs.add(StdRandom.uniform(wordcount));
            }
            w = new Stopwatch();
            String[] q = (String[]) e.getWord(idxs);
            String qstr = "";
            for (String s : q) qstr += s + " ";
            System.out.println(e.search(qstr.trim()));
            System.out.println("Search time: " + w.elapsedTime());
        }
    }

    public void testForDuplicates() throws IOException {
        FileSE3 e = new FileSE3();
        System.out.println("Indexing the bible...");
        Stopwatch w = new Stopwatch();
        e.indexFile(".\\bin\\bible.txt");
        System.out.print("it took: " + w.elapsedTime() + "\n");
        HashMap<String, ArrayList<Integer>> data = (HashMap<String, ArrayList<Integer>>) e.getData();
        ArrayList<Node> dups = new ArrayList<Node>();
        int indices = 0;
        for (Entry<String, ArrayList<Integer>> entry : data.entrySet()) {
            ArrayList<Integer> tmp = entry.getValue();
            indices += tmp.size();
            int dcount = 0;
            for (int i = 0; i < tmp.size() - 1; i++) {
                if (tmp.get(i) == tmp.get(i + 1)) dcount++;
            }
            if (dcount > 0) dups.add(new Node(entry.getKey(), dcount));
        }
        Comparator<Node> c = new Comparator<Node>() {

            public int compare(Node o1, Node o2) {
                return o1.value.compareTo(o2.value);
            }
        };
        Node[] entries = new Node[dups.size()];
        dups.toArray(entries);
        Arrays.sort(entries, c);
        int total = 0;
        for (Node dup : entries) {
            System.out.println(dup.key + "," + dup.value + ";");
            total += dup.value;
        }
        System.out.println("Total no of words: " + indices);
        System.out.println("Total no of indices: " + data.size());
        System.out.println("No of duplicates : " + total);
    }

    class Node {

        String key;

        Integer value;

        public Node(String key, Integer value) {
            this.key = key;
            this.value = value;
        }
    }
}
