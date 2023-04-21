package relex.feature;

import java.util.HashSet;

/**
 * The FeatureForeach class provides a simple, easy-to-use way of
 * traversing all of the unary and binary relations in a feature graph.
 * It invokes the FeatureNodeCallback.FNCallback() method on each 
 * relation node visited.
 *
 * Relations are chacterized by having a "name" node, indicating
 * the thing to which the relations apply. Unary relations are 
 * just other keys: For example:
 *
 *     [name <<she>>
 *      DEFINITE-FLAG <<T>>
 *      PRONOUN-FLAG <<T>>
 *      gender <<feminine>>
 *      noun_number <<singular>>]
 *
 *  Binary rlations have a "links" node:
 *      [name <<need>>
 *       links [_subj [name <<she>>]
 *              _to-do [name <<do>>]]
 *
 * which indicates that "need" is involved in two binary relations:
 *   _subj(need, she)  and _to-do(need, do)
 *
 *  Copyright (C) 2008 Linas Vepstas <linas@linas.org>
 */
public class FeatureForeach {

    /**
	 * Walk the graph, calling a callback for each node visited.
	 */
    private static Boolean _graphCrawl(FeatureNode f, HashSet<FeatureNode> alreadyVisited, FeatureNodeCallback cb) {
        Boolean rc = false;
        if (alreadyVisited.contains(f)) return rc;
        alreadyVisited.add(f);
        if (f.isValued()) return rc;
        for (String key : f.getFeatureNames()) {
            if (!key.equals("head-word") && !key.equals("NEXT") && !key.equals("ref") && !key.equals("name")) continue;
            FeatureNode fn = f.get(key);
            if (key.equals("name")) {
                String name = fn.getValue();
                if (name != null) {
                    rc = cb.FNCallback(f);
                    if (rc) return rc;
                }
            }
            if (!fn.isValued()) {
                rc = _graphCrawl(fn, alreadyVisited, cb);
                if (rc) return rc;
            }
        }
        return rc;
    }

    public static Boolean foreach(FeatureNode root, FeatureNodeCallback cb) {
        HashSet<FeatureNode> alreadyVisited = new HashSet<FeatureNode>();
        Boolean rc = _graphCrawl(root, alreadyVisited, cb);
        return rc;
    }

    /**
	 * Walk the graph, looking for all link relations in a graph.
	 *
	 * The structure of a relation is always
	 * [name <<from-val>> links [relation [name <<to-val>>]]]
	 */
    private static class RelCB implements FeatureNodeCallback {

        private RelationCallback rcb;

        public RelCB(RelationCallback cb) {
            rcb = cb;
        }

        public Boolean FNCallback(FeatureNode fn_link_from) {
            if (fn_link_from == null) return false;
            if (skip_left_wall(fn_link_from)) return false;
            Boolean stop = _binary(fn_link_from);
            if (stop) return true;
            stop = _unary(fn_link_from);
            if (stop) return true;
            return false;
        }

        private Boolean _unary(FeatureNode fnsrc) {
            for (String attrName : fnsrc.getFeatureNames()) {
                if ("name".equals(attrName)) continue;
                if ("val".equals(attrName)) continue;
                if ("links".equals(attrName)) continue;
                if ("SIG".equals(attrName)) continue;
                Boolean stop = rcb.UnaryRelationCB(fnsrc, attrName);
                if (stop) return true;
            }
            return false;
        }

        private Boolean _binary(FeatureNode fn_link_from) {
            FeatureNode fn_link = fn_link_from.get("links");
            if (fn_link == null) return false;
            Boolean stop = rcb.BinaryHeadCB(fn_link_from);
            if (stop) return true;
            for (String relation_name : fn_link.getFeatureNames()) {
                FeatureNode fn_link_to = fn_link.get(relation_name);
                if (fn_link_to != null && !fn_link_to.isValued()) {
                    FeatureNode multi = fn_link_to.get("member0");
                    if (multi != null) {
                        Integer n = 0;
                        while (multi != null) {
                            stop = rcb.BinaryRelationCB(relation_name, fn_link_from, multi);
                            if (stop) return true;
                            n++;
                            String member_name = "member" + n.toString();
                            multi = fn_link_to.get(member_name);
                        }
                    } else {
                        stop = rcb.BinaryRelationCB(relation_name, fn_link_from, fn_link_to);
                        if (stop) return true;
                    }
                }
            }
            return false;
        }
    }

    private static Boolean skip_left_wall(FeatureNode f) {
        FeatureNode fname = f.get("name");
        if (fname == null) return true;
        String name = fname.getValue();
        if (name.equals("LEFT-WALL")) return true;
        return false;
    }

    public static Boolean foreach(FeatureNode root, RelationCallback cb) {
        HashSet<FeatureNode> alreadyVisited = new HashSet<FeatureNode>();
        RelCB relcb = new RelCB(cb);
        Boolean rc = _graphCrawl(root, alreadyVisited, relcb);
        return rc;
    }
}
