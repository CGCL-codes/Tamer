package com.loribel.tools.file.tree;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.loribel.commons.util.CTools;

/**
 * Groug tree node File by package.
 */
public class GB_FileTNGroup {

    /**
     * key: (String) directory full path
     * value: (GB_FileTN) File node
     */
    private Map nodeMap = new HashMap();

    private File[] files;

    private List rootNodes = new ArrayList();

    public GB_FileTNGroup(File[] a_files) {
        super();
        files = a_files;
        buildMap();
    }

    public GB_FileTN[] getRoots() {
        return (GB_FileTN[]) rootNodes.toArray(new GB_FileTN[rootNodes.size()]);
    }

    private void buildMap() {
        nodeMap.clear();
        int len = CTools.getSize(files);
        for (int i = 0; i < len; i++) {
            File l_file = files[i];
            addToMap(l_file);
        }
    }

    private void addToMap(File a_file) {
        if (a_file == null) {
            return;
        }
        File l_parent = a_file.getParentFile();
        if (l_parent == null) {
            GB_FileTN l_node = getTreeNode(a_file);
            rootNodes.add(l_node);
        } else {
            GB_FileTN l_nodeParent = getTreeNode(l_parent);
            GB_FileTN l_node = getTreeNode(a_file);
            l_nodeParent.add(l_node);
            addToMap(l_parent);
        }
    }

    private GB_FileTN getTreeNode(File a_file) {
        String l_key = a_file.getAbsolutePath();
        GB_FileTN retour = (GB_FileTN) nodeMap.get(l_key);
        if (retour == null) {
            if (a_file.isDirectory()) {
                retour = new GB_FileTN(a_file);
            } else {
                retour = new GB_FileTN(a_file);
            }
            nodeMap.put(l_key, retour);
        }
        return retour;
    }
}
