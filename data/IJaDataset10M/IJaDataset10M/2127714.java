package org.piax.ov.ovs.itsg;

import static org.piax.trans.Literals.map;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.grlea.log.SimpleLogger;
import org.piax.ov.OverlayManager;
import org.piax.ov.common.KeyComparator;
import org.piax.ov.common.Range;
import org.piax.ov.ovs.risg.RISkipGraph.Op;
import org.piax.ov.ovs.rrsg.RRSkipGraph;
import org.piax.ov.ovs.skipgraph.MembershipVector;
import org.piax.ov.ovs.skipgraph.SkipGraph;
import org.piax.ov.ovs.skipgraph.SkipGraph.Arg;
import org.piax.ov.ovs.skipgraph.SkipGraph.CheckOp;
import org.piax.trans.Node;
import org.piax.trans.ResponseChecker;
import org.piax.trans.common.Id;
import org.piax.trans.sim.SimTransportOracle;

public class ITSkipGraphZen extends SkipGraph {

    SimpleLogger log = new SimpleLogger(ITSkipGraph.class);

    public Comparable<?> rangeEnd;

    MaxTable maxes;

    public static enum Arg {

        MAX, OPTIONAL_NODE, TOP_MAX, ONOFF, DIRECTION, RANGE, BOUNDARY
    }

    public static enum Op {

        BUDDY_WITH_MAX, SET_LINK_WITH_MAX, GET_LINK_WITH_MAX, UPDATE_MAX, UPDATE_LEFT_NEIGHBOR_MAX, UPDATE_LEVEL_MAX_NODE, RANGE_SEARCH, FOUND_IN_RANGE, NOT_FOUND_IN_RANGE, SEARCH_DESCENDANTS, REQUEST_UPDATE_RIGHT_NODE
    }

    public static int LEFT_MAX = 0;

    public static int LEFT_NEIGHBOR_MAX = 1;

    public static int LEFT_MAX_NODE = 2;

    public static int ON = 1;

    public static int OFF = 0;

    public static int DOWN = 1;

    public static int UP = 0;

    public static int VER = 2;

    public static int HOR = 3;

    public static int INCLINED = 4;

    public ITSkipGraphZen(Node self) {
        super(self);
        this.rangeEnd = (Comparable<?>) self.getAttr(OverlayManager.RANGE_END);
        maxes = new MaxTable();
    }

    public Comparable<?> getRangeEnd() {
        return rangeEnd;
    }

    protected Map<Object, Object> foundInRangeOp(Node v) {
        return map((Object) SkipGraph.Arg.OP, (Object) Op.FOUND_IN_RANGE).map(SkipGraph.Arg.NODE, v);
    }

    protected Map<Object, Object> notFoundInRangeOp(Node v) {
        return map((Object) SkipGraph.Arg.OP, (Object) Op.NOT_FOUND_IN_RANGE).map(SkipGraph.Arg.NODE, v);
    }

    protected Map<Object, Object> rangeSearchOp(Node startNode, Range searchRange, int level) {
        return map((Object) SkipGraph.Arg.OP, (Object) Op.RANGE_SEARCH).map(SkipGraph.Arg.NODE, startNode).map(Arg.RANGE, searchRange).map(SkipGraph.Arg.LEVEL, level);
    }

    protected Map<Object, Object> rangeSearchOp(Node startNode, Range searchRange, int level, Object body) {
        return map((Object) SkipGraph.Arg.OP, (Object) Op.RANGE_SEARCH).map(SkipGraph.Arg.NODE, startNode).map(Arg.RANGE, searchRange).map(SkipGraph.Arg.LEVEL, level).map(SkipGraph.Arg.BODY, body);
    }

    protected Map<Object, Object> rangeSendOp(Range searchRange, int level, Object body) {
        return map((Object) SkipGraph.Arg.OP, (Object) Op.RANGE_SEARCH).map(Arg.RANGE, searchRange).map(SkipGraph.Arg.LEVEL, level).map(SkipGraph.Arg.BODY, body);
    }

    protected Map<Object, Object> searchDescendantsOp(Range searchRange, Comparable<?> boundary, int level, Object body) {
        return map((Object) SkipGraph.Arg.OP, (Object) Op.SEARCH_DESCENDANTS).map(Arg.RANGE, searchRange).map(Arg.BOUNDARY, boundary).map(SkipGraph.Arg.LEVEL, level).map(SkipGraph.Arg.BODY, body);
    }

    protected Map<Object, Object> opUpdate(Map<Object, Object> mes, Range searchRange, int level) {
        Map<Object, Object> newMes = new HashMap<Object, Object>();
        newMes.putAll(mes);
        newMes.put(Arg.RANGE, searchRange);
        newMes.put(SkipGraph.Arg.LEVEL, level);
        return newMes;
    }

    protected Map<Object, Object> getLinkWithMaxOp(Node u, int side, int level, Comparable<?> max, Node optionalNode) {
        return map((Object) SkipGraph.Arg.OP, (Object) Op.GET_LINK_WITH_MAX).map(SkipGraph.Arg.NODE, u).map(SkipGraph.Arg.SIDE, side).map(SkipGraph.Arg.LEVEL, level).map(Arg.MAX, max).map(Arg.OPTIONAL_NODE, optionalNode);
    }

    protected Map<Object, Object> buddyWithMaxOp(Node node, int side, int level, MembershipVector m, Comparable<?> currentMax, Node optionalNode) {
        return map((Object) SkipGraph.Arg.OP, (Object) Op.BUDDY_WITH_MAX).map(SkipGraph.Arg.NODE, node).map(SkipGraph.Arg.SIDE, side).map(SkipGraph.Arg.LEVEL, level).map(SkipGraph.Arg.VAL, m).map(Arg.MAX, currentMax).map(Arg.OPTIONAL_NODE, optionalNode);
    }

    protected Map<Object, Object> requestUpdateRightNodeOp(Node node, int level) {
        return map((Object) SkipGraph.Arg.OP, (Object) Op.REQUEST_UPDATE_RIGHT_NODE).map(SkipGraph.Arg.NODE, node).map(SkipGraph.Arg.LEVEL, level);
    }

    protected Map<Object, Object> setLinkWithMaxOp(Node node, int level, Comparable<?> max, Node optionalNode) {
        return map((Object) SkipGraph.Arg.OP, (Object) Op.SET_LINK_WITH_MAX).map(SkipGraph.Arg.NODE, node).map(SkipGraph.Arg.LEVEL, level).map(Arg.MAX, max).map(Arg.OPTIONAL_NODE, optionalNode);
    }

    protected Map<Object, Object> updateLevelMaxNodeOp(Node node, int level, int OnOff) {
        return map((Object) SkipGraph.Arg.OP, (Object) Op.UPDATE_LEVEL_MAX_NODE).map(SkipGraph.Arg.NODE, node).map(SkipGraph.Arg.LEVEL, level).map(Arg.ONOFF, OnOff);
    }

    protected Map<Object, Object> updateMaxOp(Node node, Comparable<?> max) {
        return map((Object) SkipGraph.Arg.OP, (Object) Op.UPDATE_MAX).map(SkipGraph.Arg.NODE, node).map(Arg.MAX, max);
    }

    protected Map<Object, Object> updateLeftNeighborMaxOp(Node node, int level, Comparable<?> max, Comparable<?> topMax, int direction) {
        return map((Object) SkipGraph.Arg.OP, (Object) Op.UPDATE_LEFT_NEIGHBOR_MAX).map(SkipGraph.Arg.NODE, node).map(SkipGraph.Arg.LEVEL, level).map(Arg.MAX, max).map(Arg.TOP_MAX, topMax).map(Arg.DIRECTION, direction);
    }

    private Comparable<?> max(Comparable<?> val1, Comparable<?> val2) {
        if (val1 == null && val2 != null) {
            return val2;
        } else if (val1 != null && val2 == null) {
            return val1;
        } else if (val1 == null && val2 == null) {
            return null;
        } else if (compare(val1, val2) < 0) {
            return val2;
        } else return val1;
    }

    @Override
    public boolean insert(Node introducer) {
        int side, otherSide;
        self.trans.setParameter(SimTransportOracle.Param.NestedWait, Boolean.FALSE);
        if (introducer.equals(self)) {
            neighbors.put(L, 0, null);
            neighbors.put(R, 0, null);
        } else {
            Comparable<?> introducerKey = getKey(introducer);
            if (compare(introducerKey, key) < 0) {
                side = R;
                otherSide = L;
            } else {
                side = L;
                otherSide = R;
            }
            try {
                Map<Object, Object> mr = introducer.sendAndWait(getMaxLevelOp(), new CheckOp(SkipGraph.Op.RET_MAX_LEVEL));
                int maxLevel = (Integer) mr.get(SkipGraph.Arg.LEVEL);
                Map<Object, Object> sr = introducer.sendAndWait(searchOp(self, key, maxLevel), new CheckOp(SkipGraph.Op.NOT_FOUND, SkipGraph.Op.FOUND));
                if (sr.get(SkipGraph.Arg.OP) == SkipGraph.Op.FOUND) {
                    return false;
                }
                Node otherSideNeighbor = (Node) sr.get(SkipGraph.Arg.NODE);
                Map<Object, Object> nr = otherSideNeighbor.sendAndWait(getNeighborOp(side, 0), new CheckOp(SkipGraph.Op.RET_NEIGHBOR));
                Node sideNeighbor = (Node) nr.get(SkipGraph.Arg.NODE);
                if (otherSideNeighbor.equals(sideNeighbor)) {
                    sideNeighbor = null;
                }
                if (otherSideNeighbor != null) {
                    Map<Object, Object> r = otherSideNeighbor.sendAndWait(getLinkOp(self, side, 0), new CheckOp(SkipGraph.Op.SET_LINK));
                    Node newNeighbor = (Node) r.get(SkipGraph.Arg.NODE);
                    int newLevel = (int) ((Integer) r.get(SkipGraph.Arg.LEVEL));
                    neighbors.put(otherSide, newLevel, newNeighbor);
                }
                if (sideNeighbor != null) {
                    Map<Object, Object> r = sideNeighbor.sendAndWait(getLinkOp(self, otherSide, 0), new CheckOp(SkipGraph.Op.SET_LINK));
                    Node newNeighbor = (Node) r.get(SkipGraph.Arg.NODE);
                    int newLevel = (int) ((Integer) r.get(SkipGraph.Arg.LEVEL));
                    neighbors.put(side, newLevel, newNeighbor);
                }
                maxes.put(LEFT_MAX, 0, rangeEnd);
                int l = 0;
                while (true) {
                    l++;
                    m.randomElement(l);
                    Node optionalNode = null;
                    if (neighbors.get(L, l - 1) != null) {
                        Comparable<?> currentMax = null;
                        Map<Object, Object> r = neighbors.get(L, l - 1).sendAndWait(buddyWithMaxOp(self, L, l - 1, m, currentMax, null), new CheckOp(Op.SET_LINK_WITH_MAX));
                        Node newNeighbor = (Node) r.get(SkipGraph.Arg.NODE);
                        neighbors.put(L, l, newNeighbor);
                        Comparable<?> max = (Comparable<?>) r.get(Arg.MAX);
                        maxes.put(LEFT_NEIGHBOR_MAX, l - 1, max);
                        if (neighbors.get(L, l) == null) {
                            if (neighbors.get(L, l - 1) != null) {
                                neighbors.get(L, l - 1).send(updateLevelMaxNodeOp(self, l - 1, ON));
                            }
                        }
                    } else {
                        neighbors.put(L, l, null);
                        if (neighbors.get(L, l - 1) != null) {
                            neighbors.get(L, l - 1).send(updateLevelMaxNodeOp(self, l - 1, ON));
                        }
                    }
                    maxes.put(LEFT_MAX, l, max((Comparable<?>) maxes.get(LEFT_MAX, l - 1), (Comparable<?>) maxes.get(LEFT_NEIGHBOR_MAX, l - 1)));
                    if (neighbors.get(R, l - 1) != null) {
                        Comparable<?> currentMax = null;
                        Map<Object, Object> r = neighbors.get(R, l - 1).sendAndWait(buddyWithMaxOp(self, R, l - 1, m, currentMax, null), new CheckOp(Op.SET_LINK_WITH_MAX));
                        Node newNeighbor = (Node) r.get(SkipGraph.Arg.NODE);
                        neighbors.put(R, l, newNeighbor);
                        optionalNode = (Node) r.get(Arg.OPTIONAL_NODE);
                        if (optionalNode != null) {
                            maxes.put(LEFT_MAX_NODE, l - 1, optionalNode);
                        }
                    } else {
                        neighbors.put(R, l, null);
                    }
                    if (neighbors.get(R, l) == null && neighbors.get(L, l) == null) break;
                }
                maxLevel = l;
                self.send(updateMaxOp(self, (Comparable<?>) maxes.get(LEFT_MAX, maxLevel)));
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private void change_neighbor_with_max(Node u, int side, int l, Comparable<?> max, Node optionalNode) {
        Comparable<?> sideKey = neighbors.getKey(side, l);
        Comparable<?> uKey = getKey(u);
        int cmp = 0;
        if (sideKey != null) {
            if (side == R) {
                cmp = compare(sideKey, uKey);
            } else {
                cmp = compare(uKey, sideKey);
            }
        }
        Node to;
        Map<Object, Object> arg;
        if (cmp < 0) {
            to = neighbors.get(side, l);
            arg = getLinkWithMaxOp(u, side, l, max, optionalNode);
        } else {
            to = u;
            arg = setLinkWithMaxOp(self, l, max, optionalNode);
        }
        try {
            to.send(arg);
            neighbors.put(side, l, u);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void onReceiveGetLinkWithMaxOp(Node sender, Map<Object, Object> args) {
        Node u = (Node) args.get(SkipGraph.Arg.NODE);
        int side = (int) ((Integer) args.get(SkipGraph.Arg.SIDE));
        int l = (int) ((Integer) args.get(SkipGraph.Arg.LEVEL));
        Comparable<?> max = (Comparable<?>) args.get(Arg.MAX);
        Node optionalNode = (Node) args.get(Arg.OPTIONAL_NODE);
        change_neighbor_with_max(u, side, l, max, optionalNode);
    }

    protected void onReceiveBuddyWithMaxOp(Node sender, Map<Object, Object> args) {
        Node u = (Node) args.get(SkipGraph.Arg.NODE);
        Comparable<?> uKey = getKey(u);
        Node optionalNode = (Node) args.get(Arg.OPTIONAL_NODE);
        int l = (int) ((Integer) args.get(SkipGraph.Arg.LEVEL));
        MembershipVector val = (MembershipVector) args.get(SkipGraph.Arg.VAL);
        Comparable<?> max = (Comparable<?>) args.get(Arg.MAX);
        List<Id> via = getVia(args);
        int side = (int) ((Integer) args.get(SkipGraph.Arg.SIDE));
        int level = l;
        if (!m.existsElementAt(l + 1)) {
            if (getMaxLevel() > 0 && side == R) try {
                if (neighbors.get(L, getMaxLevel() - 1) != null) {
                    neighbors.get(L, getMaxLevel() - 1).send(updateLevelMaxNodeOp(self, getMaxLevel() - 1, OFF));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            m.randomElement(l + 1);
            neighbors.put(L, l + 1, null);
            neighbors.put(R, l + 1, null);
            maxes.put(LEFT_MAX, l + 1, max((Comparable<?>) maxes.get(LEFT_MAX, l), (Comparable<?>) maxes.get(LEFT_NEIGHBOR_MAX, l)));
        }
        if (side == L) {
            Comparable<?> uMax = getRangeEnd(u);
            if (compare((Comparable<?>) neighbors.getKey(R, l), uKey) == 0) {
                maxes.put(LEFT_MAX_NODE, l, null);
            }
            Comparable<?> currentMax = null;
            if (m.equals(l + 1, val)) {
                currentMax = max;
                change_neighbor_with_max(u, R, l + 1, currentMax, null);
            } else {
                currentMax = max((Comparable<?>) maxes.get(LEFT_MAX, l), max);
                try {
                    if (neighbors.get(L, l) != null) {
                        neighbors.get(L, l).send(setVia(buddyWithMaxOp(u, L, l, val, currentMax, null), via));
                    } else {
                        u.send(setVia(setLinkWithMaxOp(null, l + 1, currentMax, null), via));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (m.equals(l + 1, val)) {
                maxes.put(LEFT_NEIGHBOR_MAX, l, max);
                maxes.put(LEFT_MAX, l + 1, max((Comparable<?>) maxes.get(LEFT_MAX, l), (Comparable<?>) maxes.get(LEFT_NEIGHBOR_MAX, l)));
                change_neighbor_with_max(u, L, l + 1, null, optionalNode);
            } else {
                if (compare((Comparable<?>) neighbors.getKey(L, l), (Comparable<?>) uKey) == 0 && (neighbors.get(L, l + 1) == null)) {
                    optionalNode = self;
                }
                Comparable<?> currentMax = null;
                currentMax = max((Comparable<?>) maxes.get(LEFT_MAX, l), max);
                try {
                    if (neighbors.get(R, l) != null) {
                        neighbors.get(R, l).send(buddyWithMaxOp(u, R, l, val, currentMax, optionalNode));
                    } else {
                        u.send(setVia(setLinkWithMaxOp(null, l + 1, null, optionalNode), via));
                    }
                } catch (IOException e) {
                }
            }
        }
    }

    protected void onReceiveUpdateLevelMaxNodeOp(Node sender, Map<Object, Object> args) {
        Node u = (Node) args.get(SkipGraph.Arg.NODE);
        int l = (int) ((Integer) args.get(SkipGraph.Arg.LEVEL));
        int onOff = (int) ((Integer) args.get(Arg.ONOFF));
        if (onOff == ON) {
            maxes.put(LEFT_MAX_NODE, l, u);
            try {
                u.send(updateLeftNeighborMaxOp(u, l, null, (Comparable<?>) maxes.get(LEFT_MAX, getMaxLevel()), VER));
            } catch (IOException e) {
            }
        } else {
            maxes.put(LEFT_MAX_NODE, l, null);
        }
    }

    protected void onReceiveUpdateMaxOp(Node sender, Map<Object, Object> args) {
        Node u = (Node) args.get(SkipGraph.Arg.NODE);
        Comparable<?> uKey = getKey(u);
        Comparable<?> uMax = getRangeEnd(u);
        Comparable<?> max = (Comparable<?>) args.get(Arg.MAX);
        int l = getMaxLevel();
        try {
            if (u.equals(self)) {
                while (l > 0) {
                    boolean sendingFlag = true;
                    l--;
                    if (neighbors.get(R, l + 1) != null && neighbors.get(R, l) != null && neighbors.get(R, l + 1).equals(neighbors.get(R, l))) {
                        sendingFlag = false;
                    }
                    if (sendingFlag == true) {
                        if (neighbors.get(R, l) != null) {
                            if (maxes.get(LEFT_MAX_NODE, l) != null || compare((Comparable<?>) neighbors.getKey(L, l), (double) 0) == 0) {
                                neighbors.get(R, l).send(updateLeftNeighborMaxOp(u, l, (Comparable<?>) maxes.get(LEFT_MAX, l), (Comparable<?>) maxes.get(LEFT_MAX, getMaxLevel()), VER));
                            } else {
                                neighbors.get(R, l).send(updateLeftNeighborMaxOp(u, l, (Comparable<?>) maxes.get(LEFT_MAX, l), (Comparable<?>) maxes.get(LEFT_MAX, getMaxLevel()), VER));
                            }
                        }
                    }
                }
                if (neighbors.get(R, 0) != null) {
                    neighbors.get(R, 0).send(updateMaxOp(u, (Comparable<?>) maxes.get(LEFT_MAX, getMaxLevel())));
                }
            } else {
                boolean changeFlag = true;
                if ((compare((Comparable<?>) maxes.get(LEFT_MAX, getMaxLevel()), max) < 0)) {
                    maxes.put(LEFT_MAX, getMaxLevel(), max);
                    changeFlag = true;
                    for (int i = 0; i < getMaxLevel(); i++) {
                        if (maxes.get(LEFT_MAX_NODE, i) != null) {
                            Node to = (Node) maxes.get(LEFT_MAX_NODE, i);
                            to.send(updateLeftNeighborMaxOp(u, i, null, (Comparable<?>) maxes.get(LEFT_MAX, getMaxLevel()), VER));
                        }
                    }
                }
                while (l > 0 && changeFlag) {
                    l--;
                    if (neighbors.getKey(L, l) != null && compare(neighbors.getKey(L, l), uKey) >= 0) {
                        if (neighbors.get(L, l + 1) != null && max != null && compare(neighbors.getKey(L, l + 1), uKey) < 0) {
                            if (maxes.get(LEFT_NEIGHBOR_MAX, l) != null && compare((Comparable<?>) maxes.get(LEFT_NEIGHBOR_MAX, l), (Comparable<?>) max) < 0) {
                                maxes.put(LEFT_NEIGHBOR_MAX, l, max);
                            } else if (maxes.get(LEFT_NEIGHBOR_MAX, l) == null) {
                                maxes.put(LEFT_NEIGHBOR_MAX, l, max);
                            }
                        }
                        changeFlag = false;
                    }
                    if (neighbors.getKey(L, l) != null && compare(neighbors.getKey(L, l), uKey) < 0) {
                        if (compare((Comparable<?>) maxes.get(LEFT_MAX, l), (Comparable<?>) maxes.get(LEFT_MAX, l + 1)) < 0) {
                            maxes.put(LEFT_MAX, l, maxes.get(LEFT_MAX, l + 1));
                            if (neighbors.get(R, l) != null) {
                                neighbors.get(R, l).send(updateLeftNeighborMaxOp(u, l, (Comparable<?>) maxes.get(LEFT_MAX, l), (Comparable<?>) maxes.get(LEFT_MAX, getMaxLevel()), UP));
                            }
                        }
                    } else if (neighbors.getKey(L, l) == null) {
                        maxes.put(LEFT_MAX, l, maxes.get(LEFT_MAX, l + 1));
                        if (neighbors.get(R, l) != null && compare((Comparable<?>) uMax, (Comparable<?>) getRangeEnd(neighbors.get(R, l))) < 0) {
                            neighbors.get(R, l).send(updateLeftNeighborMaxOp(u, l, (Comparable<?>) maxes.get(LEFT_MAX, l), (Comparable<?>) maxes.get(LEFT_MAX, getMaxLevel()), UP));
                        }
                        changeFlag = true;
                    }
                }
                if (compare((Comparable<?>) uMax, (Comparable<?>) getRangeEnd()) > 0) {
                    if (neighbors.get(R, 0) != null) {
                        neighbors.get(R, 0).send(updateMaxOp(u, max));
                    }
                }
            }
        } catch (IOException e) {
        }
    }

    protected void onReceiveUpdateLeftNeighborMaxOp(Node sender, Map<Object, Object> args) {
        Node u = (Node) args.get(SkipGraph.Arg.NODE);
        Comparable<?> uKey = getKey(u);
        int l = (Integer) args.get(SkipGraph.Arg.LEVEL);
        int direction = (Integer) args.get(Arg.DIRECTION);
        Comparable<?> max = (Comparable<?>) args.get(Arg.MAX);
        Comparable<?> topMax = (Comparable<?>) args.get(Arg.TOP_MAX);
        boolean changeFlag = true;
        if (direction == VER) {
            maxes.put(LEFT_NEIGHBOR_MAX, l, topMax);
        } else {
            if ((maxes.get(LEFT_NEIGHBOR_MAX, l) != null && max != null && compare((Comparable<?>) maxes.get(LEFT_NEIGHBOR_MAX, l), (Comparable<?>) max) < 0)) {
                maxes.put(LEFT_NEIGHBOR_MAX, l, max);
            }
        }
    }

    protected class SearchResult {

        public List<Node> matches;

        public List<Node> unmatches;

        public List<List<Id>> mVias;

        public List<List<Id>> uVias;

        public SearchResult() {
            matches = new ArrayList<Node>();
            unmatches = new ArrayList<Node>();
            mVias = new ArrayList<List<Id>>();
            uVias = new ArrayList<List<Id>>();
        }

        public void addMatch(Node node) {
            matches.add(node);
        }

        public void addUnmatch(Node node) {
            unmatches.add(node);
        }

        public void addMatchVia(List<Id> via) {
            mVias.add(via);
        }

        public void addUnmatchVia(List<Id> via) {
            uVias.add(via);
        }
    }

    SearchResult rangeSearchResult;

    protected void onReceiveRangeSearchResult(Node sender, Map<Object, Object> arg) {
        Op op = (Op) arg.get(SkipGraph.Arg.OP);
        List<Id> via = getVia(arg);
        Node node = (Node) arg.get(SkipGraph.Arg.NODE);
        if (op == Op.FOUND_IN_RANGE) {
            rangeSearchResult.addMatch(node);
            rangeSearchResult.addMatchVia(via);
        } else {
            rangeSearchResult.addUnmatch(node);
            rangeSearchResult.addUnmatchVia(via);
        }
    }

    @Override
    public Node search(Comparable<?> key) {
        return null;
    }

    @Override
    public List<Node> search(Range range) {
        return null;
    }

    @Override
    public List<Node> overlapSearch(Comparable<?> key) {
        return overlapSearch(new Range(key, key));
    }

    @Override
    public void overlapSend(Range range, Object body) {
        self.trans.setParameter(SimTransportOracle.Param.NestedWait, Boolean.FALSE);
        onReceiveRangeSearchOp(self, rangeSendOp(range, getMaxLevel(), body));
    }

    @Override
    public List<Node> overlapSearch(Range range) {
        self.trans.setParameter(SimTransportOracle.Param.NestedWait, Boolean.FALSE);
        rangeSearchResult = new SearchResult();
        onReceiveRangeSearchOp(self, rangeSearchOp(self, range, getMaxLevel()));
        synchronized (rangeSearchResult) {
            try {
                rangeSearchResult.wait(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(rangeSearchResult.matches, new KeySortComparator());
        return rangeSearchResult.matches;
    }

    protected void onRangeMatch(Node sender, Map<Object, Object> args) {
        Node startNode = (Node) args.get(SkipGraph.Arg.NODE);
        List<Id> via = getVia(args);
        Object body = args.get(SkipGraph.Arg.BODY);
        try {
            if (body != null) {
                inspectObject(body, self, args);
            } else {
                startNode.send(setVia(foundInRangeOp(self), via));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean rangeOverlaps(Range range1, Range range2) {
        return ((compare(range2.min, range1.min) <= 0 && compare(range1.min, range2.max) <= 0) || (compare(range2.min, range1.max) <= 0 && compare(range1.max, range2.max) <= 0) || (compare(range1.min, range2.min) <= 0 && compare(range2.min, range1.max) <= 0) || (compare(range1.min, range2.max) <= 0 && compare(range2.max, range1.max) <= 0));
    }

    protected void onReceiveRangeSearchOp(Node sender, Map<Object, Object> args) {
        Node u = (Node) args.get(SkipGraph.Arg.NODE);
        Range range = (Range) args.get(Arg.RANGE);
        int l = (int) (Integer) args.get(SkipGraph.Arg.LEVEL);
        Comparable<?> searchKey = range.min;
        Object body = args.get(SkipGraph.Arg.BODY);
        List<Id> via = getVia(args);
        l = getMaxLevel() - 1;
        try {
            if (compare((Comparable<?>) getKey(), (Comparable<?>) searchKey) == 0) {
                if (getMaxLevel() > 0) {
                    self.send(setVia(searchDescendantsOp(range, null, getMaxLevel() - 1, body), via));
                }
            } else if (compare((Comparable<?>) getKey(), (Comparable<?>) searchKey) < 0) {
                while (l >= 0) {
                    if (neighbors.get(R, l) != null) {
                        Comparable<?> sideKey = neighbors.getKey(R, l);
                        if (compare((Comparable<?>) sideKey, (Comparable<?>) searchKey) <= 0) {
                            neighbors.get(R, l).send(setVia(rangeSearchOp(u, range, l, body), via));
                            break;
                        }
                    }
                    l--;
                }
            } else if (compare((Comparable<?>) getKey(), (Comparable<?>) searchKey) > 0) {
                while (l >= 0) {
                    if (neighbors.get(L, l) != null) {
                        Comparable<?> sideKey = neighbors.getKey(L, l);
                        if (compare((Comparable<?>) sideKey, (Comparable<?>) searchKey) >= 0) {
                            neighbors.get(L, l).send(setVia(rangeSearchOp(u, range, l, body), via));
                            break;
                        }
                    }
                    l--;
                }
            }
            if (l < 0) {
                if (compare((Comparable<?>) getKey(), (Comparable<?>) searchKey) <= 0) {
                    self.send(setVia(searchDescendantsOp(range, null, getMaxLevel() - 1, body), via));
                } else {
                    if (neighbors.get(L, 0) != null) {
                        neighbors.get(L, 0).send(setVia(searchDescendantsOp(range, null, getMaxLevel() - 1, body), via));
                    } else {
                        u.send(setVia(notFoundInRangeOp(self), via));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Comparable<?> getRangeEnd(Node node) {
        return (Comparable<?>) node.getAttr(OverlayManager.RANGE_END);
    }

    protected void onReceiveSearchDescendantsOp(Node sender, Map<Object, Object> args) {
        Range range = (Range) args.get(Arg.RANGE);
        Comparable<?> searchKey = range.max;
        int l = (int) (Integer) args.get(SkipGraph.Arg.LEVEL);
        Comparable<?> boundary = (Comparable<?>) args.get(Arg.BOUNDARY);
        Object body = args.get(SkipGraph.Arg.BODY);
        List<Id> via = getVia(args);
        l = getMaxLevel() - 1;
        if (rangeOverlaps(range, new Range(key, rangeEnd))) {
            onRangeMatch(sender, args);
        }
        while (l >= 0) {
            Comparable<?> leftNeighborMax = (Comparable<?>) maxes.get(LEFT_NEIGHBOR_MAX, l);
            if (neighbors.get(L, l) != null && leftNeighborMax != null && compare(leftNeighborMax, searchKey) >= 0) {
                try {
                    if (boundary != null && compare((Comparable<?>) neighbors.getKey(L, l), (Comparable<?>) boundary) > 0) {
                        neighbors.get(L, l).send(setVia(searchDescendantsOp(range, boundary, l, body), via));
                        boundary = neighbors.getKey(L, l);
                    } else if (boundary == null) {
                        neighbors.get(L, l).send(setVia(searchDescendantsOp(range, null, l, body), via));
                        boundary = neighbors.getKey(L, l);
                    }
                } catch (IOException e) {
                }
            }
            l--;
        }
    }

    public void onReceive(Node sender, Map<Object, Object> mes) {
        Object op = mes.get(SkipGraph.Arg.OP);
        if (op == Op.BUDDY_WITH_MAX) {
            onReceiveBuddyWithMaxOp(sender, mes);
        } else if (op == Op.GET_LINK_WITH_MAX) {
            onReceiveGetLinkWithMaxOp(sender, mes);
        } else if (op == Op.UPDATE_MAX) {
            onReceiveUpdateMaxOp(sender, mes);
        } else if (op == Op.UPDATE_LEFT_NEIGHBOR_MAX) {
            onReceiveUpdateLeftNeighborMaxOp(sender, mes);
        } else if (op == Op.UPDATE_LEVEL_MAX_NODE) {
            onReceiveUpdateLevelMaxNodeOp(sender, mes);
        } else if (op == Op.RANGE_SEARCH) {
            onReceiveRangeSearchOp(sender, mes);
        } else if (op == Op.SEARCH_DESCENDANTS) {
            onReceiveSearchDescendantsOp(sender, mes);
        } else if (op == Op.FOUND_IN_RANGE || op == Op.NOT_FOUND_IN_RANGE) {
            onReceiveRangeSearchResult(sender, mes);
        } else {
            super.onReceive(sender, mes);
        }
    }

    public String toString() {
        System.out.println();
        return "[" + getKey() + "," + getRangeEnd() + "]\n" + neighbors.toString() + maxes.toString() + "max Level= " + getMaxLevel();
    }
}
