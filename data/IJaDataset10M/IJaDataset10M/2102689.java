package org.swemof.input;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.same;
import static org.easymock.EasyMock.verify;
import java.util.ArrayList;
import java.util.Collection;
import junit.framework.TestCase;
import org.swemof.corpus.DocumentSet;

public class InputControllerTest extends TestCase {

    private SourceRegistry registry;

    private InputController controller;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        registry = new SourceRegistry();
        controller = new InputController(registry);
    }

    public void testGetDescriptorsQueriesRegistry() throws Exception {
        SourceDescriptor descriptor = createMock(SourceDescriptor.class);
        SourceFactory factory = createMock(SourceFactory.class);
        expect(factory.getDescriptor()).andReturn(descriptor);
        registry.registerFactory(factory);
        replay(factory);
        Collection<SourceDescriptor> actual = controller.getSourceDescriptors();
        assertEquals(1, actual.size());
        assertSame(descriptor, actual.iterator().next());
        verify(factory);
    }

    public void testGetDocumentSets() throws Exception {
        String uri = "someURI";
        Collection<DocumentSet> sets = new ArrayList<DocumentSet>();
        sets.add(new DocumentSet());
        SourceDescriptor descriptor = createMock(SourceDescriptor.class);
        Source source = createMock(Source.class);
        expect(source.getDocumentSets(same(uri))).andReturn(sets);
        SourceFactory factory = createMock(SourceFactory.class);
        expect(factory.getDescriptor()).andReturn(descriptor);
        expect(factory.createSource()).andReturn(source);
        registry.registerFactory(factory);
        replay(factory, source);
        Collection<DocumentSet> actual = controller.getDocumentSets(uri, descriptor);
        assertSame(sets, actual);
        verify(factory, source);
    }
}
