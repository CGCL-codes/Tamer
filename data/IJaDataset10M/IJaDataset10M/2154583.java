package visreed.extension.javaCC.model.payload;

import higraph.view.HigraphView;
import tm.backtrack.BTTimeManager;
import visreed.extension.javaCC.model.tag.JavaCCTag;
import visreed.extension.javaCC.view.JavaCodeNodeView;
import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedPayload;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.view.VisreedNodeView;

/**
 * @author Xiaoyu Guo
 */
public class JavaCodeProductionPayload extends ProductionPayload {

    private String code;

    public JavaCodeProductionPayload() {
        super(JavaCCTag.JAVA_CODE_PRODUCTION);
        this.code = "";
    }

    public JavaCodeProductionPayload(String code) {
        super(JavaCCTag.JAVA_CODE_PRODUCTION);
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /** The maximum length of the code, for display */
    public static final int MAX_DESC_CODE_DISPLAY_LENGTH = 10;

    @Override
    public String format(VisreedNode currentNode) {
        String result = "";
        String code = this.getCode();
        if (code == null) {
            result = "null";
        } else if (code.length() == 0) {
            result = "\"\"";
        } else if (code.length() < MAX_DESC_CODE_DISPLAY_LENGTH) {
            result = code;
        } else {
            result = code.substring(0, MAX_DESC_CODE_DISPLAY_LENGTH - 1);
            result += "...";
        }
        return result;
    }

    @Override
    public String getDescription() {
        return "Java Code";
    }

    @Override
    public VisreedNodeView constructView(HigraphView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> sgv, VisreedNode node, BTTimeManager timeman) {
        return new JavaCodeNodeView(sgv, node, timeman);
    }
}
