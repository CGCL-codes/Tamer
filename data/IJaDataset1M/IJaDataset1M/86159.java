package org.jplate.tmplate.directive.def;

import org.jplate.factory.FactoryIfc;
import org.jplate.tmplate.directive.NamedDirectiveDefIfc;
import org.jplate.tmplate.directive.NamedDirectiveIfc;

/**
 *
 * This interface defines the API to create an implementation of
 * NamedDirectiveDefIfc.
 *
 */
public interface NamedDirectiveDefFactoryIfc extends FactoryIfc {

    /**
     *
     * This method will create an implementation of NamedDirectiveDefIfc.
     *
     * @param name represents the name of the named directive.
     *
     * @param namedDirective represents the named directive.
     *
     * @return an implementation of NamedDirectiveDefrIfc.
     *
     */
    public NamedDirectiveDefIfc createNamedDirectiveDef(String name, NamedDirectiveIfc namedDirective);
}
