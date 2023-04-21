package org.nuclearbunny.icybee;

import org.nuclearbunny.icybee.protocol.*;
import tcl.lang.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TclTriggerListener implements MessageListener {

    private ICBClient theClient;

    private Interp interp;

    private static final Pattern rsvpPattern = Pattern.compile("You are invited to group (\\w*) by (\\w*)", Pattern.CASE_INSENSITIVE);

    private static final Pattern arrivePattern = Pattern.compile("(\\w*) .*");

    private static final Pattern bootPattern = Pattern.compile("(\\w*) booted you\\.");

    public TclTriggerListener(ICBClient client) {
        theClient = client;
        interp = theClient.getTclManager().getTclInterp();
    }

    public void messageReceived(MessageEvent e) {
        Packet p = e.getPacket();
        try {
            TclObject theMessage = TclString.newInstance(p.toString());
            interp.setVar("theMessage", theMessage, TCL.GLOBAL_ONLY);
            if (p instanceof OpenPacket) {
                processOpenPacket((OpenPacket) p);
            } else if (p instanceof PersonalPacket) {
                processPersonalPacket((PersonalPacket) p);
            } else if (p instanceof BeepPacket) {
                processBeepPacket((BeepPacket) p);
            } else if (p instanceof ErrorPacket) {
                processErrorPacket((ErrorPacket) p);
            } else if (p instanceof StatusPacket) {
                processStatusPacket((StatusPacket) p);
            }
        } catch (TclException ex) {
        }
    }

    private void processOpenPacket(OpenPacket p) throws TclException {
        String person = p.getNick();
        interp.eval("Trig_openmsg " + person);
    }

    private void processPersonalPacket(PersonalPacket p) throws TclException {
        String person = p.getNick();
        interp.eval("Trig_personalmsg " + person);
    }

    private void processBeepPacket(BeepPacket p) throws TclException {
        String person = p.getNick();
        interp.eval("Trig_beepmsg " + person);
    }

    private void processErrorPacket(ErrorPacket p) throws TclException {
        interp.eval("Trig_errormsg");
    }

    private void processStatusPacket(StatusPacket p) throws TclException {
        if (p.getPacketType() == ICBProtocol.PKT_IMPORTANT) {
            interp.eval("Trig_importantmsg");
        } else if (p.getStatusHeader().equalsIgnoreCase("Arrive") || p.getStatusHeader().equalsIgnoreCase("Sign-On")) {
            Matcher matcher = arrivePattern.matcher(p.getStatusText());
            if (matcher.matches()) {
                String person = matcher.group(1);
                interp.eval("Trig_arrivemsg " + person);
            }
        } else if (p.getStatusHeader().equalsIgnoreCase("Depart") || p.getStatusHeader().equalsIgnoreCase("Sign-Off")) {
            Matcher matcher = arrivePattern.matcher(p.getStatusText());
            if (matcher.matches()) {
                String person = matcher.group(1);
                interp.eval("Trig_leavemsg " + person);
            }
        } else if (p.getStatusHeader().equalsIgnoreCase("Boot")) {
            Matcher matcher = bootPattern.matcher(p.getStatusText());
            if (matcher.matches()) {
                String person = matcher.group(1);
                interp.eval("Trig_bootmsg " + person);
            }
        } else if (p.getStatusHeader().equalsIgnoreCase("Drop")) {
            interp.eval("Trig_dropmsg");
        } else if (p.getStatusHeader().equalsIgnoreCase("RSVP")) {
            Matcher matcher = rsvpPattern.matcher(p.getStatusText());
            if (matcher.matches()) {
                String group = matcher.group(1);
                String person = matcher.group(2);
                interp.eval("Trig_invitemsg " + person + " " + group);
            }
        }
    }
}
