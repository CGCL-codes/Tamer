package eu.planets_project.pp.plato.sensitivity;

import eu.planets_project.pp.plato.model.tree.TreeNode;

public class EqualWeightModifier implements IWeightModifier {

    public boolean performModification(TreeNode node) {
        for (TreeNode child : node.getChildren()) {
            child.setWeight(1);
        }
        return false;
    }
}
