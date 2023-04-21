package org.gvsig.symbology.symbols;

import org.gvsig.symbology.fmap.symbols.DotDensityFillSymbol;
import com.iver.cit.gvsig.fmap.core.symbols.AbstractSymbolTestCase;
import com.iver.cit.gvsig.fmap.core.symbols.ISymbol;

public class DotDensityFillSymbolTest extends AbstractSymbolTestCase {

    public DotDensityFillSymbolTest() {
        super(DotDensityFillSymbol.class);
    }

    @Override
    public ISymbol newInstance() {
        DotDensityFillSymbol dds = (DotDensityFillSymbol) super.newInstance();
        return dds;
    }
}
