package net.sourceforge.ondex.workflow.model;

import static org.junit.Assert.*;
import java.util.Collections;
import net.sourceforge.ondex.core.ONDEXGraph;
import net.sourceforge.ondex.init.PluginType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by IntelliJ IDEA.
 * User: nmrp3
 * Date: 14-Jul-2010
 * Time: 15:20:29
 * To change this template use File | Settings | File Templates.
 */
@RunWith(JMock.class)
public class TaskDescriptionTest {

    Mockery context = new JUnit4Mockery();

    @Test
    public void testExport() throws Exception, PluginType.UnknownPluginTypeException {
        assertTrue(true);
    }

    private ONDEXGraph dummyGraph() {
        final ONDEXGraph graph = context.mock(ONDEXGraph.class);
        context.checking(new Expectations() {

            {
                allowing(graph).getConcepts();
                will(returnValue(Collections.emptySet()));
                allowing(graph).getRelations();
                will(returnValue(Collections.emptySet()));
                allowing(graph).getName();
                will(returnValue("testGraph"));
            }
        });
        return graph;
    }
}
