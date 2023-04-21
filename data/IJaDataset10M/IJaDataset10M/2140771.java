package net.sf.clexw.cml.trans;

import java.util.*;
import net.sf.clexw.cml.proc.*;
import net.sf.clexw.cml.util.*;

/**
 * This class is a factory for creation instance of the CMLTransformer class used
 * for conversion of source code in the Code Modifier Language to source code in
 * the C++ language.
 *
 * @author J. Pikl
 */
public class CppTransformerFactory implements CMLTransformerFactory {

    /** The storage for managed variables. */
    private VariableStorage<List> storage = new VariableStorage<List>();

    /**
     * Returns the variable storage.
     *
     * @return the variable storage.
     */
    public VariableStorage<?> getVariableStorage() {
        return storage;
    }

    /**
     * Creates an instance of the CMLTransformer class for conversion from CML
     * to C++. Id adds some AST processing, specific for the C++ language.
     *
     * @param templDir  name of directory, containing language templates
     * @param indentStr a string used for indentation of output text
     */
    public CMLTransformer createTransformer(String templDir, String indentStr) {
        CMLTransformer transformer = new CMLTransformer(templDir, indentStr);
        SpecificCodeProcessing scp = new SpecificCodeProcessing("C++");
        scp.enableCheckOfAllLanguages(new String[] { "C++", "Java" });
        transformer.addASTProcessing(scp);
        transformer.addASTProcessing(new ManagedVarProcessing(storage));
        return transformer;
    }
}
