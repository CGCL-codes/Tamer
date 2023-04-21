package org.rubypeople.rdt.internal.core.builder;

import java.util.List;
import org.rubypeople.rdt.internal.core.symbols.SymbolIndex;

public class TC_CleanRdtCompiler extends AbstractRdtTestCase {

    AbstractRdtCompiler createCompiler(SymbolIndex symbolIndex, IMarkerManager markerManager, List singleCompilers) {
        return new CleanRdtCompiler(project, symbolIndex, markerManager, singleCompilers);
    }

    protected void assertIndexFlushed(List expectedFiles) {
        symbolIndex.assertFlushed(project);
    }

    protected void assertMarkersRemoved(List expectedFiles) {
        markerManager.assertMarkersRemovedFor(project);
    }
}
