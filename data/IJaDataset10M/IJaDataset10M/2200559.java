package org.openmi.utilities.wrapper;

import org.openmi.standard.ILink;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * SmartLinkSet class a part of the smart wrapper engine.
 */
public abstract class SmartLinkSet implements Serializable {

    protected IRunEngine engineApiAccess;

    protected ArrayList smartLinkList;

    /**
     * constructor
     */
    public SmartLinkSet() {
        smartLinkList = new ArrayList();
        ;
    }

    /**
     * GETTER for the list of links
     *
     * @return the list of links
     */
    public ArrayList getSmartLinkList() {
        return smartLinkList;
    }

    /**
     * The initialize method
     *
     * @param engineApiAccess the engine api access
     */
    public void initialize(IRunEngine engineApiAccess) {
        this.engineApiAccess = engineApiAccess;
    }

    /**
     * To add a link
     *
     * @param link The link to be added
     */
    public abstract void addLink(ILink link);

    /**
     * To remove a link
     *
     * @param linkID the link ID to be removed
     */
    public boolean removeLink(String linkID) {
        int index = -999;
        boolean wasFoundAndRemoved = false;
        for (int i = 0; i < smartLinkList.size(); i++) {
            if (((SmartLink) smartLinkList.get(i)).link.getID() == linkID) {
                index = i;
            }
        }
        if (index >= 0) {
            smartLinkList.remove(index);
            wasFoundAndRemoved = true;
        }
        return wasFoundAndRemoved;
    }

    /**
     * To get a link
     *
     * @param LinkID the link ID of the link
     */
    public ILink getLink(String LinkID) throws Exception {
        for (int i = 0; i < smartLinkList.size(); i++) {
            if (((SmartLink) smartLinkList.get(i)).link.getID().equals(LinkID)) {
                return ((SmartLink) smartLinkList.get(i)).link;
            }
        }
        throw new Exception("Failed to find link in linkList");
    }
}
