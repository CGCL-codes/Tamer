package it.unitn.goal_analysis.graph_creation;

import java.util.LinkedList;

public abstract class RelType {

    abstract Label solveForS(Node parentNode, LinkedList childNodes);

    abstract Label solveForD(Node parentNode, LinkedList childNodes);

    abstract String getName();
}
