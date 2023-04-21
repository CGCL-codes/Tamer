package sourceagile.development.client.classesList;

import java.util.HashMap;
import sourceagile.development.client.FilesViewList;
import sourceagile.development.client.features.OptionsIcons;
import sourceagile.shared.entities.entry.ClassFile;
import sourceagile.shared.utilities.FeatureNameGenerator;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Node;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;
import com.gwtext.client.widgets.tree.event.TreeNodeListenerAdapter;

public class ComponentClassesList {

    public ComponentClassesList(TreePanel tree, TreeNode componentNode, HashMap<String, ClassFile> entries, String specificationPath, int viewOption) {
        String currentPath = componentNode.getId();
        int specPathSize = 0;
        if (specificationPath != null && specificationPath.length() > 0) {
            specPathSize = specificationPath.split("/").length;
        }
        for (ClassFile entry : entries.values()) {
            if (OptionsIcons.OPTION_REQUIREMENTS != viewOption) {
                String[] entryPath = entry.getFilePath().split("/");
                for (int i = specPathSize; i < entryPath.length; i++) {
                    TreeNode parentNode = tree.getNodeById(currentPath);
                    if (!entryPath[i].equals("")) {
                        currentPath += "/" + entryPath[i];
                        if (tree.getNodeById(currentPath) == null) {
                            TreeNode node = new TreeNode(FeatureNameGenerator.spacedName(entryPath[i], entry.getClassLocale()));
                            node.setExpanded(true);
                            node.setId(currentPath);
                            node.setIcon("js/ext/resources/images/default/tree/empty.gif");
                            parentNode.appendChild(node);
                        }
                    }
                }
            }
            TreeNode parentNode = tree.getNodeById(currentPath);
            TreeNode node = getFeatureLink(entry);
            node.setId(entry.getFilePath() + "." + entry.getFileName());
            node.setExpanded(true);
            parentNode.appendChild(node);
            currentPath = componentNode.getId();
        }
    }

    private TreeNode getFeatureLink(final ClassFile entry) {
        String className = entry.getFileName();
        if (entry.getFeature() != null) {
            className = entry.getFeature().getFeatureName();
        }
        if (entry.getClassDoc().getClassStatus() != null) {
            className += " <font color='red' size=1>*</font>";
        }
        TreeNode treeNode = new TreeNode(className);
        treeNode.setIcon("js/ext/resources/images/default/tree/world.gif");
        treeNode.addListener(new TreeNodeListenerAdapter() {

            public void onClick(Node node, EventObject e) {
                FilesViewList.showClass(entry, OptionsIcons.OPTION_DESCRIPTION);
            }
        });
        return treeNode;
    }
}
