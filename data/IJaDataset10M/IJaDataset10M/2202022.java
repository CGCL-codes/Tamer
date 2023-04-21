package de.grogra.blocks.xFrogFileParser;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.vecmath.Tuple3f;
import de.grogra.blocks.BlockConst;
import de.grogra.blocks.Horn;
import de.grogra.blocks.Hydra;
import de.grogra.blocks.PhiBall;
import de.grogra.blocks.Tree;
import de.grogra.graph.impl.Edge;
import de.grogra.graph.impl.Node;

public class File extends Expr {

    private Node root = null;

    private StringBuffer xl = null;

    public File(Expr a) {
        this.a = a;
        xl = new StringBuffer(toXL());
        root = null;
        toGraph();
        if (debug) System.out.println(getClass().getSimpleName() + " :: " + a);
    }

    private String parseChildrenTree(String ss, String s3, int aktLayer) {
        String s2 = (String) children.get(ss);
        if (s2 == null) {
            return setAktLayer((String) blocks.get(ss), aktLayer);
        }
        StringTokenizer sn = new StringTokenizer(s2);
        int anzChildren = Integer.parseInt((String) sn.nextElement());
        if (ss.equals("\"Root\"")) {
            ss = "";
        }
        int akt = 0;
        while (sn.hasMoreTokens()) {
            String edge = "";
            String s1 = (String) sn.nextElement();
            akt++;
            if (ss.length() != 0) {
                String tt = ((String) blocks.get(ss)).substring(0, 2);
                if (tt.equals("Ho")) {
                    if (isRib(ss, s1)) {
                        edge = "-MULTIPLY->";
                    } else {
                        edge = "-CHILD->";
                    }
                }
                if (tt.equals("Tr")) {
                    if (isBranch(ss, s1)) {
                        edge = "-MULTIPLY->";
                    } else {
                        edge = "-CHILD->";
                    }
                }
                if (tt.equals("Ph") || tt.equals("Hy")) {
                    edge = "-MULTIPLY->";
                }
            }
            if (anzChildren > 1) {
                if (akt > 1) {
                    s3 = s3 + "\n\t\t[" + edge + " " + parseChildrenTree(s1, s3, aktLayer) + "]\n\t\t";
                } else {
                    s3 = ((ss.length() != 0) ? setAktLayer((String) blocks.get(ss), aktLayer) : "") + "[" + edge + "\n\t\t" + parseChildrenTree(s1, s3, aktLayer) + "]";
                }
            } else {
                s3 = ((ss.length() != 0) ? setAktLayer((String) blocks.get(ss), aktLayer) : "") + " " + edge + "\n\t\t " + parseChildrenTree(s1, s3, ++aktLayer);
            }
        }
        return s3;
    }

    private String setAktLayer(String s3, int aktLayer) {
        return s3.substring(0, s3.indexOf(".(")) + ".(setLayer(" + aktLayer + "), " + s3.substring(s3.indexOf(".(") + 2, s3.length());
    }

    private String toXL() {
        String importList = "";
        String lights = "\t\t";
        imports.add("import static de.grogra.blocks.BlockConst.*;");
        Iterator it = imports.iterator();
        while (it.hasNext()) {
            importList += (String) it.next() + "\n";
        }
        importList += "\n";
        it = textureImports.iterator();
        while (it.hasNext()) {
            importList += (String) it.next() + "\n";
        }
        String matrizen = "\t{\n";
        it = transformMatrizen.iterator();
        while (it.hasNext()) {
            matrizen += "\t" + (String) it.next() + "\n";
        }
        it = trajectory.iterator();
        while (it.hasNext()) {
            matrizen += (String) it.next() + "\n";
        }
        if (lightsXL.size() != 0) {
            for (Enumeration ee = lightsXL.elements(); ee.hasMoreElements(); ) {
                matrizen += (String) ee.nextElement() + "\n";
            }
            for (Enumeration ee = lightsXL.keys(); ee.hasMoreElements(); ) {
                lights += "[" + (String) ee.nextElement() + "] ";
            }
        }
        matrizen += "\t}\n\n";
        return importList + "\n" + "protected void init() {\n" + "\tfor (apply()) init2 ();\n" + "}\n\n" + "public void init2() [\n" + (matrizen.length() > 10 ? matrizen : "") + "\tAxiom ==> " + sky + lights + parseChildrenTree("\"Root\"", "", 0) + ";\n" + "]" + "\n\n";
    }

    private void parseChildrenTreeG(String ss, Node s3, int aktLayer) {
        String s2 = (String) children.get(ss);
        if (s2 == null) {
            return;
        }
        StringTokenizer sn = new StringTokenizer(s2);
        int anzChildren = Integer.parseInt((String) sn.nextElement());
        if (ss.equals("\"Root\"")) {
            ss = "";
        }
        while (sn.hasMoreTokens()) {
            int edge = de.grogra.graph.Graph.BRANCH_EDGE;
            if (anzChildren == 1) {
                edge = de.grogra.graph.Graph.SUCCESSOR_EDGE;
            }
            String s1 = (String) sn.nextElement();
            Node no = (Node) blocksGraphNodes.get(s1);
            if (no == null) return;
            no.setLayer(aktLayer + 1);
            if (ss.length() != 0) {
                Node nn1 = ((Node) blocksGraphNodes.get(ss));
                if (nn1 instanceof Horn) {
                    if (isRib(s3, no)) {
                        edge = BlockConst.MULTIPLY;
                    } else {
                        edge = BlockConst.CHILD;
                    }
                }
                if (nn1 instanceof Tree) {
                    if (isBranch(s3, no)) {
                        edge = BlockConst.MULTIPLY;
                    } else {
                        edge = BlockConst.CHILD;
                    }
                }
                if (nn1 instanceof Hydra) {
                    edge = BlockConst.MULTIPLY;
                }
                if (nn1 instanceof PhiBall) {
                    edge = BlockConst.MULTIPLY;
                }
            }
            s3.addEdgeBitsTo(no, edge, null);
            parseChildrenTreeG(s1, no, s3.getLayer());
        }
    }

    private boolean isBranch(String s3, String no) {
        if (!branches.containsKey(s3)) return false;
        String ri = (String) branches.get(s3);
        return ri.contains(no);
    }

    private boolean isBranch(Node s3, Node no) {
        if (!branches.containsKey(s3.getName())) return false;
        String ri = (String) branches.get(s3.getName());
        return ri.contains(no.getName());
    }

    private boolean isRib(String s3, String no) {
        if (!ribs.containsKey(s3)) return false;
        String ri = (String) ribs.get(s3);
        return ri.contains(no);
    }

    private boolean isRib(Node s3, Node no) {
        if (!ribs.containsKey(s3.getName())) return false;
        String ri = (String) ribs.get(s3.getName());
        return ri.contains(no.getName());
    }

    private void toGraph() {
        root = new Node();
        parseChildrenTreeG("\"Root\"", root, root.getLayer());
        addLightsToRoot();
        int count = 0;
        Node child = null;
        for (Edge e = root.getFirstEdge(); e != null; e = e.getNext(root)) {
            count++;
            child = e.getTarget();
        }
        if (count == 1) {
            root.removeAll(null);
            root = child;
        }
    }

    private void addLightsToRoot() {
        for (Enumeration ee = lightsGraphNodes.elements(); ee.hasMoreElements(); ) {
            Node nn = (Node) ee.nextElement();
            root.addEdgeBitsTo(nn, de.grogra.graph.Graph.SUCCESSOR_EDGE, null);
        }
    }

    public Node getRoot() {
        return root;
    }

    public Vector getTextures() {
        return textures;
    }

    public Tuple3f getCameraParams() {
        return camera;
    }

    public StringBuffer getXl() {
        return xl;
    }

    @Override
    public String toString() {
        if (debugS) System.out.println("TS  " + getClass().getSimpleName());
        return a.toString() + "\n";
    }
}
