package org.deri.iris.terms.concrete;

import java.net.URI;
import org.deri.iris.api.terms.IConcreteTerm;
import org.deri.iris.api.terms.concrete.IIDREF;
import org.deri.iris.terms.AbstractConcreteTermTest;

/**
 * <p>
 * Test the implementation of the IDREF data-type.
 * </p>
 * 
 * @author Adrian Marte
 */
public class IDREFTest extends AbstractConcreteTermTest {

    @Override
    protected IConcreteTerm createBasic() {
        return new IDREF("sti");
    }

    @Override
    protected String createBasicString() {
        return "sti";
    }

    @Override
    protected IConcreteTerm createEqual() {
        return new IDREF("sti");
    }

    @Override
    protected String createEqualString() {
        return "sti";
    }

    @Override
    protected IConcreteTerm createGreater() {
        return new IDREF("xml");
    }

    @Override
    protected String createGreaterString() {
        return "xml";
    }

    @Override
    protected URI getDatatypeIRI() {
        return URI.create(IIDREF.DATATYPE_URI);
    }
}
