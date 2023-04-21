package org.frankkie.parcdroidprj;

import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Dijkstra {

    public static void computePaths(OverlayPunt source) {
        for (int a = 0; a < OverlayDinges.getGpsPlekken().size(); a++) {
            OverlayDinges.getGpsPlekken().get(a).resetDijkstra();
        }
        for (int b = 0; b < OverlayDinges.getDeLijnen().size(); b++) {
            OverlayDinges.getDeLijnen().get(b).resetVoorDijkstra();
        }
        source.minDistance = 0.0;
        PriorityQueue<OverlayPunt> OverlayPuntQueue = new PriorityQueue<OverlayPunt>();
        OverlayPuntQueue.add(source);
        while (!OverlayPuntQueue.isEmpty()) {
            OverlayPunt u = OverlayPuntQueue.poll();
            System.out.println("Dijkstra.computePaths: while (!OverlayPuntQueue.isEmpty()) {");
            for (OverlayLijn e : u.adjacencies) {
                OverlayPunt v;
                if (e.punt1.equals(u)) {
                    v = e.punt2;
                } else {
                    v = e.punt1;
                }
                double weight = e.lengteLijn;
                double distanceThroughU = u.minDistance + weight;
                if (distanceThroughU < v.minDistance) {
                    OverlayPuntQueue.remove(v);
                    v.minDistance = distanceThroughU;
                    v.previous = u;
                    OverlayPuntQueue.add(v);
                }
            }
        }
    }

    public static List<OverlayPunt> getShortestPathTo(OverlayPunt target) {
        List<OverlayPunt> path = new ArrayList<OverlayPunt>();
        for (OverlayPunt OverlayPunt = target; OverlayPunt != null; OverlayPunt = OverlayPunt.previous) {
            path.add(OverlayPunt);
        }
        Collections.reverse(path);
        return path;
    }

    public static void startDijkstra() {
        System.out.println("Dijkstra.startDijkstra()");
        OverlayPunt src = OverlayDinges.getDeLijnen().get(OverlayDinges.lijnDieHetDichtsteBijIs).punt1;
        computePaths(src);
        System.out.println("Aantal GPS-Plekken: " + OverlayDinges.getGpsPlekken().size());
        for (int a = 0; a < OverlayDinges.getGpsPlekken().size(); a++) {
            OverlayPunt v = OverlayDinges.getGpsPlekken().get(a);
            System.out.println("Distance to " + v + ": " + v.minDistance);
            List<OverlayPunt> path = getShortestPathTo(v);
            System.out.println("Path: " + path);
        }
    }

    public static List<OverlayPunt> doeDijkstra(OverlayPunt veindpunt) {
        System.out.println("Dijkstra.doeDijkstra(" + veindpunt + ")");
        OverlayPunt src = OverlayDinges.lijnDieHetDichtsteBijIsLijn.punt1;
        System.out.println("OverlayDinges.lijnDieHetDichtsteBijIsLijn: " + OverlayDinges.lijnDieHetDichtsteBijIsLijn);
        System.out.println("OverlayDinges.lijnDieHetDichtsteBijIsLijn.punt1: " + OverlayDinges.lijnDieHetDichtsteBijIsLijn.punt1);
        computePaths(src);
        System.out.println("Aantal GPS-Plekken: " + OverlayDinges.getGpsPlekken().size());
        System.out.println("Distance to " + veindpunt + ": " + veindpunt.minDistance);
        return getShortestPathTo(veindpunt);
    }
}
