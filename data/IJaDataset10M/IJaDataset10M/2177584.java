package minire;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import minire.DFAScanner.MatchDescriptor;

/**
 * Represents IDs. Contains the matched strings and their metadata, or an integer
 * if the ID has been created via the # operator. This class also contains the all binary
 * and unary operators involving IDS.
 * @author Owen Cox
 */
public class ID {

    /**
	 * The result from #.
	 */
    int i;

    /**
	 * Whether or not this ID is an int.
	 */
    boolean isInt;

    /**
	 * The string match list.
	 */
    public LinkedList<Node> list;

    /**
	 * Test cases for each method.
	 * @param args
	 */
    public static void main(String[] args) {
        ID t1 = new ID();
        ID t2 = new ID();
        t1.addNode(new Node("abc", "f1", 1, 2, 3));
        t1.addNode(new Node("xyz", "f2", 2));
        t1.addNode(new Node("lol", "f2", 1, 5, 100, 2, 4));
        t2.addNode(new Node("abc", "f3", 4, 5));
        t2.addNode(new Node("abc", "f1", 4, 5));
        t2.addNode(new Node("def", "f4", 6, 7));
        System.out.println("ID1: " + t1.toString());
        System.out.println("ID2: " + t2.toString());
        System.out.println("ID1 diff ID2: " + t1.diff(t2).toString());
        System.out.println("ID maxFreqStr: " + t1.maxFreqStr().toString());
        System.out.println("ID1 union ID2: " + t1.union(t2).toString());
        System.out.println("ID1 inters ID2: " + t1.inters(t2).toString());
        System.out.println("#ID1: " + t1.num().toString());
        System.out.println("from '<([a-zA-Z\\-])*>' in src/test.txt :");
        ID t3 = new ID("<([a-zA-Z\\-])*>", "src/test.txt");
        System.out.println(t3);
    }

    /**
	 * Each node contains a matched string and its metadata.
	 */
    private static class Node {

        public String exp;

        public LinkedList<FileNode> files;

        /**
		 * Creates a node with no metadata.
		 * @param exp
		 */
        public Node(String exp) {
            this.exp = exp;
            files = new LinkedList<FileNode>();
        }

        /**
		 * Creates a node with matches for a single file.
		 * @param exp the string
		 * @param fileName the file
		 * @param locations the matches
		 */
        public Node(String exp, String fileName, int... locations) {
            this.exp = exp;
            files = new LinkedList<FileNode>();
            FileNode f = new FileNode(fileName);
            for (int i : locations) {
                f.occurences.add(i);
            }
            files.add(f);
        }

        /**
		 * @return string representation of the node.
		 */
        public String toString() {
            String s = "\"" + exp + "\" ";
            for (FileNode f : files) {
                s += "<" + f.toString() + ">";
            }
            return s;
        }

        /**
		 * @return Whether or not the strings match between the two nodes.
		 */
        public boolean equals(Node n) {
            if (n == null) return false;
            if (n.exp.compareTo(this.exp) == 0) {
                return true;
            }
            return false;
        }

        /**
		 * Combines two node's metadatas.
		 * @param n
		 */
        public void union(Node n) {
            for (FileNode f : n.files) {
                files.add(f);
            }
            for (int i = 0; i < files.size(); i++) {
                for (int j = i + 1; j < files.size(); j++) {
                    if (files.get(i).equals(files.get(j))) {
                        files.get(i).union(files.get(j));
                        files.remove(j);
                        j--;
                    }
                }
            }
        }

        /**
		 * Creates a deep clone of the node.
		 */
        public Node clone() {
            Node clone = new Node(exp);
            for (FileNode f : files) {
                clone.files.add(f.clone());
            }
            return clone;
        }
    }

    /**
	 * Holds the locations of the matches for a particular file.
	 */
    public static class FileNode {

        public String fileName;

        public LinkedList<Integer> occurences;

        public FileNode(String fileName) {
            this.fileName = fileName;
            occurences = new LinkedList<Integer>();
        }

        /**
		 * @param n
		 * @return whether or not the two filenodes are of the same file.
		 */
        public boolean equals(FileNode f) {
            if (fileName.compareTo(f.fileName) == 0) {
                return true;
            }
            return false;
        }

        /**
		 * Combines two FileNodes
		 * @param n
		 */
        public void union(FileNode f) {
            for (Integer i : f.occurences) {
                occurences.add(i);
            }
            for (int i = 0; i < occurences.size(); i++) {
                for (int j = i + 1; j < occurences.size(); j++) {
                    if (occurences.get(i) == (occurences.get(j))) {
                        occurences.remove(j);
                        j--;
                    }
                }
            }
        }

        /**
		 * String representation of the File Node
		 */
        public String toString() {
            String s = "\"" + fileName + "\"";
            for (int i : occurences) {
                s += ", " + i;
            }
            return s;
        }

        /**
		 * Clones the filenode.
		 */
        public FileNode clone() {
            FileNode clone = new FileNode(fileName);
            for (int i : occurences) {
                clone.occurences.add(i);
            }
            return clone;
        }
    }

    /**
	 * Adds a new node to the list.
	 * @param n
	 */
    private void addNode(Node n) {
        list.add(n);
    }

    /**
	 * Creates an int ID.
	 */
    public ID(int i) {
        this.i = i;
        isInt = true;
    }

    /**
	 * @return whether or not this ID is an int
	 */
    public boolean isInt() {
        return isInt;
    }

    /**
	 * Creates an empty ID that is not an integer.
	 */
    public ID() {
        list = new LinkedList<Node>();
        isInt = false;
    }

    /**
	 * Creates an ID containing all matches of a regEx within the file specified
	 * @param regEx
	 * @param file
	 */
    public ID(String regEx, String file) {
        isInt = false;
        list = new LinkedList<Node>();
        List<MatchDescriptor> matches = null;
        DFAScanner scanner = new DFAScanner();
        scanner.addRegex(regEx);
        try {
            matches = scanner.findAllInFile(new File(file));
        } catch (IOException e) {
            System.out.println("An error occured while trying to access the given file: " + file);
            System.out.println(e.getMessage());
            System.exit(0);
        }
        for (MatchDescriptor m : matches) {
            list.add(new Node(m.getString(), file, m.getLocation()));
        }
        cleanUp();
    }

    /**
	 * Creates an ID containing all matches of a regEx within the string specified.
	 * This is only ever used internally and as such we do not expect the ID to be passed anywhere else.
	 * 
	 * @param regex the regex to search for.
	 * @param str the string to search through.
	 * @return the id containing all valid matches.
	 */
    public static ID fromString(String regex, String str) {
        ID ret = new ID();
        List<MatchDescriptor> matches = null;
        DFAScanner scanner = new DFAScanner();
        scanner.addRegex(regex);
        matches = scanner.findAllInString(str);
        for (MatchDescriptor m : matches) {
            ret.list.add(new Node(m.getString(), "String", m.getLocation()));
        }
        ret.cleanUp();
        return ret;
    }

    /**
	 * @return the string representation of the ID
	 */
    public String toString() {
        if (isInt) {
            return "" + i;
        }
        String s = "{";
        for (Node n : list) {
            if (s.length() > 1) s += ", ";
            s += n.toString();
        }
        s += "}";
        return s;
    }

    /**
	 * @param id2
	 * @return the contents of the ID minus strings found in id2.
	 */
    public ID diff(ID id2) {
        if (this.isInt() || id2.isInt()) {
            System.out.println("Cannot perform diff operation on an integer ID.");
            System.exit(0);
        }
        ID newID = new ID();
        for (Node n : list) {
            newID.addNode(n.clone());
        }
        for (Node n : id2.list) {
            for (int i = 0; i < newID.list.size(); i++) {
                if (newID.list.get(i).equals(n)) {
                    newID.list.remove(i);
                    i--;
                }
            }
        }
        return newID;
    }

    /**
	 * @param id2
	 * @return the union of the two IDs. Combines all strings and metadata.
	 */
    public ID union(ID id2) {
        if (this.isInt() || id2.isInt()) {
            System.out.println("Cannot perform union operation on an integer ID.");
            System.exit(0);
        }
        ID newID = new ID();
        for (Node n : list) {
            newID.addNode(n.clone());
        }
        for (Node n : id2.list) {
            newID.addNode(n.clone());
        }
        newID.cleanUp();
        return newID;
    }

    /**
	 * @param id2
	 * @return the strings common to both IDs with the metadata unioned.
	 */
    public ID inters(ID id2) {
        if (this.isInt() || id2.isInt()) {
            System.out.println("Cannot perform inters operation on an integer ID.");
            System.exit(0);
        }
        ID newID = new ID();
        boolean found1, found2;
        Node node;
        for (Node n : list) {
            newID.addNode(n.clone());
        }
        for (Node n : id2.list) {
            newID.addNode(n.clone());
        }
        for (int i = 0; i < newID.list.size(); i++) {
            found1 = false;
            found2 = false;
            node = newID.list.get(i);
            for (Node n : list) {
                if (n.equals(node)) found1 = true;
            }
            for (Node n : id2.list) {
                if (n.equals(node)) found2 = true;
            }
            if (!found1 || !found2) {
                newID.list.remove(i);
                i--;
            }
        }
        newID.cleanUp();
        return newID;
    }

    /**
	 * @return the string with the most occurences (from all files) with the assosiated metadata.
	 */
    public ID maxFreqStr() {
        if (this.isInt()) {
            System.out.println("Cannot perform maxFreqStr operation on an integer ID.");
            System.exit(0);
        }
        ID newID = new ID();
        int max = 0;
        int current;
        Node best = null;
        for (Node n : list) {
            current = 0;
            for (FileNode f : n.files) {
                current += f.occurences.size();
            }
            if (current >= max) {
                max = current;
                best = n;
            }
        }
        newID.addNode(best.clone());
        return newID;
    }

    /**
	 * @return the number of different strings within the ID. Is the # operator.
	 */
    public ID num() {
        return new ID(list.size());
    }

    /**
	 * Looks for duplicate strings and files within strings, and unions the metadata.
	 */
    public void cleanUp() {
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i).equals(list.get(j))) {
                    list.get(i).union(list.get(j));
                    list.remove(j);
                    j--;
                }
            }
        }
    }

    /**
	 * Finds a string matched at location i.
	 * Returns null if none are matched.
	 * @param i
	 * @return
	 */
    public String atLocation(int i) {
        for (Node n : list) {
            for (FileNode f : n.files) {
                for (int j : f.occurences) {
                    if (i == j) return n.exp;
                }
            }
        }
        return null;
    }
}
