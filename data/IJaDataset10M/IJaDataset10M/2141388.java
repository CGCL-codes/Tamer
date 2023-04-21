package org.deft.repository.ast.decoration;

/**
 * NodeInformation are information objects that can be used to tag TreeNodes when converting
 * formatted output from the TreeNode-AST of the source file. As the AST will be ultimately
 * converted to a List of Tokens enriched with XML-Markup, each information can add its data
 * as XML to the TreeNode it belongs to. 
 * 
 * @author Andreas Bartho
 */
public abstract class NodeInformation {

    public abstract Ident getIdent();

    public abstract NodeInformation copy();

    public abstract void addContentFromOtherNodeInformation(NodeInformation newInformation);
}
