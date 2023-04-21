package net.confex.db.ora;

import net.confex.tree.ITreeNodeFactory;
import net.confex.utils.Utils;

public class NodeFactory implements ITreeNodeFactory {

    private String current_tree_packages = "net.confex.db.ora.";

    /**
	 * �� ����� ������ ���������� �����.
	 * ���� ����� �� ������ ������������ null
	 * 
	 * @param class_name
	 * @return Class 
	 */
    @SuppressWarnings("unchecked")
    public Class getClassForName(String class_name) {
        String[] tok = Utils.tokenize(current_tree_packages, ",");
        Class cl = null;
        for (int i = 0; i < tok.length; i++) {
            String tree_package = tok[i];
            if (!tree_package.endsWith(".")) tree_package += ".";
            try {
                cl = Class.forName(tree_package + class_name);
                break;
            } catch (ClassNotFoundException ex) {
            }
        }
        return cl;
    }
}
