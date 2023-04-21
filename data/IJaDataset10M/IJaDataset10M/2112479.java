package ca.ucalgary.cpsc.agilePlanner.test.persister.xml.converter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;
import persister.Disconnect;
import persister.xml.converter.DisconnectDataConverter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class DisconnectDataConverterTest {

    Mockery context = new Mockery();

    DisconnectDataConverter converter = new DisconnectDataConverter();

    @Test
    public void shouldMarsalTheDataToDisconnectAClient() {
        final HierarchicalStreamWriter mockWriter = context.mock(HierarchicalStreamWriter.class);
        final Disconnect disconnectData = context.mock(Disconnect.class);
        final MarshallingContext marshalContext = context.mock(MarshallingContext.class);
        context.checking(new Expectations() {

            {
                one(mockWriter).startNode("DisconnectData");
                one(mockWriter).addAttribute("clientid", "1");
                one(mockWriter).endNode();
            }

            {
                one(disconnectData).getClientId();
                will(returnValue(1));
            }
        });
        converter.marshal(disconnectData, mockWriter, marshalContext);
        context.assertIsSatisfied();
    }

    @Test
    public void shouldUnMarshalDisconnectionMessage() {
        Disconnect disconnectData;
        final HierarchicalStreamReader mockReader = context.mock(HierarchicalStreamReader.class);
        final UnmarshallingContext unMarshalContext = context.mock(UnmarshallingContext.class);
        context.checking(new Expectations() {

            {
                one(mockReader).getAttribute("clientid");
                will(returnValue("3"));
            }
        });
        disconnectData = (Disconnect) converter.unmarshal(mockReader, unMarshalContext);
        context.assertIsSatisfied();
        assertThat(disconnectData.getClientId(), equalTo(3));
    }
}
