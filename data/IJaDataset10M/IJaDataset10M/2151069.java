package gov.nist.atlas.impl;

import gov.nist.atlas.ATLASClass;
import gov.nist.atlas.ATLASElement;
import gov.nist.atlas.Corpus;
import gov.nist.atlas.spi.TypeImplementationDelegate;
import gov.nist.atlas.type.ATLASType;
import gov.nist.atlas.type.AbstractATLASType;
import gov.nist.atlas.type.AnalysisType;
import gov.nist.maia.MAIAScheme;

/**
 * @author Christophe Laprun
 * @version $Revision: 1.5 $
 */
public class AnalysisTypeImpl extends AbstractATLASType implements AnalysisType {

    public AnalysisTypeImpl(ATLASType superType, String name, MAIAScheme scheme, TypeImplementationDelegate delegate) {
        super(superType, name, scheme, delegate);
    }

    public ATLASClass getATLASClass() {
        return ATLASClass.ANALYSIS;
    }

    public boolean isParentValid(ATLASElement parent) {
        return parent instanceof Corpus;
    }
}
