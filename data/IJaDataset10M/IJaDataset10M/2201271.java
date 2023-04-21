package mscheme.values;

import mscheme.environment.StaticEnvironment;
import mscheme.exceptions.SchemeException;

public interface ICompileable {

    /** The CVS id of the file containing this class. */
    String CVS_ID = "$Id: ICompileable.java 724 2004-12-28 13:37:07Z sielenk $";

    /**
     * Compiles a value as normal code.
     * @throws InterruptedException
     */
    Object getForceable(StaticEnvironment compilationEnv) throws SchemeException, InterruptedException;
}
