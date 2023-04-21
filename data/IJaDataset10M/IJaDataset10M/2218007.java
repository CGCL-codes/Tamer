package sk.naive.talker.adapter;

/**
 *
 * @author <a href="mailto:virgo@naive.deepblue.sk">Richard "Virgo" Richter</a>
 * @version $Revision: 1.8 $ $Date: 2004/10/11 21:26:01 $
 */
public interface TagProcessor {

    public String process(MessageProcessingContext ctx, String params) throws TagProcessorException;
}
