package com.ohioedge.j2ee.api.algorithm.graph;

import java.util.LinkedList;
import java.util.Collection;
import org.j2eebuilder.util.LogManager;

/**
 * @(#)Vertex.java	1.3.1 10/15/2002
 */
public interface Vertex {

    public Integer getVertexID();

    public int getDistance();

    public void setDistance(int dist);

    public LinkedList getColOfPath();

    public void addPath(Vertex path);

    public void addCyclePath(Vertex cyclePath);

    public LinkedList getColOfCyclePath();

    public boolean getKnown();

    public void setKnown(boolean known);

    public LinkedList getColOfAdjacent();

    public void addAdjacent(Vertex v);

    public Integer getOrganizationID();

    public String getName();

    public void setJunctionType(String junctionType);

    public String getJunctionType() throws JunctionNotFoundException;
}
