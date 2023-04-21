package org.wilmascope.gmlparser;

public interface GMLParserConstants {

    int EOF = 0;

    int GRAPH = 15;

    int VERSION = 16;

    int DIRECTED = 17;

    int NAME = 18;

    int NODE = 19;

    int EDGE = 20;

    int ID = 21;

    int LABEL = 22;

    int SOURCE = 23;

    int TARGET = 24;

    int NUM = 25;

    int DEC = 26;

    int CTLSTRING = 27;

    int DEFAULT = 0;

    int IN_IGNORABLE = 1;

    String[] tokenImage = { "<EOF>", "\" \"", "\"\\t\"", "\"\\n\"", "\"\\r\"", "\"\\\\\"", "\"Line\"", "\"LabelGraphics\"", "\"node_style\"", "\"edge_style\"", "\"style\"", "\"edgeAnchor\"", "\"[\"", "\"]\"", "<token of kind 14>", "\"graph\"", "\"version\"", "\"directed\"", "\"name\"", "\"node\"", "\"edge\"", "\"id\"", "\"label\"", "\"source\"", "\"target\"", "<NUM>", "<DEC>", "<CTLSTRING>", "\"[\"", "\"]\"", "\"graphics\"", "\"x\"", "\"y\"", "\"w\"", "\"h\"", "\"type\"", "\"visible\"", "\"arrow\"", "\"capstyle\"", "\"splinesteps\"" };
}
