package serl.equalschecker.alloydom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ListIterator;
import org.eclipse.core.runtime.Status;
import serl.equalschecker.EqualsCheckerPlugin;
import serl.equalschecker.globals.Globals;

/**
 *
 *
 * @author Chandan Raj Rupakheti (rupakhcr@clarkson.edu)
 *
 */
public class FactBlock extends ArrayList<Fact> implements LogicElement {

    /**
	 * Generated serialVersionUID
	 */
    private static final long serialVersionUID = 6810702164795761449L;

    private String tab;

    private boolean conflict;

    /**
	 * Creates a new instance of the class FactBlock
	 * 
	 */
    public FactBlock() {
        super();
        conflict = false;
        this.tab = SymbolTable.TAB + SymbolTable.TAB;
    }

    /**
	 * Creates a new instance of the class FactBlock
	 * 
	 * @param c
	 */
    public FactBlock(Collection<? extends Fact> c) {
        super(c);
        conflict = false;
    }

    /**
	 * Creates a new instance of the class FactBlock
	 * 
	 * @param initialCapacity
	 */
    public FactBlock(int initialCapacity) {
        super(initialCapacity);
        conflict = false;
    }

    /**
	 * @return the tab
	 */
    public String getTab() {
        return tab;
    }

    /**
	 * @param tab the tab to set
	 */
    public void setTab(String tab) {
        this.tab = tab;
    }

    public AlloyType getType() {
        return AlloyPrimType.BOOL_DEFAULT;
    }

    public String getDefinition() {
        if (this.size() == 1) {
            Fact singleFact = this.get(0);
            singleFact.setTab(tab + SymbolTable.TAB);
            return singleFact.getDefinition() + SymbolTable.NEW_LINE;
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append(tab + SymbolTable.BRACE_SMALL_OPEN + SymbolTable.NEW_LINE);
        String childTab = tab + SymbolTable.TAB;
        int i = 1;
        for (Fact aFact : this) {
            aFact.setTab(childTab);
            buffer.append(aFact.getDefinition());
            buffer.append(SymbolTable.NEW_LINE);
            if (i != this.size()) {
                buffer.append(childTab + SymbolTable.OP_AND + SymbolTable.NEW_LINE);
            }
            ++i;
        }
        buffer.append(tab + SymbolTable.BRACE_SMALL_CLOSE + SymbolTable.NEW_LINE);
        return buffer.toString();
    }

    public boolean hasConflict() {
        if (!Globals.isAlloyOptimizationOn()) return false;
        if (this.conflict) return true;
        ListIterator<Fact> fItr = this.listIterator();
        while (fItr.hasNext()) {
            Fact fFact = fItr.next();
            Fact opposite = null;
            if (fFact instanceof FactConditional) {
                opposite = FactNot.getOpposite((FactConditional) fFact);
            }
            if (opposite == null) continue;
            ListIterator<Fact> sItr = this.listIterator(fItr.nextIndex());
            while (sItr.hasNext()) {
                Fact sFact = sItr.next();
                if (sFact instanceof FactConditional) {
                    if (opposite.equals(sFact)) {
                        this.conflict = true;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
	 * Please note that the implementation of this equals method may violate symmetric property
	 * with its parent type. It is more like set equality rather than list equality
	 * with reduced constraint on ordering of elements (Fact in our case)
	 */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FactBlock)) return false;
        FactBlock that = (FactBlock) o;
        if (this.size() != that.size()) return false;
        try {
            if (this.containsAll(that)) return true;
        } catch (Exception e) {
            EqualsCheckerPlugin.log(Status.WARNING, "FactBlock.containsAll has problem.", e);
        }
        return false;
    }

    @Override
    public boolean add(Fact o) {
        boolean optimize = Globals.isAlloyOptimizationOn();
        if (optimize && this.conflict) return false;
        if (o instanceof FactConditional) {
            FactConditional fCond = (FactConditional) o;
            if (fCond.isConflict()) {
                this.conflict = true;
                if (optimize) {
                    this.clear();
                    return false;
                }
            }
            if (fCond.isRedundant() && optimize) return false;
        }
        if (optimize && this.contains(o)) return false;
        return super.add(o);
    }

    @Override
    public void add(int index, Fact o) {
        boolean optimize = Globals.isAlloyOptimizationOn();
        if (optimize && this.conflict) return;
        if (o instanceof FactConditional) {
            FactConditional fCond = (FactConditional) o;
            if (fCond.isConflict()) {
                this.conflict = true;
                if (optimize) {
                    this.clear();
                    return;
                }
            }
            if (fCond.isRedundant() && optimize) return;
        }
        if (optimize && this.contains(o)) return;
        super.add(index, o);
    }

    @Override
    public boolean addAll(Collection<? extends Fact> c) {
        int curSize = this.size();
        for (Fact aFact : c) {
            this.add(aFact);
        }
        return curSize != this.size();
    }

    @Override
    public boolean addAll(int index, Collection<? extends Fact> c) {
        int curSize = this.size();
        for (Fact aFact : c) {
            this.add(index++, aFact);
        }
        return curSize != this.size();
    }
}
