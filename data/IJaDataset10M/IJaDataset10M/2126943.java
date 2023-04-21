package org.decisiondeck.jmcda.xws.transformer.xml;

import org.decisiondeck.jmcda.persist.xmcda2.XMCDAAlternativesMatrix;
import org.decisiondeck.jmcda.persist.xmcda2.generated.XAlternativesComparisons;
import org.decisiondeck.xmcda_oo.structure.Alternative;
import org.decisiondeck.xmcda_oo.utils.matrix.IRdZeroToOneMatrix;
import com.google.common.base.Function;

public class FromMatrix implements Function<IRdZeroToOneMatrix<Alternative, Alternative>, XAlternativesComparisons> {

    @Override
    public XAlternativesComparisons apply(IRdZeroToOneMatrix<Alternative, Alternative> matrix) {
        return new XMCDAAlternativesMatrix().write(matrix);
    }
}
