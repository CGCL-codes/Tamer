package com.platonov.colorizer.engine.segmentation.graph;

/**
 * User: Platonov
 * Date: 27.08.11
 * ������ �������� ������
 */
public class Edge implements Comparable {

    float w;

    int a, b;

    public Edge() {
    }

    public int compareTo(Object o) {
        Edge e = (Edge) o;
        if (e.w == w) return 0; else if (e.w > w) return -1; else return 1;
    }
}
