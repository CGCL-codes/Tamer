package sk.naive.talker.command;

/**
 *
 * @author <a href="mailto:rytier@naive.deepblue.sk">Martin "Rytier" Kerni</a>
 * @version $Revision: 1.3 $ $Date: 2004/02/07 23:06:40 $
 */
public class Think extends NonaddressedLocalMessageCommand {

    public String getSenderMessageKey(String message) {
        return "think.audience";
    }
}
