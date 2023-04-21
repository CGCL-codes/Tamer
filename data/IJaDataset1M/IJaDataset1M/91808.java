package org.deft.repository.ast.transform;

import org.deft.repository.ast.TreeNode;

public interface ITreeNodeFilter {

    public boolean accept(TreeNode node);
}
