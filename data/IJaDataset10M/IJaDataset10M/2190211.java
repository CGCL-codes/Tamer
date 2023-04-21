package org.netbeams.dsp.test.randomnumber;

import java.util.ArrayList;
import java.util.Collection;
import org.apache.log4j.Logger;
import org.netbeams.dsp.ComponentDescriptor;
import org.netbeams.dsp.DSPComponent;
import org.netbeams.dsp.DSPContext;
import org.netbeams.dsp.DSPException;
import org.netbeams.dsp.MessageCategory;
import org.netbeams.dsp.message.Message;

public class RandomNumberDSPComponent implements DSPComponent {

    private static final Logger log = Logger.getLogger(RandomNumberDSPComponent.class);

    private static final ComponentDescriptor componentDescriptor;

    public static final String COMPONENT_TYPE = "org.netbeams.dsp.test.randomnumber";

    public static final String MSG_CONTENT_TYPE_RANDOM_NUMBER = "random.number";

    private DSPContext context;

    private String componentNodeId;

    private RandomNumberProducer producer;

    static {
        componentDescriptor = new ComponentDescriptor();
        Collection<MessageCategory> producedMessageCategories = new ArrayList<MessageCategory>();
        Collection<MessageCategory> consumedMessageCategories = new ArrayList<MessageCategory>();
        MessageCategory messageCategory = new MessageCategory(RandomNumberDSPComponent.class.getName(), MSG_CONTENT_TYPE_RANDOM_NUMBER);
        producedMessageCategories.add(messageCategory);
        componentDescriptor.setMsgCategoryProduced(producedMessageCategories);
        componentDescriptor.setMsgCategoryConsumed(consumedMessageCategories);
    }

    public RandomNumberDSPComponent() {
        log.debug("Instantiating the RandomNumber DSP component");
    }

    ;

    public void initComponent(String componentNodeId, DSPContext context) throws DSPException {
        this.context = context;
        this.componentNodeId = componentNodeId;
        log.info("Initializing component: " + componentNodeId);
    }

    public String getComponentNodeId() {
        return componentNodeId;
    }

    public String getComponentType() {
        return COMPONENT_TYPE;
    }

    public ComponentDescriptor getComponentDescriptor() {
        return componentDescriptor;
    }

    public void deliver(Message message) throws DSPException {
    }

    public Message deliverWithReply(Message message) throws DSPException {
        return null;
    }

    public Message deliverWithReply(Message message, long waitTime) throws DSPException {
        return null;
    }

    public void startComponent() {
        log.info("Starting Random Number DSP component");
        producer = new RandomNumberProducer(context);
        producer.startProducer();
    }

    public void stopComponent() throws DSPException {
        log.info("Stopping Random Number DSP component");
        producer.stopProducer();
    }
}
