package it.aco.mandragora.vo;

import java.util.Collection;

public class TreeNode1VO extends ValueObject {

    private String idTreeNode1;

    private String idTreeNode0;

    private String description;

    private Collection<TreeNode11VO> treeNode11VOs;

    public String getIdTreeNode1() {
        return idTreeNode1;
    }

    public void setIdTreeNode1(String idTreeNode1) {
        this.idTreeNode1 = idTreeNode1;
    }

    public String getIdTreeNode0() {
        return idTreeNode0;
    }

    public void setIdTreeNode0(String idTreeNode0) {
        this.idTreeNode0 = idTreeNode0;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection<TreeNode11VO> getTreeNode11VOs() {
        return treeNode11VOs;
    }

    public void setTreeNode11VOs(Collection<TreeNode11VO> treeNode11VOs) {
        this.treeNode11VOs = treeNode11VOs;
    }
}
