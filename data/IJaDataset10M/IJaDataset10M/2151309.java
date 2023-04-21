package ParseTree;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.text.Document;
import mip.pcre.pcre_v2.PCREPattern;
import mip.pcre.pcre_v2.Refer;

/**
 *
 * @author heckarim
 */
public class RegexTree {

    public Node root;

    public PCREPattern rule;

    public static String _default_folder = System.getProperty("user.dir") + System.getProperty("file.separator");

    public static String _default_file_name = "parsetree.dot";

    private boolean no_CRB = false;

    public RegexTree(PCREPattern rule) {
        this.rule = rule;
    }

    public RegexTree(String rule) {
        this.rule = new PCREPattern(rule);
    }

    public RegexTree(String rule, boolean no_CRB) {
        this.no_CRB = no_CRB;
        this.rule = new PCREPattern(rule);
    }

    public void parseTree() {
        ParseTree parser = new ParseTree(rule);
        System.out.println("parseTree: " + rule);
        this.root = parser.parsePcre();
    }

    public void printTree() {
        PCRE.Refer.println("Print Tree: ", null);
        this.printTreeRecursive(null, root, 0);
    }

    public void printTree(Document doc) {
        PCRE.Refer.println("Print Tree: ", doc);
        this.printTreeRecursive(doc, root, 0);
    }

    public void printTreeRecursive(Document doc, Node root, int tab) {
        if (root == null) {
            return;
        }
        for (int i = 0; i < tab; i++) {
            PCRE.Refer.print("    ", doc);
        }
        PCRE.Refer.print(tab + ".", doc);
        PCRE.Refer.println(root.value + "," + Refer.convert[root.id], doc);
        if (root.left == null && root.right == null) {
            return;
        }
        if (root.left != null) {
            this.printTreeRecursive(doc, root.left, tab + 1);
        }
        if (root.right != null) {
            this.printTreeRecursive(doc, root.right, tab + 1);
        }
    }

    private int generateDotFile_recursive(Node root, BufferedWriter bw, int order) throws IOException {
        if (root == null) {
            return order;
        }
        String colorNodev2 = "blue";
        String coloredge = "green";
        if (Refer.isOperator(root.id)) {
            colorNodev2 = "red";
        }
        int neworder = order + 1;
        if (Refer.isOperator(root.id)) {
            bw.write("\n" + order + " [label=\"[" + Refer.convert[root.id] + "]\" color=" + colorNodev2 + "];");
        } else {
            bw.write("\n" + order + " [label=\"" + root.value + "\" color=" + colorNodev2 + "];");
        }
        if (root.left == null && root.right == null) {
            return neworder;
        }
        if (root.left != null) {
            bw.write("\n" + order + " -> " + neworder + " [color=" + coloredge + "];");
            neworder = this.generateDotFile_recursive(root.left, bw, neworder);
        }
        if (root.right != null) {
            bw.write("\n" + order + " -> " + neworder + " [color=" + coloredge + "];");
            neworder = this.generateDotFile_recursive(root.right, bw, neworder);
        }
        return neworder;
    }

    public void generateDotFile(String name, String folder) {
        BufferedWriter bw = null;
        try {
            if (null == folder || folder.isEmpty()) {
                folder = _default_folder;
            }
            if (null == name || name.isEmpty()) {
                name = _default_file_name;
            }
            bw = new BufferedWriter(new FileWriter(folder + name));
            bw.write("digraph \"parse Tree\" {" + "\ngraph [ranksep=.2,rankdir=TD];" + "\nNodev2 [shape=circle,fontname=Arial,fontsize=14];" + "\nNodev2 [width=1,fixedsize=true];" + "\nedge [fontname=Arial,fontsize=14];" + "\n-1 [width=0.2,shape=point color=red];" + "\n-1 -> 0 [ color=red];");
            this.generateDotFile_recursive(this.root, bw, 0);
            bw.write("\n}\n");
            bw.flush();
            bw.close();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
}
