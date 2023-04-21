package erki.abcpeter.parsers.mailbox;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * A message stored in the mailbox for later delivery to the receiver.
 * 
 * @author Edgar Kalkowski
 */
public class Message implements Serializable {

    private static final long serialVersionUID = -1115467349450687746L;

    private Date date;

    private String from, to;

    private LinkedList<String> lines;

    /**
     * Create a new message.
     * 
     * @param from
     *        The nickname of the sender.
     * @param to
     *        The nickname of the receiver.
     * @param lines
     *        The actual message as a list of lines.
     */
    public Message(String from, String to, List<String> lines) {
        date = new Date();
        this.from = from;
        this.to = to;
        this.lines = new LinkedList<String>();
        for (String line : lines) {
            this.lines.add(line);
        }
    }

    /** @return The nickname of the user who sent this message. */
    public String getFrom() {
        return from;
    }

    /** @return The nickname of the user who shall receive this message. */
    public String getTo() {
        return to;
    }

    /**
     * @return The actual message as a list lines. The returned list is a copy
     *         of the actual message so changing something won't work.
     */
    public LinkedList<String> getLines() {
        LinkedList<String> clone = new LinkedList<String>();
        for (String line : lines) {
            clone.add(line);
        }
        return clone;
    }

    /**
     * @return The date and time this message was written formatted as a string
     *         like "dd.mm.yyyy hh:mm:ss".
     */
    public String getDateFormatted() {
        String result = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        result += day < 10 ? "0" + day + "." : day + ".";
        result += month < 10 ? "0" + month + "." : month + ".";
        result += year + " ";
        result += hour < 10 ? "0" + hour + ":" : hour + ":";
        result += minute < 10 ? "0" + minute + ":" : minute + ":";
        result += second < 10 ? "0" + second : second;
        return result;
    }
}
