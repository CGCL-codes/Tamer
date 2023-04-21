package eu.more.diaball;

import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import org.jdom.Content;
import org.jdom.Element;
import org.jdom.filter.ElementFilter;
import org.soda.dpws.DPWSContext;
import org.soda.dpws.DPWSException;
import eu.more.diaball.interfaces.Message;
import eu.more.diaball.messagereceiver.generated.DIABALLMessage;
import eu.more.diaball.messagereceiver.generated.EncyrptedRequest;
import eu.more.diaball.messagereceiver.generated.KeyValuePair;
import eu.more.diaball.messagereceiver.generated.MessageReceiver;
import eu.more.diaball.messagereceiver.generated.NullTypeElement;
import eu.more.diaball.messagereceiver.generated.Out;
import eu.more.diaball.messagereceiver.generated.Out1;
import eu.more.diaball.messagereceiver.generated.impl.DIABALLMessageImpl;
import eu.more.diaball.messagereceiver.generated.impl.NullTypeElementImpl;
import eu.more.diaball.messagereceiver.generated.impl.Out1Impl;
import eu.more.diaball.messagereceiver.generated.impl.OutImpl;
import eu.more.diaball.util.MessageImpl;

public class MessageReceiverServiceImpl implements MessageReceiver {

    MessageProviderImpl messageProviderImpl;

    public MessageReceiverServiceImpl(MessageProviderImpl messageProviderImpl) {
        this.messageProviderImpl = messageProviderImpl;
    }

    @SuppressWarnings("unchecked")
    public NullTypeElement ReceiveMessage(DPWSContext context, DIABALLMessage parameters) throws DPWSException {
        ElementFilter filterName = new ElementFilter("authentication");
        List contentListName = context.getCurrentMessageHeader().getContent(filterName);
        String user = "";
        if (contentListName.size() > 0) {
            Element nameElement = (Element) contentListName.get(0);
            Content contentOfElement = nameElement.getContent(0);
            String authToken = contentOfElement.getValue();
            System.out.println("We have read the \"authentication\" header: " + authToken);
        }
        String type = parameters.getType();
        int typeI = -1;
        if (type.equals("NORMAL")) {
            typeI = Message.TYPE_NORMAL;
        }
        if (type.equals("ALARM_PHASE_1")) {
            typeI = Message.TYPE_ALARM_PHASE_1;
        }
        if (type.equals("ALARM_PHASE_2")) {
            typeI = Message.TYPE_ALARM_PHASE_2;
        }
        if (type.equals("ALARM_PHASE_3")) {
            typeI = Message.TYPE_ALARM_PHASE_3;
        }
        if (type.equals("EMERGENCY_PHASE_1")) {
            typeI = Message.TYPE_EMERGENCY_PHASE_1;
        }
        if (type.equals("EMERGENCY_PHASE_2")) {
            typeI = Message.TYPE_EMERGENCY_PHASE_2;
        }
        if (type.equals("EMERGENCY_PHASE_3")) {
            typeI = Message.TYPE_EMERGENCY_PHASE_3;
        }
        NullTypeElement ret = new NullTypeElementImpl("null");
        if (typeI == -1) {
            throw new DPWSException("Unkonwn type of message");
        }
        String source = parameters.getSource();
        if (!"".equals(user)) {
            source = user;
        } else {
            source = source;
        }
        String content = parameters.getContent();
        MessageImpl message = new MessageImpl(typeI, content, source);
        List keyValuePairs = parameters.getParams();
        Iterator it = keyValuePairs.iterator();
        while (it.hasNext()) {
            try {
                KeyValuePair kv = (KeyValuePair) it.next();
                message.setParam(kv.getKey(), kv.getValue());
            } catch (ClassCastException c) {
                System.err.println("Ignored:");
                c.printStackTrace();
            }
        }
        messageProviderImpl.notify(message);
        return ret;
    }

    public Out1 ReceiveGlucoseMeasurement(DPWSContext context, eu.more.diaball.messagereceiver.generated.ReceiveGlucoseMeasurement parameters) throws DPWSException {
        int typeI = Message.TYPE_NORMAL;
        String source = parameters.getSubjectName();
        String content = "Bg value is " + parameters.getValue();
        MessageImpl message = new MessageImpl(typeI, content, source);
        messageProviderImpl.notify(message);
        Out1 out1 = new Out1Impl();
        out1.setValue("success");
        return out1;
    }

    public Out ReceiveMessageSimple(DPWSContext context, eu.more.diaball.messagereceiver.generated.ReceiveMessageSimple parameters) throws DPWSException {
        String in = parameters.getIn();
        StringTokenizer tokenizer = new StringTokenizer(in, "|");
        DIABALLMessage tr = new DIABALLMessageImpl();
        tr.setType(tokenizer.nextToken());
        tr.setSource(tokenizer.nextToken());
        tr.setContent(tokenizer.nextToken());
        NullTypeElement n = ReceiveMessage(context, tr);
        Out out = new OutImpl();
        out.setValue(n.getValue());
        return out;
    }

    public NullTypeElement ReceiveEncyrpted(DPWSContext context, EncyrptedRequest parameters) throws DPWSException {
        String inString = parameters.getIn();
        return null;
    }
}
