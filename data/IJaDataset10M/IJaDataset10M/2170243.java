package net.sf.signs.intermediate;

import net.sf.signs.*;
import net.sf.signs.gates.InterpreterComponent;
import java.util.*;

@SuppressWarnings("serial")
public class SequenceOfStatements extends SequentialStatement {

    private static final boolean dump = false;

    private ArrayList<SequentialStatement> statements;

    public SequenceOfStatements(IntermediateObject parent_, SourceLocation location_) {
        super(parent_, location_);
        statements = new ArrayList<SequentialStatement>();
    }

    public void add(SequentialStatement stmt_) {
        statements.add(stmt_);
        stmt_.setParent(this);
    }

    public void append(SequenceOfStatements seq_) {
        int n = seq_.getNumStatements();
        for (int i = 0; i < n; i++) {
            SequentialStatement stmt = seq_.getStatement(i);
            add(stmt);
            stmt.setParent(this, true);
        }
    }

    public int getNumStatements() {
        return statements.size();
    }

    public SequentialStatement getStatement(int idx_) {
        return (SequentialStatement) statements.get(idx_);
    }

    public boolean isSynthesizable(OperationCache cache_) throws SignsException {
        for (int i = 0; i < getNumStatements(); i++) {
            SequentialStatement stmt = getStatement(i);
            if (stmt instanceof SequentialWait) {
                if (i != 0) {
                    System.out.println("Warning: for a process to be synthesizable, the (single) wait statement has to be the first statement of the process.");
                    return false;
                }
            }
            if (!stmt.isSynthesizable(cache_)) return false;
        }
        return true;
    }

    public Bindings computeBindings(Bindings oldBindings_, Clock clock_, OperationCache cache_) throws SignsException, SignsException {
        Clock clock = clock_;
        Bindings newBindings = new Bindings(oldBindings_);
        for (int i = 0; i < getNumStatements(); i++) {
            SequentialStatement stmt = getStatement(i);
            if (stmt instanceof SequentialWait) {
                if (i > 0) throw new SignsException("Wait statement has to be the first statement in process to be synthesizeable.", stmt.getLocation());
                if (clock != null) throw new SignsException("Clock has already been specified in this scope.", stmt.getLocation());
                SequentialWait sw = (SequentialWait) stmt;
                clock = sw.getClock(cache_);
                System.out.println("Clock detected: " + clock);
            } else {
                if (dump) {
                    System.out.println("SequenceOfStatements: computing bindings for " + stmt);
                    System.out.println("SequenceOfStatements: bindings before " + stmt + ": ");
                    newBindings.dumpBindings();
                }
                Bindings tmpBindings = stmt.computeBindings(newBindings, clock, cache_);
                if (dump) {
                    System.out.println("SequenceOfStatements: new bindings from " + stmt + ": ");
                    tmpBindings.dumpBindings();
                }
                newBindings.merge(tmpBindings, this, cache_, getLocation());
                if (dump) {
                    System.out.println("SequenceOfStatements: new bindings after merge of results from " + stmt + ": ");
                    newBindings.dumpBindings();
                }
            }
        }
        return newBindings;
    }

    public void generateCode(InterpreterComponent interpreter_, OperationCache cache_) throws SignsException {
        for (int i = 0; i < getNumStatements(); i++) {
            SequentialStatement stmt = getStatement(i);
            stmt.generateCode(interpreter_, cache_);
        }
    }

    @Override
    public int getNumChildren() {
        return statements.size();
    }

    @Override
    public IntermediateObject getChild(int idx_) {
        return statements.get(idx_);
    }
}
