package org.citycarshare.application.authentication;

import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.citycarshare.util.*;
import org.citycarshare.application.*;
import org.citycarshare.application.authentication.*;

/**
 * Defines the business rules that determine likely-to-change
 * aspects of membership to pods.
 *
 * @author Brian "Dexter" Allen, vector@acm.org
 * @version $Id: MembershipRules.java,v 1.2 2002/06/20 03:05:56 dexter_allen Exp $
 */
public class MembershipRules {

    public static Set getAllRegionsForPods(Collection argPods) {
        Utils.ensureNotNull(argPods, "argPods");
        Iterator i = argPods.iterator();
        Pod currPod;
        Region currRegion;
        Set allRegions = new HashSet();
        while (i.hasNext()) {
            currPod = (Pod) i.next();
            addRegionTree(currPod.getRegion(), allRegions);
        }
        return allRegions;
    }

    private static void addRegionTree(Region argCurrRegion, Collection outAllRegions) {
        if (argCurrRegion == null) {
            return;
        }
        addRegionTree(argCurrRegion.getParent(), outAllRegions);
        outAllRegions.add(argCurrRegion);
    }

    /**
     * Entry function to get a Set of all the Pods that this
     * set of MembershipGroups allows.
     * This is currently defined as all of the Pods that are
     * actively linked to this Membership Group, plus all those
     * linked to this Membership Group's parent, and so on up the
     * hierarchy of MembershipGroups.
     * Note that a pod must be associated with a MembershipGroup
     * to be allowed.
     */
    public static Set getAllowedPods(Collection argMembershipGroups) {
        Set pods = new HashSet();
        if (argMembershipGroups == null) {
            return pods;
        }
        MembershipGroup mg = null;
        Iterator i = argMembershipGroups.iterator();
        while (i.hasNext()) {
            mg = (MembershipGroup) i.next();
            addAllowedPods(mg, pods);
        }
        return pods;
    }

    /**
     * Recursively walk up the MembershipGroup tree, adding
     * resulting pods along the way.
     */
    private static void addAllowedPods(MembershipGroup argGrp, Collection outPods) {
        if (argGrp == null) {
            return;
        }
        addAllowedPods(argGrp.getParent(), outPods);
        outPods.addAll(argGrp.getPods());
    }

    private static Log ourLog = LogFactory.getLog(MembershipRules.class.getName());
}
