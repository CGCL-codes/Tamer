package org.renjin.compiler.cfg;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import java.io.IOException;
import java.util.List;
import org.junit.Test;
import org.renjin.compiler.CompilerTestCase;
import org.renjin.compiler.cfg.BasicBlock;
import org.renjin.compiler.cfg.ControlFlowGraph;
import org.renjin.compiler.cfg.DominanceTree;
import org.renjin.compiler.ir.tac.IRBody;

public class DominanceTreeTest extends CompilerTestCase {

    @Test
    public void immediateDominators() {
        IRBody block = buildScope("y<-1; if(q) y<-y+1 else y<-4; y");
        ControlFlowGraph cfg = new ControlFlowGraph(block);
        System.out.println(cfg);
        BasicBlock bb0 = cfg.getBasicBlocks().get(0);
        BasicBlock bb1 = cfg.getBasicBlocks().get(1);
        BasicBlock bb2 = cfg.getBasicBlocks().get(2);
        BasicBlock bb3 = cfg.getBasicBlocks().get(3);
        DominanceTree domTree = new DominanceTree(cfg);
        assertThat(domTree.getImmediateDominator(bb1), equalTo(bb0));
        assertThat(domTree.getImmediateDominator(bb2), equalTo(bb0));
        assertThat(domTree.getImmediateDominator(bb3), equalTo(bb0));
        assertThat(domTree.getImmediateDominator(cfg.getExit()), equalTo(cfg.getEntry()));
    }

    @Test
    public void dominanceFrontier() throws IOException {
        IRBody block = parseCytron();
        ControlFlowGraph cfg = new ControlFlowGraph(block);
        List<BasicBlock> bb = cfg.getLiveBasicBlocks();
        DominanceTree dtree = new DominanceTree(cfg);
        assertThat(dtree.getFrontier(bb.get(1)), itemsEqualTo(bb.get(1), cfg.getExit()));
        assertThat(dtree.getFrontier(bb.get(2)), itemsEqualTo(bb.get(7)));
        assertThat(dtree.getFrontier(bb.get(3)), itemsEqualTo(bb.get(5)));
        assertThat(dtree.getFrontier(bb.get(4)), itemsEqualTo(bb.get(5)));
        assertThat(dtree.getFrontier(bb.get(5)), itemsEqualTo(bb.get(7)));
        assertThat(dtree.getFrontier(bb.get(6)), itemsEqualTo(bb.get(7)));
        assertThat(dtree.getFrontier(bb.get(7)), itemsEqualTo(bb.get(1), cfg.getExit()));
        assertThat(dtree.getFrontier(bb.get(8)), itemsEqualTo(bb.get(1), bb.get(8), cfg.getExit()));
        assertThat(dtree.getFrontier(bb.get(9)), itemsEqualTo(bb.get(10)));
        assertThat(dtree.getFrontier(bb.get(10)), itemsEqualTo(bb.get(1), bb.get(8), cfg.getExit()));
        assertThat(dtree.getFrontier(bb.get(11)), itemsEqualTo(bb.get(1), cfg.getExit()));
    }
}
