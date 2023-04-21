package au.edu.qut.yawl.elements.e2wfoj;

import au.edu.qut.yawl.elements.*;
import au.edu.qut.yawl.elements.data.YParameter;
import au.edu.qut.yawl.elements.data.YVariable;
import au.edu.qut.yawl.elements.state.YIdentifier;
import au.edu.qut.yawl.elements.state.YMarking;
import au.edu.qut.yawl.elements.state.YOrJoinUtils;
import au.edu.qut.yawl.elements.state.YSetOfMarkings;
import au.edu.qut.yawl.elements.state.YInternalCondition;
import au.edu.qut.yawl.exceptions.YStateException;
import au.edu.qut.yawl.exceptions.YSyntaxException;
import au.edu.qut.yawl.util.YVerificationMessage;
import java.io.*;
import java.util.*;
import java.util.Collections;

/**
 *  A Reset net formalisation of a YAWL net.
 *
 **/
public final class E2WFOJNet {

    private Map _Transitions = new HashMap(100);

    private Map _Places = new HashMap(100);

    private Map _OJ = new HashMap();

    private Map _YOJ = new HashMap();

    private YTask _orJoin;

    private YNet _yNet;

    private Set alreadyConsideredMarkings = new HashSet(100);

    private Set _Conditions = new HashSet(100);

    /**
     * Constructor for Reset net.
     *
     */
    public E2WFOJNet(YNet yNet, YTask orJoin) {
        _yNet = yNet;
        _orJoin = orJoin;
        ConvertToResetNet();
        OJRemove(_orJoin);
        _OJ = null;
        _YOJ = null;
        _orJoin = null;
        _yNet = null;
    }

    private E2WFOJNet() {
    }

    /**
     * The method converts a YAWL net into a Reset net.
     *
     */
    private void ConvertToResetNet() {
        Map netElements = _yNet.getNetElements();
        Iterator netEles = netElements.values().iterator();
        while (netEles.hasNext()) {
            YExternalNetElement nextElement = (YExternalNetElement) netEles.next();
            if (nextElement instanceof YCondition) {
                RPlace p = new RPlace(nextElement.getID());
                _Places.put(p.getID(), p);
                _Conditions.add(nextElement);
            } else if (nextElement instanceof YTask) {
                RPlace p = new RPlace("p_" + nextElement.getID());
                _Places.put(p.getID(), p);
            }
        }
        Map _StartTransitions = new HashMap();
        Map _EndTransitions = new HashMap();
        Iterator netEls = netElements.values().iterator();
        while (netEls.hasNext()) {
            YExternalNetElement next = (YExternalNetElement) netEls.next();
            if (next instanceof YTask) {
                YTask nextElement = (YTask) next;
                if (nextElement.getJoinType() == nextElement._AND) {
                    RTransition t = new RTransition(nextElement.getID() + "_start");
                    _StartTransitions.put(t.getID(), t);
                    Set pre = nextElement.getPresetElements();
                    Iterator preEls = pre.iterator();
                    while (preEls.hasNext()) {
                        YExternalNetElement preElement = (YExternalNetElement) preEls.next();
                        RFlow inflow = new RFlow((RPlace) _Places.get(preElement.getID()), t);
                        t.setPreset(inflow);
                        RFlow outflow = new RFlow(t, (RPlace) _Places.get("p_" + nextElement.getID()));
                        t.setPostset(outflow);
                    }
                } else if (nextElement.getJoinType() == nextElement._XOR) {
                    Set pre = nextElement.getPresetElements();
                    Iterator preEls = pre.iterator();
                    while (preEls.hasNext()) {
                        YExternalNetElement preElement = (YExternalNetElement) preEls.next();
                        RTransition t = new RTransition(nextElement.getID() + "_start^" + preElement.getID());
                        _StartTransitions.put(t.getID(), t);
                        RFlow inflow = new RFlow((RPlace) _Places.get(preElement.getID()), t);
                        t.setPreset(inflow);
                        RFlow outflow = new RFlow(t, (RPlace) _Places.get("p_" + nextElement.getID()));
                        t.setPostset(outflow);
                    }
                } else if (nextElement.getJoinType() == nextElement._OR) {
                    RTransition t = new RTransition(nextElement.getID() + "_start");
                    _StartTransitions.put(t.getID(), t);
                    _OJ.put(t.getID(), t);
                    _YOJ.put(nextElement.getID(), nextElement);
                }
                if (nextElement.getSplitType() == nextElement._AND) {
                    RTransition t = new RTransition(nextElement.getID() + "_end");
                    _EndTransitions.put(t.getID(), t);
                    Set post = nextElement.getPostsetElements();
                    Iterator postEls = post.iterator();
                    while (postEls.hasNext()) {
                        YExternalNetElement postElement = (YExternalNetElement) postEls.next();
                        RFlow inflow = new RFlow((RPlace) _Places.get("p_" + nextElement.getID()), t);
                        t.setPreset(inflow);
                        RFlow outflow = new RFlow(t, (RPlace) _Places.get(postElement.getID()));
                        t.setPostset(outflow);
                    }
                    Set removeSet = new HashSet(nextElement.getRemoveSet());
                    if (!removeSet.isEmpty()) {
                        addCancelSet(t, removeSet);
                    }
                } else if (nextElement.getSplitType() == nextElement._XOR) {
                    Set post = nextElement.getPostsetElements();
                    Iterator postEls = post.iterator();
                    while (postEls.hasNext()) {
                        YExternalNetElement postElement = (YExternalNetElement) postEls.next();
                        RTransition t = new RTransition(nextElement.getID() + "_end^" + postElement.getID());
                        _EndTransitions.put(t.getID(), t);
                        RFlow inflow = new RFlow((RPlace) _Places.get("p_" + nextElement.getID()), t);
                        t.setPreset(inflow);
                        RFlow outflow = new RFlow(t, (RPlace) _Places.get(postElement.getID()));
                        t.setPostset(outflow);
                        Set removeSet = new HashSet(nextElement.getRemoveSet());
                        if (!removeSet.isEmpty()) {
                            addCancelSet(t, removeSet);
                        }
                    }
                } else if (nextElement.getSplitType() == nextElement._OR) {
                    Set xSubSet = new HashSet();
                    Set post = nextElement.getPostsetElements();
                    for (int i = 1; i <= post.size(); i++) {
                        Set subSet = generateCombination(post, i);
                        xSubSet.addAll(subSet);
                    }
                    for (Iterator xSubSetEls = xSubSet.iterator(); xSubSetEls.hasNext(); ) {
                        Set x = (Set) xSubSetEls.next();
                        String t_id = "";
                        for (Iterator i = x.iterator(); i.hasNext(); ) {
                            YExternalNetElement postElement = (YExternalNetElement) i.next();
                            t_id += postElement.getID() + " ";
                        }
                        RTransition t = new RTransition(nextElement.getID() + "_end^{" + t_id + "}");
                        _EndTransitions.put(t.getID(), t);
                        RFlow inflow = new RFlow((RPlace) _Places.get("p_" + nextElement.getID()), t);
                        t.setPreset(inflow);
                        for (Iterator i = x.iterator(); i.hasNext(); ) {
                            YExternalNetElement postElement = (YExternalNetElement) i.next();
                            RFlow outflow = new RFlow(t, (RPlace) _Places.get(postElement.getID()));
                            t.setPostset(outflow);
                        }
                        Set removeSet = new HashSet(nextElement.getRemoveSet());
                        if (!removeSet.isEmpty()) {
                            addCancelSet(t, removeSet);
                        }
                    }
                }
            }
        }
        _Transitions.putAll(_StartTransitions);
        _Transitions.putAll(_EndTransitions);
    }

    /**
     * This method is used to generate combinations of markings for 
     * comparison. 
     */
    private Set generateCombination(Set netElements, int size) {
        Set subSets = new HashSet();
        Object[] elements = netElements.toArray();
        int[] indices;
        CombinationGenerator x = new CombinationGenerator(elements.length, size);
        while (x.hasMore()) {
            Set combsubSet = new HashSet();
            indices = x.getNext();
            for (int i = 0; i < indices.length; i++) {
                combsubSet.add(elements[indices[i]]);
            }
            subSets.add(combsubSet);
        }
        return subSets;
    }

    /**
     *This method is used to associate a cancellation set (RPlaces) with each RTransition.
     *This is the implementation of R.
     */
    private void addCancelSet(RTransition rt, Set removeSet) {
        Set removeSetT = new HashSet(removeSet);
        Set removeSetR = new HashSet();
        removeSet.retainAll(_Conditions);
        for (Iterator i = removeSet.iterator(); i.hasNext(); ) {
            YExternalNetElement c = (YExternalNetElement) i.next();
            RPlace p = (RPlace) _Places.get(c.getID());
            if (p != null) {
                removeSetR.add(p);
            }
        }
        removeSetT.removeAll(_Conditions);
        for (Iterator i = removeSetT.iterator(); i.hasNext(); ) {
            YExternalNetElement t = (YExternalNetElement) i.next();
            RPlace p = (RPlace) _Places.get("p_" + t.getID());
            if (p != null) {
                removeSetR.add(p);
            }
        }
        rt.setRemoveSet(removeSetR);
    }

    /**
     * This method is called with an OR-join task to 
     * prepare for OR-join enabling call.
     *
     **/
    private void OJRemove(YTask j) {
        _YOJ.remove(j.getID());
        for (Iterator i = _OJ.values().iterator(); i.hasNext(); ) {
            RTransition rj = (RTransition) i.next();
            _Transitions.remove(rj.getID());
        }
        for (Iterator i = _YOJ.values().iterator(); i.hasNext(); ) {
            YExternalNetElement otherOrjoin = (YExternalNetElement) i.next();
            Set pre = otherOrjoin.getPresetElements();
            Iterator preEls = pre.iterator();
            while (preEls.hasNext()) {
                YExternalNetElement preElement = (YExternalNetElement) preEls.next();
                RTransition t = new RTransition(otherOrjoin.getID() + "_start^" + preElement.getID());
                _Transitions.put(t.getID(), t);
                RFlow inflow = new RFlow((RPlace) _Places.get(preElement.getID()), t);
                t.setPreset(inflow);
                RFlow outflow = new RFlow(t, (RPlace) _Places.get("p_" + otherOrjoin.getID()));
                t.setPostset(outflow);
            }
        }
    }

    /**
     * Perform structural restriction of a reset net.
     */
    public void restrictNet(YTask j) {
        Set restrictedTrans = new HashSet();
        Set restrictedPlaces = new HashSet();
        Set pre = new HashSet();
        Set Ypre = j.getPresetElements();
        for (Iterator iterPlace = Ypre.iterator(); iterPlace.hasNext(); ) {
            YCondition condition = (YCondition) iterPlace.next();
            RPlace place = (RPlace) _Places.get(condition.getID());
            if (place != null) {
                pre.add(place);
            }
        }
        Set rk = new HashSet();
        Set trans = getPreset(pre);
        restrictedTrans.addAll(trans);
        restrictedPlaces.addAll(pre);
        while (!rk.equals(restrictedTrans)) {
            rk = new HashSet(restrictedTrans);
            pre = getPreset(trans);
            trans = getPreset(pre);
            restrictedTrans.addAll(trans);
            restrictedPlaces.addAll(pre);
        }
        performRestriction(restrictedTrans, restrictedPlaces);
    }

    /**
    * This method is used to perform active projection restriction.
    *
    */
    public void restrictNet(YMarking M) {
        Set markedPlaces = new HashSet();
        Set Ymarked = new HashSet(M.getLocations());
        for (Iterator iterPlace = Ymarked.iterator(); iterPlace.hasNext(); ) {
            YNetElement nextElement = (YNetElement) iterPlace.next();
            if (nextElement instanceof YCondition) {
                RPlace place = (RPlace) _Places.get(nextElement.getID());
                if (place != null) {
                    markedPlaces.add(place);
                }
            }
            if (nextElement instanceof YTask) {
                String internalPlace = "p_" + nextElement.getID();
                RPlace place = (RPlace) _Places.get(internalPlace);
                if (place != null) {
                    markedPlaces.add(place);
                }
            }
        }
        Set restrictedTrans = new HashSet();
        Set restrictedPlaces = new HashSet();
        Set post = new HashSet();
        Set fk = new HashSet();
        Set trans = getPostset(markedPlaces);
        restrictedTrans.addAll(trans);
        restrictedPlaces.addAll(markedPlaces);
        while (!fk.equals(restrictedTrans)) {
            fk = new HashSet(restrictedTrans);
            post = getPostset(trans);
            trans = getPostset(post);
            restrictedTrans.addAll(trans);
            restrictedPlaces.addAll(post);
        }
        Set TransToRemove = new HashSet(_Transitions.values());
        for (Iterator iterT = TransToRemove.iterator(); iterT.hasNext(); ) {
            RElement tElement = (RElement) iterT.next();
            Set preSet = tElement.getPresetElements();
            if (!restrictedPlaces.containsAll(preSet)) {
                restrictedTrans.remove(tElement);
            }
        }
        performRestriction(restrictedTrans, restrictedPlaces);
    }

    /**
    * Private method for removing tranisitions and places. Used for both
    * structural restriction and marking dependent restriction.
    *
    */
    private void performRestriction(Set restrictedTrans, Set restrictedPlaces) {
        Set irrelevantTrans = new HashSet(_Transitions.values());
        irrelevantTrans.removeAll(restrictedTrans);
        for (Iterator iterT = irrelevantTrans.iterator(); iterT.hasNext(); ) {
            RTransition t = (RTransition) iterT.next();
            _Transitions.remove(t.getID());
        }
        for (Iterator iterT = restrictedTrans.iterator(); iterT.hasNext(); ) {
            RTransition t = (RTransition) iterT.next();
            if (t.isCancelTransition()) {
                Set removeSet = new HashSet(t.getRemoveSet());
                removeSet.retainAll(restrictedPlaces);
            }
            RElement tElement = (RElement) t;
            Set postSetToRemove = new HashSet(tElement.getPostsetElements());
            postSetToRemove.removeAll(restrictedPlaces);
            if (postSetToRemove.size() > 0) {
                Map postSetFlows = new HashMap(tElement.getPostsetFlows());
                for (Iterator iterP = postSetToRemove.iterator(); iterP.hasNext(); ) {
                    RPlace p = (RPlace) iterP.next();
                    postSetFlows.remove(p.getID());
                }
                tElement.setPostsetFlows(postSetFlows);
            }
            Set preSetToRemove = new HashSet(tElement.getPresetElements());
            preSetToRemove.removeAll(restrictedPlaces);
            if (preSetToRemove.size() > 0) {
                Map preSetFlows = new HashMap(tElement.getPresetFlows());
                for (Iterator iterP = preSetToRemove.iterator(); iterP.hasNext(); ) {
                    RPlace p = (RPlace) iterP.next();
                    preSetFlows.remove(p.getID());
                }
                tElement.setPresetFlows(preSetFlows);
            }
        }
        Set irrelevantPlaces = new HashSet(_Places.values());
        irrelevantPlaces.removeAll(restrictedPlaces);
        for (Iterator iterP = irrelevantPlaces.iterator(); iterP.hasNext(); ) {
            RPlace p = (RPlace) iterP.next();
            _Places.remove(p.getID());
            RElement pElement = (RElement) p;
            Set preSetToRemove = new HashSet(pElement.getPresetElements());
            preSetToRemove.removeAll(restrictedTrans);
            if (preSetToRemove.size() > 0) {
                Map preSetFlows = new HashMap(pElement.getPresetFlows());
                for (Iterator iterT = preSetToRemove.iterator(); iterT.hasNext(); ) {
                    RTransition t = (RTransition) iterT.next();
                    preSetFlows.remove(t.getID());
                }
                pElement.setPresetFlows(preSetFlows);
            }
            Set postSetToRemove = new HashSet(pElement.getPostsetElements());
            postSetToRemove.removeAll(restrictedTrans);
            if (postSetToRemove.size() > 0) {
                Map postSetFlows = new HashMap(pElement.getPostsetFlows());
                for (Iterator iterT = postSetToRemove.iterator(); iterT.hasNext(); ) {
                    RTransition t = (RTransition) iterT.next();
                    postSetFlows.remove(t.getID());
                }
                pElement.setPostsetFlows(postSetFlows);
            }
        }
    }

    private static Set getPostset(Set elements) {
        Set postset = new HashSet();
        Iterator iter = elements.iterator();
        while (iter.hasNext()) {
            RElement e = (RElement) iter.next();
            postset.addAll(e.getPostsetElements());
        }
        return postset;
    }

    private static Set getPreset(Set elements) {
        Set preset = new HashSet();
        Iterator iter = elements.iterator();
        while (iter.hasNext()) {
            RElement e = (RElement) iter.next();
            preset.addAll(e.getPresetElements());
        }
        return preset;
    }

    /**
     * This method takes two markings s and t, and check whether s'<= s is coverable 
     * from the predecessors of t.
     */
    private boolean Coverable(RMarking s, RMarking t) {
        alreadyConsideredMarkings = new HashSet();
        RSetOfMarkings tSet = new RSetOfMarkings();
        tSet.addMarking(t);
        RSetOfMarkings rm = FiniteBasisPred(tSet);
        RMarking x;
        for (Iterator iter = rm.getMarkings().iterator(); iter.hasNext(); ) {
            x = (RMarking) iter.next();
            if (x.isLessThanOrEqual(s)) {
                alreadyConsideredMarkings = null;
                return true;
            }
        }
        alreadyConsideredMarkings = null;
        rm = null;
        return false;
    }

    /**
     * This methods returns the FiniteBasis of the Predecessors for a set of 
     * RMarkings.
     */
    private RSetOfMarkings FiniteBasisPred(RSetOfMarkings I) {
        RSetOfMarkings K = new RSetOfMarkings();
        RSetOfMarkings Kn = new RSetOfMarkings();
        RSetOfMarkings Pred = new RSetOfMarkings();
        K.addAll(I);
        Pred.addAll(K);
        Kn = getMinimalCoveringSet(pb(K), Pred);
        while (!IsUpwardEqual(K, Kn)) {
            K.removeAll();
            K.addAll(Kn);
            Pred.removeAll();
            Pred.addAll(K);
            Kn = getMinimalCoveringSet(pb(K), Pred);
        }
        Kn = null;
        Pred = null;
        return K;
    }

    /**
     * This method checks whether the basis of the two sets of markings are equal.
     * This is the implementation of K = Kn.
     *
     */
    private boolean IsUpwardEqual(RSetOfMarkings K, RSetOfMarkings Kn) {
        return K.equals(Kn);
    }

    /**
     * This method is called with a set of RMarkings to generate
     * a set of precedessors. 
     */
    private RSetOfMarkings pb(RSetOfMarkings I) {
        RSetOfMarkings Z = new RSetOfMarkings();
        RMarking M;
        for (Iterator i = I.getMarkings().iterator(); i.hasNext(); ) {
            M = (RMarking) i.next();
            Z.addAll(pb(M));
        }
        Z = getMinimalCoveringSet2(Z);
        return Z;
    }

    /**
     * This method is called with a RMarking to generate
     * a set of precedessors of this marking. The method returns 
     * a finite basis for the predecessors set.(optimisation) 
     */
    private RSetOfMarkings pb(RMarking M) {
        RSetOfMarkings Z = new RSetOfMarkings();
        if (!alreadyConsideredMarkings.contains(M)) {
            for (Iterator i = _Transitions.values().iterator(); i.hasNext(); ) {
                RTransition t = (RTransition) i.next();
                if (isBackwardsEnabled(M, t)) {
                    RMarking preM = getPreviousRMarking(M, t);
                    if (!preM.isBiggerThanOrEqual(M)) {
                        Z.addMarking(preM);
                    }
                }
            }
            alreadyConsideredMarkings.add(M);
        }
        return Z;
    }

    /**
    * This method determines whether a transition should be backwards 
    * enabled at a given RMarking. Currently, a transition 
    * is not backwards enabled only if there is a token in removeSet. 
    * 
    */
    private boolean isBackwardsEnabled(RMarking currentM, RTransition t) {
        Set postSet = t.getPostsetElements();
        Set removeSet = t.getRemoveSet();
        Map markedPlaces = currentM.getMarkedPlaces();
        Integer count;
        if (removeSet.size() > 0) {
            for (Iterator x = removeSet.iterator(); x.hasNext(); ) {
                RPlace place = (RPlace) x.next();
                String placeName = place.getID();
                if (markedPlaces.containsKey(placeName)) {
                    if (postSet.contains(place)) {
                        count = (Integer) markedPlaces.get(placeName);
                        if (count.intValue() > 1) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private RMarking getPreviousRMarking(RMarking currentM, RTransition t) {
        Map premarkedPlaces = new HashMap(currentM.getMarkedPlaces());
        Set postSet = new HashSet(t.getPostsetElements());
        Set preSet = new HashSet(t.getPresetElements());
        Set removeSet = new HashSet(t.getRemoveSet());
        RElement netElement;
        String netElementName;
        Integer countString, tokenCount;
        postSet.removeAll(removeSet);
        for (Iterator iterator = postSet.iterator(); iterator.hasNext(); ) {
            netElement = (RElement) iterator.next();
            netElementName = netElement.getID();
            if (premarkedPlaces.containsKey(netElementName)) {
                countString = (Integer) premarkedPlaces.get(netElementName);
                int count = countString.intValue();
                if (count == 1) {
                    premarkedPlaces.remove(netElementName);
                } else if (count > 1) {
                    count = count - 1;
                    tokenCount = new Integer(count);
                    premarkedPlaces.put(netElementName, tokenCount);
                }
            }
        }
        preSet.removeAll(removeSet);
        tokenCount = new Integer(1);
        for (Iterator iterator = preSet.iterator(); iterator.hasNext(); ) {
            netElement = (RElement) iterator.next();
            netElementName = netElement.getID();
            if (premarkedPlaces.containsKey(netElementName)) {
                countString = (Integer) premarkedPlaces.get(netElementName);
                int count = countString.intValue();
                count++;
                tokenCount = new Integer(count);
            }
            premarkedPlaces.put(netElementName, tokenCount);
        }
        removeSet.retainAll(t.getPresetElements());
        tokenCount = new Integer(1);
        for (Iterator iterator = removeSet.iterator(); iterator.hasNext(); ) {
            netElement = (RElement) iterator.next();
            netElementName = netElement.getID();
            premarkedPlaces.put(netElementName, tokenCount);
        }
        return new RMarking(premarkedPlaces);
    }

    /**
     * This method is used to determine whether 
     * an OrJoin task of a YAWL net should be enabled at 
     * a given marking. The method returns TRUE if an OrJoin 
     * should be enabled at the given marking and FALSE, otherwise.
     */
    public boolean orJoinEnabled(YMarking M, YTask orJoin) {
        Set MarkedTasks = new HashSet();
        Map RMap = new HashMap();
        List YLocations = new Vector(M.getLocations());
        for (Iterator i = YLocations.iterator(); i.hasNext(); ) {
            YNetElement nextElement = (YNetElement) i.next();
            if (nextElement instanceof YCondition) {
                YCondition condition = (YCondition) nextElement;
                RPlace place = (RPlace) _Places.get(condition.getID());
                if (place != null) {
                    String placename = place.getID();
                    Integer tokenCount = new Integer(1);
                    if (RMap.containsKey(placename)) {
                        Object value = RMap.get(placename);
                        Integer countString = new Integer(value.toString());
                        int count = countString.intValue();
                        count++;
                        tokenCount = new Integer(count);
                    }
                    RMap.put(placename, tokenCount);
                }
            }
            if (nextElement instanceof YTask) {
                MarkedTasks.add(nextElement);
            }
        }
        for (Iterator placeConvIter = MarkedTasks.iterator(); placeConvIter.hasNext(); ) {
            YTask task = (YTask) placeConvIter.next();
            String internalPlace = "p_" + task.getID();
            RPlace place = (RPlace) _Places.get(internalPlace);
            if (place != null) {
                String placename = place.getID();
                Integer tokenCount = new Integer(1);
                RMap.put(placename, tokenCount);
            }
        }
        RMarking RM = new RMarking(RMap);
        RMap = null;
        Set X = orJoin.getPresetElements();
        Map newMap = new HashMap();
        Set emptyPreSetPlaces = new HashSet();
        Integer tokenCount = new Integer(1);
        for (Iterator x = X.iterator(); x.hasNext(); ) {
            YCondition preSetCondition = (YCondition) x.next();
            RPlace preSetPlace = (RPlace) _Places.get(preSetCondition.getID());
            if (preSetPlace != null) {
                if (YLocations.contains(preSetCondition)) {
                    String preSetPlaceName = preSetPlace.getID();
                    newMap.put(preSetPlaceName, tokenCount);
                } else {
                    emptyPreSetPlaces.add(preSetPlace);
                }
            }
        }
        RSetOfMarkings Y = new RSetOfMarkings();
        for (Iterator i = emptyPreSetPlaces.iterator(); i.hasNext(); ) {
            RPlace q = (RPlace) i.next();
            tokenCount = new Integer(1);
            String qname = q.getID();
            newMap.put(qname, tokenCount);
            RMarking M_w = new RMarking(new HashMap(newMap));
            Y.addMarking(M_w);
            newMap.remove(qname);
        }
        for (Iterator i = Y.getMarkings().iterator(); i.hasNext(); ) {
            RMarking M_w = (RMarking) i.next();
            if (Coverable(RM, M_w)) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method is used to generate the minimal covering set 
     * for a given set of Markings.
     */
    private RSetOfMarkings getMinimalCoveringSet2(RSetOfMarkings Z) {
        RSetOfMarkings Z_min = new RSetOfMarkings();
        Z_min.addAll(Z);
        for (Iterator z = Z.getMarkings().iterator(); z.hasNext(); ) {
            RMarking M = (RMarking) z.next();
            RSetOfMarkings Z_inner = new RSetOfMarkings();
            Z_inner.addAll(Z_min);
            Z_inner.removeMarking(M);
            for (Iterator x = Z_inner.getMarkings().iterator(); x.hasNext(); ) {
                RMarking M_i = (RMarking) x.next();
                if (M.isBiggerThanOrEqual(M_i)) {
                    Z_min.removeMarking(M);
                }
            }
        }
        return Z_min;
    }

    private RSetOfMarkings getMinimalCoveringSet(RSetOfMarkings pbZ, RSetOfMarkings Z) {
        RSetOfMarkings Z_min = new RSetOfMarkings();
        Z_min.addAll(Z);
        Z_min.addAll(pbZ);
        for (Iterator z = pbZ.getMarkings().iterator(); z.hasNext(); ) {
            RMarking M = (RMarking) z.next();
            RSetOfMarkings Z_inner = new RSetOfMarkings();
            Z_inner.addAll(Z_min);
            Z_inner.removeMarking(M);
            for (Iterator x = Z_inner.getMarkings().iterator(); x.hasNext(); ) {
                RMarking M_i = (RMarking) x.next();
                if (M.isBiggerThanOrEqual(M_i)) {
                    Z_min.removeMarking(M);
                } else if (M_i.isBiggerThanOrEqual(M)) {
                    Z_min.removeMarking(M_i);
                }
            }
        }
        return Z_min;
    }
}
