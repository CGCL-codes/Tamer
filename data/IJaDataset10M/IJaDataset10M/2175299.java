package ca.ucalgary.cpsc.agilePlanner.test.persister.xml.converter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;
import persister.Event;
import persister.xml.converter.MouseMoveConverter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class MouseMoveConverterTest {

    Mockery context = new Mockery();

    MouseMoveConverter converter = new MouseMoveConverter();

    @Test
    public void shouldMarsalTheDataToDisconnectAClient() {
        final HierarchicalStreamWriter mockWriter = context.mock(HierarchicalStreamWriter.class);
        final Event move = context.mock(Event.class);
        final MarshallingContext marshalContext = context.mock(MarshallingContext.class);
        context.checking(new Expectations() {

            {
                one(mockWriter).startNode("MouseMove");
                one(mockWriter).addAttribute("clientID", "3");
                one(mockWriter).addAttribute("name", "John Smith");
                one(mockWriter).addAttribute("locationX", "3");
                one(mockWriter).addAttribute("locationY", "4");
                one(mockWriter).addAttribute("touchID", "11");
                one(mockWriter).endNode();
            }

            {
                one(move).getId();
                will(returnValue(3L));
                one(move).getName();
                will(returnValue("John Smith"));
                one(move).getLocationX();
                will(returnValue(3));
                one(move).getLocationY();
                will(returnValue(4));
                one(move).getLoadedInfo();
                will(returnValue(null));
                one(move).getTouchID();
                will(returnValue(11L));
            }
        });
        converter.marshal(move, mockWriter, marshalContext);
        context.assertIsSatisfied();
    }

    @Test
    public void shouldUnMarshalAMouseMoveEvent() {
        Event move;
        final HierarchicalStreamReader mockReader = context.mock(HierarchicalStreamReader.class);
        final UnmarshallingContext unMarshalContext = context.mock(UnmarshallingContext.class);
        context.checking(new Expectations() {

            {
                one(mockReader).getAttribute("clientID");
                will(returnValue("45"));
                one(mockReader).getAttribute("name");
                will(returnValue("John Smith"));
                one(mockReader).getAttribute("locationX");
                will(returnValue("2"));
                one(mockReader).getAttribute("locationY");
                will(returnValue("4"));
                one(mockReader).getAttribute("touchID");
                will(returnValue("11"));
                one(mockReader).hasMoreChildren();
                will(returnValue(false));
            }
        });
        move = (Event) converter.unmarshal(mockReader, unMarshalContext);
        context.assertIsSatisfied();
        assertThat(move.getId(), equalTo(45L));
        assertThat(move.getName(), equalTo("John Smith"));
        assertThat(move.getLocationX(), equalTo(2));
        assertThat(move.getLocationY(), equalTo(4));
    }
}
