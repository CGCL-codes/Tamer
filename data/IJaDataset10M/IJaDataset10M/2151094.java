package DE.FhG.IGD.semoa.security;

import java.util.*;
import codec.x501.*;

/**
 * This class represents a single policy rule definition.
 * ...
 *
 * @author Ulrich Pinsdorf
 * @version $Id: Rule.java 169 2000-12-21 10:44:05Z upinsdor $
 */
public class Rule extends Object {

    public static final int GRANT = 100;

    public static final int REJECT = 200;

    private int type_;

    private String owner_;

    private String sender_;

    private String sponsor_;

    private String[] blocks_;

    public Rule(int type) {
        if (type != GRANT && type != REJECT) throw new IllegalArgumentException("Unknown rule type");
        type_ = type;
        owner_ = null;
        sender_ = null;
        blocks_ = null;
    }

    public int getRuleType() {
        return type_;
    }

    public void setOwner(String owner) {
        owner_ = owner;
    }

    public String getOwner() {
        return owner_;
    }

    public boolean ownerDefined() {
        return (owner_ != null);
    }

    public void setSender(String sender) {
        sender_ = sender;
    }

    public String getSender() {
        return sender_;
    }

    public boolean senderDefined() {
        return (sender_ != null);
    }

    public void setSponsor(String sponsor) {
        sponsor_ = sponsor;
    }

    public String getSponsor() {
        return sponsor_;
    }

    public boolean sponsorDefined() {
        return (sponsor_ != null);
    }

    public void setBlocks(String[] blocks) {
        blocks_ = blocks;
    }

    public String[] getBlocks() {
        return blocks_;
    }

    public boolean blocksDefined() {
        return (blocks_ != null);
    }

    public boolean matches(Query query) {
        Name dname;
        boolean result;
        try {
            if (ownerDefined()) {
                if (!query.ownerDefined()) {
                    return false;
                }
                dname = new Name(owner_);
                if (!dname.partOf(query.getOwner())) {
                    return false;
                }
            }
            if (sponsorDefined()) {
                if (!query.sponsorDefined()) {
                    return false;
                }
                dname = new Name(sponsor_);
                if (!dname.partOf(query.getSponsor())) {
                    return false;
                }
            }
            if (senderDefined()) {
                String hostname;
                if (!query.senderDefined()) {
                    return false;
                }
                hostname = query.getSender().getHost();
                if (!hostname.equalsIgnoreCase(sender_)) {
                    return false;
                }
            }
        } catch (BadNameException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    public boolean equals(Object o) {
        Rule r;
        if (!(o instanceof Rule)) {
            throw new IllegalArgumentException("Type of parameter not Rule.");
        }
        r = (Rule) o;
        if (r.getRuleType() != type_) {
            return false;
        }
        if (!r.getOwner().equals(owner_)) {
            return false;
        }
        if (!r.getSender().equals(sender_)) {
            return false;
        }
        if (!r.getSponsor().equals(sponsor_)) {
            return false;
        }
        return true;
    }

    public String toString() {
        String key;
        String s;
        int i;
        s = "UNKNOWN";
        if (type_ == GRANT) {
            s = "GRANT";
        } else if (type_ == REJECT) {
            s = "REJECT";
        }
        if (ownerDefined()) {
            s += "\n  OWNER   " + owner_;
        }
        if (senderDefined()) {
            s += "\n  SENDER  " + sender_;
        }
        if (sponsorDefined()) {
            s += "\n  SPONSOR " + sponsor_;
        }
        if (blocksDefined()) {
            for (i = 0; i < blocks_.length; i++) {
                s += "\n  BLOCK   " + blocks_[i];
            }
        }
        return s;
    }
}
