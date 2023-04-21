package com.sun.mail.imap.protocol;

import java.io.*;
import java.util.*;
import javax.security.sasl.*;
import javax.security.auth.callback.*;
import com.sun.mail.iap.*;
import com.sun.mail.imap.*;
import com.sun.mail.util.*;

/**
 * This class contains a single method that does authentication using
 * SASL.  This is in a separate class so that it can be compiled with
 * J2SE 1.5.  Eventually it should be merged into IMAPProtocol.java.
 */
public class IMAPSaslAuthenticator implements SaslAuthenticator {

    private IMAPProtocol pr;

    private String name;

    private Properties props;

    private boolean debug;

    private PrintStream out;

    private String host;

    public IMAPSaslAuthenticator(IMAPProtocol pr, String name, Properties props, boolean debug, PrintStream out, String host) {
        this.pr = pr;
        this.name = name;
        this.props = props;
        this.debug = debug;
        this.out = out;
        this.host = host;
    }

    public boolean authenticate(String[] mechs, String realm, String authzid, String u, String p) throws ProtocolException {
        synchronized (pr) {
            Vector v = new Vector();
            String tag = null;
            Response r = null;
            boolean done = false;
            if (debug) {
                out.print("IMAP SASL DEBUG: Mechanisms:");
                for (int i = 0; i < mechs.length; i++) out.print(" " + mechs[i]);
                out.println();
            }
            SaslClient sc;
            final String r0 = realm;
            final String u0 = u;
            final String p0 = p;
            CallbackHandler cbh = new CallbackHandler() {

                public void handle(Callback[] callbacks) {
                    if (debug) out.println("IMAP SASL DEBUG: callback length: " + callbacks.length);
                    for (int i = 0; i < callbacks.length; i++) {
                        if (debug) out.println("IMAP SASL DEBUG: callback " + i + ": " + callbacks[i]);
                        if (callbacks[i] instanceof NameCallback) {
                            NameCallback ncb = (NameCallback) callbacks[i];
                            ncb.setName(u0);
                        } else if (callbacks[i] instanceof PasswordCallback) {
                            PasswordCallback pcb = (PasswordCallback) callbacks[i];
                            pcb.setPassword(p0.toCharArray());
                        } else if (callbacks[i] instanceof RealmCallback) {
                            RealmCallback rcb = (RealmCallback) callbacks[i];
                            rcb.setText(r0 != null ? r0 : rcb.getDefaultText());
                        } else if (callbacks[i] instanceof RealmChoiceCallback) {
                            RealmChoiceCallback rcb = (RealmChoiceCallback) callbacks[i];
                            if (r0 == null) rcb.setSelectedIndex(rcb.getDefaultChoice()); else {
                                String[] choices = rcb.getChoices();
                                for (int k = 0; k < choices.length; k++) {
                                    if (choices[k].equals(r0)) {
                                        rcb.setSelectedIndex(k);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            };
            try {
                sc = Sasl.createSaslClient(mechs, authzid, name, host, (Map) props, cbh);
            } catch (SaslException sex) {
                if (debug) out.println("IMAP SASL DEBUG: Failed to create SASL client: " + sex);
                return false;
            }
            if (sc == null) {
                if (debug) out.println("IMAP SASL DEBUG: No SASL support");
                return false;
            }
            if (debug) out.println("IMAP SASL DEBUG: SASL client " + sc.getMechanismName());
            try {
                tag = pr.writeCommand("AUTHENTICATE " + sc.getMechanismName(), null);
            } catch (Exception ex) {
                if (debug) out.println("IMAP SASL DEBUG: AUTHENTICATE Exception: " + ex);
                return false;
            }
            OutputStream os = pr.getIMAPOutputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] CRLF = { (byte) '\r', (byte) '\n' };
            boolean isXGWTRUSTEDAPP = sc.getMechanismName().equals("XGWTRUSTEDAPP");
            while (!done) {
                try {
                    r = pr.readResponse();
                    if (r.isContinuation()) {
                        byte[] ba = null;
                        if (!sc.isComplete()) {
                            ba = r.readByteArray().getNewBytes();
                            if (ba.length > 0) ba = BASE64DecoderStream.decode(ba);
                            if (debug) out.println("IMAP SASL DEBUG: challenge: " + ASCIIUtility.toString(ba, 0, ba.length) + " :");
                            ba = sc.evaluateChallenge(ba);
                        }
                        if (ba == null) {
                            if (debug) out.println("IMAP SASL DEBUG: no response");
                            os.write(CRLF);
                            os.flush();
                            bos.reset();
                        } else {
                            if (debug) out.println("IMAP SASL DEBUG: response: " + ASCIIUtility.toString(ba, 0, ba.length) + " :");
                            ba = BASE64EncoderStream.encode(ba);
                            if (isXGWTRUSTEDAPP) bos.write("XGWTRUSTEDAPP ".getBytes());
                            bos.write(ba);
                            bos.write(CRLF);
                            os.write(bos.toByteArray());
                            os.flush();
                            bos.reset();
                        }
                    } else if (r.isTagged() && r.getTag().equals(tag)) done = true; else if (r.isBYE()) done = true; else v.addElement(r);
                } catch (Exception ioex) {
                    if (debug) ioex.printStackTrace();
                    r = Response.byeResponse(ioex);
                    done = true;
                }
            }
            if (sc.isComplete()) {
                String qop = (String) sc.getNegotiatedProperty(Sasl.QOP);
                if (qop != null && (qop.equalsIgnoreCase("auth-int") || qop.equalsIgnoreCase("auth-conf"))) {
                    if (debug) out.println("IMAP SASL DEBUG: " + "Mechanism requires integrity or confidentiality");
                    return false;
                }
            }
            Response[] responses = new Response[v.size()];
            v.copyInto(responses);
            pr.notifyResponseHandlers(responses);
            pr.handleResult(r);
            pr.setCapabilities(r);
            return true;
        }
    }
}
