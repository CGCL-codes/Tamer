package gov.nist.applet.phone.ua.pidf.parser;

import java.util.*;

/**
 *
 * @author  deruelle
 * @version 1.0
 */
public class PresenceTag {

    private Vector atomTagList;

    private PresentityTag presentityTag;

    private String fromAddress;

    /** Creates new PresenceTag */
    public PresenceTag() {
        atomTagList = new Vector();
    }

    /**
	 * Retrieve the address of the subscriber
	 * @return the address of the subscriber
	 */
    public String getAddress() {
        return this.fromAddress;
    }

    /**
	 * Set the address of the subscriber
	 * @param address - the address of the subscriber
	 */
    public void setAddress(String address) {
        this.fromAddress = address;
    }

    public void addAtomTag(AtomTag atomTag) {
        if (atomTag != null) atomTagList.addElement(atomTag);
    }

    public void setPresentityTag(PresentityTag presentityTag) {
        this.presentityTag = presentityTag;
    }

    public Vector getAtomTagList() {
        return atomTagList;
    }

    public PresentityTag getPresentityTag() {
        return presentityTag;
    }

    public String toString() {
        String result = "<?xml version=\"1.0\"?>\n" + "<!DOCTYPE presence\n" + "PUBLIC \"-//IETF//DTD RFCxxxx XPIDF 1.0//EN\" \"xpidf.dtd\">\n" + "<presence>\n";
        result += presentityTag.toString();
        for (int i = 0; i < atomTagList.size(); i++) {
            AtomTag atomTag = (AtomTag) atomTagList.elementAt(i);
            result += atomTag.toString();
        }
        result += "</presence>";
        return result;
    }
}
