package org.deri.iris.dbstorage;

import java.util.Map;
import java.util.Set;
import org.deri.iris.api.IProgram;
import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.factory.IProgramFactory;
import org.deri.iris.api.storage.IMixedDatatypeRelation;
import org.deri.iris.api.storage.IRelation;

/**
 * <p>
 * A simple IProgramFactory implementation.
 * </p>
 * <p>
 * $Id: ProgramFactory.java,v 1.4 2007-09-10 10:31:56 fefacca Exp $
 * </p>
 * @author Francisco Garcia
 * @author Darko Anicic, DERI Innsbruck
 * @author Richard Pöttler (richard dot poettler at deri dot org)
 * @version $Revision: 1.4 $
 */
public class ProgramFactory implements IProgramFactory {

    private static final IProgramFactory FACTORY = new ProgramFactory();

    private static Map conf = null;

    private ProgramFactory() {
    }

    public static IProgramFactory getInstance() {
        return FACTORY;
    }

    public IProgram createProgram() {
        return new Program(conf);
    }

    public IProgram createProgram(Map<IPredicate, IMixedDatatypeRelation> f, Set<IRule> r, Set<IQuery> q) {
        return new Program(conf, f, r, q);
    }

    public void setDbConfigurationFile(Map conf) {
        this.conf = conf;
    }
}
