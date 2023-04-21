package b2bpl.bpl.analysis;

import java.util.HashMap;
import b2bpl.bpl.ast.BPLBasicBlock;
import b2bpl.bpl.ast.BPLImplementationBody;

/**
 * Builds up a {@link ControlFlowGraph} for a given BoogiePL procedure
 * implementation.
 *
 * @author Ovidio Mallo
 *
 * @see ControlFlowGraph
 */
public class CFGBuilder {

    /**
   * Builds up a control flow graph for the given {@code body} of a BoogiePL
   * procedure implementation.
   *
   * @param body  The body of a BoogiePL procedure implementation.
   * @return      The control flow graph for the BoogiePL procedure
   *              implementation.
   */
    public ControlFlowGraph build(BPLImplementationBody body) {
        ControlFlowGraph cfg = new ControlFlowGraph();
        HashMap<String, BasicBlock> cfgBlocks = new HashMap<String, BasicBlock>();
        for (BPLBasicBlock bplBlock : body.getBasicBlocks()) {
            BasicBlock cfgBlock = new BasicBlock(bplBlock);
            cfgBlocks.put(bplBlock.getLabel(), cfgBlock);
            cfg.addBlock(cfgBlock);
        }
        for (BasicBlock cfgBlock : cfgBlocks.values()) {
            BPLBasicBlock bplBlock = cfgBlock.getBPLBlock();
            String[] targetLabels = bplBlock.getTransferCommand().getTargetLabels();
            if (targetLabels.length == 0) {
                cfgBlock.addSuccessor(cfg.getExitBlock());
            } else {
                for (String succLabel : targetLabels) {
                    BasicBlock succCFGBlock = cfgBlocks.get(succLabel);
                    cfgBlock.addSuccessor(succCFGBlock);
                }
            }
        }
        return cfg;
    }
}
