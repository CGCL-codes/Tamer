package net.todd.games.boardgame;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Node;

public class BranchGroupAdapter implements IBranchGroup {

    private final BranchGroup branchGroup;

    public BranchGroupAdapter(BranchGroup branchGroup) {
        this.branchGroup = branchGroup;
    }

    public void addChild(Node node) {
        branchGroup.addChild(node);
    }

    public void addChild(IBranchGroup child) {
        branchGroup.addChild(child.underlyingImplementation());
    }

    public void compile() {
        branchGroup.compile();
    }

    public void removeAllChildren() {
        branchGroup.removeAllChildren();
    }

    public BranchGroup underlyingImplementation() {
        return branchGroup;
    }
}
