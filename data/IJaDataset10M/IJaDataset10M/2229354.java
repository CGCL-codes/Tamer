package xdocument.mset;

import util.FileIO;
import java.io.File;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.LinkedList;

public class Graphviz {

    public static final int TREE_VIEW = 0;

    public static final int SUBNODE_VIEW = 1;

    public static final int OLD_VIEW = 2;

    public static void save(MSET mset) {
        File file = new File("graphviz.dot");
        save(file, mset);
    }

    public static void save(int type, File file, MSET mset) {
        StringBuffer buff = new StringBuffer();
        try {
            switch(type) {
                case 2:
                    AllLinks.save(buff, mset.root);
                    break;
                case 0:
                    NodeView.digraph(buff, mset.root);
                    break;
                case 1:
                    SNVGrouped.digraph(buff, mset.getSubNodeView());
                    break;
            }
            FileIO.save(file, buff.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String idx(MSET.Node n) {
        return n.getID().toString();
    }

    public static String id(MSET.SubNode sn) {
        return sn.getNode().getID() + "s" + sn.getStart() + "e" + sn.getEnd() + (sn.isStartMarker() ? "smark" : (sn.isEndMarker() ? "emark" : ""));
    }

    public static String id(MSET.Node.Position p) {
        return idx(p.getOwnerNode()) + ":p" + p.getIndex();
    }

    public static String text(MSET.SubNode sn) {
        return sn.getText().replaceAll("\n", "\\\\n");
    }

    public static String text(MSET.Node n) {
        return n.getText().replaceAll("\n", "\\\\n");
    }

    static class NodeView {

        private static String insert(String str, int i, String insertStr) {
            return str.substring(0, i) + insertStr + str.substring(i, str.length());
        }

        public static void digraph(StringBuffer buff, MSET.Node n) {
            LinkedList<MSET.Node> Q = new LinkedList();
            buff.append("digraph NodeView {\n" + "node [fontname=Courier,shape=record];\n\n");
            Iterator<MSET.Node.Position> ip;
            Iterator<MSET.Node> in;
            MSET.Node.Position p;
            Q.add(n);
            int delta;
            String pID;
            while (!Q.isEmpty()) {
                n = Q.remove();
                String text = text(n);
                ip = n.getInsertPositions().values().iterator();
                if (n.getPosition().getOwnerNode() != null) buff.append(id(n.getPosition()) + " -> " + idx(n) + ":id" + " [label=p" + n.getPosition().getIndex() + "]\n");
                buff.append(idx(n) + " [label=\"<id>" + idx(n));
                delta = 0;
                while (ip.hasNext()) {
                    p = ip.next();
                    pID = "|<p" + p.getIndex() + ">";
                    text = insert(text, p.getIndex() + delta, pID);
                    delta += pID.length();
                    Q.addAll(p.getInserts().values());
                }
                buff.append("|" + text + "\"];\n\n");
            }
            buff.append("}");
        }
    }

    static class NodeDeleteView {

        public static void digraph(StringBuffer buff, MSET.Node n) {
            LinkedList<MSET.Node> Q = new LinkedList();
            buff.append("digraph NodeDeleteView {\n" + "node [fontname=Courier,shape=record];\n\n");
            Iterator<MSET.Node.Position> ip;
            Iterator<MSET.Node> in;
            MSET.Node.Position p;
            MSET.SubNode sn;
            Q.add(n);
            while (!Q.isEmpty()) {
                n = Q.remove();
                ip = n.getInsertPositions().values().iterator();
                if (n.getPosition().getOwnerNode() != null) buff.append(id(n.getPosition()) + " -> " + idx(n) + ":id" + " [label=p" + n.getPosition().getIndex() + "]\n");
                buff.append(idx(n) + " [label=\"<id>" + idx(n));
                sn = n.getSubNodeSet().getFirst();
                while (sn != null) {
                    p = (ip.hasNext() ? ip.next() : null);
                    buff.append((sn.isDeleted() ? "|{*|" : "|{"));
                    if (p != null && p.getIndex() == sn.getEnd()) {
                        buff.append("<p" + p.getIndex() + ">");
                        Q.addAll(p.getInserts().values());
                        System.out.println("\n------------------\n" + p.getInserts().values());
                    }
                    buff.append(text(sn) + "}");
                    sn = sn.getLocalNext();
                }
                buff.append("\"];\n\n");
            }
            buff.append("}");
        }
    }

    static class SNVGrouped {

        public static void digraph(StringBuffer buff, SubNodeView snv) {
            LinkedList<MSET.SubNode> Q = new LinkedList();
            buff.append("digraph SNVGrouped {\ngraph [rankdir=LR];\n" + "node [fontname=Courier,shape=record," + "fillcolor=grey];\n\n");
            MSET.SubNode sn;
            StringBuffer globals;
            Q.add(snv.getFirst());
            while (!Q.isEmpty()) {
                sn = Q.remove();
                buff.append("\nsubgraph cluster_" + idx(sn.getNode()) + " {\nlabel=" + idx(sn.getNode()) + ";\n");
                globals = new StringBuffer();
                do {
                    append(buff, globals, sn);
                    if (sn.getGlobalNext() != null && sn.getGlobalNext().getLocalPrevious() == null) Q.add(sn.getGlobalNext());
                } while ((sn = sn.getLocalNext()) != null);
                buff.append("}\n" + globals + "\n\n");
            }
            buff.append("}\n");
        }

        public static void append(StringBuffer buff, StringBuffer globals, MSET.SubNode sn) {
            String id = id(sn);
            buff.append(id + " [group=" + sn.getNode().getID() + ",label=\"");
            buff.append("{s" + sn.getStart() + "|e" + sn.getEnd() + "}|");
            buff.append("{" + text(sn) + "}\"" + (sn.isStartMarker() ? ",style=filled,fillcolor=green" : sn.isEndMarker() ? ",style=filled,fillcolor=greenyellow" : sn.length() == 0 ? ",style=filled,fillcolor=red" : sn.isDeleted() ? ",style=filled" : "") + "];\n");
            if (sn.getLocalPrevious() == sn.getGlobalPrevious() && sn.getLocalPrevious() != null) buff.append(id + " -> " + id(sn.getLocalPrevious()) + " [style=bold]; /* PREV MERGED */\n"); else {
                if (sn.getLocalPrevious() != null) buff.append(id + " -> " + id(sn.getLocalPrevious()) + " [color=black]; /* PREV LOCAL */\n"); else buff.append("/* " + id + " -> null; PREV LOCAL */\n");
                if (sn.getGlobalPrevious() != null) globals.append(id + " -> " + id(sn.getGlobalPrevious()) + " [color=green]; /* PREV GLOBAL */\n"); else globals.append("/* " + id + " -> null; PREV GLOBAL */\n");
            }
            if (sn.getLocalNext() == sn.getGlobalNext() && sn.getLocalNext() != null) buff.append(id + " -> " + id(sn.getLocalNext()) + " [style=bold]; /* NEXT MERGED */\n"); else {
                if (sn.getLocalNext() != null) buff.append(id + " -> " + id(sn.getLocalNext()) + " [color=black]; /* NEXT LOCAL */\n"); else buff.append("/* " + id + " -> null; NEXT LOCAL */\n");
                if (sn.getGlobalNext() != null) globals.append(id + " -> " + id(sn.getGlobalNext()) + " [color=blue]; /* NEXT GLOBAL */\n"); else globals.append("/* " + id + " -> null; NEXT GLOBAL */\n");
            }
            buff.append("\n");
        }
    }

    static class SNV {

        public static void digraph(StringBuffer buff, SubNodeView snv) {
            buff.append("digraph SNV {\ngraph [rankdir=RL];\n" + "node [fontname=Courier,shape=record," + "fillcolor=grey];\n\n");
            append(buff, snv.getFirst());
            buff.append("}\n");
        }

        public static void append(StringBuffer buff, MSET.SubNode sn) {
            if (sn != null) {
                String id = id(sn);
                buff.append(id + " [group=" + sn.getNode().getID() + ",label=\"");
                buff.append("{u" + sn.getNode().getID().getOwner() + "|n" + sn.getNode().getID().getNumber() + "}|");
                buff.append("{s" + sn.getStart() + "|e" + sn.getEnd() + "}|");
                buff.append("{" + text(sn) + "}\"" + (sn.isStartMarker() ? ",style=filled,fillcolor=green" : sn.isEndMarker() ? ",style=filled,fillcolor=greenyellow" : sn.length() == 0 ? ",style=filled,fillcolor=red" : sn.isDeleted() ? ",style=filled" : "") + "];\n");
                if (sn.getLocalPrevious() == sn.getGlobalPrevious() && sn.getLocalPrevious() != null) buff.append(id + " -> " + id(sn.getLocalPrevious()) + " [style=bold]; /* PREV MERGED */\n"); else {
                    if (sn.getLocalPrevious() != null) buff.append(id + " -> " + id(sn.getLocalPrevious()) + " [color=black]; /* PREV LOCAL */\n"); else buff.append("/* " + id + " -> null; PREV LOCAL */\n");
                    if (sn.getGlobalPrevious() != null) buff.append(id + " -> " + id(sn.getGlobalPrevious()) + " [color=blue]; /* PREV GLOBAL */\n"); else buff.append("/* " + id + " -> null; PREV GLOBAL */\n");
                }
                if (sn.getLocalNext() == sn.getGlobalNext() && sn.getLocalNext() != null) buff.append(id + " -> " + id(sn.getLocalNext()) + " [style=bold]; /* NEXT MERGED */\n"); else {
                    if (sn.getLocalNext() != null) buff.append(id + " -> " + id(sn.getLocalNext()) + " [color=black]; /* NEXT LOCAL */\n"); else buff.append("/* " + id + " -> null; NEXT LOCAL */\n");
                    if (sn.getGlobalNext() != null) buff.append(id + " -> " + id(sn.getGlobalNext()) + " [color=blue]; /* NEXT GLOBAL */\n"); else buff.append("/* " + id + " -> null; NEXT GLOBAL */\n");
                }
                buff.append("\n");
                append(buff, sn.getGlobalNext());
            }
        }
    }

    public static void save(File file, MSET mset) {
        StringBuffer buff = new StringBuffer();
        AllLinks.save(buff, mset.root);
        FileIO.save(file, buff.toString());
    }

    static class AllLinks {

        public static void save(StringBuffer buff, MSET.Node root) {
            buff.append("digraph MSET {\n");
            buff.append("node [fontname=\"Courier\" shape=\"record\"" + " width=0.1 height=0.1]\n");
            append(buff, null, root);
            buff.append("}\n");
        }

        public static String label(MSET.Node n) {
            String viz = "{<id>u" + n.getID().getOwner() + "|n" + n.getID().getNumber() + "}";
            MSET.SubNode sn = n.getSubNodeSet().getFirst();
            for (int i = 0; sn != null; i++) {
                if (sn.isDeleted()) viz += "|{*| <" + "sn" + sn.getStart() + "_" + sn.getEnd() + ">" + sn.getText() + "}"; else viz += "| <" + "sn" + sn.getStart() + "_" + sn.getEnd() + ">" + sn.getText();
                sn = sn.getLocalNext();
            }
            return viz.replaceAll("\n", "\\\\n");
        }

        public static void snPoint(String parent, StringBuffer b, SubNodeView.SubNodeSet set) {
            MSET.SubNode sn = set.getFirst(), tmp;
            for (int i = 0; sn != null; i++) {
                tmp = sn.getGlobalPrevious();
                if (tmp != null) {
                    String snID = parent + ":sn" + sn.getStart() + "_" + sn.getEnd();
                    String tmpID = "N_" + tmp.getNode().getID() + ":sn" + tmp.getStart() + "_" + tmp.getEnd();
                    b.append(snID + " -> " + tmpID + " [color=\"red\"] \n");
                }
                sn = sn.getLocalNext();
            }
        }

        public static void append(StringBuffer buff, String parent, MSET.Node n) {
            String name = "N_" + n.getID();
            String label = "{<id>u" + n.getID().getOwner() + "|n" + n.getID().getNumber() + "}" + toString(n.getSubNodeSet()).replaceAll("\n", "\\\\n");
            String gP = toPString(name, n.getSubNodeSet());
            buff.append(parent + " -> " + id(n) + "\n");
            buff.append(name(n) + " [" + "label=\"" + label(n) + "\"]" + "\n");
            snPoint(parent, buff, n.getSubNodeSet());
            TreeMap ps = n.getInsertPositions();
            Iterator it = ps.values().iterator();
            while (it.hasNext()) append(buff, name(n), (MSET.Node.Position) it.next());
        }

        public static String toString(SubNodeView.SubNodeSet set) {
            MSET.SubNode sn = set.getFirst(), tmp;
            String viz = "";
            for (int i = 0; sn != null; i++) {
                if (sn.isDeleted()) viz += "|{*| <" + "sn" + sn.getStart() + "_" + sn.getEnd() + ">" + sn.getText() + "}"; else viz += "| <" + "sn" + sn.getStart() + "_" + sn.getEnd() + ">" + sn.getText();
                sn = sn.getLocalNext();
            }
            return viz;
        }

        public static String toPString(String parent, SubNodeView.SubNodeSet set) {
            MSET.SubNode sn = set.getFirst(), tmp;
            String viz = "";
            for (int i = 0; sn != null; i++) {
                tmp = sn.getGlobalPrevious();
                if (tmp != null) {
                    String snID = parent + ":sn" + sn.getStart() + "_" + sn.getEnd();
                    String tmpID = "N_" + tmp.getNode().getID() + ":sn" + tmp.getStart() + "_" + tmp.getEnd();
                    viz += snID + " -> " + tmpID + " [color=\"red\"] \n";
                }
                sn = sn.getLocalNext();
            }
            return viz;
        }

        public static void append(StringBuffer buff, String parent, MSET.Node.Position p) {
            String name = "P" + p.getIndex() + "_" + parent;
            String label = "label = \"@" + p.getIndex() + "\"";
            buff.append(parent + ":sn" + p.getIndex() + " -> " + name + "\n");
            buff.append(name + " [" + label + " shape=\"ellipse\"]" + "\n");
            Iterator i = p.iterator();
            MSET.Node node = null;
            MSET.Node.ID tmpID;
            while (i.hasNext()) {
                tmpID = (MSET.Node.ID) i.next();
                node = p.getInsert(tmpID);
                append(buff, name, node);
            }
        }
    }

    public static String name(MSET.Node node) {
        return "N_" + node.getID();
    }

    public static String id(MSET.Node node) {
        return "N_" + node.getID() + ":id";
    }

    public static String name(MSET.Node.Position pos) {
        return "p" + pos.getIndex() + "_" + name(pos.getOwnerNode());
    }
}
