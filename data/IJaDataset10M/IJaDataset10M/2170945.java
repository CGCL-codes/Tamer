package org.dml.level025;

import org.dml.database.bdb.level2.BDBVectorIterator;
import org.dml.level010.Symbol;
import org.dml.level020.SymbolIterator;
import org.dml.tools.RunTime;
import org.dml.tools.TwoKeyHashMap;
import org.dml.tracking.Factory;
import com.sleepycat.je.DatabaseException;

/**
 * 
 * a set of Symbols which are all children of X, where X is settable<br>
 * X is the domain<br>
 * the domain and the fact that this is a DomainSet is only known in Java, not in the db<br>
 * TODO JUnit test
 */
public class DomainSet extends SetOfTerminalSymbols {

    private static final TwoKeyHashMap<Level025_DMLEnvironment, Symbol, DomainSet> allDomainSetInstances = new TwoKeyHashMap<Level025_DMLEnvironment, Symbol, DomainSet>();

    private final Symbol domain;

    private DomainSet(Level025_DMLEnvironment passedEnv, Symbol passedSelf, Symbol passedDomain) {
        super(passedEnv, passedSelf);
        RunTime.assumedNotNull(passedEnv, passedSelf, passedDomain);
        RunTime.assumedTrue(passedEnv.isInitedSuccessfully());
        domain = passedDomain;
    }

    private static final void registerDSInstance(Level025_DMLEnvironment env, Symbol self, DomainSet newOne) {
        RunTime.assumedNotNull(env, self, newOne);
        RunTime.assumedFalse(allDomainSetInstances.ensure(env, self, newOne));
    }

    private static final DomainSet getDSInstance(Level025_DMLEnvironment env, Symbol self) {
        RunTime.assumedNotNull(env, self);
        return allDomainSetInstances.get(env, self);
    }

    public static DomainSet getAsDomainSet(Level025_DMLEnvironment passedEnv, Symbol passedSelf, Symbol passedDomain) {
        RunTime.assumedNotNull(passedEnv, passedSelf, passedDomain);
        DomainSet existingOne = getDSInstance(passedEnv, passedSelf);
        if (null != existingOne) {
            if (existingOne.domain != passedDomain) {
                RunTime.badCall("already existing DomainSet had different Domain setting");
            }
            existingOne.assumedValid();
            return existingOne;
        }
        DomainSet ret = new DomainSet(passedEnv, passedSelf, passedDomain);
        ret.assumedValid();
        RunTime.assumedTrue(ret.getAsSymbol() == passedSelf);
        registerDSInstance(passedEnv, passedSelf, ret);
        return ret;
    }

    @Override
    public void assumedValid() {
        super.assumedValid();
        RunTime.assumedNotNull(domain);
        RunTime.assumedFalse(selfAsSymbol == domain);
        SymbolIterator iter = env.getIterator_on_Terminals_of(selfAsSymbol);
        try {
            iter.goFirst();
            while (null != iter.now()) {
                RunTime.assumedTrue(env.isVector(domain, iter.now()));
                iter.goNext();
            }
        } finally {
            iter.close();
            iter = null;
        }
    }

    /**
	 * @param element
	 * @return false if it didn't already exist
	 */
    @Override
    public boolean addToSet(Symbol element) {
        RunTime.assumedNotNull(element);
        RunTime.assumedFalse(selfAsSymbol == element);
        if (!env.isVector(domain, element)) {
            RunTime.badCall("passed element is not from domain");
        }
        return super.addToSet(element);
    }

    /**
	 * @param which
	 *            should be a child of domain
	 * @return true if self->which
	 */
    @Override
    public boolean hasSymbol(Symbol which) {
        RunTime.assumedNotNull(which);
        RunTime.assumedFalse(selfAsSymbol == which);
        if (!env.isVector(domain, which)) {
            RunTime.badCall("passed element is not from domain");
        }
        return super.hasSymbol(which);
    }
}
