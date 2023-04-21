package ru.amse.baltijsky.javascheme.tree;

/**
 * Visits schema tree nodes, returning a value of type T. Actions are taken depending on the type of the node
 * (branching, nesting, simple, flow changing, or a try block).
 * Type parameters:
 * </p>
 * T � return type
 * C � context to work with the node
 * E � exceptions thrown by the methods
 */
public interface TreeVisitor<T, C, E extends Throwable> {

    T visit(SchemaNode node, C context) throws E;

    T visit(BranchNode node, C context) throws E;

    T visit(NestingNode node, C context) throws E;

    T visit(FlowChangeNode node, C context) throws E;

    T visit(TryNode node, C context) throws E;
}
