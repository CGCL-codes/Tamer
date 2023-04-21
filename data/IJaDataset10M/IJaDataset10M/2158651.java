package sk.naive.talker.command;

/**
 *
 * @author <a href="mailto:rytier@naive.deepblue.sk">Martin "Rytier" Kerni</a>
 * @version $Revision: 1.7 $ $Date: 2004/02/07 23:06:40 $
 */
public class Semote extends NonaddressedMessageCommand {

    public String getAudienceMessageKey(String message) {
        return "semote.audience";
    }

    public String getStoredMessageKey(String message) {
        return "semote.stored";
    }

    public String getSenderMessageKey(String message) {
        return "semote.audience";
    }
}
