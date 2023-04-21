package com.comarch.depth.csharp.templates;

import java.util.HashSet;
import java.util.Set;
import depth.formatting.block.BlockTemplate;

public class CSharpBlockTemplate extends BlockTemplate {

    private Set<String> blockSet;

    public CSharpBlockTemplate() {
        blockSet = new HashSet<String>();
        blockSet.add("UsingNamespaceDirective");
        blockSet.add("NamespaceDeclaration");
        blockSet.add("Class");
        blockSet.add("Struct");
        blockSet.add("Interface");
        blockSet.add("Enum");
        blockSet.add("Delegate");
        blockSet.add("ConstantDeclaration");
        blockSet.add("Field");
        blockSet.add("Method");
        blockSet.add("Property");
        blockSet.add("Event");
        blockSet.add("Indexer");
        blockSet.add("Operator");
        blockSet.add("Constructor");
        blockSet.add("FinalizerDeclaration");
        blockSet.add("StaticConstructorDeclaration");
        blockSet.add("Statement");
    }

    @Override
    public boolean isBlock(String nodeName) {
        return blockSet.contains(nodeName);
    }
}
