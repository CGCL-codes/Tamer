package cn.edu.sjtu.stap.cfg;

public class CfgCall extends CfgNode {

    private static final long serialVersionUID = 1L;

    private CfgReturn returnNode;

    public CfgCall(CfgNodeKind kind) {
        super(kind);
    }

    public CfgReturn getReturnNode() {
        return returnNode;
    }

    public void setReturnNode(CfgReturn r) {
        this.returnNode = r;
    }
}
