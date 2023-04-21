package org.progeeks.wn;

import java.io.*;
import java.util.*;
import com.phoenixst.plexus.*;
import net.didion.jwnl.*;
import net.didion.jwnl.data.*;
import net.didion.jwnl.data.list.*;
import net.didion.jwnl.data.relationship.*;
import net.didion.jwnl.dictionary.*;
import net.didion.jwnl.dictionary.Dictionary;

/**
 *
 *  @version   $Revision: 1.2 $
 *  @author    Paul Speed
 */
public class WordNetTest {

    public static void print(String indent, PointerTargetTree ptt) {
        ptt.print();
        print(indent, ptt.getRootNode());
    }

    public static void print(String indent, PointerTargetTreeNode node) {
        Synset sense = (Synset) node.getSynset();
        Word[] words = sense.getWords();
        System.out.print(indent);
        for (int i = 0; i < words.length; i++) {
            Word w = words[i];
            if (i > 0) System.out.print(", ");
            System.out.print(w.getLemma());
        }
        System.out.println(" -- " + sense.getGloss());
        if (node.getType() != null) {
            System.out.println(indent + "    " + node.getType());
        }
        PointerTargetTreeNodeList children = node.getChildTreeList();
        for (Iterator i = children.iterator(); i.hasNext(); ) {
            print(indent + "    ", (PointerTargetTreeNode) i.next());
        }
    }

    public static String nodeToString(Object n) {
        if (n instanceof Synset) {
            Synset s = (Synset) n;
            return (s.getPOS().getLabel() + ":" + getWords(s) + " -- " + s.getGloss());
        } else {
            return (String.valueOf(n));
        }
    }

    public static void printTree(Graph g, String indent, Object n, int depth, int maxDepth) {
        if (depth >= maxDepth) return;
        for (Traverser t = g.traverser(n, null); t.hasNext(); ) {
            Object o = t.next();
            Graph.Edge e = t.getEdge();
            System.out.print(indent + "    ");
            if (e.getUserObject() instanceof PointerType) {
                PointerType type = (PointerType) e.getUserObject();
                if (e.isDirected()) {
                    if (e.getTail() == n) System.out.print("-->"); else System.out.print("<--");
                }
                System.out.print("(" + type.getLabel() + ") ");
            }
            System.out.println(nodeToString(o));
            printTree(g, indent + "    ", o, depth + 1, maxDepth);
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: WordNetText <property file>");
            System.exit(0);
        }
        JWNL.initialize(new FileInputStream(args[0]));
        Dictionary dict = Dictionary.getInstance();
        Graph g = new WordNetGraph(dict);
        for (int i = 1; i < args.length; i++) {
            System.out.println(nodeToString(args[i]));
            printTree(g, "", args[i], 0, 3);
        }
    }

    public static List<String> getWords(Synset s) {
        Word[] words = s.getWords();
        List<String> results = new ArrayList<String>();
        for (int i = 0; i < words.length; i++) {
            results.add(words[i].getLemma());
        }
        return (results);
    }

    public static List<Synset> expandWords(List strings) throws Exception {
        List<Synset> results = new ArrayList<Synset>();
        for (Iterator i = strings.iterator(); i.hasNext(); ) {
            IndexWordSet set = Dictionary.getInstance().lookupAllIndexWords((String) i.next());
            for (Iterator j = set.getIndexWordCollection().iterator(); j.hasNext(); ) {
                IndexWord w = (IndexWord) j.next();
                results.addAll(Arrays.asList(w.getSenses()));
            }
        }
        return (results);
    }

    public static void print(String indent, Relationship r) {
        PointerTargetNodeList list = r.getNodeList();
        for (Iterator i = list.iterator(); i.hasNext(); ) {
            PointerTargetNode node = (PointerTargetNode) i.next();
            System.out.println(indent + "  ->" + node.getSynset());
            System.out.println(indent + "    " + node.getType());
            System.out.println(indent + "    " + node.getWord());
            System.out.println(indent + "    " + node.getPointerTarget());
        }
    }

    public static void printChildPointers(String indent, Synset root) throws Exception {
        Pointer[] children = root.getPointers();
        for (int i = 0; i < children.length; i++) {
            Pointer p = children[i];
            PointerType type = p.getType();
            if (type == PointerType.HYPONYM) continue;
            System.out.println(indent + "[" + i + "] = " + p.getType().getLabel() + " -> " + getWords(p.getTargetSynset()) + " -- " + p.getTargetSynset().getGloss());
            if (type == PointerType.HYPERNYM) printChildPointers(indent + "    ", p.getTargetSynset());
        }
    }

    public static void printConnections(Synset root, List<Synset> otherWords) throws Exception {
        System.out.println("Root:" + getWords(root) + " -- " + root.getGloss());
        printChildPointers("    ", root);
    }
}
