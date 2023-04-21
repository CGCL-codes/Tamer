package cn.edu.sjtu.stap.hg.builder;

import org.aspectj.org.eclipse.jdt.internal.compiler.ast.*;
import cn.edu.sjtu.stap.hg.HierarchyGraph;

public interface IHgBuilder {

    public void initialize();

    public void buildHierarchyGraph(CompilationUnitDeclaration u);

    public HierarchyGraph getResult();
}
