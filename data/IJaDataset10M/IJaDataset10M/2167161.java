package org.genxdm.bridgetest;

import static org.junit.Assert.assertNotNull;
import org.genxdm.Cursor;
import org.genxdm.Feature;
import org.genxdm.ProcessingContext;
import org.genxdm.bridgetest.utilities.Events;
import org.genxdm.exceptions.GenXDMException;
import org.genxdm.io.FragmentBuilder;
import org.junit.Test;

public abstract class CursorBase<N> extends TestBase<N> {

    @Test
    public void writes() throws GenXDMException {
        ProcessingContext<N> context = newProcessingContext();
        FragmentBuilder<N> builder = context.newFragmentBuilder();
        assertNotNull(builder);
        Events<N> matcher = new Events<N>(builder);
        if (!context.isSupported(Feature.DOCUMENT_URI)) matcher.ignoreDocumentURI();
        if (!context.isSupported(Feature.NAMESPACE_AXIS)) matcher.ignoreExtraNamespaceDeclarations();
        matcher.record();
        N doc = createComplexTestDocument(matcher);
        assertNotNull(doc);
        Cursor<N> cursor = context.newCursor(doc);
        assertNotNull(cursor);
        matcher.match();
        cursor.write(matcher);
    }

    @Test
    public void comparisons() {
    }
}
