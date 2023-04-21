package org.dml.level040;

import org.dml.level010.Symbol;
import org.dml.level025.SetOfTerminalSymbols;
import org.dml.level030.DomainPointer;
import org.dml.level030.Pointer;
import org.dml.tools.RunTime;
import org.dml.tools.TwoKeyHashMap;
import org.references.Position;

/**
 * EC is this EC<br>
 * EC->ref2prev->another EC(1)<br>
 * EC->ref2elem->Symbol(which is encapsulated in this EC)<br>
 * EC->ref2next->another EC(2)<br>
 * <br>
 * AllEC->EC<br>
 * AllPrev->ref2prev<br>
 * AllNext->ref2Next<br>
 * AllElems->ref2elem<br>
 * AllEC->another EC(1)<br>
 * AllEC->another EC(2)<br>
 * 
 * the same EC cannot be used in 2 listsOfEC, else lots of inconsistencies will happen<br>
 */
public class ElementCapsule {

    private static final TwoKeyHashMap<Level040_DMLEnvironment, SetOfTerminalSymbols, ElementCapsule> allElementCapsuleInstances = new TwoKeyHashMap<Level040_DMLEnvironment, SetOfTerminalSymbols, ElementCapsule>();

    private final SetOfTerminalSymbols selfAsSet;

    private final Level040_DMLEnvironment env;

    private final DomainPointer cachedRef2Prev;

    private final Pointer cachedRef2Element;

    private final DomainPointer cachedRef2Next;

    /**
	 * private constructor<br>
	 * EC must already exist in storage<br>
	 * 
	 */
    private ElementCapsule(Level040_DMLEnvironment passedEnv, SetOfTerminalSymbols existingSelfAsSet, DomainPointer existingRef2Prev, Pointer existingRef2Element, DomainPointer existingRef2Next) {
        RunTime.assumedNotNull(existingSelfAsSet, passedEnv);
        RunTime.assumedTrue(passedEnv.isInitedSuccessfully());
        selfAsSet = existingSelfAsSet;
        env = passedEnv;
        cachedRef2Prev = existingRef2Prev;
        cachedRef2Element = existingRef2Element;
        cachedRef2Next = existingRef2Next;
        RunTime.assumedTrue(this.isItself());
    }

    private static final void registerInstance(Level040_DMLEnvironment passedEnv, SetOfTerminalSymbols self, ElementCapsule newOne) {
        RunTime.assumedNotNull(passedEnv, self, newOne);
        RunTime.assumedFalse(allElementCapsuleInstances.ensure(passedEnv, self, newOne));
    }

    private static final ElementCapsule getInstance(Level040_DMLEnvironment passedEnv, SetOfTerminalSymbols self) {
        RunTime.assumedNotNull(passedEnv, self);
        return allElementCapsuleInstances.get(passedEnv, self);
    }

    public static ElementCapsule getExistingElementCapsule(Level040_DMLEnvironment passedEnv, Symbol existingSymbol) {
        RunTime.assumedNotNull(passedEnv, existingSymbol);
        RunTime.assumedTrue(passedEnv.isInitedSuccessfully());
        if (!isElementCapsule(passedEnv, existingSymbol)) {
            RunTime.badCall("existingSelfSymbol is not already an ElementCapsule");
        }
        SetOfTerminalSymbols existingSet = passedEnv.getAsSet(existingSymbol);
        existingSet.assumedValid();
        RunTime.assumedTrue(existingSet.getAsSymbol() == existingSymbol);
        RunTime.assumedTrue(existingSet.size() == 3);
        ElementCapsule existingEC = getInstance(passedEnv, existingSet);
        if (null == existingEC) {
            Symbol ref2Prev = passedEnv.findCommonTerminalForInitials(passedEnv.allPrevElementCapsules_Set.getAsSymbol(), existingSymbol);
            RunTime.assumedNotNull(ref2Prev);
            DomainPointer existingRef2Prev = passedEnv.getExistingDomainPointer(ref2Prev, passedEnv.allElementCapsules_Set.getAsSymbol(), true);
            Symbol ref2Next = passedEnv.findCommonTerminalForInitials(passedEnv.allNextElementCapsules_Set.getAsSymbol(), existingSymbol);
            RunTime.assumedNotNull(ref2Next);
            DomainPointer existingRef2Next = passedEnv.getExistingDomainPointer(ref2Next, passedEnv.allElementCapsules_Set.getAsSymbol(), true);
            Symbol ref2Elem = passedEnv.findCommonTerminalForInitials(passedEnv.allRef2ElementsInEC_Set.getAsSymbol(), existingSymbol);
            RunTime.assumedNotNull(ref2Elem);
            Pointer existingRef2Element = passedEnv.getExistingPointer(ref2Elem, true);
            existingEC = new ElementCapsule(passedEnv, existingSet, existingRef2Prev, existingRef2Element, existingRef2Next);
            existingEC.assumedIsValidCapsule();
            registerInstance(passedEnv, existingSet, existingEC);
        } else {
            existingEC.assumedIsValidCapsule();
        }
        return existingEC;
    }

    public static ElementCapsule getNewEmptyElementCapsule(Level040_DMLEnvironment passedEnv, Symbol existingSymbol) {
        RunTime.assumedNotNull(passedEnv, existingSymbol);
        RunTime.assumedTrue(passedEnv.isInitedSuccessfully());
        if (isElementCapsule(passedEnv, existingSymbol)) {
            RunTime.badCall("existingSelfSymbol is already an ElementCapsule");
        }
        SetOfTerminalSymbols existingSet = passedEnv.getAsSet(existingSymbol);
        existingSet.assumedValid();
        RunTime.assumedTrue(existingSet.getAsSymbol() == existingSymbol);
        RunTime.assumedTrue(existingSet.size() == 0);
        DomainPointer newRef2Prev = passedEnv.getNewNullDomainPointer(passedEnv.allElementCapsules_Set.getAsSymbol());
        DomainPointer newRef2Next = passedEnv.getNewNullDomainPointer(passedEnv.allElementCapsules_Set.getAsSymbol());
        Pointer newRef2Element = passedEnv.getNewNullPointer();
        RunTime.assumedNull(newRef2Element.getPointee());
        RunTime.assumedFalse(existingSet.addToSet(newRef2Prev.getAsSymbol()));
        RunTime.assumedFalse(existingSet.addToSet(newRef2Element.getAsSymbol()));
        RunTime.assumedFalse(existingSet.addToSet(newRef2Next.getAsSymbol()));
        RunTime.assumedFalse(passedEnv.allPrevElementCapsules_Set.addToSet(newRef2Prev.getAsSymbol()));
        RunTime.assumedFalse(passedEnv.allNextElementCapsules_Set.addToSet(newRef2Next.getAsSymbol()));
        RunTime.assumedFalse(passedEnv.allRef2ElementsInEC_Set.addToSet(newRef2Element.getAsSymbol()));
        internal_setAsElementCapsule(passedEnv, existingSymbol);
        ElementCapsule existingEC = new ElementCapsule(passedEnv, existingSet, newRef2Prev, newRef2Element, newRef2Next);
        existingEC.assumedIsValidCapsule();
        registerInstance(passedEnv, existingSet, existingEC);
        return existingEC;
    }

    public static boolean isElementCapsule(Level040_DMLEnvironment passedEnv, Symbol passedSelf) {
        RunTime.assumedNotNull(passedEnv, passedSelf);
        RunTime.assumedTrue(passedEnv.isInitedSuccessfully());
        return passedEnv.allElementCapsules_Set.hasSymbol(passedSelf);
    }

    private static void internal_setAsElementCapsule(Level040_DMLEnvironment passedEnv, Symbol passedSelf) {
        RunTime.assumedNotNull(passedSelf, passedEnv);
        RunTime.assumedTrue(passedEnv.isInitedSuccessfully());
        RunTime.assumedFalse(passedEnv.allElementCapsules_Set.addToSet(passedSelf));
        RunTime.assumedTrue(isElementCapsule(passedEnv, passedSelf));
    }

    /**
	 * override this and don't call super() if you extend this
	 */
    protected boolean isItself() {
        RunTime.assumedNotNull(selfAsSet, env);
        RunTime.assumedTrue(env.isInitedSuccessfully());
        return isElementCapsule(env, selfAsSet.getAsSymbol());
    }

    /**
	 * @param name2
	 * @return
	 */
    public void assumedIsValidCapsule() {
        RunTime.assumedNotNull(selfAsSet);
        RunTime.assumedTrue(this.isItself());
        long size = selfAsSet.size();
        RunTime.assumedTrue(3 == size);
        RunTime.assumedTrue(cachedRef2Element.getAsSymbol() != cachedRef2Next.getAsSymbol());
        RunTime.assumedTrue(cachedRef2Prev.getAsSymbol() != cachedRef2Next.getAsSymbol());
        RunTime.assumedTrue(selfAsSet.getAsSymbol() != cachedRef2Next.getAsSymbol());
        RunTime.assumedTrue(selfAsSet.hasSymbol(cachedRef2Prev.getAsSymbol()));
        RunTime.assumedTrue(selfAsSet.hasSymbol(cachedRef2Element.getAsSymbol()));
        RunTime.assumedTrue(selfAsSet.hasSymbol(cachedRef2Next.getAsSymbol()));
        RunTime.assumedTrue(cachedRef2Next.getPointee() != selfAsSet.getAsSymbol());
        RunTime.assumedTrue(cachedRef2Prev.getPointee() != selfAsSet.getAsSymbol());
        RunTime.assumedTrue(cachedRef2Element.getPointee() != selfAsSet.getAsSymbol());
    }

    /**
	 * @return
	 */
    public boolean isAlone() {
        boolean ret1 = (this.getSideCapsule(Position.BEFORE) == null);
        boolean ret2 = (this.getSideCapsule(Position.AFTER) == null);
        if (ret1 != ret2) {
            RunTime.bug("they should be same value");
        }
        return ret1;
    }

    /**
	 * @param pos
	 *            BEFORE aka prev or AFTER aka next
	 * @return
	 */
    public ElementCapsule getSideCapsule(Position pos) {
        this.assumedIsValidCapsule();
        Symbol theEC = null;
        switch(pos) {
            case BEFORE:
                RunTime.assumedTrue(selfAsSet.hasSymbol(cachedRef2Prev.getAsSymbol()));
                theEC = cachedRef2Prev.getPointee();
                break;
            case AFTER:
                RunTime.assumedTrue(selfAsSet.hasSymbol(cachedRef2Next.getAsSymbol()));
                theEC = cachedRef2Next.getPointee();
                break;
            default:
                RunTime.bug("wrong position passed");
        }
        if (null != theEC) {
            return ElementCapsule.getExistingElementCapsule(env, theEC);
        } else {
            return null;
        }
    }

    /**
	 * @param pos
	 *            which side
	 * @param newECOrNull
	 *            thisEC will point to newEC after call
	 */
    public void setSideCapsule(Position pos, ElementCapsule newECOrNull) {
        switch(pos) {
            case BEFORE:
                if (null == newECOrNull) {
                    cachedRef2Prev.pointTo(null);
                } else {
                    cachedRef2Prev.pointTo(newECOrNull.getAsSymbol());
                }
                break;
            case AFTER:
                if (null == newECOrNull) {
                    cachedRef2Next.pointTo(null);
                } else {
                    cachedRef2Next.pointTo(newECOrNull.getAsSymbol());
                }
                break;
            default:
                RunTime.bug("cannot reach this");
        }
    }

    /**
	 * @return Symbol of this
	 */
    public Symbol getAsSymbol() {
        this.assumedIsValidCapsule();
        RunTime.assumedNotNull(selfAsSet);
        return selfAsSet.getAsSymbol();
    }

    /**
	 * (this)asSymbol->ref2elem->elem
	 * 
	 * @return elem
	 */
    public Symbol getElement() {
        RunTime.assumedNotNull(cachedRef2Element);
        Symbol ret = cachedRef2Element.getPointee();
        return ret;
    }

    /**
	 * (this)asSymbol->ref2elem->newElementOrNull
	 * 
	 * @param newElementOrNull
	 * @return old one
	 */
    public Symbol setElement(Symbol newElementOrNull) {
        RunTime.assumedNotNull(cachedRef2Element);
        Symbol oldOrNull = this.getElement();
        cachedRef2Element.pointTo(newElementOrNull);
        RunTime.assumedTrue(this.getElement() == newElementOrNull);
        return oldOrNull;
    }
}
