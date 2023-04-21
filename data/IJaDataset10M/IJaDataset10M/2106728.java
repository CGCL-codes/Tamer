package org.dbe.composer.wfengine.bpel.def.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import org.dbe.composer.wfengine.bpel.def.ISdlMultipleActivityContainerDef;
import org.dbe.composer.wfengine.bpel.def.SdlActivityDef;
import org.dbe.composer.wfengine.bpel.def.activity.support.SdlLinkContainerDef;
import org.dbe.composer.wfengine.bpel.def.activity.support.SdlLinkDef;
import org.dbe.composer.wfengine.bpel.def.visitors.ISdlDefVisitor;

/**
 * Definition for bpel flow activity.
 */
public class SdlActivityFlowDef extends SdlActivityDef implements ISdlMultipleActivityContainerDef {

    private ArrayList mActivities = new ArrayList();

    /** Container used to store links for Flow. */
    private SdlLinkContainerDef mLinks;

    /**
     * Default constructor
     */
    public SdlActivityFlowDef() {
    }

    /**
     * Getter for the link container. Will create the container if it's null.
     */
    public SdlLinkContainerDef getLinkContainer() {
        if (mLinks == null) {
            mLinks = new SdlLinkContainerDef();
        }
        return mLinks;
    }

    /**
     * Provides the ability to add a Link to the Flow activity.
     *
     * @param aLink the link to be added.
     */
    public void addLink(SdlLinkDef aLink) {
        getLinkContainer().add(aLink);
    }

    /**
     * Provides the ability to remove a Link from a Flow activity.
     *
     * @param aLink the Link element to be removed
     * @return boolean status indicating if element was removed
     */
    public boolean removeLink(SdlLinkDef aLink) {
        return getLinkContainer().remove(aLink);
    }

    /**
     * Provide a list of the Link objects for the user to iterate .
     *
     * @return iterator of SdlLinkDef objects
     */
    public Iterator getLinkList() {
        if (mLinks == null) return Collections.EMPTY_LIST.iterator(); else return mLinks.getLinks();
    }

    /**
     * Adds an activity definition to the list of activities to execute.
     * @param aActivity the link to be added.
     */
    public void addActivity(SdlActivityDef aActivity) {
        mActivities.add(aActivity);
    }

    /**
     * Provides the ability to delete an activity.
     * @param aActivity the activity to remove from container
     * @return boolean status indicating if element was removed
     */
    public boolean removeActivity(SdlActivityDef aActivity) {
        boolean found = false;
        Iterator iter = getActivityList();
        while (iter.hasNext() && !found) {
            if (aActivity == (SdlActivityDef) iter.next()) {
                iter.remove();
                found = true;
            }
        }
        return found;
    }

    /**
     * Provide a list of the activity elements for the user to iterate .
     * @return iterator of SdlActivityDef objects
     */
    public Iterator getActivityList() {
        return mActivities.iterator();
    }

    public void accept(ISdlDefVisitor aVisitor) {
        aVisitor.visit(this);
    }
}
