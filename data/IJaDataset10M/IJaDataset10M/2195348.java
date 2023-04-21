package com.jsnyder.baselinetest.eclipse.plugins.launch;

public interface FileTreeNodeVisitor {

    public void visit(FileTreeNode node);

    public void visit(SuiteTreeNode node);
}
