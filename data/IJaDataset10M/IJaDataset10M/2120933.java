package com.google.code.cxf.protobuf;

import java.util.ArrayList;
import java.util.List;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.MessageSenderInterceptor;
import org.apache.cxf.interceptor.OneWayProcessorInterceptor;
import org.apache.cxf.interceptor.OutgoingChainInterceptor;
import org.apache.cxf.interceptor.ServiceInvokerInterceptor;
import org.apache.cxf.transport.local.LocalTransportFactory;
import org.junit.Test;
import com.google.code.cxf.protobuf.ProtobufMessageHandler;
import com.google.code.cxf.protobuf.ProtobufServerFactoryBean;
import com.google.code.cxf.protobuf.addressbook.AddressBookProtos.AddressBook;
import com.google.code.cxf.protobuf.addressbook.AddressBookProtos.AddressBookServiceMessage;
import com.google.code.cxf.protobuf.addressbook.AddressBookProtos.AddressBookSize;
import com.google.code.cxf.protobuf.addressbook.AddressBookProtos.NamePattern;
import com.google.code.cxf.protobuf.addressbook.AddressBookProtos.Person;
import com.google.code.cxf.protobuf.interceptor.ProtobufMessageInInterceptor;
import com.google.code.cxf.protobuf.interceptor.ProtobufMessageOutInterceptor;
import com.google.protobuf.Message;

public class AddressBookMessageHandlerTest extends AbstractCXFProtobufTest {

    @Test
    public void testService() throws Exception {
        ProtobufServerFactoryBean serverFactoryBean = new ProtobufServerFactoryBean();
        serverFactoryBean.setAddress("http://localhost:8888/SearchService");
        serverFactoryBean.setTransportId(LocalTransportFactory.TRANSPORT_ID);
        serverFactoryBean.setServiceBean(new ProtobufMessageHandler() {

            List<Person> records = new ArrayList<Person>();

            /**
             * @see com.google.code.cxf.protobuf.ProtobufMessageHandler#handleMessage(com.google.protobuf.Message)
             */
            public Message handleMessage(Message message) {
                AddressBookServiceMessage searchServiceMessage = (AddressBookServiceMessage) message;
                if (searchServiceMessage.hasField(AddressBookServiceMessage.getDescriptor().findFieldByName("listPeople"))) {
                    AddressBook.Builder addressbook = AddressBook.newBuilder();
                    NamePattern request = searchServiceMessage.getListPeople();
                    for (Person person : records) {
                        if (person.getName().indexOf(request.getPattern()) >= 0) {
                            addressbook.addPerson(person);
                        }
                    }
                    return addressbook.build();
                } else if (searchServiceMessage.hasField(AddressBookServiceMessage.getDescriptor().findFieldByName("addPerson"))) {
                    Person request = searchServiceMessage.getAddPerson();
                    records.add(request);
                    return AddressBookSize.newBuilder().setSize(records.size()).build();
                } else {
                    throw new RuntimeException("Payload not found in message " + message);
                }
            }
        });
        serverFactoryBean.setMessageClass(AddressBookServiceMessage.class);
        Server server = serverFactoryBean.create();
        List<Interceptor<? extends org.apache.cxf.message.Message>> interceptors = server.getEndpoint().getInInterceptors();
        System.out.println(interceptors);
        assertEquals(0, interceptors.size());
        interceptors = server.getEndpoint().getOutInterceptors();
        System.out.println(interceptors);
        assertEquals(1, interceptors.size());
        assertTrue(hasInterceptor(interceptors, MessageSenderInterceptor.class));
        interceptors = serverFactoryBean.getServiceFactory().getService().getInInterceptors();
        System.out.println(interceptors);
        assertEquals(3, interceptors.size());
        assertTrue(hasInterceptor(interceptors, ServiceInvokerInterceptor.class));
        assertTrue(hasInterceptor(interceptors, OutgoingChainInterceptor.class));
        assertTrue(hasInterceptor(interceptors, OneWayProcessorInterceptor.class));
        interceptors = serverFactoryBean.getServiceFactory().getService().getOutInterceptors();
        System.out.println(interceptors);
        assertEquals(0, interceptors.size());
        interceptors = server.getEndpoint().getBinding().getInInterceptors();
        System.out.println(interceptors);
        assertEquals(1, interceptors.size());
        assertTrue(hasInterceptor(interceptors, ProtobufMessageInInterceptor.class));
        interceptors = server.getEndpoint().getBinding().getOutInterceptors();
        System.out.println(interceptors);
        assertEquals(1, interceptors.size());
        assertTrue(hasInterceptor(interceptors, ProtobufMessageOutInterceptor.class));
        Person.Builder person = Person.newBuilder();
        person.setId(10);
        person.setName("Bela");
        AddressBookServiceMessage searchServiceMessage = AddressBookServiceMessage.newBuilder().setAddPerson(person).build();
        AddressBookSize response = AddressBookSize.parseFrom(testUtilities.invokeBytes("http://localhost:8888/SearchService", LocalTransportFactory.TRANSPORT_ID, toBytes(searchServiceMessage)));
        System.out.println(response);
        assertEquals(1, response.getSize());
    }
}
