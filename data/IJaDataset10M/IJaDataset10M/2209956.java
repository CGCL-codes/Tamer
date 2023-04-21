package com.worldware.mail.bounce;

import java.io.*;
import java.util.*;
import com.worldware.mail.*;

public class ProcessSmap extends ProcessBounce {

    public boolean isRecognized(MailMessage mm, String firstLine, String contentType, String subject, String from) {
        if (subject == null) return false;
        if (firstLine == null) return false;
        if (!subject.equalsIgnoreCase("Undeliverable message")) return false;
        if (!firstLine.startsWith("------- Failure Reasons")) return false;
        return true;
    }

    /** 
	  * @return true if the message was completely understood. false if a human should
	  * look at it.
	  * It is legitimate to process all the parts of a message that are understood,
	  * and to return them in badAd, and still return false because one of them did not make sense
	  */
    public boolean parseMessage(MailMessage mm, String firstLine, String mimeType, String subject, String from, BouncedList badAd) {
        Enumeration e = mm.bodyElements();
        boolean found = false;
        boolean once = false;
        while (e.hasMoreElements()) {
            String line = (String) e.nextElement();
            if (line.startsWith("User  not listed")) {
                String address = ((String) e.nextElement()).trim();
                StringBuffer b = new StringBuffer();
                if (!MailAddress.verifyAddress(address, b)) {
                    return false;
                }
                badAd.addAddress(address, BouncedAddress.CON_OK, "SBM2" + line.trim());
                found = true;
            }
            if (line.startsWith("CVS: InsufficientMemory")) {
                return false;
            }
            if (line.startsWith("------- Returned Message --------")) return found;
        }
        return found;
    }
}
