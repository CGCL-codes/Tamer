package preprocessing.methods;

import weka.core.FastVector;
import javax.swing.*;
import javax.swing.tree.*;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: lagon
 * Date: Nov 2, 2009
 * Time: 11:08:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class PreprocessorGUIUtilities {

    /**
     *
     *  CONVERTS METHOD NAME TO INDEX
     */
    protected static int findNodeWithName(String name, DefaultMutableTreeNode node) {
        Enumeration<DefaultMutableTreeNode> childs = node.children();
        int idx = 0;
        while (childs.hasMoreElements()) {
            DefaultMutableTreeNode child = childs.nextElement();
            String s = (String) child.getUserObject();
            if (s.compareTo(name) == 0) {
                return idx;
            }
            idx++;
        }
        return -1;
    }

    public static void addPreprocessingMethodToTree(Preprocessor method, DefaultMutableTreeNode rootNode, int startPathDepth) {
        String[] methodPath = method.getPreprocessingMethodTree().split("\\.");
        for (int i = startPathDepth; i < methodPath.length; i++) {
            int ret = findNodeWithName(methodPath[i], rootNode);
            if (ret == -1) {
                DefaultMutableTreeNode t = new DefaultMutableTreeNode(methodPath[i]);
                rootNode.add(t);
                rootNode = t;
            } else {
                rootNode = (DefaultMutableTreeNode) rootNode.getChildAt(ret);
            }
        }
    }

    public static void addPreprocessingMethodToTree(Preprocessor method, DefaultMutableTreeNode rootNode) {
        addPreprocessingMethodToTree(method, rootNode, 0);
    }

    public static DefaultTreeModel createTreeFromList(FastVector list) {
        return createTreeFromList(list.elements(), "Preprocessing Methods");
    }

    public static DefaultTreeModel createTreeFromList(FastVector list, String rootNodeName) {
        return createTreeFromList(list.elements(), rootNodeName);
    }

    public static DefaultTreeModel createTreeFromList(Enumeration<Preprocessor> list) {
        return createTreeFromList(list, "Preprocessing Methods");
    }

    public static DefaultTreeModel createTreeFromList(Enumeration<Preprocessor> list, String rootNodeName) {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(rootNodeName);
        while (list.hasMoreElements()) {
            addPreprocessingMethodToTree(list.nextElement(), rootNode);
        }
        return new DefaultTreeModel(rootNode);
    }

    public static Iterator<Preprocessor>[] getEnumerationsByPackages(ArrayList<Preprocessor> list) {
        return getEnumerationsByPackages(list.iterator());
    }

    public static Iterator<Preprocessor>[] getEnumerationsByPackages(Iterator<Preprocessor> list) {
        HashMap<String, ArrayList<Preprocessor>> hm = new HashMap<String, ArrayList<Preprocessor>>();
        while (list.hasNext()) {
            Preprocessor p = list.next();
            String[] path = p.getPreprocessingMethodTree().split("\\.");
            String node = path[0];
            ArrayList<Preprocessor> al = hm.get(node);
            if (al == null) {
                al = new ArrayList<Preprocessor>(5);
                hm.put(node, al);
            }
            al.add(p);
        }
        Iterator<Preprocessor>[] result = new Iterator[hm.size()];
        Iterator<ArrayList<Preprocessor>> iter = hm.values().iterator();
        int idx = 0;
        while (iter.hasNext()) {
            result[idx] = iter.next().iterator();
            idx++;
        }
        return result;
    }
}
