package oqube.patchwork.graph;

import java.util.Iterator;
import java.util.List;
import oqube.bytes.ClassFile;
import oqube.bytes.instructions.Instruction;
import oqube.bytes.instructions.Sequence;

/**
 * A basic block is defined as a linear sequence of instructions without any
 * branching out or in the block, except for the last instruction in the block.
 * <p>
 * A basic block is constructed through the Graph factory object.
 * 
 * @author bailly
 * @version $Id: BasicBlock.java 1 2007-03-05 22:06:45Z arnaud.oqube $
 */
public class BasicBlock implements Comparable {

    public static class EndBlock extends BasicBlock {

        public EndBlock() {
            super(Integer.MAX_VALUE, null);
        }

        public boolean isEnd() {
            return true;
        }

        public String toString() {
            return "END";
        }
    }

    ;

    public static class StartBlock extends BasicBlock {

        public StartBlock() {
            super(Integer.MIN_VALUE, null);
        }

        public boolean isStart() {
            return true;
        }

        public String toString() {
            return "START";
        }
    }

    ;

    private ClassFile classFile;

    private int numBlock;

    private int startLine, endLine;

    /**
   * @return Returns the endLine.
   */
    public int getEndLine() {
        return endLine;
    }

    /**
   * @param endLine The endLine to set.
   */
    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }

    /**
   * @return Returns the startLine.
   */
    public int getStartLine() {
        return startLine;
    }

    /**
   * @param startLine The startLine to set.
   */
    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    /**
   * @param pc
   * @param i
   */
    public BasicBlock(int pc, ClassFile cf) {
        this.start = pc;
        this.end = this.start;
        this.classFile = cf;
    }

    private int start, end;

    /** sequence of instructions objects */
    private Sequence instructions = new Sequence(classFile);

    /**
   */
    public void addInstruction(Instruction i) {
        instructions.add(i);
    }

    public Iterator getInstructions() {
        return instructions.iterator();
    }

    /**
   * Return direct access to sequence of code. This method should be used with
   * caution.
   * 
   * @return
   */
    public Sequence getAllInstructions() {
        return instructions;
    }

    /**
   * @param i
   */
    public void setStart(int i) {
        this.start = i;
    }

    /**
   * @return
   */
    public int getEnd() {
        return this.start + instructions.size() - 1;
    }

    /**
   * @return
   */
    public int getStart() {
        return start;
    }

    /**
   * @param i
   */
    public void setEnd(int i) {
        end = i;
    }

    public boolean equals(Object obj) {
        BasicBlock bb = (BasicBlock) obj;
        if (bb == null) return false;
        return (bb.start == start);
    }

    public int hashCode() {
        return start;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Block ").append(numBlock).append('\n');
        sb.append("< ").append(start).append(',').append(getEnd()).append(" >");
        List inslist = instructions.getInstructions();
        if (inslist.size() > 2) sb.append('\n').append(inslist.get(0)).append("\n...\n").append(inslist.get(inslist.size() - 1)); else {
            Iterator it = instructions.iterator();
            while (it.hasNext()) {
                sb.append('\n').append(it.next());
            }
        }
        return sb.toString();
    }

    public int compareTo(Object o) {
        BasicBlock b = (BasicBlock) o;
        if (b == null) return -1;
        return getStart() < b.getStart() ? -1 : (getStart() == b.getStart() ? 0 : 1);
    }

    /**
   * @return
   */
    private int getLength() {
        return instructions.size();
    }

    /**
   * @return Returns the numBlock.
   */
    public int getNumBlock() {
        return numBlock;
    }

    /**
   * @param numBlock
   *          The numBlock to set.
   */
    public void setNumBlock(int numBlock) {
        this.numBlock = numBlock;
    }

    public boolean isStart() {
        return false;
    }

    public boolean isEnd() {
        return false;
    }
}
