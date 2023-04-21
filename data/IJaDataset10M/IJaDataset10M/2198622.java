package org.ztest.graph.deprecated;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.ztest.graph.ZIEdge;
import org.ztest.graph.ZIGraph;
import org.ztest.graph.jgrapht.ZJGraphTGraph;
import org.ztest.jdom.ZWriteXML;

public class ZDependencyUtil {

    public static <N> ZIGraph<Set<N>, ZDependencyEdgeMeta<N>, ZWriteXML> computeDependencyGraph(ZIGraph<N, ZWriteXML, ZWriteXML> classInfo, Set<Set<N>> sets) throws Exception {
        ZIGraph<Set<N>, ZDependencyEdgeMeta<N>, ZWriteXML> ret = new ZJGraphTGraph<Set<N>, ZDependencyEdgeMeta<N>, ZWriteXML>();
        Map<N, Set<Set<N>>> class2ClassSetMap = computeClass2ClassSetMap(sets);
        Map<N, Set<N>> successorCache = new HashMap<N, Set<N>>();
        for (ZIEdge<N, ZWriteXML> e : classInfo.getEdgeSet()) {
            N class1 = e.getSource();
            N class2 = e.getTarget();
            if (class1.equals(class2)) {
                continue;
            }
            Set<Set<N>> l1 = class2ClassSetMap.get(class1);
            if (l1 == null) {
                continue;
            }
            Set<N> successors = getLayeredSuccessors(classInfo, class2ClassSetMap, successorCache, class2);
            for (N successor : successors) {
                computeActualDependencyGraph(classInfo, ret, class2ClassSetMap, class1, class2, successor);
            }
        }
        return ret;
    }

    private static <N> Set<N> getLayeredSuccessors(ZIGraph<N, ZWriteXML, ZWriteXML> classInfo, Map<N, Set<Set<N>>> layerMap, Map<N, Set<N>> layeredSuccessorCache, N t1) throws Exception {
        Set<N> ret = layeredSuccessorCache.get(t1);
        if (ret != null) {
            return ret;
        }
        if (layerMap.containsKey(t1)) {
            ret = new HashSet<N>();
            ret.add(t1);
            layeredSuccessorCache.put(t1, ret);
            return ret;
        }
        ret = new HashSet<N>();
        layeredSuccessorCache.put(t1, ret);
        Set<N> successors = classInfo.getSuccessors(t1, null, false);
        for (N crt : successors) {
            if (!crt.equals(t1)) {
                Set<N> crtSet = getLayeredSuccessors(classInfo, layerMap, layeredSuccessorCache, crt);
                ret.addAll(crtSet);
            }
        }
        return ret;
    }

    private static <N> void computeActualDependencyGraph(ZIGraph<N, ZWriteXML, ZWriteXML> classInfo, ZIGraph<Set<N>, ZDependencyEdgeMeta<N>, ZWriteXML> actualDependencyGraph, Map<N, Set<Set<N>>> dependencyMap, N t1, N t2, N successor) {
        Set<Set<N>> l1 = dependencyMap.get(t1);
        Set<Set<N>> l2 = dependencyMap.get(successor);
        for (Set<N> set1 : l1) {
            for (Set<N> set2 : l2) {
                if (!set1.equals(set2)) {
                    ZIEdge<Set<N>, ZDependencyEdgeMeta<N>> actualEdge = actualDependencyGraph.addEdge(set1, set2);
                    List<ZIEdge<N, ZWriteXML>> path = classInfo.getShortestPath(t2, successor);
                    ZIEdge<N, ZWriteXML> edge = classInfo.getEdge(t1, t2);
                    path.add(0, edge);
                    ZDependencyEdgeMeta<N> meta = actualEdge.getMetaData();
                    if (meta == null) {
                        meta = new ZDependencyEdgeMeta<N>();
                        actualEdge.setMetaData(meta);
                    }
                    meta.getPaths().add(path);
                }
            }
        }
    }

    private static <N> Map<N, Set<Set<N>>> computeClass2ClassSetMap(Set<Set<N>> classSets) {
        Map<N, Set<Set<N>>> ret = new HashMap<N, Set<Set<N>>>();
        for (Set<N> set : classSets) {
            for (N clazz : set) {
                Set<Set<N>> dependencySet = ret.get(clazz);
                if (dependencySet == null) {
                    dependencySet = new HashSet<Set<N>>();
                    ret.put(clazz, dependencySet);
                }
                dependencySet.add(set);
            }
        }
        return ret;
    }
}
