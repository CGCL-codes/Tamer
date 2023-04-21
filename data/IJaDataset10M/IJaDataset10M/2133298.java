package org.joogie.boogie;

import java.util.HashSet;
import java.util.LinkedList;
import org.joogie.boogie.statements.Statement;

/**
 * @author schaef
 * this is only a stub an might be replaced by other boogie parser/cfg implementations
 */
public class BasicBlock {

    private LocationTag locationTag = null;

    private String blockName;

    public HashSet<BasicBlock> Successors = new HashSet<BasicBlock>();

    public HashSet<BasicBlock> Predecessors = new HashSet<BasicBlock>();

    public LinkedList<Statement> Statements = new LinkedList<Statement>();

    public boolean IsLoopHead = false;

    public void connectToSuccessor(BasicBlock succ) {
        this.Successors.add(succ);
        succ.Predecessors.add(this);
    }

    public void disconnectFromSuccessor(BasicBlock succ) {
        this.Successors.remove(succ);
        succ.Predecessors.remove(this);
    }

    public String getName() {
        return blockName;
    }

    public void setLocationTag(LocationTag lt) {
        locationTag = lt;
    }

    public LocationTag getLocationTag() {
        return locationTag;
    }

    public BasicBlock() {
        this("");
    }

    public BasicBlock(String prefix) {
        blockName = prefix + "Block" + BoogieProgram.getInstance().getUniqueNumber().toString();
    }

    @Override
    public BasicBlock clone() {
        BasicBlock clone = new BasicBlock();
        clone.IsLoopHead = this.IsLoopHead;
        clone.Statements = new LinkedList<Statement>();
        for (Statement s : this.Statements) {
            clone.Statements.addLast(s.clone());
        }
        return clone;
    }

    public BasicBlock clone(String prefix) {
        BasicBlock clone = new BasicBlock(prefix);
        clone.IsLoopHead = this.IsLoopHead;
        clone.Statements = new LinkedList<Statement>();
        for (Statement s : this.Statements) {
            clone.Statements.addLast(s.clone());
        }
        return clone;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    public String toBoogie() {
        StringBuilder sb = new StringBuilder();
        if (locationTag != null) {
            sb.append(locationTag.toString());
        }
        sb.append(blockName + ":\n");
        for (Statement s : Statements) {
            sb.append(s.toBoogie() + ";\n");
        }
        if (Successors.size() > 0) {
            sb.append("\t goto ");
            boolean firstgoto = true;
            for (BasicBlock b : Successors) {
                if (firstgoto) {
                    firstgoto = false;
                } else {
                    sb.append(", ");
                }
                sb.append(b.getName());
            }
            sb.append(";\n");
        } else {
            sb.append("\t return;\n");
        }
        return sb.toString();
    }
}
