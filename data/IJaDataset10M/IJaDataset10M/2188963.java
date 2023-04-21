package de.transformer;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;
import de.hfu.mmj.loanbroker.common.core.ConsumerResponse;

public class ConsumerResponseToString extends AbstractMessageTransformer {

    @Override
    public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
        ConsumerResponse consumerResponse = (ConsumerResponse) message.getPayload();
        StringBuilder sb = new StringBuilder();
        sb.append("Hello bla bla").append('\n');
        if (!Double.isNaN(consumerResponse.getRateQuote())) {
            sb.append("Our best offer is: ").append(consumerResponse.getRateQuote()).append("%.").append('\n');
        } else {
            sb.append("There was no offer! We're sorry!").append('\n');
        }
        sb.append("Best regards,\nYour Loanbroker");
        return sb.toString();
    }
}
