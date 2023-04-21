package gov.lanl.Database;

import java.util.*;

public class JNDISearchFilter extends SearchFilter {

    private String object_classname;

    public void setObject(Object obj) {
        object_classname = obj.getClass().getName();
    }

    public String toString() {
        if (filter instanceof SearchBaseLeaf) return convertSearchBaseLeaf((SearchBaseLeaf) filter);
        if (filter instanceof SearchBaseLeafInt) return convertSearchBaseLeafInt((SearchBaseLeafInt) filter);
        if (filter instanceof SearchBaseLeafComparison) return convertSearchBaseLeafComparison((SearchBaseLeafComparison) filter);
        return convertSearchBase((SearchBaseNode) filter);
    }

    private String convertSearchBase(SearchBaseNode SearchTree) {
        StringBuffer filter;
        int num_values = SearchTree.nodes.size();
        if (SearchTree.oper == NOT) filter = new StringBuffer("(!"); else if (SearchTree.oper == AND) filter = new StringBuffer("(&"); else if (SearchTree.oper == OR) filter = new StringBuffer("(|"); else {
            System.out.println("Invalid operator in JNDI toString conversion.");
            System.out.println("  Operator was changed to an OR.");
            filter = new StringBuffer("(|");
        }
        for (Enumeration e = SearchTree.nodes.elements(); e.hasMoreElements(); ) {
            Object node = e.nextElement();
            String node_string;
            if (node instanceof SearchBaseLeaf) node_string = convertSearchBaseLeaf((SearchBaseLeaf) node); else if (node instanceof SearchBaseLeafInt) node_string = convertSearchBaseLeafInt((SearchBaseLeafInt) node); else if (node instanceof SearchBaseNode) node_string = convertSearchBase((SearchBaseNode) node); else if (node instanceof SearchBaseLeafComparison) node_string = convertSearchBaseLeafComparison((SearchBaseLeafComparison) node); else {
                System.out.println("Invalid search base node in JNDI toString conversion.");
                System.out.println("  Search base node ignored.");
                node_string = "";
            }
            filter.append(node_string);
        }
        filter.append(")");
        return filter.toString();
    }

    private String convertSearchBaseLeaf(SearchBaseLeaf Leaf) {
        StringBuffer filter;
        int num_values = Leaf.matches.length;
        if (Leaf.oper == NOT_IN) filter = new StringBuffer("(!"); else filter = new StringBuffer();
        String attr_name = JNDIConfig.translateAttributeName(object_classname, Leaf.elementName);
        if (num_values == 1) filter.append("(" + attr_name + "=" + Leaf.matches[0] + ")"); else {
            filter.append("(|");
            for (int i = 0; i < num_values; i++) filter.append("(" + attr_name + "=" + Leaf.matches[i] + ")");
            filter.append(")");
        }
        if (Leaf.oper == NOT_IN) filter.append(")");
        return filter.toString();
    }

    private String convertSearchBaseLeafInt(SearchBaseLeafInt Leaf) {
        int[] values = Leaf.matches;
        int num_values = values.length;
        String[] string_values = new String[num_values];
        for (int i = 0; i < num_values; i++) string_values[i] = (new Integer(values[i]).toString());
        SearchBaseLeaf temp = new SearchBaseLeaf(Leaf.elementName, Leaf.oper, string_values);
        return convertSearchBaseLeaf(temp);
    }

    private String convertSearchBaseLeafComparison(SearchBaseLeafComparison Leaf) {
        StringBuffer filter;
        String oper_string = ConvertBinaryOperator(Leaf.oper);
        if ("LIKE".equals(oper_string)) oper_string = "=";
        String attr_name = JNDIConfig.translateAttributeName(object_classname, Leaf.elementName);
        return "(" + attr_name + oper_string + Leaf.value + ")";
    }
}
