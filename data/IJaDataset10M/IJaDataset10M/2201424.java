package de.tum.mw.masterarbeit.montagevorranggraph.model;

public class Edge {

    public Node source;

    public Node target;

    public String availability;

    public Vorgang vorgang;

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (source != null) sb.append("source: " + source + " ");
        if (target != null) sb.append("target: " + target + " ");
        if (vorgang != null) sb.append("vorgang: " + vorgang);
        return sb.toString();
    }
}
