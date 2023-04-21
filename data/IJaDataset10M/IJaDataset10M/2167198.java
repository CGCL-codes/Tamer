package au.gov.nla.aons.mvc.util;

import com.jenkov.prizetags.tree.impl.Tree;
import com.jenkov.prizetags.tree.itf.ITreeNode;

public class RepositoryScanTree extends Tree {

    /**
     * 
     */
    private static final long serialVersionUID = -4087857800483047878L;

    private Long repositoryScanId;

    public RepositoryScanTree() {
        super();
    }

    public RepositoryScanTree(ITreeNode root) {
        super(root);
    }

    public Long getRepositoryScanId() {
        return repositoryScanId;
    }

    public void setRepositoryScanId(Long repositoryScanId) {
        this.repositoryScanId = repositoryScanId;
    }
}
