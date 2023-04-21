package org.openmi.utilities.wrapper;

import org.openmi.standard.ILink;

/**
 * SmartInputLinkSet class
 * a part of the smart wrapper engine
 */
public class SmartInputLinkSet extends SmartLinkSet implements java.io.Serializable {

    /**
     * To add a new link
     *
     * @param link the new link
     */
    public void addLink(ILink link) {
        SmartInputLink smartInputLink = new SmartInputLink();
        smartInputLink.link = link;
        smartInputLink.initialize(engineApiAccess);
        smartLinkList.add(smartInputLink);
    }

    /**
     * To upadate the input
     *
     * @throws Exception
     */
    public void updateInput() throws Exception {
        for (int i = 0; i < smartLinkList.size(); i++) {
            ((SmartInputLink) smartLinkList.get(i)).updateInput(engineApiAccess.getInputTime(((SmartInputLink) smartLinkList.get(i)).link.getTargetQuantity().getID(), ((SmartInputLink) smartLinkList.get(i)).link.getTargetElementSet().getID()));
        }
    }
}
