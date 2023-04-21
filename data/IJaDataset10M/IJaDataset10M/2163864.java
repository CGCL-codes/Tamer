package net.sf.openforge.lim;

import java.util.*;
import net.sf.openforge.lim.io.*;
import net.sf.openforge.lim.memory.*;
import net.sf.openforge.lim.op.*;

/**
 * One of the potential problems with defining a {@link Visitor} implementation
 * to be a subclass of {@link DefaultVisitor} is that it may hide a missing
 * method in the implementation, whereas a direct implementer of {@link Visitor}
 * would fail in compilation.  Scanner mitigates that problem by providing generic
 * {@link Module} hierarchy traversal without the need to subclass
 * {@link DefaultVisitor}.
 * <P>
 * A {@link Visitor} implementer constructs a Scanner using itself as the
 * argument.  Visiting then begins as usual.  When a {@link Module} object
 * is visited, the visitor may continue the traversal into the module by
 * calling, for example, {@link Scanner#enter(Block)} (as opposed to a {@link DefaultVisitor}
 * subclass, which would call <code>super.visit(block)</code>).
 *
 * @version $Id: Scanner.java 88 2006-01-11 22:39:52Z imiller $ 
 */
public final class Scanner extends DefaultVisitor {

    private static final String _RCS_ = "$Rev: 88 $";

    /** The real visitor */
    private Visitor visitor;

    /** Set true if all iterators should protect against
        ConcurrentModificationExceptions */
    private boolean safeMode;

    /**
     * Constructs a new Scanner.
     *
     * @param visitor the visitor for which traversal is being performed
     */
    public Scanner(Visitor visitor) {
        this(visitor, false);
    }

    /**
     * Constructs a new scanner which may be put into safe mode, in
     * which the iterators used to traverse module components operate
     * on newly created lists to avoid concurrent modification
     * exceptions.
     *
     * @param visitor the visitor for which traversal is being performed
     * @param safeMode true to create new lists to iterate over for
     * all iterators.
     */
    public Scanner(Visitor visitor, boolean safeMode) {
        this.visitor = visitor;
        this.safeMode = safeMode;
    }

    public void visit(Design design) {
        visitor.visit(design);
    }

    /**
     * Continue visiting inside a Design.
     */
    public void enter(Design design) {
        super.visit(design);
    }

    public void visit(Task task) {
        visitor.visit(task);
    }

    /**
     * Continue visiting inside a Task.
     */
    public void enter(Task task) {
        super.visit(task);
    }

    public void visit(Call call) {
        visitor.visit(call);
    }

    /**
     * Continue visiting inside a Call's Procedure.
     */
    public void enter(Call call) {
        super.visit(call);
    }

    public void visit(Procedure procedure) {
        visitor.visit(procedure);
    }

    /**
     * Continue visiting inside a Procedure.
     */
    public void enter(Procedure procedure) {
        super.visit(procedure);
    }

    public void visit(Block block) {
        visitor.visit(block);
    }

    /**
     * Continue visiting inside a Block.
     */
    public void enter(Block block) {
        super.visit(block);
    }

    public void visit(Loop loop) {
        visitor.visit(loop);
    }

    /**
     * Continue visiting inside a Loop.
     */
    public void enter(Loop loop) {
        super.visit(loop);
    }

    public void visit(WhileBody whileBody) {
        visitor.visit(whileBody);
    }

    /**
     * Continue visiting inside a WhileBody.
     */
    public void enter(WhileBody whileBody) {
        super.visit(whileBody);
    }

    public void visit(UntilBody untilBody) {
        visitor.visit(untilBody);
    }

    /**
     * Continue visiting inside an UntilBody.
     */
    public void enter(UntilBody untilBody) {
        super.visit(untilBody);
    }

    public void visit(ForBody forBody) {
        visitor.visit(forBody);
    }

    /** 
     * Continue visiting inside a ForBody.
     */
    public void enter(ForBody forBody) {
        super.visit(forBody);
    }

    public void visit(Branch branch) {
        visitor.visit(branch);
    }

    /**
     * Continue visiting inside a Branch.
     */
    public void enter(Branch branch) {
        super.visit(branch);
    }

    public void visit(Decision decision) {
        visitor.visit(decision);
    }

    /**
     * Continue visiting inside a Decision.
     */
    public void enter(Decision decision) {
        super.visit(decision);
    }

    public void visit(Switch sw) {
        visitor.visit(sw);
    }

    /**
     * Continue visiting inside a Switch.
     */
    public void enter(Switch sw) {
        super.visit(sw);
    }

    public void enter(Scoreboard scbd) {
        super.visit(scbd);
    }

    public void visit(Scoreboard scbd) {
        visitor.visit(scbd);
    }

    public void enter(PriorityMux pmux) {
        super.visit(pmux);
    }

    public void visit(PriorityMux pmux) {
        visitor.visit(pmux);
    }

    public void enter(RegisterReferee regReferee) {
        super.visit(regReferee);
    }

    public void visit(RegisterReferee regReferee) {
        visitor.visit(regReferee);
    }

    public void enter(RegisterGateway regGateway) {
        super.visit(regGateway);
    }

    public void visit(RegisterGateway regGateway) {
        visitor.visit(regGateway);
    }

    public void enter(MemoryReferee memReferee) {
        super.visit(memReferee);
    }

    public void visit(MemoryReferee memReferee) {
        visitor.visit(memReferee);
    }

    public void enter(MemoryGateway memGateway) {
        super.visit(memGateway);
    }

    public void visit(MemoryGateway memGateway) {
        visitor.visit(memGateway);
    }

    public void enter(Latch latch) {
        super.visit(latch);
    }

    public void visit(Latch latch) {
        visitor.visit(latch);
    }

    public void enter(Kicker kicker) {
        super.visit(kicker);
    }

    public void visit(Kicker kicker) {
        visitor.visit(kicker);
    }

    public void enter(ArrayWrite comp) {
        super.visit(comp);
    }

    public void visit(ArrayWrite comp) {
        visitor.visit(comp);
    }

    public void enter(ArrayRead comp) {
        super.visit(comp);
    }

    public void visit(ArrayRead comp) {
        visitor.visit(comp);
    }

    public void enter(HeapWrite comp) {
        super.visit(comp);
    }

    public void visit(HeapWrite comp) {
        visitor.visit(comp);
    }

    public void enter(HeapRead comp) {
        super.visit(comp);
    }

    public void visit(HeapRead comp) {
        visitor.visit(comp);
    }

    public void enter(AbsoluteMemoryRead comp) {
        super.visit(comp);
    }

    public void visit(AbsoluteMemoryRead comp) {
        visitor.visit(comp);
    }

    public void enter(AbsoluteMemoryWrite comp) {
        super.visit(comp);
    }

    public void visit(AbsoluteMemoryWrite comp) {
        visitor.visit(comp);
    }

    public void enter(MemoryRead comp) {
        super.visit(comp);
    }

    public void visit(MemoryRead comp) {
        visitor.visit(comp);
    }

    public void enter(MemoryWrite comp) {
        super.visit(comp);
    }

    public void visit(MemoryWrite comp) {
        visitor.visit(comp);
    }

    public void enter(PinRead comp) {
        super.visit(comp);
    }

    public void visit(PinRead comp) {
        visitor.visit(comp);
    }

    public void enter(PinWrite comp) {
        super.visit(comp);
    }

    public void visit(PinWrite comp) {
        visitor.visit(comp);
    }

    public void enter(PinStateChange comp) {
        super.visit(comp);
    }

    public void visit(PinStateChange comp) {
        visitor.visit(comp);
    }

    public void enter(PinReferee pinReferee) {
        super.visit(pinReferee);
    }

    public void visit(PinReferee pinReferee) {
        visitor.visit(pinReferee);
    }

    public void enter(TaskCall mod) {
        super.visit(mod);
    }

    public void visit(TaskCall mod) {
        visitor.visit(mod);
    }

    public void enter(SimplePinAccess mod) {
        super.visit(mod);
    }

    public void visit(SimplePinAccess mod) {
        visitor.visit(mod);
    }

    public void enter(FifoAccess mod) {
        super.visit(mod);
    }

    public void visit(FifoAccess mod) {
        visitor.visit(mod);
    }

    public void enter(FifoRead mod) {
        super.visit(mod);
    }

    public void visit(FifoRead mod) {
        visitor.visit(mod);
    }

    public void enter(FifoWrite mod) {
        super.visit(mod);
    }

    public void visit(FifoWrite mod) {
        visitor.visit(mod);
    }

    public void enter(EndianSwapper mod) {
        super.visit(mod);
    }

    public void visit(EndianSwapper mod) {
        visitor.visit(mod);
    }

    public void visit(AddOp add) {
        visitor.visit(add);
    }

    public void visit(AndOp andOp) {
        visitor.visit(andOp);
    }

    public void visit(NumericPromotionOp numericPromotion) {
        visitor.visit(numericPromotion);
    }

    public void visit(CastOp cast) {
        visitor.visit(cast);
    }

    public void visit(ComplementOp complement) {
        visitor.visit(complement);
    }

    public void visit(ConditionalAndOp conditionalAnd) {
        visitor.visit(conditionalAnd);
    }

    public void visit(ConditionalOrOp conditionalOr) {
        visitor.visit(conditionalOr);
    }

    public void visit(Constant constant) {
        visitor.visit(constant);
    }

    public void visit(DivideOp divide) {
        visitor.visit(divide);
    }

    public void visit(EqualsOp equals) {
        visitor.visit(equals);
    }

    public void visit(GreaterThanEqualToOp greaterThanEqualTo) {
        visitor.visit(greaterThanEqualTo);
    }

    public void visit(GreaterThanOp greaterThan) {
        visitor.visit(greaterThan);
    }

    public void visit(LeftShiftOp leftShift) {
        visitor.visit(leftShift);
    }

    public void visit(LessThanEqualToOp lessThanEqualTo) {
        visitor.visit(lessThanEqualTo);
    }

    public void visit(LessThanOp lessThan) {
        visitor.visit(lessThan);
    }

    public void visit(MinusOp minus) {
        visitor.visit(minus);
    }

    public void visit(ModuloOp modulo) {
        visitor.visit(modulo);
    }

    public void visit(MultiplyOp multiply) {
        visitor.visit(multiply);
    }

    public void visit(NoOp nop) {
        visitor.visit(nop);
    }

    public void visit(TimingOp top) {
        visitor.visit(top);
    }

    public void visit(NotEqualsOp notEquals) {
        visitor.visit(notEquals);
    }

    public void visit(NotOp not) {
        visitor.visit(not);
    }

    public void visit(OrOp or) {
        visitor.visit(or);
    }

    public void visit(PlusOp plus) {
        visitor.visit(plus);
    }

    public void visit(ReductionOrOp reductionOr) {
        visitor.visit(reductionOr);
    }

    public void visit(RegisterRead read) {
        visitor.visit(read);
    }

    public void visit(RegisterWrite write) {
        visitor.visit(write);
    }

    public void visit(RightShiftOp rightShift) {
        visitor.visit(rightShift);
    }

    public void visit(RightShiftUnsignedOp rightShiftUnsigned) {
        visitor.visit(rightShiftUnsigned);
    }

    public void visit(ShortcutIfElseOp shortcutIfElse) {
        visitor.visit(shortcutIfElse);
    }

    public void visit(SubtractOp subtract) {
        visitor.visit(subtract);
    }

    public void visit(XorOp xor) {
        visitor.visit(xor);
    }

    public void visit(InBuf ib) {
        visitor.visit(ib);
    }

    public void visit(OutBuf ob) {
        visitor.visit(ob);
    }

    public void visit(Reg reg) {
        visitor.visit(reg);
    }

    public void visit(SRL16 srl_16) {
        visitor.visit(srl_16);
    }

    public void visit(Mux m) {
        visitor.visit(m);
    }

    public void visit(And a) {
        visitor.visit(a);
    }

    public void visit(Not n) {
        visitor.visit(n);
    }

    public void visit(Or o) {
        visitor.visit(o);
    }

    public void visit(EncodedMux m) {
        visitor.visit(m);
    }

    public void visit(MemoryBank m) {
        visitor.visit(m);
    }

    public void visit(SimplePin m) {
        visitor.visit(m);
    }

    public void visit(SimplePinRead m) {
        visitor.visit(m);
    }

    public void visit(SimplePinWrite m) {
        visitor.visit(m);
    }

    /**
     * Creates a new LinkedList with the contents of the given
     * collection and returns a new iterator over that linked list if
     * this Scanner is in safe mode.
     *
     * @param collection the 'Collection' to iterate over
     * @return an 'Iterator' over the given collection
     */
    protected Iterator getIterator(Collection collection) {
        if (safeMode) {
            return (new LinkedList(collection)).iterator();
        }
        return super.getIterator(collection);
    }
}
