package com.shimari.fxbot;

import com.shimari.bot.*;
import com.shimari.fxtp.Claim;
import com.shimari.fxtp.FXTP_Exception;
import com.shimari.framework.*;
import java.util.regex.*;

/**
 * Handle inquiries relating to a specific claim
 */
public class ClaimHandler extends FX_Handler {

    private static final Pattern CLAIM_NAME = Pattern.compile("^(?:claim )?(\\S*[a-zA-Z]\\S*?)\\??\\s*$");

    /**
     * Construct a new claimhandler from the config
     */
    public ClaimHandler(Config config) throws ConfigException {
        super(config);
    }

    /**
     * Handle the message
     */
    public boolean handle(Message m) {
        finer("Checking " + m);
        String request = m.getMessage();
        Matcher regx = CLAIM_NAME.matcher(request);
        if (regx.matches()) {
            String claimName = regx.group(1);
            info("Getting claim info for: <" + claimName + ">");
            try {
                Claim c = getFXTP().getClaim(claimName);
                m.sendReply(c.getSymbol() + ": " + c.getDescription() + " (" + c.getStatus() + "; " + c.getLast() + "; " + c.getPairs() + ")");
                return true;
            } catch (FXTP_Exception t) {
                t.printStackTrace(System.err);
                warning("Caught exception reading claim: " + t);
            }
        }
        return false;
    }
}
