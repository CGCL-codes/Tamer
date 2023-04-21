package ru.athena.runTool.Model;

public interface AbstractNode {

    public abstract boolean accept(HierarchicalVisitor visitor) throws Exception;
}
