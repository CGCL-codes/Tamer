package sk.naive.talker.tcpadapter;

import sk.naive.talker.adapter.*;

/**
 * ListTagProcessor.
 * @author <a href="mailto:virgo@naive.deepblue.sk">Richard "Virgo" Richter</a>
 * @version $Revision: 1.7 $ $Date: 2005/01/10 22:11:24 $
 */
public class ListTagProcessor implements TagProcessor {

    public String process(MessageProcessingContext ctx, String params) throws TagProcessorException {
        if (params == null) {
            params = "  - ";
        }
        StringBuffer buffer = new StringBuffer(ctx.getConsumer().result());
        for (String item : ((ItemTagProcessor.ItemConsumer) ctx.getConsumer()).items()) {
            buffer.append(TCPUser.TCP_BR).append(((TCPUser) ctx.getUser()).ansiReset()).append(params).append(item);
        }
        ctx.switchConsumer(new MessageProcessingContext.BufferMessageConsumer(), false);
        return buffer.append(((TCPUser) ctx.getUser()).ansiReset()).append(TCPUser.TCP_BR).toString();
    }
}
