package DE.FhG.IGD.semoa.comm;

import DE.FhG.IGD.util.*;
import DE.FhG.IGD.semoa.server.*;
import DE.FhG.IGD.semoa.service.*;
import java.io.*;
import java.text.*;
import java.util.*;

/**
 * The primary objective of this filter is to add date/time stamp 
 * to the message.
 *
 * @author Stivens Milic
 * @version "$Revision: 827 $/$Date: 2003-01-22 00:29:27 -0500 (Wed, 22 Jan 2003) $"
 */
public class AddTimeFilter extends AbstractService implements MessageFilter.Out {

    private byte[] message_;

    public AddTimeFilter() {
    }

    public String info() {
        return "This filter adds date/time stamp to an outgoing message.";
    }

    public String author() {
        return "Stivens Milic";
    }

    public String revision() {
        return "$Revision: 827 $/$Date: 2003-01-22 00:29:27 -0500 (Wed, 22 Jan 2003) $";
    }

    /**
     * It trims date/time stamp to certain formate.
     * @param time unformated date/time value.
     */
    private static String trimDate(long time) {
        DateFormat dateFormat;
        String str;
        int len;
        int i;
        dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, Locale.US);
        str = dateFormat.format(new Date());
        len = str.length();
        for (i = 0; i < (20 - len); i++) {
            str = str + " ";
        }
        return str;
    }

    /**
     * Calls the filter procedure that adds date/time stamp to the
     * given message.
     *
     * @param data The data of the message that is to be filtered.
     * @return The <code>ErrorCode</code> that resulted from this
     *   filter.It returns <code>ErrorCode.REJECT</code> 
     *   if an exception occured or <code>ErrorCode.MODIFIED
     *   </code> if everything went fine.
     */
    public ErrorCode filter(byte[] data) {
        ByteArrayOutputStream bos;
        String timestr;
        try {
            timestr = "Time: ";
            timestr = timestr + trimDate(System.currentTimeMillis());
            bos = new ByteArrayOutputStream();
            bos.write(timestr.getBytes());
            bos.write(data);
            message_ = bos.toByteArray();
            bos.close();
        } catch (Exception e) {
            System.err.println("[AddTimeFilter] Caught exception " + e.getClass().getName() + "(\"" + e.getMessage() + "\")");
            return ErrorCode.REJECT;
        }
        return ErrorCode.MODIFIED;
    }

    /**
     * It returns the message back to the caller instance.
     */
    public byte[] getMessage() {
        return message_;
    }
}
