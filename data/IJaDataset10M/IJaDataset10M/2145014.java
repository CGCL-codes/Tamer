package jamare;

import gnu.regexp.RE;
import gnu.regexp.REException;
import java.util.GregorianCalendar;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.FileOutputStream;

/**
 *  The UnixForwardMailFilter forwards all mail from a certain folder to a unix
 *  mailbox file, such as /var/spool/mail/&lt;username&gt;. It is possible to
 *  configure the UnixForwardMailFilter to leave a copy in the folder, or te
 *  remove it after forwarding.
 *
 * @author     Reinier Zwitserloot
 * @created    August 29, 2001
 * @version    1.0
 */
public class UnixForwardMailFilter extends MailFilter {

    private String forwardFile;

    /**
	 *  Constructor for the UnixForwardMailFilter object.
	 *
	 * @param  pFile                     The local fileName to append to.
	 * @exception  NullPointerException  If the value is null or empty.
	 * @since                            1.0
	 */
    public UnixForwardMailFilter(String pFile) throws NullPointerException {
        if (pFile == null || pFile.equals("")) {
            throw new NullPointerException("Must pass a non-null, non-empty String.");
        }
        this.forwardFile = pFile;
    }

    /**
	 *  Gets the Name attribute of the ForwardMailFilter object
	 *
	 * @return    The Name value
	 * @since     1.0
	 */
    public String getName() {
        return "UnixForwardMailFilter:  to " + forwardFile;
    }

    /**
	 *  Appends the incoming mail to the unix mailbox file.
	 *
	 * @param  pEvent    Event object
	 * @param  pCapsule  Capsule object
	 * @param  pBox      parent MailBox
	 * @return           MailCapsule object.
	 * @since            1.0
	 */
    public MailCapsule handleEvent(MailEvent pEvent, MailCapsule pCapsule, MailBox pBox) {
        FileOutputStream fos;
        MailMessage msg = pCapsule.getMessage();
        MailHeader[] hdr = msg.getHeaders();
        String body = msg.getBody();
        String separator;
        RE separatorMatcher;
        try {
            separatorMatcher = new RE("^FROM\\s+.*$", RE.REG_MULTILINE | RE.REG_ICASE);
        } catch (REException e) {
            throw new InternalError("Um, static RE is bad? BUG!");
        }
        body = separatorMatcher.substituteAll(body, " $0");
        separator = "From " + MailUtils.unifyMailAddress(msg.getFrom()) + "  " + MailUtils.toSeparatorDate(new GregorianCalendar());
        try {
            fos = new FileOutputStream(forwardFile, true);
            fos.write(separator.getBytes("US-ASCII"));
            fos.write(0x0A);
            for (int i = 0; i < hdr.length; i++) {
                fos.write(hdr[i].getLine().getBytes("US-ASCII"));
                fos.write(0x0A);
            }
            fos.write(0x0A);
            fos.write(body.getBytes("US-ASCII"));
            if (body.charAt(body.length() - 1) != ((char) (0x0A))) {
                fos.write(0x0A);
            }
            fos.close();
        } catch (UnsupportedEncodingException e) {
            throw new InternalError("Um, US-ASCII is an unknown encoding type? Very wrong!");
        } catch (FileNotFoundException e) {
            pBox.logError("UnixForwardMailFilter: Appending to " + forwardFile + " failed: Couldn't open file: " + e.getMessage());
        } catch (IOException e) {
            pBox.logError("UnixForwardMailFilter: Appending to " + forwardFile + " failed: I/O error while writing: " + e.getMessage());
        }
        return pCapsule;
    }
}
